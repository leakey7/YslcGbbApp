package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResSearchNorObj;

public class SearchNormalEvent {

    private boolean Flag;
    private ResSearchNorObj DataObj;
    private String Error;

    public SearchNormalEvent(boolean flag, ResSearchNorObj dataObj) {
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

    public ResSearchNorObj getDataObj() {
        return DataObj;
    }
}
