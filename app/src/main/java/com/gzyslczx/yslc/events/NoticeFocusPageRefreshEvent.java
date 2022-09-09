package com.gzyslczx.yslc.events;

public class NoticeFocusPageRefreshEvent {

    private int FocusPage;

    public NoticeFocusPageRefreshEvent(int focusPage) {
        FocusPage = focusPage;
    }

    public int getFocusPage() {
        return FocusPage;
    }
}
