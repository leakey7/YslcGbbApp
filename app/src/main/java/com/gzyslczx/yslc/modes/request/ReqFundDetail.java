package com.gzyslczx.yslc.modes.request;

public class ReqFundDetail {

    private String fundcode, userid;

    public ReqFundDetail(String fundcode) {
        this.fundcode = fundcode;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
