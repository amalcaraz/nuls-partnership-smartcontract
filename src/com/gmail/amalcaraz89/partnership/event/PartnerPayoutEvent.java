package com.gmail.amalcaraz89.partnership.event;

import com.gmail.amalcaraz89.partnership.model.PartnerPayout;
import io.nuls.contract.sdk.Address;
import io.nuls.contract.sdk.Event;

import java.math.BigInteger;

public class PartnerPayoutEvent extends PartnerPayout implements Event {

    public PartnerPayoutEvent(long date, Address address, BigInteger amount) {
       super(date, address, amount);
    }
}
