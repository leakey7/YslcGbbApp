package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResSpecialSupportObj;

import java.util.List;

public class SpecialSupByDateEvent {

    private boolean Flag;
    private ResSpecialSupportObj DataObj;
    private String Error;

    public SpecialSupByDateEvent(boolean flag, ResSpecialSupportObj dataObj) {
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

    public ResSpecialSupportObj getDataObj() {
        return DataObj;
    }
}
