package com.gzyslczx.yslc.events;

public class UpdateMsgListEvent {

    private boolean Flag;
    private String Error;
    private int pos;

    public UpdateMsgListEvent(boolean flag) {
        Flag = flag;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
