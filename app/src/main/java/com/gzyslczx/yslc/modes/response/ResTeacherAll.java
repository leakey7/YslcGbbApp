package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResTeacherAll {

    private boolean IsSuccess;
    private String Message;
    private List<ResTeacherAllObj> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResTeacherAllObj> getResultObj() {
        return ResultObj;
    }
}
