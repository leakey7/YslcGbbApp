package com.gzyslczx.yslc.events;

public class NoticeYSFlashRefreshEvent {

    private int YsFlashType;

    public NoticeYSFlashRefreshEvent(int ysFlashType) {
        YsFlashType = ysFlashType;
    }

    public int getYsFlashType() {
        return YsFlashType;
    }
}
