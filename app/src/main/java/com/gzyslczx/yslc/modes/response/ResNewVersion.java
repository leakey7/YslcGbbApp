package com.gzyslczx.yslc.modes.response;

public class ResNewVersion {

    private boolean IsSuccess;
    private String Message;
    private ResNewVersionObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResNewVersionObj getResultObj() {
        return ResultObj;
    }
}
