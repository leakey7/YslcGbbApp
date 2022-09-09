package com.gzyslczx.yslc.events;

public class AddFundOptionEvent {

    private boolean Flag;
    private String Error;


    public AddFundOptionEvent(boolean flag) {
        Flag = flag;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFlag() {
        return Flag;
    }
}
