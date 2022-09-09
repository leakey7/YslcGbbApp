package com.gzyslczx.yslc.tools.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;

public class HuShenCharView extends View implements GestureDetector.OnGestureListener {

    private int ViewH, ViewW;
    private float leftPatent, topParent, AveH, AveW, maxV, minV, textW=0;
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int itemSize = 1;
    private final float textSize = 24;
    private GestureDetector mDetector;
    private String leftTime, rightTime;
    private List<FindChartData> chartStockDataList, chartMixedDataList, chartHuShenDataList;
    private boolean isInit = false, isPress = false;
    private float DesL = 0, DesT = 0, DesR = 0, DesB = 0;
    private DecimalFormat format;
    private boolean DrawXYVale = false;


    public HuShenCharView(Context context) {
        super(context);
        Init();
    }

    public HuShenCharView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HuShenCharView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HuShenCharView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        leftPatent = left;
        topParent = top;
        InitVar();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInit) {
            isInit = false;
            DrawModel();
            if (isPress) {
                DrawSlidingLine();
                DrawDes();
            }
            canvas.drawBitmap(mBitmap, 0, 0, null);
        } else {
            if (chartStockDataList.size() > 0) {
                canvas.drawBitmap(mBitmap, 0, 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    /*
     * 初始化
     * */
    private void Init() {
        InitPaint();
        mDetector = new GestureDetector(getContext(), this);
        mDetector.setIsLongpressEnabled(false);
        //数据
        chartStockDataList = new ArrayList<FindChartData>();
        chartMixedDataList = new ArrayList<FindChartData>();
        chartHuShenDataList = new ArrayList<FindChartData>();
        format = new DecimalFormat("0.0000");
    }

    /*
     * 初始化基本变量
     * */
    private void InitVar() {
        ViewH = getMeasuredHeight();
        ViewW = getMeasuredWidth();
        mBitmap = Bitmap.createBitmap(ViewW, ViewH, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    /*
     * 初始化画笔
     * */
    private void InitPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setTextSize(textSize);
            mPaint.setStyle(Paint.Style.FILL);
        }
    }

    /*
     * 绘制框架
     * */
    private void DrawModel() {
        float modelStar = topParent;
        float modelEnd = topParent + ViewH;
        float lineStar = leftPatent + textW;
        float lineEnd = leftPatent + ViewW;
        float centerY = topParent + ViewH / 2f;
        float centerYTop = topParent + ViewH / 4f;
        float centerYBottom = topParent + ViewH * 3 / 4f;
        mPaint.setStrokeWidth(1f);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray_e6));
        mCanvas.drawLine(lineStar, centerY, lineEnd, centerY, mPaint);
        mCanvas.drawLine(lineStar, centerYTop, lineEnd, centerYTop, mPaint);
        mCanvas.drawLine(lineStar, centerYBottom, lineEnd, centerYBottom, mPaint);
//        DrawText(lineStar, topParent + textSize, topParent + ViewH - textSize, topParent + (ViewH - textSize) / 2f);
        DrawLine(chartStockDataList, R.color.main_blue);
        DrawLine(chartMixedDataList, R.color.yellow_FFCF00);
        DrawLine(chartHuShenDataList, R.color.main_red);
    }

    /*
     * 绘制文字
     * */
    private void DrawText(float starX, float starY, float endY, float centerY) {
        if (DrawXYVale) {
            mPaint.setStrokeWidth(2f);
            mPaint.setColor(ContextCompat.getColor(getContext(), R.color.black_333));
            mPaint.setTextAlign(Paint.Align.RIGHT);
            mCanvas.drawText("0", starX, endY, mPaint);
            mCanvas.drawText(String.valueOf(maxV), starX, starY, mPaint);
            mCanvas.drawText(format.format(maxV / 2f), starX, centerY, mPaint);
            float bottomPos = topParent + ViewH;
            int size = chartStockDataList.size();
            if (size % 2 == 0) {
                mPaint.setTextAlign(Paint.Align.LEFT);
                mCanvas.drawText(leftTime, starX, bottomPos, mPaint);
                mPaint.setTextAlign(Paint.Align.RIGHT);
                mCanvas.drawText(rightTime, leftPatent + ViewW, bottomPos, mPaint);
            } else {
                int index = size / 2;
                mPaint.setTextAlign(Paint.Align.LEFT);
                mCanvas.drawText(leftTime, starX, bottomPos, mPaint);
                mPaint.setTextAlign(Paint.Align.RIGHT);
                mCanvas.drawText(rightTime, leftPatent + ViewW, bottomPos, mPaint);
                mPaint.setTextAlign(Paint.Align.CENTER);
                mCanvas.drawText(chartStockDataList.get(index).getTime(), chartStockDataList.get(index).getxAxis(),
                        bottomPos, mPaint);
            }
        }
    }

    /*
     * 绘制折线
     * */
    private void DrawLine(List<FindChartData> chartData, int color) {
        mPaint.setStrokeWidth(4f);
        mPaint.setColor(ContextCompat.getColor(getContext(), color));
        for (int i = 1; i < chartData.size(); i++) {
            mCanvas.drawLine(chartData.get(i - 1).getxAxis(), chartData.get(i - 1).getyAxis(),
                    chartData.get(i).getxAxis(), chartData.get(i).getyAxis(), mPaint);
        }
    }

    /*
     * 绘制滑动线
     * */
    private void DrawSlidingLine() {
        mPaint.setStrokeWidth(1);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray_999));
        mCanvas.drawLine(PressX, topParent, PressX, topParent + ViewH - textSize * 3 / 2, mPaint);
    }

    /*
     * 绘制详情
     * */
    private void DrawDes() {
        mPaint.setStrokeWidth(2);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mCanvas.drawRect(DesL, DesT, DesR, DesB, mPaint);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.black_333));
        mPaint.setTextAlign(Paint.Align.LEFT);
        float start = DesL + textSize;
        int size = itemSize - 1;
        try {
            mCanvas.drawText(chartStockDataList.get(size - PressItem).getTime(), start, DesT + 2 * textSize, mPaint);
        } catch (ArrayIndexOutOfBoundsException exception) {
            mCanvas.drawText(chartStockDataList.get(size).getTime(), start, DesT + 2 * textSize, mPaint);

        }
        mCanvas.drawText("股票型比例：", start, DesT + 4 * textSize, mPaint);
        mCanvas.drawText("混合型比例：", start, DesT + 5 * textSize, mPaint);
        mCanvas.drawText("沪深300：", start, DesT + 6 * textSize, mPaint);
        float valueStart = start+mPaint.measureText("股票型比例：");
        mPaint.setTextAlign(Paint.Align.LEFT);
        try {
            mCanvas.drawText(String.valueOf(chartStockDataList.get(size - PressItem).getValue()),
                    valueStart, DesT + 4 * textSize, mPaint);
            mCanvas.drawText(String.valueOf(chartMixedDataList.get(size - PressItem).getValue()),
                    valueStart, DesT + textSize*5, mPaint);
            mCanvas.drawText(String.valueOf(chartHuShenDataList.get(size - PressItem).getValue()),
                    valueStart, DesT + 6 * textSize, mPaint);
        } catch (ArrayIndexOutOfBoundsException exception) {
            mCanvas.drawText(String.valueOf(chartStockDataList.get(size).getValue()),
                    valueStart, DesT + 4 * textSize, mPaint);
            mCanvas.drawText(String.valueOf(chartMixedDataList.get(size).getValue()),
                    valueStart, DesT + 5 * textSize, mPaint);
            mCanvas.drawText(String.valueOf(chartHuShenDataList.get(size).getValue()),
                    valueStart, DesT + 6 * textSize, mPaint);
        }
    }

    /*
     * 计算平均宽高
     * */
    private void CountAveValue() {
        float vHeight = maxV-minV+1f;
        AveH = ViewH / vHeight;
        AveW = ViewW / (itemSize - 1f);
    }

    public void AddStockData(float value, String time) {
        chartStockDataList.add(new FindChartData(value, time));
    }

    public void AddMixedData(float value, String time) {
        chartMixedDataList.add(new FindChartData(value, time));
    }

    public void AddHuShenData(float value, String time) {
        chartHuShenDataList.add(new FindChartData(value, time));
    }

    public void setMaxVMinV_ItemSize(float maxV, float minV, int itemSize) {
        this.maxV = maxV;
        this.minV = minV;
        if (DrawXYVale) {
            textW = mPaint.measureText(String.valueOf(maxV));
        }
        this.itemSize = itemSize;
    }

    public void isInitData() {
        CountAveValue();
        for (int i = 0; i < chartStockDataList.size(); i++) {
            float v = chartStockDataList.get(i).getValue();
            float v1 = chartMixedDataList.get(i).getValue();
            float v2 = chartHuShenDataList.get(i).getValue();
            float x = leftPatent + ViewW - AveW * i;
            float y = 0f;
            float y1 = 0f;
            float y2 = 0f;
            if (DrawXYVale) {
                y = topParent + ViewH - textSize * 3 / 2 - AveH * v;
                y1 = topParent + ViewH - textSize * 3 / 2 - AveH * v1;
                y2 = topParent + ViewH - textSize * 3 / 2 - AveH * v2;
            }else {
                y = (float) topParent + ((maxV-v)*AveH);
                y1 = (float) topParent + ((maxV-v1)*AveH);
                y2 = (float) topParent + ((maxV-v2)*AveH);
            }
            //股票型
            chartStockDataList.get(i).setxAxis(x);
            chartStockDataList.get(i).setyAxis(y);
            //混合型
            chartMixedDataList.get(i).setxAxis(x);
            chartMixedDataList.get(i).setyAxis(y1);
            //沪深三百型
            chartHuShenDataList.get(i).setxAxis(x);
            chartHuShenDataList.get(i).setyAxis(y2);
        }
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        isInit = true;
        isPress = false;
        invalidate();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        if (!isPress) {
            Observable.timer(1, TimeUnit.SECONDS)
                    .takeWhile(new Predicate<Long>() {
                        @Override
                        public boolean test(Long aLong) throws Throwable {
                            return !isPress;
                        }
                    }).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Throwable {
                    CountDes(e);
                }
            });
        } else {
            isPress = false;
            isInit = true;
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (isPress) {
            Log.d("触摸测试", "修改了isPress=" + isPress);
            isPress = false;
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("触摸测试", "正在滑动-isPress=" + isPress);
        if (isPress) {
            CountDes(e2);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (isPress) {
            isPress = false;
        }
        return true;
    }

    /*
     * 计算长按坐标
     * */
    private int PressItem = 0;
    private float PressX = 0;

    private void CountDes(MotionEvent e) {
        float oXAxis = e.getX();
        int last = itemSize - 1;
        if (leftPatent <= oXAxis && leftPatent + ViewW >= oXAxis) {
            isPress = true;
            float flag = oXAxis / AveW;
            int mark = (int) flag;
            if (flag > mark && flag < mark + 1 && mark > 0) {
                PressItem = mark + 1;
            } else if (flag >= 0 && flag < 1) {
                PressItem = mark;
            }
            if (PressItem >= 0 && PressItem <= last) {
                float nXAxis = chartStockDataList.get(last - PressItem).getxAxis();
                float nYAxis = chartStockDataList.get(last - PressItem).getyAxis();
                if (nXAxis >= oXAxis) {
                    float x = nXAxis;
                    CountDesPos(x, nYAxis);
                    SetupDes(x);
                }
            }
        } else {
            if (leftPatent < oXAxis || oXAxis > chartStockDataList.get(0).getxAxis()) {
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                isInit = true;
                isPress = true;
            }
            invalidate();
        }
    }

    /*
     * 计算详情坐标
     * */
    private float height = textSize * 7;

    private void CountDesPos(float x, float y) {
        float width = mPaint.measureText(chartStockDataList.get(PressItem).getTime()) * 2;
        if (x - width >= leftPatent) {
            //显示在左侧
            if (y - height >= topParent) {
                //显示在上方
                DesL = x - width;
                DesR = x;
                DesT = y - height;
                DesB = y;
            } else {
                //显示在下方
                DesL = x - width;
                DesR = x;
                DesT = y;
                DesB = y + height;
            }
        } else {
            //显示在右侧
            if (y - height >= topParent) {
                //显示在上方
                DesL = x;
                DesR = x + width;
                DesT = y - height;
                DesB = y;
            } else {
                //显示在下方
                DesL = x;
                DesR = x + width;
                DesT = y;
                DesB = y + height;
            }
        }

    }

    /*
     * 设置详情
     * */
    private void SetupDes(float x) {
        PressX = x;
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        isInit = true;
        invalidate();
    }

    public List<FindChartData> getChartStockDataList() {
        return chartStockDataList;
    }

    public List<FindChartData> getChartMixedDataList() {
        return chartMixedDataList;
    }

    public List<FindChartData> getChartHuShenDataList() {
        return chartHuShenDataList;
    }
}
