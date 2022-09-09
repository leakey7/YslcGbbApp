package com.gzyslczx.yslc.modes.request;

public class ReqFundTongVerifyCodeLogin {

    /*
     * 默认Code = 927953;
     * */
    private String phone, code, platform;

    public ReqFundTongVerifyCodeLogin(String phone) {
        this.phone = phone;
        this.code = "927953";
        this.platform = "android";
    }
}
