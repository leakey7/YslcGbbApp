package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResAdv;

public class FundTongHomeAdvEvent {

    private boolean Flag;
    private String Error;
    private ResAdv Data;

    public FundTongHomeAdvEvent(boolean flag, ResAdv data) {
        Flag = flag;
        Data = data;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public ResAdv getData() {
        return Data;
    }

    public void setError(String error) {
        Error = error;
    }
}
