package com.gmail.amalcaraz89.partnership.model;

import io.nuls.contract.sdk.Block;

public class PartnershipResume {

    protected String title;
    protected String desc;
    protected double nodeCommission;
    protected long payoutInterval;
    protected long lastPayoutTime = Block.timestamp();

    public PartnershipResume(String title, String desc, double nodeCommission, long payoutInterval) {
        this.title = title;
        this.desc = desc;
        this.nodeCommission = nodeCommission;
        this.payoutInterval = payoutInterval;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) { this.desc = desc; }

    public double getNodeCommission() { return nodeCommission; }

    public void setNodeCommission(double nodeCommission) { this.nodeCommission = nodeCommission; }

    public long getPayoutInterval() { return payoutInterval; }

    public void setPayoutInterval(long payoutInterval) { this.payoutInterval = payoutInterval; }

    public long getLastPayoutTime() { return lastPayoutTime; }

    public void setLastPayoutTime(long lastPayoutTime) { this.lastPayoutTime = lastPayoutTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartnershipResume that = (PartnershipResume) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        if (nodeCommission != that.nodeCommission) return false;
        if (payoutInterval != that.payoutInterval) return false;
        if (lastPayoutTime != that.lastPayoutTime) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + Double.valueOf(nodeCommission).hashCode();
        result = 31 * result + (int) (payoutInterval ^ (payoutInterval >>> 32));
        result = 31 * result + (int) (lastPayoutTime ^ (lastPayoutTime >>> 32));

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
                '}';
    }
}
