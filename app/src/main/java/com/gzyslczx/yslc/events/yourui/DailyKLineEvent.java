package com.gzyslczx.yslc.events.yourui;

import com.gzyslczx.yslc.tools.yourui.KlineEntity;

public class DailyKLineEvent {

    private KlineEntity klineEntity;
    private int offset, count;
    private boolean isEnd = false;
    private short period;

    public DailyKLineEvent(KlineEntity klineEntity, int offset, int count, short PERIOD) {
        this.klineEntity = klineEntity;
        this.offset = offset;
        this.count = count;
        this.period = PERIOD;
        isEnd = false;
    }

    public KlineEntity getKlineEntity() {
        return klineEntity;
    }

    public int getOffset() {
        return offset;
    }

    public int getCount() {
        return count;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public short getPeriod() {
        return period;
    }
}
