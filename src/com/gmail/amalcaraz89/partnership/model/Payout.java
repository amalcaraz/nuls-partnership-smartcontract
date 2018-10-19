package com.gmail.amalcaraz89.partnership.model;

import java.math.BigInteger;
import java.util.List;

public class Payout {

    private long date;
    private BigInteger amount;
    private List<PartnerPayout> partners;

    public Payout(long date, BigInteger amount, List<PartnerPayout> partners) {
        this.date = date;
        this.amount = amount;
        this.partners = partners;
    }

    public long getDate() { return date; }

    public void setDate(long date) { this.date = date; }

    public BigInteger getAmount() { return amount; }

    public void setAmount(BigInteger amount) { this.amount = amount; }

    public List<PartnerPayout> getPartners() { return partners; }

    public void setPartners(List<PartnerPayout> partners) { this.partners = partners; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payout that = (Payout) o;

        if (date != that.date) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (partners != null ? partners.equals(that.partners) : that.partners == null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (date ^ (date >>> 32));
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (partners != null ? partners.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "date: " + date +
                ", amount: " + amount +
                ", partners: " + partners.toString() +
                '}';
    }
}
