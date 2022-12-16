package com.gzyslczx.stockmarketlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public abstract class BaseChartView extends View {

    private int HorDivLineNum = 0;//横分割实线数
    private int VerDivLineNum = 0;//竖分割实线数
    private boolean HorCenterDotted = false;//横中线虚线
    private boolean VerCenterDotted = false;//竖中线虚线
    private int DivideLineColor;//分割实线颜色
    private int DottedColor;//虚线颜色
    private float XAxisItemHeight;//X轴标识区高度
    private float XAxisItemTextSize;//X轴标识字体大小
    private String[] XAxisItemText;
    private float BtmLineOnX;
    private Paint DividePaint, DottedPaint;

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
        DivideLineColor = typedArray.getColor(R.styleable.BaseChartView_DivideLineColor, Color.LTGRAY);
        DottedColor = typedArray.getColor(R.styleable.BaseChartView_DottedLineColor, Color.GRAY);
        XAxisItemHeight = typedArray.getDimension(R.styleable.BaseChartView_XAxisItemHeight, dp2px(context, 20));
        XAxisItemTextSize = typedArray.getDimension(R.styleable.BaseChartView_XAxisItemTextSize, sp2px(context, 14));
        InitDividePaint();
        InitDottedPaint();
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
        InitDraw(canvas);
        DrawChart(canvas);
    }

    private void InitDividePaint(){
        DividePaint = new Paint();
        DividePaint.setColor(DivideLineColor);
        DividePaint.setStyle(Paint.Style.FILL);
        DividePaint.setStrokeWidth(2);
    }

    private void InitDottedPaint(){
        DottedPaint = new Paint();
        DottedPaint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));
        DottedPaint.setStyle(Paint.Style.STROKE);
        DottedPaint.setStrokeWidth(2);
    }

    /*
    * 初始绘图
    * */
    private void InitDraw(Canvas canvas){
        if (XAxisItemHeight>0){
            BtmLineOnX = getMeasuredHeight()-XAxisItemHeight;
        }
        canvas.drawRect(0, 0, 0, BtmLineOnX, DividePaint); //边框
        if (VerDivLineNum>0){
            float aveDivideWidth = getMeasuredWidth() / (float)VerDivLineNum;
            if (VerDivLineNum%2==0){
                if (VerCenterDotted){
                    float center = getMeasuredWidth()/2f;
                    canvas.drawLine(center, 0, center, BtmLineOnX, DottedPaint);
                }
            }else{
                if (VerCenterDotted){

                }
            }
        }
        if (HorDivLineNum>0){

        }
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

    public int getHorDivLineNum() {
        return HorDivLineNum;
    }

    public void setHorDivLineNum(int horDivLineNum) {
        HorDivLineNum = horDivLineNum;
    }

    public int getVerDivLineNum() {
        return VerDivLineNum;
    }

    public void setVerDivLineNum(int verDivLineNum) {
        VerDivLineNum = verDivLineNum;
    }

    public boolean isHorCenterDotted() {
        return HorCenterDotted;
    }

    public void setHorCenterDotted(boolean horCenterDotted) {
        HorCenterDotted = horCenterDotted;
    }

    public boolean isVerCenterDotted() {
        return VerCenterDotted;
    }

    public void setVerCenterDotted(boolean verCenterDotted) {
        VerCenterDotted = verCenterDotted;
    }

    public int getDottedColor() {
        return DottedColor;
    }

    public void setDottedColor(int dottedColor) {
        DottedColor = dottedColor;
    }

    public float getXAxisItemHeight() {
        return XAxisItemHeight;
    }

    public void setXAxisItemHeight(float XAxisItemHeight) {
        this.XAxisItemHeight = XAxisItemHeight;
    }

    public float getXAxisItemTextSize() {
        return XAxisItemTextSize;
    }

    public void setXAxisItemTextSize(float XAxisItemTextSize) {
        this.XAxisItemTextSize = XAxisItemTextSize;
    }

    public int getDivideLineColor() {
        return DivideLineColor;
    }

    public void setDivideLineColor(int divideLineColor) {
        DivideLineColor = divideLineColor;
    }

    public String[] getXAxisItemText() {
        return XAxisItemText;
    }

    public void setXAxisItemText(String[] XAxisItemText) {
        this.XAxisItemText = XAxisItemText;
    }

    public float getBtmLineOnX() {
        return BtmLineOnX;
    }

    public void setBtmLineOnX(float btmLineOnX) {
        BtmLineOnX = btmLineOnX;
    }
}
