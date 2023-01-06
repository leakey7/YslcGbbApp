package com.gzyslczx.yslc.events.yourui;

public class NoticeFiveDayMinuteEvent {

    private int date;
    private boolean isLoop;

    public NoticeFiveDayMinuteEvent(int date) {
        this.date = date;
        this.isLoop = false;
    }

    public NoticeFiveDayMinuteEvent(int date, boolean isLoop) {
        this.date = date;
        this.isLoop = isLoop;
    }

    public int getDate() {
        return date;
    }

    public boolean isLoop() {
        return isLoop;
    }
}
