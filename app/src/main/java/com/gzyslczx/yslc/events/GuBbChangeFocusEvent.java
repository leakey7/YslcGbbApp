package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResMyFocusObj;

public class GuBbChangeFocusEvent {

    private ResMyFocusObj FocusObj;
    private boolean IsFocus, Flag, IsTeacher;
    private String Error;

    public GuBbChangeFocusEvent(boolean flag, ResMyFocusObj focusObj, boolean isFocus, boolean isTeacher) {
        Flag = flag;
        FocusObj = focusObj;
        IsFocus = isFocus;
        IsTeacher = isTeacher;
    }

    public boolean isFlag() {
        return Flag;
    }

    public ResMyFocusObj getFocusObj() {
        return FocusObj;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public boolean isTeacher() {
        return IsTeacher;
    }
}
