package com.gzyslczx.yslc.events;

public class NoticeMainRefreshEvent {

    private int ShowPage;

    public NoticeMainRefreshEvent(int showPage) {
        ShowPage = showPage;
    }

    public int getShowPage() {
        return ShowPage;
    }
}
