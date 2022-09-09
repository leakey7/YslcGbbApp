package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResMainInstitutionBuy {

    private boolean IsSuccess;
    private String Message;
    private List<ResMainInstitutionBuyObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResMainInstitutionBuyObj> getResultObj() {
        return ResultObj;
    }
}
