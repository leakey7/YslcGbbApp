package com.gzyslczx.yslc.events.yourui;

public class MoreTypeStockEvent {

    private Short type;
    private Short remit=99;

    public MoreTypeStockEvent(Short type) {
        this.type = type;
    }

    public MoreTypeStockEvent(Short type, Short remit) {
        this.type = type;
        this.remit = remit;
    }

    public Short getType() {
        return type;
    }

    public Short getRemit() {
        return remit;
    }
}
