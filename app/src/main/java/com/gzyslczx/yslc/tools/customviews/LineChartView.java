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

public class LineChartView extends View implements GestureDetector.OnGestureListener {

    private int ViewH, ViewW;
    private float leftPatent, topParent, AveH, AveW, maxV, textW;
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int itemSize = 1;
    private final float textSize = 16;
    private GestureDetector mDetector;
    private String leftTime, rightTime;
    ;
    private List<FindChartData> chartDataList;
    private boolean isInit = false, isPress = false;
    private float DesL = 0, DesT = 0, DesR = 0, DesB = 0;
    private DecimalFormat format;


    public LineChartView(Context context) {
        super(context);
        Init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            if (chartDataList.size() > 0) {
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
        chartDataList = new ArrayList<FindChartData>();
        format = new DecimalFormat("0.00");
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
        float modelStar = topParent + textSize / 2f;
        float modelEnd = topParent + ViewH - textSize * 3 / 2;
        float lineStar = leftPatent + textW;
        float lineEnd = leftPatent + ViewW;
        float centerY = topParent + ViewH / 2f - textSize;
        float centerYTop = topParent + ViewH / 4f - textSize / 2f;
        float centerYBottom = topParent + ViewH * 3 / 4f - textSize * 3 / 2f;
        mPaint.setStrokeWidth(2f);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        mCanvas.drawLine(lineStar, modelEnd, lineEnd, modelEnd,
                mPaint);
        mPaint.setStrokeWidth(1f);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        mCanvas.drawLine(lineStar, modelStar, lineEnd, modelStar, mPaint);
        mCanvas.drawLine(lineStar, centerY, lineEnd, centerY, mPaint);
        mCanvas.drawLine(lineStar, centerYTop, lineEnd, centerYTop, mPaint);
        mCanvas.drawLine(lineStar, centerYBottom, lineEnd, centerYBottom, mPaint);
        DrawText(lineStar, topParent + textSize, topParent + ViewH - textSize, topParent + (ViewH - textSize) / 2f);
        DrawLine();
    }

    /*
     * 绘制文字
     * */
    private void DrawText(float starX, float starY, float endY, float centerY) {
        mPaint.setStrokeWidth(2f);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.black_333));
        mPaint.setTextAlign(Paint.Align.RIGHT);
        mCanvas.drawText("0", starX, endY, mPaint);
        mCanvas.drawText(String.valueOf(maxV), starX, starY, mPaint);
        mCanvas.drawText(format.format(maxV / 2f), starX, centerY, mPaint);
        float bottomPos = topParent + ViewH;
        int size = chartDataList.size();
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
            mCanvas.drawText(chartDataList.get(index).getTime(), chartDataList.get(index).getxAxis(),
                    bottomPos, mPaint);
        }
    }

    /*
     * 绘制折线
     * */
    private void DrawLine() {
        mPaint.setStrokeWidth(2f);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.main_blue));
        for (int i = 1; i < chartDataList.size(); i++) {
            mCanvas.drawLine(chartDataList.get(i - 1).getxAxis(), chartDataList.get(i - 1).getyAxis(),
                    chartDataList.get(i).getxAxis(), chartDataList.get(i).getyAxis(), mPaint);
        }
    }

    /*
     * 绘制滑动线
     * */
    private void DrawSlidingLine() {
        mPaint.setStrokeWidth(1);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        mCanvas.drawLine(PressX, topParent, PressX, topParent + ViewH - textSize * 3 / 2, mPaint);
    }

    /*
     * 绘制详情
     * */
    private void DrawDes() {
        mPaint.setStrokeWidth(2);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mCanvas.drawRect(DesL, DesT, DesR, DesB, mPaint);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.black_333));
        mPaint.setTextAlign(Paint.Align.LEFT);
        float start = DesL + textSize;
        int size = itemSize - 1;
        try {
            mCanvas.drawText(chartDataList.get(size - PressItem).getTime(), start, DesT + 2 * textSize, mPaint);
        } catch (ArrayIndexOutOfBoundsException exception) {
            mCanvas.drawText(chartDataList.get(size).getTime(), start, DesT + 2 * textSize, mPaint);

        }
        mCanvas.drawText("指数", start + 4 * AveW, DesT + 4 * textSize, mPaint);
        mPaint.setTextAlign(Paint.Align.RIGHT);
        try {
            mCanvas.drawText(String.valueOf(chartDataList.get(size - PressItem).getValue()),
                    DesR - textSize, DesT + 4 * textSize, mPaint);
        } catch (ArrayIndexOutOfBoundsException exception) {
            mCanvas.drawText(String.valueOf(chartDataList.get(size).getValue()),
                    DesR - textSize, DesT + 4 * textSize, mPaint);
        }
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.main_blue));
        mCanvas.drawCircle(start + AveW, DesT + textSize * 3.5f, AveW, mPaint);
    }

    /*
     * 计算平均宽高
     * */
    private void CountAveValue() {
        AveH = (ViewH - 2 * textSize) / maxV;
        AveW = (ViewW - textW) / (itemSize - 1);
    }

    public void AddData(float value, String time) {
        chartDataList.add(new FindChartData(value, time));
    }

    public void setMaxV_ItemSize(float maxV, int itemSize) {
        this.maxV = maxV;
        textW = mPaint.measureText(String.valueOf(maxV));
        this.itemSize = itemSize;
    }

    public void isInitData() {
        CountAveValue();
        for (int i = 0; i < chartDataList.size(); i++) {
            if (i == 0) {
                rightTime = chartDataList.get(0).getTime();
            }
            if (i == itemSize - 1) {
                leftTime = chartDataList.get(i).getTime();
            }
            float v = chartDataList.get(i).getValue();
            float x = leftPatent + ViewW - AveW * i;
            float y = topParent + ViewH - textSize * 3 / 2 - AveH * v;
            chartDataList.get(i).setxAxis(x);
            chartDataList.get(i).setyAxis(y);
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
                float nXAxis = chartDataList.get(last - PressItem).getxAxis();
                float nYAxis = chartDataList.get(last - PressItem).getyAxis();
                if (nXAxis >= oXAxis) {
                    float x = nXAxis;
                    CountDesPos(x, nYAxis);
                    SetupDes(x);
                }
            }
        } else {
            if (leftPatent < oXAxis || oXAxis > chartDataList.get(0).getxAxis()) {
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
    private float height = textSize * 5;

    private void CountDesPos(float x, float y) {
        float width = mPaint.measureText(chartDataList.get(PressItem).getTime()) * 2;
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

    public List<FindChartData> getChartDataList() {
        return chartDataList;
    }
}
