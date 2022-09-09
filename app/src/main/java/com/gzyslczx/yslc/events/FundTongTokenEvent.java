package com.gzyslczx.yslc.events;

public class FundTongTokenEvent {

    private boolean FLAG;
    private String TOKEN, ERROR;

    public FundTongTokenEvent(boolean FLAG, String TOKEN) {
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
