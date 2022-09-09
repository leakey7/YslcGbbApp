package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResFundDetailObj;

public class FundTongDetailEvent {

    private boolean Flag;
    private String Error;
    private ResFundDetailObj DateObj;

    public FundTongDetailEvent(boolean flag, ResFundDetailObj dateObj) {
        Flag = flag;
        DateObj = dateObj;
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

    public ResFundDetailObj getDateObj() {
        return DateObj;
    }
}
