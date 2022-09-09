package com.gzyslczx.yslc.events;

public class SuggestCommitEvent {

    private boolean Flag;
    private String Msg;

    public SuggestCommitEvent(boolean flag, String msg) {
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
