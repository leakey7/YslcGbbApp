package com.gzyslczx.yslc.events;

public class GuBbHomeChangePageEvent {

    private int CurrentPage;

    public GuBbHomeChangePageEvent(int currentPage) {
        CurrentPage = currentPage;
    }

    public int getCurrentPage() {
        return CurrentPage;
    }
}
