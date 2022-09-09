package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResFundTongTradeLevel {

    private boolean IsSuccess;
    private String Message;
    private List<ResFundTongTradeLevelObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResFundTongTradeLevelObj> getResultObj() {
        return ResultObj;
    }
}
