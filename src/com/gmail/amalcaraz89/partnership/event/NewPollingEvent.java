package com.gmail.amalcaraz89.partnership.event;

import com.gmail.amalcaraz89.partnership.model.Polling;
import io.nuls.contract.sdk.Event;

public class NewPollingEvent extends Polling implements Event {

    public NewPollingEvent(Polling polling) {
        super(polling);
    }
}
