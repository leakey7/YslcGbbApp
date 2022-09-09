package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResLabelSelfInfo;

public class LabelSelfInfoEvent {

    private boolean Flag;
    private ResLabelSelfInfo LabelSelfInfo;
    private String Error;

    public LabelSelfInfoEvent(boolean flag, ResLabelSelfInfo labelSelfInfo) {
        Flag = flag;
        LabelSelfInfo = labelSelfInfo;
    }

    public boolean isFlag() {
        return Flag;
    }

    public ResLabelSelfInfo getLabelSelfInfo() {
        return LabelSelfInfo;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getError() {
        return Error;
    }
}
