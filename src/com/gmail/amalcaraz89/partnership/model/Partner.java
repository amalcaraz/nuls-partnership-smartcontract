package com.gmail.amalcaraz89.partnership.model;

import io.nuls.contract.sdk.Address;
import java.math.BigInteger;

public class Partner {

    private Address address;
    private BigInteger participation = BigInteger.ZERO;
    private double commission = 0;

    public Partner(Address address, BigInteger participation, double commission) {
        this.address = address;
        this.participation = participation;
        this.commission = commission;
    }

    public Partner(Partner partner) {
        this.address = partner.address;
        this.participation = partner.participation;
        this.commission = partner.commission;
    }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }

    public BigInteger getParticipation() { return participation; }

    public void setParticipation(BigInteger participation) { this.participation = participation; }

    public double getCommision() { return commission; }

    public void setCommision(double commission) { this.commission = commission; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Partner that = (Partner) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (participation != null ? !participation.equals(that.participation) : that.participation != null) return false;
        if (commission != that.commission) return false;

        return true;

    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (participation != null ? participation.hashCode() : 0);
        result = 31 * result + Double.valueOf(commission).hashCode();

        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "address: \"" + address + "\"" +
                ", participation: " + participation +
                ", commission: " + commission +
                '}';
    }

}
