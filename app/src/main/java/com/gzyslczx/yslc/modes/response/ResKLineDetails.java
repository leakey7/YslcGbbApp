package com.gzyslczx.yslc.modes.response;

public class ResKLineDetails {

    private boolean IsSuccess;
    private String Message;
    private ResKLineDetailsObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResKLineDetailsObj getResultObj() {
        return ResultObj;
    }
}
