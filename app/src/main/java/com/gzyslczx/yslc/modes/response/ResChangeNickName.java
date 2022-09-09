package com.gzyslczx.yslc.modes.response;

public class ResChangeNickName {

    private boolean IsSuccess;
    private String Message;
    private ResMineInfoObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResMineInfoObj getResultObj() {
        return ResultObj;
    }
}
