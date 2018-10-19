package com.gmail.amalcaraz89.partnership.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Partnership extends PartnershipResume {

    private BigInteger totalPayoutsAmount = BigInteger.ZERO;
    private List<Partner> partners = new ArrayList<Partner>();
    private List<Payout> lastPayouts = new ArrayList<Payout>();

    public Partnership(String title, String desc, double nodeCommission, long payoutInterval) {
        super(title, desc, nodeCommission, payoutInterval);
    }

    public BigInteger getTotalPayoutsAmount() { return totalPayoutsAmount; }

    public void setTotalPayoutsAmount(BigInteger totalPayoutsAmount) { this.totalPayoutsAmount = totalPayoutsAmount; }

    public List<Partner> getPartners() { return partners; }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }

    public List<Payout> getLastPayouts() { return lastPayouts; }

    public void setLastPayouts(List<Payout> lastPayouts) { this.lastPayouts = lastPayouts; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Partnership that = (Partnership) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        if (nodeCommission != that.nodeCommission) return false;
        if (payoutInterval != that.payoutInterval) return false;
        if (lastPayoutTime != that.lastPayoutTime) return false;
        if (totalPayoutsAmount != null ? !totalPayoutsAmount.equals(that.totalPayoutsAmount) : that.totalPayoutsAmount != null) return false;
        if (partners != null ? partners.equals(that.partners) : that.partners == null) return false;
        if (lastPayouts != null ? lastPayouts.equals(that.lastPayouts) : that.lastPayouts == null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (totalPayoutsAmount != null ? totalPayoutsAmount.hashCode() : 0);
        result = 31 * result + (partners != null ? partners.hashCode() : 0);
        result = 31 * result + (lastPayouts != null ? lastPayouts.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "title: \"" + title + "\"" +
                ", desc: \"" + desc + "\"" +
                ", nodeCommission: " + nodeCommission +
                ", payoutInterval: " + payoutInterval +
                ", lastPayoutTime: " + lastPayoutTime +
                ", totalPayoutsAmount: " + totalPayoutsAmount +
                ", partners: " + partners.toString() +
                ", lastPayouts: " + lastPayouts.toString() +
                '}';
    }
}
