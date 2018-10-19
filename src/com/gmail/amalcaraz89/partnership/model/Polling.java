package com.gmail.amalcaraz89.partnership.model;

import com.gmail.amalcaraz89.partnership.func.PollingResult;
import com.gmail.amalcaraz89.partnership.func.PollingStatus;
import io.nuls.contract.sdk.Address;

import java.util.HashMap;
import java.util.Map;

public class Polling {

    private Long id;
    private int type;
    private int status = PollingStatus.OPEN;
    private int result = PollingResult.PENDING;
    private Address initiator;
    private Partner partner;
    private long startDate;
    private long endDate;
    private Map<Address, Boolean> votes = new HashMap<Address, Boolean>();

    public Polling() {}

    public Polling(Polling polling) {
        this.id = polling.id;
        this.type = polling.type;
        this.status = polling.status;
        this.result = polling.result;
        this.initiator = polling.initiator;
        this.partner = polling.partner;
        this.startDate = polling.startDate;
        this.endDate = polling.endDate;
        this.votes = polling.votes;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public int getResult() { return result; }

    public void setResult(int result) { this.result = result; }

    public Address getInitiator() { return initiator; }

    public void setInitiator(Address initiator) { this.initiator = initiator; }

    public Partner getPartner() { return partner; }

    public void setPartner(Partner partner) { this.partner = partner; }

    public long getStartDate() { return startDate; }

    public void setStartDate(long startDate) { this.startDate = startDate; }

    public long getEndDate() { return endDate; }

    public void setEndDate(long endDate) { this.endDate = endDate; }

    public Map<Address, Boolean> getVotes() { return votes; }

    public void setVotes(Map<Address, Boolean> votes) { this.votes = votes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Polling that = (Polling) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != that.type) return false;
        if (status != that.status) return false;
        if (result != that.result) return false;
        if (initiator != null ? initiator.equals(that.initiator) : that.initiator == null) return false;
        if (partner != null ? partner.equals(that.partner) : that.partner == null) return false;
        if (startDate != that.startDate) return false;
        if (endDate != that.endDate) return false;
        if (votes != null ? votes.equals(that.votes) : that.votes == null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + type;
        result = 31 * result + status;
        result = 31 * result + this.result;
        result = 31 * result + (initiator != null ? initiator.hashCode() : 0);
        result = 31 * result + (partner != null ? partner.hashCode() : 0);
        result = 31 * result + (int) (startDate ^ (startDate >>> 32));
        result = 31 * result + (int) (endDate ^ (endDate >>> 32));
        result = 31 * result + (votes != null ? votes.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", type: " + type +
                ", status: " + status +
                ", result: " + result +
                ", initiator: \"" + initiator + "\"" +
                ", partner: " + partner +
                ", startDate: " + startDate +
                ", endDate: " + endDate +
                ", votes: " + votes +
                '}';
    }
}
