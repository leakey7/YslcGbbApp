package com.gzyslczx.yslc.events;

public class LogoutEvent {

    private boolean Flag;
    private String Error;

    public LogoutEvent(boolean flag) {
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
