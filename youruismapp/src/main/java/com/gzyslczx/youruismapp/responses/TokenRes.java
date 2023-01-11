package com.gzyslczx.youruismapp.responses;

public class TokenRes {

    private int code;
    private String msg, token;
    private long endDate;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getToken() {
        return token;
    }

    public long getEndDate() {
        return endDate;
    }
}
