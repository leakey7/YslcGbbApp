package com.gzyslczx.yslc.events;

public class GuBbChangePraiseEvent {

    private String Nid;
    private boolean Flag, IsPraise, IsTeacher;
    private int Position, PraiseNum;

    public GuBbChangePraiseEvent(String nid, boolean flag, boolean isTeacher, int position) {
        Nid = nid;
        Flag = flag;
        IsTeacher = isTeacher;
        Position = position;
    }

    public void setPraise(boolean praise) {
        IsPraise = praise;
    }

    public void setPraiseNum(int praiseNum) {
        PraiseNum = praiseNum;
    }

    public String getNid() {
        return Nid;
    }

    public boolean isFlag() {
        return Flag;
    }

    public boolean isPraise() {
        return IsPraise;
    }

    public boolean isTeacher() {
        return IsTeacher;
    }

    public int getPosition() {
        return Position;
    }

    public int getPraiseNum() {
        return PraiseNum;
    }
}
