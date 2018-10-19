package com.gmail.amalcaraz89.partnership.func;

import com.gmail.amalcaraz89.partnership.model.Partner;
import com.gmail.amalcaraz89.partnership.model.Polling;
import io.nuls.contract.sdk.Address;

import java.util.List;

public interface PollingManagerInterface {

    Polling createPolling(Address initiator, int voteType, Partner partner, long expirationDate);

    Polling vote(long pollingId, Partner partner, boolean vote);

    Polling resolvePolling(long pollingId, List<Partner> participants);

    List<Polling> getPollingList();

    List<Polling> getPendingPollingList();

    Polling getPollingDetail(long pollingId);

    List<Partner> getParticipantsPendingForVote(Polling polling, List<Partner> participants);

}
