package com.gzyslczx.yslc.events;

public class DeleteFundOptionEvent {

    private boolean Flag;
    private String Error;

    public DeleteFundOptionEvent(boolean flag) {
        Flag = flag;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}
