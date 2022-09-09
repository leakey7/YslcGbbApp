package com.gzyslczx.yslc.events;

public class TeacherSelfInfoRefreshEvent {

    private int type;

    public TeacherSelfInfoRefreshEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
