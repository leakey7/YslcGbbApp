package com.gzyslczx.yslc.modes.request;

public class ReqDragOption {

    private int sortinfo;
    private String phone, stockcode;

    public ReqDragOption(int sortinfo, String phone, String stockcode) {
        this.sortinfo = sortinfo;
        this.phone = phone;
        this.stockcode = stockcode;
    }
}
