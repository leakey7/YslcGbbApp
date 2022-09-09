package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMainFinancingBuy {

    private boolean IsSuccess;
    private String Message;
    private List<ResMainFinancingBuyObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMainFinancingBuyObj> getResultObj() {
        return ResultObj;
    }
}
