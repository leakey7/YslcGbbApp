package com.gzyslczx.yslc.events.yourui;

import com.gzyslczx.yslc.tools.yourui.HisTrendExtEntity;

public class FiveDayMinuteEvent {

    private HisTrendExtEntity hisTrendExtEntity;
    private long date;

    public FiveDayMinuteEvent(HisTrendExtEntity hisTrendExtEntity, long date) {
        this.hisTrendExtEntity = hisTrendExtEntity;
        this.date = date;
    }

    public HisTrendExtEntity getHisTrendExtEntity() {
        return hisTrendExtEntity;
    }

    public long getDate() {
        return date;
    }
}
