package com.gzyslczx.yslc.events;

public class OptionSetTopEvent {

    private boolean Flag;
    private String Error;

    public OptionSetTopEvent(boolean flag) {
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
