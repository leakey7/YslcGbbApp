package com.gzyslczx.yslc.modes.response;

public class ResMsgInfoList {

    private boolean IsSuccess;
    private String Message;
    private ResMsgInfoListObj ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public ResMsgInfoListObj getResultObj() {
        return ResultObj;
    }
}
