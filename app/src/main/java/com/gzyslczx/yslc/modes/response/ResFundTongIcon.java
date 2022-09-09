package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResFundTongIcon {

    private boolean IsSuccess;
    private String Message;
    private List<ResFundTongIconObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResFundTongIconObj> getResultObj() {
        return ResultObj;
    }
}
