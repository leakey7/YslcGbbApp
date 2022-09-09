package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResLabelArtObj;
import com.gzyslczx.yslc.modes.response.ResTeacherArtObj;

public class GuBbTeacherDetailEvent {

    private boolean Flag;
    private ResTeacherArtObj Data;
    private String Error;

    public GuBbTeacherDetailEvent(boolean flag, ResTeacherArtObj data) {
        Flag = flag;
        Data = data;
    }

    public boolean isFlag() {
        return Flag;
    }

    public ResTeacherArtObj getData() {
        return Data;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getError() {
        return Error;
    }
}
