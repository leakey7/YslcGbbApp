package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResFundFindIndex {

    private boolean IsSuccess;
    private String Message;
    private List<ResTradeIndexObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResTradeIndexObj> getResultObj() {
        return ResultObj;
    }
}
