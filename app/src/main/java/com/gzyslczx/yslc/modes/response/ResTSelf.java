package com.gzyslczx.yslc.modes.response;

public class ResTSelf {

    private boolean IsSuccess;
    private String Message;
    private ResTSelfObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResTSelfObj getResultObj() {
        return ResultObj;
    }
}
