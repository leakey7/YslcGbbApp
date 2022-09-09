package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResConceptSelect {

    private boolean IsSuccess;
    private String Message;
    private List<ResConceptSelectObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResConceptSelectObj> getResultObj() {
        return ResultObj;
    }
}
