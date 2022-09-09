package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResLabelArtObj;

public class GuBbLabelDetailEvent {

    private boolean Flag;
    private ResLabelArtObj Data;
    private String Error;


    public GuBbLabelDetailEvent(boolean flag, ResLabelArtObj data) {
        Flag = flag;
        Data = data;
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

    public ResLabelArtObj getData() {
        return Data;
    }
}
