package com.gzyslczx.yslc.events;

public class NoticeHiddenBtmBarEvent {

    private boolean isHidden;

    public NoticeHiddenBtmBarEvent(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public boolean isHidden() {
        return isHidden;
    }
}
