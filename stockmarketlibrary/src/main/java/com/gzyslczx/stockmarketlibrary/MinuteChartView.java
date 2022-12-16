package com.gzyslczx.stockmarketlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.util.List;

public class MinuteChartView extends BaseChartView{

    private List<Line> mLines; //折线数据
    private float DefItemSize = 241; //默认数据量

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
        if (mLines!=null && mLines.size()>0){

        }
    }

    @Override
    void InitViewAttr(TypedArray typedArray) {

    }


    public float getItemSize() {
        return DefItemSize;
    }

    public void setItemSize(float ItemSize) {
        DefItemSize = ItemSize;
    }
}
