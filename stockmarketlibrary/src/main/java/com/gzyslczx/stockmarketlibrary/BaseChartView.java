package com.gzyslczx.stockmarketlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public abstract class BaseChartView extends View {

    private int HorDivLineNum = 0;//横分割线数
    private int VerDivLineNum = 0;//竖分割线数
    private boolean HorCenterDotted = false;//横中线虚线
    private boolean VerCenterDotted = false;//竖中线虚线
    private int DottedColor;//虚线颜色
    private float XAxisItemHeight;//X轴标识区高度
    private float XAxisItemTextSize;//X轴标识字体大小

    public BaseChartView(Context context) {
        super(context);
    }

    public BaseChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseChartView);
        HorDivLineNum = typedArray.getInt(R.styleable.BaseChartView_HorizontalDivideLineNum, 0);
        VerDivLineNum = typedArray.getInt(R.styleable.BaseChartView_VerticalDivideLineNum, 0);
        HorCenterDotted = typedArray.getBoolean(R.styleable.BaseChartView_HorizontalCenterDotted, false);
        VerCenterDotted = typedArray.getBoolean(R.styleable.BaseChartView_VerticalCenterDotted, false);
        DottedColor = typedArray.getColor(R.styleable.BaseChartView_DottedLineColor, Color.GRAY);
        XAxisItemHeight = typedArray.getDimension(R.styleable.BaseChartView_XAxisItemHeight, dp2px(context, 20));
        XAxisItemTextSize = typedArray.getDimension(R.styleable.BaseChartView_XAxisItemTextSize, sp2px(context, 14));
        InitViewAttr(typedArray);
        typedArray.recycle();
    }

    public BaseChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawChart(canvas);
    }

    /*
    * 绘图函数
    * */
    abstract void DrawChart(Canvas canvas);

    /*
    * 初始化
    * */
    abstract void InitViewAttr(TypedArray typedArray);

    private float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    private float getFontDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public int sp2px(Context context, int sp) {
        return (int) (getFontDensity(context) * sp + 0.5);
    }

    public int dp2px(Context context, int dp) {
        return (int) (getDensity(context) * dp + 0.5);
    }

}
