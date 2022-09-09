package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMineInfoObj;

public class MineInfoEvent {

    private boolean Flag;
    private String Error;
    private ResMineInfoObj DataObj;

    public MineInfoEvent(boolean flag, ResMineInfoObj dataObj) {
        Flag = flag;
        DataObj = dataObj;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isFlag() {
        return Flag;
    }

    public String getError() {
        return Error;
    }

    public ResMineInfoObj getDataObj() {
        return DataObj;
    }
}
