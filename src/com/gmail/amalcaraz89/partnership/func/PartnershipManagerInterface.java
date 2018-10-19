package com.gmail.amalcaraz89.partnership.func;

import com.gmail.amalcaraz89.partnership.model.Partnership;
import com.gmail.amalcaraz89.partnership.model.Payout;
import com.gmail.amalcaraz89.partnership.model.Partner;
import com.gmail.amalcaraz89.partnership.model.Polling;
import io.nuls.contract.sdk.Address;

import java.math.BigInteger;
import java.util.List;

public interface PartnershipManagerInterface {

    void enterPartnership(Address address, BigInteger participation);

    void enterPartnership(Address address, BigInteger participation, double commission);

    void leavePartnership(Address address);

    void changeParticipation(Address address, BigInteger participation);

    void changeParticipation(Address address, BigInteger participation, double commission);

    void askAddPartner(Address initiator, Address address, BigInteger participation);

    void askAddPartner(Address initiator, Address address, BigInteger participation, double commission);

    void askRemovePartner(Address initiator, Address address);

    void askChangePartnerParticipation(Address initiator, Address address, BigInteger participation);

    void askChangePartnerParticipation(Address initiator, Address address, BigInteger participation, double commission);

    void checkForPayout();

    List<Payout> getPayoutList();

    Payout getPayoutDetail(long payoutDate);

    Partnership getPartnershipDetail();

    List<Partner> getPartnersList();

    Partner getPartnerDetail(Address address);

    List<Partner> getPartnersPendingOfVote(long pollingId);

    List<Polling> getPollingList();

    List<Polling> getPendingPollingList();

    Polling getPollingDetail(long pollingId);

    void rescueNuls(Address receiver);

    // Polling

    void resolvePollings();

    void vote(long pollingId, Address address, boolean vote);

    void resolvePolling(long pollingId);

}
