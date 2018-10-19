package com.gmail.amalcaraz89.partnership.event;

import com.gmail.amalcaraz89.partnership.model.PartnerPayout;
import com.gmail.amalcaraz89.partnership.model.Payout;
import io.nuls.contract.sdk.Event;

import java.math.BigInteger;
import java.util.List;

public class PayoutEvent extends Payout implements Event {

    public PayoutEvent(long date, BigInteger amount, List<PartnerPayout> partners) {
      super(date, amount, partners);
    }
}
