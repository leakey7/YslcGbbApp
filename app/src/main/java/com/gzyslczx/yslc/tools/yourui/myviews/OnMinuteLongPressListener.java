package com.gzyslczx.yslc.tools.yourui.myviews;

public interface OnMinuteLongPressListener {

    void onMinuteLongPress(float yesterdayPrice, double realPrice, float avePrice, double gain, boolean needTime, String time, long volume, double oldPrice);

    void onCancelMinuteLongPress();
}
