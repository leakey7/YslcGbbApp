package com.gzyslczx.yslc.modes.response;

import java.util.List;

public class ResSearchArticleMore {

    private boolean IsSuccess;
    private String Message;
    private List<ResSearchAbout> ResultObj;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public List<ResSearchAbout> getResultObj() {
        return ResultObj;
    }
}
