package com.gzyslczx.yslc.adapters.fund.bean;

public class FundDetailGainData {

    private String v, t;
    private boolean isUp;

    public FundDetailGainData(String v, String t) {
        this.v = v;
        this.t = t;
        if (v.substring(0, 1).equals("-")){
            isUp = false;
        }else {
            isUp = true;
        }
    }

    public String getV() {
        return v;
    }

    public String getT() {
        return t;
    }

    public boolean isUp() {
        return isUp;
    }
}
