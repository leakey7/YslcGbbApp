package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMsgBox {

    private boolean IsSuccess;
    private String Message;
    private List<ResMsgBoxObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMsgBoxObj> getResultObj() {
        return ResultObj;
    }
}
