package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResKLineType {

    private boolean IsSuccess;
    private String Message;
    private List<ResKLineTypeObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResKLineTypeObj> getResultObj() {
        return ResultObj;
    }
}
