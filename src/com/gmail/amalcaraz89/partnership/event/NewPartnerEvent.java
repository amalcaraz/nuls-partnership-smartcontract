package com.gmail.amalcaraz89.partnership.event;

import com.gmail.amalcaraz89.partnership.model.Partner;
import io.nuls.contract.sdk.Event;

public class NewPartnerEvent extends Partner implements Event {

    public NewPartnerEvent(Partner partner) {
      super(partner);
    }
}
