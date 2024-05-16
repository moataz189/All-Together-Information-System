package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

public class AddCallEvent {
    private DistressCall distressCall;

    public AddCallEvent(DistressCall distressCall) {
        this.distressCall = distressCall;
    }

    public DistressCall getDistressCall() {
        return distressCall;
    }
}