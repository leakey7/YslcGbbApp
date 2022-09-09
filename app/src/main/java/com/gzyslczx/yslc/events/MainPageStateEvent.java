package com.gzyslczx.yslc.events;

public class MainPageStateEvent {

    private int state;

    public MainPageStateEvent() {
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
