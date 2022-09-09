package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResSpecialSupDetailObj;

public class SpecialSupDetailEvent {

    private boolean Flag;
    private ResSpecialSupDetailObj DataObj;
    private String Error;

    public SpecialSupDetailEvent(boolean flag, ResSpecialSupDetailObj dataObj) {
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

    public ResSpecialSupDetailObj getDataObj() {
        return DataObj;
    }
}
