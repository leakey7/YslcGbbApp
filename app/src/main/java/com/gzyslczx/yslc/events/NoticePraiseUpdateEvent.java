package com.gzyslczx.yslc.events;

public class NoticePraiseUpdateEvent {

    private boolean Flag, IsTeacher, IsPraise;
    private String NID;
    private int PraiseNum;

    public NoticePraiseUpdateEvent(boolean flag, boolean isTeacher, boolean isPraise, String NID, int praiseNum) {
        Flag = flag;
        IsTeacher = isTeacher;
        IsPraise = isPraise;
        this.NID = NID;
        PraiseNum = praiseNum;
    }

    public boolean isFlag() {
        return Flag;
    }

    public boolean isTeacher() {
        return IsTeacher;
    }

    public boolean isPraise() {
        return IsPraise;
    }

    public String getNID() {
        return NID;
    }

    public int getPraiseNum() {
        return PraiseNum;
    }
}
