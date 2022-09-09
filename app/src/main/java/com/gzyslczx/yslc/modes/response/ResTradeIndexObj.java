package com.gzyslczx.yslc.modes.response;

public class ResTradeIndexObj {

    private String id, code, name, publishDate, date;
    private float close, rate;
    private boolean active;

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getDate() {
        return date;
    }

    public float getClose() {
        return close;
    }

    public float getRate() {
        return rate;
    }

    public boolean isActive() {
        return active;
    }
}
