package com.gzyslczx.yslc.events;

public class OptionDragEvent {

    private boolean Flag;
    private String Error;

    public OptionDragEvent(boolean flag) {
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
