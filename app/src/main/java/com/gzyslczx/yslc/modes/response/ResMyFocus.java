package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMyFocus {

    private boolean IsSuccess;
    private String Message;
    private List<ResMyFocusObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMyFocusObj> getResultObj() {
        return ResultObj;
    }
}
