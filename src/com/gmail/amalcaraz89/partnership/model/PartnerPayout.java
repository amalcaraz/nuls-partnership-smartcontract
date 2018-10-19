package com.gmail.amalcaraz89.partnership.model;

import io.nuls.contract.sdk.Address;
import java.math.BigInteger;

public class PartnerPayout {

    private long date;
    private Address address;
    private BigInteger amount;

    public PartnerPayout(long date, Address address, BigInteger amount) {
        this.date = date;
        this.address = address;
        this.amount = amount;
    }

    public long getDate() { return date; }

    public void setDate(long date) { this.date = date; }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }

    public BigInteger getAmount() { return amount; }

    public void setAmount(BigInteger amount) { this.amount = amount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartnerPayout that = (PartnerPayout) o;

        if (date != that.date) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;

        return true;

    }

    @Override
    public int hashCode() {
        int result = (int) (date ^ (date >>> 32));
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "date: " + date +
                ", address: \"" + address + "\"" +
                ", amount: " + amount +
                '}';
    }

}
