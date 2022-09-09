package com.gzyslczx.yslc.events;

public class NoticeUpdateMineInfoEvent {
    private boolean needUpdate;

    public NoticeUpdateMineInfoEvent(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }
}
