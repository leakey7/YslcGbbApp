package com.gzyslczx.yslc.events;

import android.content.Intent;

public class GuBbTokenOnPushEvent {

    private boolean FLAG;
    private String TOKEN, ERROR;
    private Intent intent;

    public GuBbTokenOnPushEvent(boolean FLAG, String TOKEN) {
        this.FLAG = FLAG;
        this.TOKEN = TOKEN;
    }

    public String getERROR() {
        return ERROR;
    }

    public void setERROR(String ERROR) {
        this.ERROR = ERROR;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public boolean isFLAG() {
        return FLAG;
    }

    public String getTOKEN() {
        return TOKEN;
    }
}
