package com.gzyslczx.yslc.events;

public class NoticeOptionPageChangeEvent {

    private int CurrentPage;

    public NoticeOptionPageChangeEvent(int currentPage) {
        CurrentPage = currentPage;
    }

    public int getCurrentPage() {
        return CurrentPage;
    }
}
