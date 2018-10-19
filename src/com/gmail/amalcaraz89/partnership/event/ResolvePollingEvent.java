package com.gmail.amalcaraz89.partnership.event;

import com.gmail.amalcaraz89.partnership.model.Polling;
import io.nuls.contract.sdk.Event;

public class ResolvePollingEvent extends Polling implements Event {

    public ResolvePollingEvent(Polling polling) {
        super(polling);
    }
}
