package com.gzyslczx.stockmarketlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class MinuteChartView extends BaseChartView{


    private float DefItemSize = 241; //默认数据量

    private float AveWidth; //子项平均宽度
    private float AveHeight; //子项平均高度
    private float MaxValue, MinValue;

    private Paint paint;

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
        //折线适配器
        if (getLineAdapter()!=null){
        }


    }

    @Override
    void InitViewAttr(TypedArray typedArray) {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2f);
    }


    public float getItemSize() {
        return DefItemSize;
    }

    public void setItemSize(float ItemSize) {
        DefItemSize = ItemSize;
    }

}
