package com.gzyslczx.stockmarketlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MinuteChartView extends BaseChartView {

    private final String TAG = "分时图";

    private float DefItemSize = 241; //默认数据量

    private float AveWidth; //子项平均宽度
    private float AveHeight; //子项平均高度
    private HashSet<String> lineTag; //线段标识，不可重复

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
        AveWidth = getMeasuredWidth() / DefItemSize; //平均宽度度
        float HalfOfAveWidth = AveWidth / 2f; // 平均宽度的一半
        //折线适配器
        if (getLineAdapter() != null && getLineAdapter().getLineItemSize() > 0 && lineTag != null) {
            AveHeight = (getLineAdapter().getMAX() - getLineAdapter().getMIN()) / getBtmLineOnX(); //平均高度
            getLineAdapter().DrawLine(canvas, lineTag, AveHeight, AveWidth, HalfOfAveWidth, TAG); //绘制线段
        }
    }

    @Override
    void InitViewAttr(TypedArray typedArray) {
    }


    public float getItemSize() {
        return DefItemSize;
    }

    public void setItemSize(float ItemSize) {
        if (ItemSize>0) { //小于或等于0默认条目数
            DefItemSize = ItemSize;
        }
    }

    public void addLineTag(@NonNull String ... tag) {
        if (lineTag == null) {
            lineTag = new HashSet<>();
        }
        for (String str : tag) {
            lineTag.add(str);
        }
    }

}
