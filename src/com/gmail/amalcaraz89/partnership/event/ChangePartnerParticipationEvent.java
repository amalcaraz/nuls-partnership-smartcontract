package com.gmail.amalcaraz89.partnership.event;

import com.gmail.amalcaraz89.partnership.model.Partner;
import io.nuls.contract.sdk.Event;

public class ChangePartnerParticipationEvent extends Partner implements Event {

    public ChangePartnerParticipationEvent(Partner partner) {
      super(partner);
    }
}
