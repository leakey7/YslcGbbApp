package com.gzyslczx.yslc.modes.response;

public class ResKLineList {

    private boolean IsSuccess;
    private String Message;
    private ResKLineListObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResKLineListObj getResultObj() {
        return ResultObj;
    }
}
