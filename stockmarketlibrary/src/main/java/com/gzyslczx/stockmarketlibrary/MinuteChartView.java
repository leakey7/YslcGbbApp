package com.gzyslczx.stockmarketlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class MinuteChartView extends BaseChartView{

    public MinuteChartView(Context context) {
        super(context);
    }

    public MinuteChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MinuteChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MinuteChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    void DrawChart(Canvas canvas) {

    }

    @Override
    void InitViewAttr(TypedArray typedArray) {

    }
}
