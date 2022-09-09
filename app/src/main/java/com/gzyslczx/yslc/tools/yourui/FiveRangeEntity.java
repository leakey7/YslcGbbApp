package com.gzyslczx.yslc.tools.yourui;

public class FiveRangeEntity {

    private String tag;
    private double price, prePrise;
    private long volume;

    public FiveRangeEntity(String tag, double price, double prePrise, long volume) {
        this.tag = tag;
        this.price = price;
        this.prePrise = prePrise;
        this.volume = volume;
    }

    public String getTag() {
        return tag;
    }

    public double getPrice() {
        return price;
    }

    public long getVolume() {
        return volume;
    }

    public double getPrePrise() {
        return prePrise;
    }
}
