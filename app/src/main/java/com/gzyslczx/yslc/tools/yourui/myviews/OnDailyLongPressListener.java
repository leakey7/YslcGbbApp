package com.gzyslczx.yslc.tools.yourui.myviews;

import com.gzyslczx.yslc.tools.yourui.DailyMAEntity;
import com.yourui.sdk.message.use.StockKLine;

public interface OnDailyLongPressListener {

    void onDailyLongPress(boolean needTime, StockKLine stockKLine, DailyMAEntity dailyMAEntity);

    void onKDJLongPress(double K, double D, double J);

    void onMACDLongPress(double MACD, double DIFF, double DEA);

}
