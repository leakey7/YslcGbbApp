package com.gzyslczx.yslc.modes.response;

public class ResLogin {

    private boolean IsSuccess;
    private String Message;
    private ResLoginObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResLoginObj getResultObj() {
        return ResultObj;
    }
}
