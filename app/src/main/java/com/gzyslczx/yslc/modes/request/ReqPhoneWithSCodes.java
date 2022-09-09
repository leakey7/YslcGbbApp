package com.gzyslczx.yslc.modes.request;

public class ReqPhoneWithSCodes {
    private String phone;
    private String[] stockcodes;

    public ReqPhoneWithSCodes(String phone, String[] stockcodes) {
        this.phone = phone;
        this.stockcodes = stockcodes;
    }
}
