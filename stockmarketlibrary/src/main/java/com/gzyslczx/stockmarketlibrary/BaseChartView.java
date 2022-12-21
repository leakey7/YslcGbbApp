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

    private int HorDivLineNum;//横分割实线数
    private int VerDivLineNum;//竖分割实线数
    private boolean HorCenterDotted = false;//横中线虚线
    private boolean VerCenterDotted = false;//竖中线虚线
    private int DivideLineColor;//分割实线颜色
    private int DottedColor;//虚线颜色
    private float XAxisItemHeight;//X轴标识区高度
    private float XAxisItemTextSize;//X轴标识字体大小
    private String[] XAxisItemText;
    private float BtmLineOnX;//底轴X坐标
    private float AveWidthOfArea;//分割区域平均宽度
    private float AveHeightOfArea;//分割区域平均高度
    private float CenterOnX;//中轴线X坐标
    private float CenterOnY;//中轴线Y坐标
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
        InitViewAttr(typedArray);
        InitDividePaint();
        InitDottedPaint();
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

    /*
     * 初始化实线分割线笔
     * */
    private void InitDividePaint() {
        DividePaint = new Paint();
        DividePaint.setColor(DivideLineColor);
        DividePaint.setStyle(Paint.Style.FILL);
        DividePaint.setStrokeWidth(2);
    }

    /*
     * 初始化虚线分割线笔
     * */
    private void InitDottedPaint() {
        DottedPaint = new Paint();
        DottedPaint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));
        DottedPaint.setStyle(Paint.Style.STROKE);
        DottedPaint.setStrokeWidth(2);
    }

    /*
     * 初始绘图
     * */
    private void InitDraw(Canvas canvas) {
        BtmLineOnX = getMeasuredHeight() - XAxisItemHeight;
        canvas.drawRect(0, 0, 0, BtmLineOnX, DividePaint); //边框
        AveWidthOfArea = getMeasuredWidth() / (VerDivLineNum + 1f); //实线分割区域平均宽度
        CenterOnX = getMeasuredWidth() / 2f; //竖中轴线X坐标
        if (VerCenterDotted) {
            //中轴竖虚线分割线
            canvas.drawLine(CenterOnX, 0, CenterOnX, BtmLineOnX, DottedPaint);
        }
        //竖分割实线条数大于0
        if (VerDivLineNum > 0) {
            if (VerDivLineNum == 1 && !VerCenterDotted) {
                //中间竖实线分割线
                canvas.drawLine(CenterOnX, 0, CenterOnX, BtmLineOnX, DividePaint);
            } else {
                //分割线数大于1
                if (VerDivLineNum % 2 == 0) {
                    //偶数分割线
                    for (int i = 1; i <= VerDivLineNum; i++) {
                        //实线分割线
                        canvas.drawLine(AveWidthOfArea * i, 0, AveWidthOfArea * i, BtmLineOnX, DividePaint);
                    }
                } else {
                    //奇数分割线
                    if (VerCenterDotted) {
                        //中轴线分割线为虚线
                        int centerIndex = (VerDivLineNum + 1) / 2;
                        for (int i = 1; i <= VerDivLineNum; i++) {
                            //实线分割线
                            if (i != centerIndex) {
                                canvas.drawLine(AveWidthOfArea * i, 0, AveWidthOfArea * i, BtmLineOnX, DividePaint);
                            }
                        }
                    } else {
                        //中轴线分割线为实线
                        for (int i = 1; i <= VerDivLineNum; i++) {
                            //实线分割线
                            canvas.drawLine(AveWidthOfArea * i, 0, AveWidthOfArea * i, BtmLineOnX, DividePaint);
                        }
                    }
                }
            }
        } //竖分割实线
        AveHeightOfArea = getMeasuredHeight() / (HorDivLineNum + 1f); //实线分割区域平均高度
        CenterOnY = getMeasuredHeight() / 2f; //横中轴线Y坐标
        if (HorCenterDotted) {
            //中轴横虚线分割线
            canvas.drawLine(0, CenterOnY, getMeasuredWidth(), CenterOnY, DottedPaint);
        }
        //横分割实线条数大于0
        if (HorDivLineNum > 0) {
            if (HorDivLineNum == 1 && !HorCenterDotted) {
                //中间横实线分割线
                canvas.drawLine(0, CenterOnY, getMeasuredWidth(), CenterOnY, DividePaint);
            } else {
                //分割线数大于1
                if (HorDivLineNum % 2 == 0) {
                    //偶数分割线
                    for (int i = 1; i <= HorDivLineNum; i++) {
                        //实线分割线
                        canvas.drawLine(0, AveHeightOfArea * i, getMeasuredWidth(), AveHeightOfArea * i, DividePaint);
                    }
                } else {
                    //奇数分割线
                    if (HorCenterDotted) {
                        //中轴线分割线为虚线
                        int centerIndex = (HorDivLineNum + 1) / 2;
                        for (int i = 1; i <= HorDivLineNum; i++) {
                            //实线分割线
                            if (i != centerIndex) {
                                canvas.drawLine(0, AveHeightOfArea * i, getMeasuredWidth(), AveHeightOfArea * i, DividePaint);
                            }
                        }
                    } else {
                        //中轴线分割线为实线
                        for (int i = 1; i <= HorDivLineNum; i++) {
                            //实线分割线
                            canvas.drawLine(0, AveHeightOfArea * i, getMeasuredWidth(), AveHeightOfArea * i, DividePaint);
                        }
                    }
                }
            }
        } //横分割实线
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

    public float getAveWidthOfArea() {
        return AveWidthOfArea;
    }

    public float getAveHeightOfArea() {
        return AveHeightOfArea;
    }

    public float getCenterOnX() {
        return CenterOnX;
    }

    public float getCenterOnY() {
        return CenterOnY;
    }

}
