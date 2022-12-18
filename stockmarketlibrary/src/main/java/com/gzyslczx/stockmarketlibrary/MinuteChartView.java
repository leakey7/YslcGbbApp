package com.gzyslczx.stockmarketlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MinuteChartView extends BaseChartView{

    private List<Line> mLines; //折线数据
    private float DefItemSize = 241; //默认数据量

    private float AveWidth; //子项平均宽度
    private float AveHeight; //子项平均高度
    private float MaxValue=Float.MIN_VALUE, MinValue=Float.MAX_VALUE; //子项最大值和最小值

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
        AveWidth = getMeasuredWidth() / DefItemSize;
        AveHeight = getBtmLineOnX() / (MaxValue - MinValue);
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

    public void AddLineData(Line line){
        if (mLines==null){
            mLines = new ArrayList<Line>();
        }
        MaxValue = Math.max(MaxValue, line.getYValue());
        MinValue = Math.min(MinValue, line.getYValue());
        mLines.add(line);
    }

    public void AddLineData(List<Line> lineList){
        if (mLines==null){
            mLines = new ArrayList<Line>();
        }
        for (int i=0; i<lineList.size(); i++) {
            MaxValue = Math.max(MaxValue, lineList.get(i).getYValue());
            MinValue = Math.min(MinValue, lineList.get(i).getYValue());
            mLines.add(lineList.get(i));
        }
    }

}
