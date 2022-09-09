package com.gzyslczx.yslc.events;

public class NoticeFocusUpdateEvent {

    private String ID, ERROR;
    private boolean IsFocus, IsTeacher;

    public NoticeFocusUpdateEvent(String ID, boolean isFocus, boolean isTeacher) {
        this.ID = ID;
        IsFocus = isFocus;
        IsTeacher = isTeacher;
    }

    public String getID() {
        return ID;
    }

    public boolean isFocus() {
        return IsFocus;
    }

    public boolean isTeacher() {
        return IsTeacher;
    }

    public String getERROR() {
        return ERROR;
    }

    public void setERROR(String ERROR) {
        this.ERROR = ERROR;
    }
}
