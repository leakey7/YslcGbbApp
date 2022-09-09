package com.gzyslczx.yslc.modes.response;

public class ResToken {

    private boolean IsSuccess;
    private String Message;
    private ResTokenObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResTokenObj getResultObj() {
        return ResultObj;
    }
}
