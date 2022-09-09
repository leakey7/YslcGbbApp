package com.gzyslczx.yslc.events;

/*
* 反馈请求股扒扒Token权限事件
* */
public class GuBbTokenEvent {

    private boolean FLAG;
    private String TOKEN, ERROR;

    public GuBbTokenEvent(boolean FLAG, String TOKEN) {
        this.FLAG = FLAG;
        this.TOKEN = TOKEN;
    }

    public boolean isFLAG() {
        return FLAG;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public String getERROR() {
        return ERROR;
    }

    public void setERROR(String ERROR) {
        this.ERROR = ERROR;
    }
}
