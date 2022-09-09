package com.gzyslczx.yslc.events;

public class SpecialSupPraiseEvent {

    private boolean Flag, IsPraise;
    private String Error;
    private int PraiseNum;
    private String Code;

    public SpecialSupPraiseEvent(boolean flag, boolean isPraise, int praiseNum) {
        Flag = flag;
        IsPraise = isPraise;
        PraiseNum = praiseNum;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
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

    public int getPraiseNum() {
        return PraiseNum;
    }
}
