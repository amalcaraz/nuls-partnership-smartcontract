package com.gmail.amalcaraz89.partnership.func;

import com.gmail.amalcaraz89.partnership.event.NewPollingEvent;
import com.gmail.amalcaraz89.partnership.event.VotePollingEvent;
import com.gmail.amalcaraz89.partnership.event.ResolvePollingEvent;
import com.gmail.amalcaraz89.partnership.model.Partner;
import com.gmail.amalcaraz89.partnership.model.Polling;
import io.nuls.contract.sdk.Address;
import io.nuls.contract.sdk.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.nuls.contract.sdk.Utils.emit;
import static io.nuls.contract.sdk.Utils.require;

public class PollingManager implements PollingManagerInterface {

    private double percentageOfParticipationToResolvePolling;
    private double percentageOfPositiveVotesToAcceptPolling;
    private Map<Long, Polling> pollingMap = new HashMap<Long, Polling>();

    public PollingManager(double percentageOfParticipationToResolvePolling, double percentageOfPositiveVotesToAcceptPolling) {

        require(percentageOfParticipationToResolvePolling > 0 && percentageOfParticipationToResolvePolling <= 100, "percentageOfParticipationToResolvePolling should be between (0, 100]");
        require(percentageOfPositiveVotesToAcceptPolling > 0 && percentageOfPositiveVotesToAcceptPolling <= 100, "percentageOfPositiveVotesToAcceptPolling should be between (0, 100]");

        this.percentageOfParticipationToResolvePolling = percentageOfParticipationToResolvePolling;
        this.percentageOfPositiveVotesToAcceptPolling = percentageOfPositiveVotesToAcceptPolling;

    }

    @Override
    public Polling createPolling(Address initiator, int voteType, Partner partner, long expirationDate) {

        Map<Long, Polling> pollingMap = this.pollingMap;

        Polling polling = Utils.findPolling(pollingMap, partner.getAddress(), null, PollingStatus.OPEN, -1, -1);

        require(polling == null, "This address is already involved in a polling waiting for approval");

        Polling pollingByInitiator = Utils.findPolling(pollingMap, null, initiator, PollingStatus.OPEN, -1, -1);

        require(pollingByInitiator == null, "This address already initiated a polling which is waiting for approval");

        long pollingId = (long) (pollingMap.size() + 1);
        long now = Block.timestamp();

        polling = new Polling();
        polling.setId(pollingId);
        polling.setType(voteType);
        polling.setStatus(PollingStatus.OPEN);
        polling.setStatus(PollingResult.PENDING);
        polling.setInitiator(initiator);
        polling.setStartDate(now);
        polling.setEndDate(expirationDate);
        polling.setPartner(partner);

        if (!initiator.equals(partner.getAddress())) {

            polling.getVotes().put(initiator, true);

        }

        pollingMap.put(pollingId, polling);
        emit(new NewPollingEvent(polling));

        return polling;

    }

    @Override
    public Polling vote(long pollingId, Partner partner, boolean vote) {

        Polling polling = this.getPolling(pollingId);

        require(polling.getStatus() != PollingStatus.CLOSED, "The voting period for this polling is over, call \"resolvePolling\"");
        require(this.checkPollingStatus(polling) != PollingStatus.CLOSED, "This polling is already closed");
        require(this.participantAllowedToVote(polling, partner), "You are not allowed to vote");

        Map<Address, Boolean> votes = polling.getVotes();
        Boolean oldVote = votes.get(partner.getAddress());

        require(oldVote == null, "There is already a vote of the partner for this polling");

        Address address = partner.getAddress();

        votes.put(address, vote);
        emit(new VotePollingEvent(pollingId, address, vote));

        return polling;

    }

    @Override
    public Polling resolvePolling(long pollingId, List<Partner> participants) {

        Polling polling = this.getPolling(pollingId);

        require(polling.getResult() == PollingResult.PENDING, "Polling " + pollingId + " is already resolved");

        List<Partner> pendingPartners = this.getParticipantsPendingForVote(polling, participants);

        require(this.checkPollingStatus(polling) == PollingStatus.CLOSED || pendingPartners.size() == 0, "Polling " + pollingId + " is already open");
        boolean result = this.getPollingResult(polling, participants);

        polling.setResult(result ? PollingResult.ACCEPTED : PollingResult.REFUSED);
        polling.setStatus(PollingStatus.CLOSED);

        emit(new ResolvePollingEvent(polling));
        return polling;

    }

    @Override
    public List<Polling> getPollingList() {

        Map<Long, Polling> pollingMap = this.pollingMap;
        List<Polling> pollingList = new ArrayList<Polling>();

        for (long i = 1; i <= pollingMap.size(); i++) {

            Polling polling = pollingMap.get(i);
            pollingList.add(polling);

        }

        return pollingList;

    }

    @Override
    public List<Polling> getPendingPollingList() {

        Map<Long, Polling> pollingMap = this.pollingMap;
        List<Polling> pollingList = new ArrayList<Polling>();

        for (long i = 1; i <= pollingMap.size(); i++) {

            Polling polling = pollingMap.get(i);

            if (polling.getStatus() == PollingStatus.OPEN) {

                pollingList.add(polling);

            }

        }

        return pollingList;

    }

    @Override
    public Polling getPollingDetail(long pollingId) {

       return this.getPolling(pollingId);

    }

    @Override
    public List<Partner> getParticipantsPendingForVote(Polling polling, List<Partner> participants) {

        List<Partner> pendingPartners = new ArrayList<Partner>();

        Map<Address, Boolean> votes = polling.getVotes();

        for (int i = 0; i < participants.size(); i++) {

            Partner participant = participants.get(i);

            if (this.participantAllowedToVote(polling, participant) && votes.get(participant.getAddress()) == null) {

                pendingPartners.add(participant);

            }

        }

        return pendingPartners;

    }

    private Polling getPolling(long pollingId) {

        require(pollingId > 0, "pollingId should be greater than 0");

        Map<Long, Polling> pollingMap = this.pollingMap;

        Polling polling = pollingMap.get(pollingId);

        require(polling != null, "polling does not exist");

        return polling;

    }

    private int checkPollingStatus(Polling polling) {

        int status = polling.getStatus();

        if (polling.getEndDate() <= Block.timestamp() && status != PollingStatus.CLOSED) {

            status = PollingStatus.CLOSED;
            polling.setStatus(PollingStatus.CLOSED);

        }

        return status;

    }

    private boolean getPollingResult(Polling polling, List<Partner> participants) {

        Map<Address, Boolean> votes = polling.getVotes();
        int totalPartnersCount = participants.size();
        int totalVotesCount = 0;
        int positiveVotesCount = 0;

        for (int i = 0; i < totalPartnersCount; i++) {

            Partner participant = participants.get(i);
            Boolean vote = votes.get(participant.getAddress());

            if (vote != null) {

                positiveVotesCount += vote ? 1 : 0;
                totalVotesCount++;

            }

        }

        double participationPercent = ((double) totalVotesCount) / totalPartnersCount;
        boolean resolvePolling = participationPercent >= (this.percentageOfParticipationToResolvePolling / 100);

        if (resolvePolling) {

            double positivePercent = ((double) positiveVotesCount) / totalVotesCount;
            return positivePercent >= (this.percentageOfPositiveVotesToAcceptPolling / 100);

        } else {

            // If there isn't enough votes the polling is refused
            return false;

        }

    }

    private boolean participantAllowedToVote(Polling polling, Partner partner) {

        return !(partner.getAddress().equals(polling.getPartner().getAddress()));

    }

}
