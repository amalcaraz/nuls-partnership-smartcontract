package com.gmail.amalcaraz89.partnership.func;

import com.gmail.amalcaraz89.partnership.event.NewPartnerEvent;
import com.gmail.amalcaraz89.partnership.event.PartnerPayoutEvent;
import com.gmail.amalcaraz89.partnership.event.PayoutEvent;
import com.gmail.amalcaraz89.partnership.event.RemovePartnerEvent;
import com.gmail.amalcaraz89.partnership.event.ChangePartnerParticipationEvent;
import com.gmail.amalcaraz89.partnership.model.Partner;
import com.gmail.amalcaraz89.partnership.model.PartnerPayout;
import com.gmail.amalcaraz89.partnership.model.Partnership;
import com.gmail.amalcaraz89.partnership.model.Payout;
import com.gmail.amalcaraz89.partnership.model.Polling;
import io.nuls.contract.sdk.Address;
import io.nuls.contract.sdk.Block;
import io.nuls.contract.sdk.Msg;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static io.nuls.contract.sdk.Utils.emit;
import static io.nuls.contract.sdk.Utils.require;

public class PartnershipManager extends Owner implements PartnershipManagerInterface {

    // TODO: make it configurable
    private final static long RESCUE_MIN_WAIT = 1000L * 60 * 60 * 24 * 30;
    private final static long MIN_PAYOUT_INTERVAL = 1000L * 60 * 5;
    // private final static long MIN_PAYOUT_INTERVAL = 1000L * 60 * 60;
    private Partnership partnership;
    private PollingManagerInterface pollingManager;

    public PartnershipManager(
            String title,
            String desc,
            double nodeCommission,
            long payoutInterval,
            double percentageOfParticipationToResolvePolling,
            double percentageOfPositiveVotesToAcceptPolling,
            Address owner
    ) {

        super(owner);

        require(title != null, "title can not be empty");
        require(desc != null, "desc can not be empty");
        require(nodeCommission >= 10 && nodeCommission <= 100, "nodeCommission should be between [10, 100]");
        require(payoutInterval >= MIN_PAYOUT_INTERVAL, "payoutInterval should be greater or equal than 1 hour");

        this.partnership = new Partnership(title, desc, nodeCommission, payoutInterval);
        this.pollingManager = new PollingManager(percentageOfParticipationToResolvePolling, percentageOfPositiveVotesToAcceptPolling);

    }

    @Override
    public void enterPartnership(Address address, BigInteger participation) {

        this.enterPartnership(address, participation, this.getPartnerCommission());

    }

    @Override
    public void enterPartnership(Address address, BigInteger participation, double commission) {

        Partner newPartner = this.getNewPartner(address, participation, commission);

        if (this.partnership.getPartners().size() == 0) {

            this.addPartner(newPartner);

        } else {

            this.createPolling(address, PollingType.ADD_PARTNER, newPartner);

        }

    }

    @Override
    public void leavePartnership(Address address) {

        Partner partner = this.getPartner(address);
        this.removePartner(partner);

    }

    @Override
    public void changeParticipation(Address address, BigInteger participation) {

        double commission = this.getPartner(address).getCommision();

        this.changeParticipation(address, participation, commission);

    }

    @Override
    public void changeParticipation(Address address, BigInteger participation, double commission) {

        Partner newPartner = this.getModifiedPartner(address, participation, commission);
        List<Partner> partners = this.partnership.getPartners();

        if (partners.size() == 1 && partners.get(0).getAddress().equals(address)) {

            this.changePartnerParticipation(newPartner);

        } else {

            this.createPolling(address, PollingType.CHANGE_PARTNER_PARTICIPATION, newPartner);

        }

    }


    @Override
    public void askAddPartner(Address initiator, Address address, BigInteger participation) {

        this.askAddPartner(initiator, address, participation, this.getPartnerCommission());

    }

    @Override
    public void askAddPartner(Address initiator, Address address, BigInteger participation, double commission) {

        Partner newPartner = this.getNewPartner(address, participation, commission);

        this.createPolling(initiator, PollingType.ADD_PARTNER, newPartner);

    }

    @Override
    public void askRemovePartner(Address initiator, Address address) {

        require(!this.isOwner(address), "Owner can not be removed");

        Partner removePartner = this.getPartner(address);

        this.createPolling(initiator, PollingType.REMOVE_PARTNER, removePartner);

    }

    @Override
    public void askChangePartnerParticipation(Address initiator, Address address, BigInteger participation) {

        double commission = this.getPartner(address).getCommision();

        this.askChangePartnerParticipation(initiator, address, participation, commission);

    }

    @Override
    public void askChangePartnerParticipation(Address initiator, Address address, BigInteger participation, double commission) {

        require(!this.isOwner(address), "Owner participation can not be changed");

        Partner newPartner = this.getModifiedPartner(address, participation, commission);

        this.createPolling(initiator, PollingType.CHANGE_PARTNER_PARTICIPATION, newPartner);

    }

    @Override
    public void checkForPayout() {

        BigInteger payoutAmount = Msg.address().balance();
        long now = Block.timestamp();
        long payoutDate = this.getPayoutDate();

        require(payoutDate <= now, "The current payout period has not finished yet");
        require(this.pollingManager.getPendingPollingList().size() == 0, "There are pending pollings, call \"resolvePolling\" before");

        if (payoutAmount.compareTo(BigInteger.ZERO) > 0) {

            List<PartnerPayout> partnerPayouts = new ArrayList<PartnerPayout>();

            List<Partner> partners = this.partnership.getPartners();
            BigInteger totalParticipation = this.getTotalParticipation(partners);

            for (int i = 0; i < partners.size(); i++) {

                Partner partner = partners.get(i);
                BigInteger partnerParticipation = BigInteger.valueOf((long) (partner.getParticipation().doubleValue() * (1 - (partner.getCommision() / 100))));

                if (partnerParticipation.compareTo(BigInteger.ZERO) > 0) {

                    BigInteger amount = payoutAmount.multiply(partnerParticipation).divide(totalParticipation);
                    Address address = partner.getAddress();

                    address.transfer(amount);

                    partnerPayouts.add(new PartnerPayout(now, address, amount));
                    emit(new PartnerPayoutEvent(now, address, amount));
                }

            }

            this.partnership.setTotalPayoutsAmount(this.partnership.getTotalPayoutsAmount().add(payoutAmount));

            this.partnership.getLastPayouts().add(new Payout(now, payoutAmount, partnerPayouts));
            emit(new PayoutEvent(now, payoutAmount, partnerPayouts));

        }

        this.partnership.setLastPayoutTime(now);

    }

    @Override
    public List<Payout> getPayoutList() {

        return this.partnership.getLastPayouts();

    }

    @Override
    public Payout getPayoutDetail(long payoutDate) {

        require(payoutDate > 0, "payoutDate should be greater than 0");

        Payout payout = Utils.findPayout(this.partnership.getLastPayouts(), payoutDate);

        require(payout != null, "payout does not exists");

        return payout;

    }

    @Override
    public Partnership getPartnershipDetail() {

        return this.partnership;

    }

    @Override
    public List<Partner> getPartnersList() {

        return this.partnership.getPartners();

    }

    @Override
    public Partner getPartnerDetail(Address address) {

        return this.getPartner(address);

    }

    @Override
    public List<Partner> getPartnersPendingOfVote(long pollingId) {

        Polling polling = this.pollingManager.getPollingDetail(pollingId);

        require(polling.getStatus() != PollingStatus.CLOSED, "This polling is already closed");

        return this.pollingManager.getParticipantsPendingForVote(polling, this.partnership.getPartners());

    }

    @Override
    public void rescueNuls(Address receiver) {

        this.requireOwner(receiver, "Only owner can rescue the balance");

        require(this.partnership.getPartners().size() == 0, "There are active partners yet");

        long now = Block.timestamp();
        long remainingTime = (this.partnership.getLastPayoutTime() + this.partnership.getPayoutInterval() + RESCUE_MIN_WAIT) - now;

        require (remainingTime <= 0, "Not allowed to rescue nuls until " + (now + remainingTime));

        receiver.transfer(Msg.address().balance());

    }

    @Override
    public void vote(long pollingId, Address address, boolean vote) {

        // Requires the address to be a current partner
        Partner partner = this.getPartner(address);

        this.pollingManager.vote(pollingId, partner, vote);

    }

    @Override
    public void resolvePolling(long pollingId) {

        Polling polling = this.pollingManager.resolvePolling(pollingId, this.partnership.getPartners());

        if (polling.getResult() == PollingResult.ACCEPTED) {

            switch (polling.getType()) {
                case PollingType.ADD_PARTNER:

                    this.addPartner(polling.getPartner());
                    break;

                case PollingType.REMOVE_PARTNER:

                    this.removePartner(polling.getPartner());
                    break;

                case PollingType.CHANGE_PARTNER_PARTICIPATION:
                default:

                    this.changePartnerParticipation(polling.getPartner());
                    break;
            }
        }
    }

    @Override
    public void resolvePollings() {

        List<Polling> pollings = this.pollingManager.getPendingPollingList();

        require(pollings.size() > 0, "There are no pollings to resolve");

        for (int i = 0; i < pollings.size(); i++) {

            this.resolvePolling(pollings.get(i).getId());

        }

    }

    @Override
    public List<Polling> getPollingList() {

        return this.pollingManager.getPollingList();

    }

    @Override
    public List<Polling> getPendingPollingList() {

        return this.pollingManager.getPendingPollingList();

    }

    @Override
    public Polling getPollingDetail(long pollingId) {

        return this.pollingManager.getPollingDetail(pollingId);

    }

    private void addPartner(Partner partner) {

        if (!Utils.containsPartner(this.partnership.getPartners(), partner.getAddress())) {

            this.partnership.getPartners().add(partner);
            emit(new NewPartnerEvent(partner));

        }

    }

    private void removePartner(Partner partner) {

        Partner oldPartner = Utils.findPartner(this.partnership.getPartners(), partner.getAddress());

        if (oldPartner != null) {

            this.partnership.getPartners().remove(oldPartner);
            emit(new RemovePartnerEvent(partner));

        }

    }

    private void changePartnerParticipation(Partner partner) {

        Partner oldPartner = Utils.findPartner(this.partnership.getPartners(), partner.getAddress());

        if (oldPartner != null) {

            BigInteger newParticipation = partner.getParticipation();
            double newCommission = partner.getCommision();

            oldPartner.setParticipation(newParticipation);
            oldPartner.setCommision(newCommission);
            emit(new ChangePartnerParticipationEvent(partner));

        }

    }

    private void createPolling(Address initiator, int voteType, Partner partner) {

        require(this.getPayoutDate() > Block.timestamp(), "The current payout period is already finished, call \"doPayout\"");

        this.pollingManager.createPolling(initiator, voteType, partner, this.getPayoutDate());

    }

    private Partner getNewPartner(Address address, BigInteger participation, double commission) {

        require(address != null, "address can not be empty");
        require(participation.compareTo(BigInteger.ZERO) >= 0, "participation should be greater or equal than 0");
        require(commission >= 0 && commission <= 100, "commission should be between [0, 100]");
        require(!Utils.containsPartner(this.partnership.getPartners(), address), "address already belongs to a partner");

        return new Partner(address, participation, commission);

    }

    private Partner getModifiedPartner(Address address, BigInteger participation, double commission) {

        require(participation.compareTo(BigInteger.ZERO) >= 0, "participation should be greater or equal than 0");
        require(commission >= 0 && commission <= 100, "commission should be between [0, 100]");

        Partner partner = this.getPartner(address);

        return new Partner(partner.getAddress(), participation, commission);

    }

    private Partner getPartner(Address address) {

        require(address != null, "address can not be empty");

        Partner partner = Utils.findPartner(this.partnership.getPartners(), address);

        require(partner != null, "address doesn't belong to any partner");

        return partner;

    }

    private long getPayoutDate() {

        return (this.partnership.getLastPayoutTime() + this.partnership.getPayoutInterval());

    }

    private double getPartnerCommission() {

        return (100 - this.partnership.getNodeCommission());

    }

    private BigInteger getTotalParticipation(List<Partner> partners) {

        BigInteger totalParticipation = BigInteger.ZERO;

        for (int i = 0; i < partners.size(); i++) {

            Partner partner = partners.get(i);
            totalParticipation = totalParticipation.add(BigInteger.valueOf((long) (partner.getParticipation().doubleValue() * (1 - (partner.getCommision() / 100)))));

        }

        return totalParticipation;

    }

}
