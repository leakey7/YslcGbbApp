package com.gzyslczx.yslc.events.yourui;

public class NoticeDailyKLineLoadMoreEvent {

    private int offset;
    private short period;
    private short remitMode;

    public NoticeDailyKLineLoadMoreEvent(int offset, short period, short remitMode) {
        this.offset = offset;
        this.period = period;
        this.remitMode = remitMode;
    }

    public int getOffset() {
        return offset;
    }

    public short getPeriod() {
        return period;
    }

    public short getRemitMode() {
        return remitMode;
    }
}
