package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResNewVersionObj;

public class NewVersionEvent {

    private boolean Flag;
    private String Error;
    private ResNewVersionObj DataObj;

    public NewVersionEvent(boolean flag, ResNewVersionObj dataObj) {
        Flag = flag;
        DataObj = dataObj;
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

    public ResNewVersionObj getDataObj() {
        return DataObj;
    }
}
