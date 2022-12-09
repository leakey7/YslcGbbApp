package com.gzyslczx.yslc.tools.yourui.myviews;

import com.gzyslczx.yslc.tools.yourui.DailyMAEntity;
import com.yourui.sdk.message.use.StockKLine;

public interface OnDailyLongPressListener {

    void onDailyLongPress(boolean needTime, StockKLine stockKLine, DailyMAEntity dailyMAEntity);

    void onKDJLongPress(double K, double D, double J);

    void onMACDLongPress(double MACD, double DIFF, double DEA);

    void onVOLLongPress(long volume, long money, int color);

    void onBOLLLongPress(double M, double U, double D);

    void onASILongPress(double asi, double asit);

    void onWRLongPress(double wr14, double wr28);

    void onBIASLongPress(double bias6, double bias12, double bias24);

    void onRSILongPress(double rsi6, double rsi12, double rsi24);

    void onVRLongPress(double vr);

}
