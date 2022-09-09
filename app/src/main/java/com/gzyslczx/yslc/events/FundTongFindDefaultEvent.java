package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResFundTongDefaultObj;

public class FundTongFindDefaultEvent {

    private boolean Flag;
    private String Error;
    private ResFundTongDefaultObj Data;

    public FundTongFindDefaultEvent(boolean flag, ResFundTongDefaultObj data) {
        Flag = flag;
        Data = data;
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

    public ResFundTongDefaultObj getData() {
        return Data;
    }
}
