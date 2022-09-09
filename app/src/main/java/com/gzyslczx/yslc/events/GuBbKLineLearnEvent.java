package com.gzyslczx.yslc.events;

public class GuBbKLineLearnEvent {

    private boolean Flag;
    private String Error;
    private int Cid;


    public GuBbKLineLearnEvent(boolean flag, int cid) {
        Flag = flag;
        Cid = cid;
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

    public int getCid() {
        return Cid;
    }
}
