package com.gzyslczx.yslc.events.yourui;

import com.yourui.sdk.message.use.FiveRangeData;

public class FiveRangeEvent {

    private FiveRangeData data;
    private double closePrice;

    public FiveRangeEvent(FiveRangeData data, double closePrice) {
        this.data = data;
        this.closePrice = closePrice;
    }

    public FiveRangeData getData() {
        return data;
    }

    public double getClosePrice() {
        return closePrice;
    }
}
