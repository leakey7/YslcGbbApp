package com.gzyslczx.yslc.events;

public class NoticeLauncherInitEvent {

    private boolean flag;

    public NoticeLauncherInitEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }
}
