package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMineInfoObj;

public class UserChangeNickNameEvent {

    private boolean Flag;
    private String Error;
    private ResMineInfoObj Data;

    public UserChangeNickNameEvent(boolean flag, ResMineInfoObj data) {
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

    public ResMineInfoObj getData() {
        return Data;
    }
}
