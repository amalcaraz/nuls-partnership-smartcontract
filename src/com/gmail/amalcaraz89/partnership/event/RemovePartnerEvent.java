package com.gmail.amalcaraz89.partnership.event;

import com.gmail.amalcaraz89.partnership.model.Partner;
import io.nuls.contract.sdk.Event;

public class RemovePartnerEvent extends Partner implements Event {

    public RemovePartnerEvent(Partner partner) {
      super(partner);
    }
}
