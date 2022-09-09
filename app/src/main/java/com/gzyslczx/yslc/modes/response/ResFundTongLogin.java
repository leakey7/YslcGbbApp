package com.gzyslczx.yslc.modes.response;

public class ResFundTongLogin {

    private boolean IsSuccess;
    private String Message;
    private ResFundTongLoginObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResFundTongLoginObj getResultObj() {
        return ResultObj;
    }
}
