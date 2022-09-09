package com.gzyslczx.yslc.events;

public class GuBbKLinePraiseEvent {

    private boolean Flag, IsPraise;
    private String Error;
    private int Id, PraiseNum, Cid, Position;

    public GuBbKLinePraiseEvent(boolean flag, boolean isPraise, int id, int praiseNum) {
        Flag = flag;
        IsPraise = isPraise;
        Id = id;
        PraiseNum = praiseNum;
    }

    public int getCid() {
        return Cid;
    }

    public void setCid(int cid) {
        Cid = cid;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
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

    public boolean isPraise() {
        return IsPraise;
    }

    public int getId() {
        return Id;
    }

    public int getPraiseNum() {
        return PraiseNum;
    }
}
