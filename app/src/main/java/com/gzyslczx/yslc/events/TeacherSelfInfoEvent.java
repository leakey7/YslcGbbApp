package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResTSelfInfo;

public class TeacherSelfInfoEvent {

    private boolean Flag;
    private ResTSelfInfo info;
    private String Error;

    public TeacherSelfInfoEvent(boolean flag, ResTSelfInfo info) {
        Flag = flag;
        this.info = info;
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

    public ResTSelfInfo getInfo() {
        return info;
    }
}
