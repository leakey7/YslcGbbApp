package com.gzyslczx.yslc.events;

public class UpdatePushEvent {

    private boolean Flag;
    private String Msg;

    public UpdatePushEvent(boolean flag, String msg) {
        Flag = flag;
        Msg = msg;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getMsg() {
        return Msg;
    }
}
