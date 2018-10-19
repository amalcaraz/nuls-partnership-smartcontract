package com.gmail.amalcaraz89.partnership;

import com.gmail.amalcaraz89.partnership.func.PartnershipManager;
import com.gmail.amalcaraz89.partnership.func.PartnershipManagerInterface;
import com.gmail.amalcaraz89.partnership.func.Utils;
import com.gmail.amalcaraz89.partnership.model.Partner;
import com.gmail.amalcaraz89.partnership.model.Partnership;
import com.gmail.amalcaraz89.partnership.model.Payout;
import com.gmail.amalcaraz89.partnership.model.Polling;
import io.nuls.contract.sdk.Address;
import io.nuls.contract.sdk.Contract;
import io.nuls.contract.sdk.Msg;
import io.nuls.contract.sdk.annotation.Payable;
import io.nuls.contract.sdk.annotation.View;
import io.nuls.contract.sdk.annotation.Required;

import java.math.BigInteger;
import java.util.List;

public class PartnershipContract implements Contract {

    private final String name = "Smart Partnership v1.0.0";
    private PartnershipManagerInterface partnershipManager;

    public PartnershipContract(
            @Required String title,
            @Required String desc,
            @Required double nodeCommission,
            @Required long payoutInterval,
            @Required double initialDeposit,
            @Required double percentageOfParticipationToResolvePolling,
            @Required double percentageOfPositiveVotesToAcceptPolling
    ) {

        this.partnershipManager = new PartnershipManager(
                title,
                desc,
                nodeCommission,
                payoutInterval,
                percentageOfParticipationToResolvePolling,
                percentageOfPositiveVotesToAcceptPolling,
                Msg.sender()
        );

        Address sender = Msg.sender();
        BigInteger part = Utils.getNulsPrecision(initialDeposit);

        this.partnershipManager.enterPartnership(sender, part, 0);

    }

    @Payable
    public void enterPartnership(@Required double participation) {

        Address sender = Msg.sender();
        BigInteger part = Utils.getNulsPrecision(participation);

        this.partnershipManager.enterPartnership(sender, part);

    }

    @Payable
    public void leavePartnership() {

        Address sender = Msg.sender();

        this.partnershipManager.leavePartnership(sender);

    }

    @Payable
    public void changeParticipation(@Required double participation) {

        Address sender = Msg.sender();
        BigInteger part = Utils.getNulsPrecision(participation);

        this.partnershipManager.changeParticipation(sender, part);

    }

    @Payable
    public void addPartner(@Required Address address, @Required double participation) {

        Address sender = Msg.sender();
        BigInteger part = Utils.getNulsPrecision(participation);

        this.partnershipManager.askAddPartner(sender, address, part);

    }

    @Payable
    public void removePartner(@Required Address address) {

        Address sender = Msg.sender();

        this.partnershipManager.askRemovePartner(sender, address);

    }

    @Payable
    public void changePartnerParticipation(@Required Address address, @Required double participation) {

        Address sender = Msg.sender();
        BigInteger part = Utils.getNulsPrecision(participation);

        this.partnershipManager.askChangePartnerParticipation(sender, address, part);

    }

    @Payable
    public void vote(@Required long pollingId, @Required boolean vote) {

        Address sender = Msg.sender();

        this.partnershipManager.vote(pollingId, sender, vote);

    }

    @Payable
    public void resolvePolling(@Required long pollingId) {

        this.partnershipManager.resolvePolling(pollingId);

    }

    @Payable
    public void resolvePollings() {

        this.partnershipManager.resolvePollings();

    }

    @Payable
    public void doPayout() {

        this.partnershipManager.checkForPayout();

    }

    @View
    public Partnership viewPartnershipDetail() {

        return this.partnershipManager.getPartnershipDetail();

    }

    @View
    public List<Partner> viewPartnersList() {

        return this.partnershipManager.getPartnersList();

    }

    @View
    public Partner viewPartnerDetail(@Required Address address) {

        return this.partnershipManager.getPartnerDetail(address);

    }

    @View
    public List<Partner> viewPartnersPendingOfVote(@Required long pollingId) {

        return this.partnershipManager.getPartnersPendingOfVote(pollingId);

    }

    @View
    public List<Polling> viewPollingList() {

        return this.partnershipManager.getPollingList();

    }

    @View
    public List<Polling> viewPendingPollingList() {

        return this.partnershipManager.getPendingPollingList();

    }

    @View
    public Polling viewPollingDetail(@Required long pollingId) {

        return this.partnershipManager.getPollingDetail(pollingId);

    }

    @View
    public List<Payout> viewPayoutList() {

        return this.partnershipManager.getPayoutList();

    }

    @View
    public Payout viewPayoutDetail(@Required long payoutDate) {

        return this.partnershipManager.getPayoutDetail(payoutDate);

    }

    @View
    public List<Payout> viewNextPayoutPreviewList() {

        return this.partnershipManager.getPayoutList();

    }

    @View
    public Payout viewNextPayoutPreviewDetail(@Required long payoutDate) {

        return this.partnershipManager.getPayoutDetail(payoutDate);

    }

    @View
    public String name() {
        return name;
    }

    @Payable
    public void rescueNuls() {

        Address sender = Msg.sender();

        this.partnershipManager.rescueNuls(sender);

    }

    @Payable
    @Override
    public void _payable() {
        this.doPayout();
    }

}