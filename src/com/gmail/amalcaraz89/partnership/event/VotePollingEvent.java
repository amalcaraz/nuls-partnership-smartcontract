package com.gmail.amalcaraz89.partnership.event;

import io.nuls.contract.sdk.Address;
import io.nuls.contract.sdk.Event;

public class VotePollingEvent implements Event {

    private Long id;
    private Address partnerAddress;
    private boolean vote;

    public VotePollingEvent(Long id, Address partnerAddress, boolean vote) {
        this.id = id;
        this.partnerAddress = partnerAddress;
        this.vote = vote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VotePollingEvent that = (VotePollingEvent) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (partnerAddress != null ? !partnerAddress.equals(that.partnerAddress) : that.partnerAddress != null) return false;
        if (vote != that.vote) return false;

        return true;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (partnerAddress != null ? partnerAddress.hashCode() : 0);
        result = 31 * result + Boolean.valueOf(vote).hashCode();

        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", partnerAddress: " + partnerAddress +
                ", vote: " + vote +
                '}';
    }
}
