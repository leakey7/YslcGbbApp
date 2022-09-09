package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResFundTongHuShenObj;

public class FunTongHuShenEvent {

    private boolean Flag;
    private String Error;
    private ResFundTongHuShenObj Data;

    public FunTongHuShenEvent(boolean flag,ResFundTongHuShenObj Data) {
        Flag = flag;
        this.Data = Data;
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

    public ResFundTongHuShenObj getData() {
        return Data;
    }
}
