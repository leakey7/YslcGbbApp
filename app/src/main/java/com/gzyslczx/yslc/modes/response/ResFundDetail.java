package com.gzyslczx.yslc.modes.response;

public class ResFundDetail {

    private boolean IsSuccess;
    private String Message;
    private ResFundDetailObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResFundDetailObj getResultObj() {
        return ResultObj;
    }
}
