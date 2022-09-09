package com.gzyslczx.yslc.modes.response;

public class ResFundFindRank {

    private boolean IsSuccess;
    private String Message;
    private ResFundFindRankObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResFundFindRankObj getResultObj() {
        return ResultObj;
    }
}
