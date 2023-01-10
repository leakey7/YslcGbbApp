package com.gzyslczx.yslc.tools.yourui.myviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.yourui.HisTrendExtEntity;
import com.yourui.sdk.message.use.TrendDataModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MinuteChartView extends View {

    private int RealPLineColor, AvePLineColor, DottedLineColor, UpColor, DownColor, GrayColor, BlackColor;
    private Paint RealPricePaint, AvePricePaint, DottedPaint, UpPaint, DownPaint, GrayPaint, BlackPaint;
    private final String Time_930 = "9:30", Time_1030 = "10:30", Time_11301300 = "11:30/13:00", Time_1400 = "14:00", Time_1500 = "15:00", ZeroGain = "0.00%";
    private float MaxValue, MinValue; //最高价，最低价
    private List<TrendDataModel> dataList;
    private float YesterdayPrice = -1; //昨收价
    private StringBuilder UpGain = new StringBuilder(), DownGain = new StringBuilder(); //涨跌幅
    private DecimalFormat decimalFormat = new DecimalFormat(".00");
    private float DefItemSize = 241; //默认数据量
    private MinuteVolumeLink minuteVolumeLink; //成交量联动接口
    private GestureDetector mGestureDetector; //手势
    private boolean enableLongPress = false; //是否支持长按
    private boolean isLongPress = false; //长按状态：true=处于长按，false=不处于长按
    private float IndicateLineX = 0, IndicateLineY = 0; //指示线(X,Y)
    private OnMinuteLongPressListener longPressListener;
    private boolean isFiveDay = false;
    private HisTrendExtEntity hisData1, hisData2, hisData3, hisData4, hisData5;

    public MinuteChartView(Context context) {
        super(context);
    }

    public MinuteChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        PrintLogD("初始属性");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StockChartView);
        RealPLineColor = typedArray.getColor(R.styleable.StockChartView_RealPLineColor, ContextCompat.getColor(context, R.color.black_333));
        AvePLineColor = typedArray.getColor(R.styleable.StockChartView_AvePLineColor, ContextCompat.getColor(context, R.color.orange_f66e5c));
        DottedLineColor = typedArray.getColor(R.styleable.StockChartView_DottedLineColor, ContextCompat.getColor(context, R.color.gray_999));
        UpColor = typedArray.getColor(R.styleable.StockChartView_UpColor, ContextCompat.getColor(context, R.color.red_up));
        DownColor = typedArray.getColor(R.styleable.StockChartView_DownColor, ContextCompat.getColor(context, R.color.green_down));
        GrayColor = typedArray.getColor(R.styleable.StockChartView_GrayColor, ContextCompat.getColor(context, R.color.gray_eee));
        BlackColor = typedArray.getColor(R.styleable.StockChartView_BlackColor, ContextCompat.getColor(context, R.color.black));
        typedArray.recycle();
        InitPaint();
    }

    public MinuteChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MinuteChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawChart(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            //双指操作
            return super.onTouchEvent(event);
        } else if (event.getPointerCount() == 1) {
            //单指操作
            if (isLongPress) {
                //长按状态
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        PrintLogD("按下取消指示线");
                        isLongPress = false;
                        IndicateLineX = 0;
                        IndicateLineY = 0;
                        if (minuteVolumeLink != null) {
                            minuteVolumeLink.LongPressLink(isLongPress, 0, 0);
                        }
                        if (longPressListener != null) {
                            if (!isFiveDay) {
                                int lastIndex = dataList.size() - 1;
                                double gain = (dataList.get(lastIndex).getPrice() - YesterdayPrice) / YesterdayPrice * 100f;
                                longPressListener.onMinuteLongPress(YesterdayPrice, dataList.get(lastIndex).getPrice(), dataList.get(lastIndex).getAvgPrice(),
                                        gain, false, null, dataList.get(lastIndex).getTradeAmount(), dataList.get(lastIndex - 1).getPrice());
                            } else {
                                if (hisData1 != null && hisData1.getTrendDataModelList().size() > 0) {
                                    int lastIndex = hisData1.getTrendDataModelList().size() - 1;
                                    double gain = (hisData1.getTrendDataModelList().get(lastIndex).getPrice() - hisData1.getPreClosePrice())
                                            / hisData1.getPreClosePrice() * 100f;
                                    longPressListener.onMinuteLongPress(hisData1.getPreClosePrice(), hisData1.getTrendDataModelList().get(lastIndex).getPrice(),
                                            hisData1.getTrendDataModelList().get(lastIndex).getAvgPrice(), gain, true, null,
                                            hisData1.getTrendDataModelList().get(lastIndex).getTradeAmount(), hisData1.getTrendDataModelList().get(lastIndex - 1).getPrice());
                                }
                            }
                            longPressListener.onCancelMinuteLongPress();
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        getParent().requestDisallowInterceptTouchEvent(true);
                        PrintLogD("长按后滑动");
                        IndicateLineX = event.getX();
                        IndicateLineY = event.getY();
                        if (minuteVolumeLink != null) {
                            minuteVolumeLink.LongPressLink(isLongPress, IndicateLineX, IndicateLineY);
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        getParent().requestDisallowInterceptTouchEvent(false);
                        PrintLogD("长按后抬起");
                        break;
                }
                return super.onTouchEvent(event);
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    /*
     * 绘图
     * */
    private void DrawChart(Canvas canvas) {
        PrintLogD("绘制分时图");
        float topOnYAxis = 0; //顶部坐标Y点
        float leftOnXAxis = 0; //左端坐标X点
        float rightOnXAxis = leftOnXAxis + getMeasuredWidth(); //右端坐标X点
        float btmOnYAxis = topOnYAxis + getMeasuredHeight(); //底部坐标Y点
        float btmWithoutTimeOnYAxis = btmOnYAxis - DisplayTool.dp2px(getContext(), 10); //底部时间轴Y坐标
        //计算相关高度和Y轴坐标
        float quarterHeight = (btmWithoutTimeOnYAxis - topOnYAxis) / 4f; //View高度的四分一
        float quarterHeightOnY = topOnYAxis + quarterHeight; //四分之一高度Y坐标点
        float halfHeightOnY = topOnYAxis + quarterHeight * 2f; //二分之一高度Y坐标点
        float threeQuarterHeightOnY = topOnYAxis + quarterHeight * 3f; //四分之三高度Y坐标点
        if (!isFiveDay) {
            //当天分时
            //计算相关宽度和X轴坐标
            float quarterWidth = getMeasuredWidth() / 4f; //View宽度的四分一
            float quarterWidthOnX = leftOnXAxis + quarterWidth; //四分之一宽度X坐标点
            float halfWidthOnX = leftOnXAxis + quarterWidth * 2f; //二分之一宽度X坐标点
            float threeQuarterWidthOnX = leftOnXAxis + quarterWidth * 3f; //四分之三宽度X坐标点
            //绘制宫格线
            canvas.drawLine(leftOnXAxis, topOnYAxis, rightOnXAxis, topOnYAxis, GrayPaint); //顶横线
            canvas.drawLine(leftOnXAxis, quarterHeightOnY, rightOnXAxis, quarterHeightOnY, GrayPaint); //一横线
            canvas.drawLine(leftOnXAxis, halfHeightOnY, rightOnXAxis, halfHeightOnY, DottedPaint); //二横线
            canvas.drawLine(leftOnXAxis, threeQuarterHeightOnY, rightOnXAxis, threeQuarterHeightOnY, GrayPaint); //三横线
            canvas.drawLine(leftOnXAxis, btmWithoutTimeOnYAxis, rightOnXAxis, btmWithoutTimeOnYAxis, GrayPaint); //底横线
            canvas.drawLine(leftOnXAxis, topOnYAxis, leftOnXAxis, btmWithoutTimeOnYAxis, GrayPaint); //左竖线
            canvas.drawLine(quarterWidthOnX, topOnYAxis, quarterWidthOnX, btmWithoutTimeOnYAxis, GrayPaint); //一竖线
            canvas.drawLine(halfWidthOnX, topOnYAxis, halfWidthOnX, btmWithoutTimeOnYAxis, GrayPaint); //二竖线
            canvas.drawLine(threeQuarterWidthOnX, topOnYAxis, threeQuarterWidthOnX, btmWithoutTimeOnYAxis, GrayPaint); //三竖线
            canvas.drawLine(rightOnXAxis, topOnYAxis, rightOnXAxis, btmWithoutTimeOnYAxis, GrayPaint); //右竖线
            //绘制时间
            BlackPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(Time_930, leftOnXAxis, btmOnYAxis, BlackPaint); //9:30
            BlackPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(Time_1030, quarterWidthOnX, btmOnYAxis, BlackPaint); //10:30
            canvas.drawText(Time_11301300, halfWidthOnX, btmOnYAxis, BlackPaint); //11:30/13:00
            canvas.drawText(Time_1400, threeQuarterWidthOnX, btmOnYAxis, BlackPaint); //14:00
            BlackPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(Time_1500, rightOnXAxis, btmOnYAxis, BlackPaint); //15:00
            //绘制0%涨幅
            float ZeroGainOnYAxis = halfHeightOnY + BlackPaint.measureText(ZeroGain.substring(0, 1)) / 2f; //零涨幅Y点
            canvas.drawText(ZeroGain, rightOnXAxis, ZeroGainOnYAxis, BlackPaint); //0.00%涨幅点
            if (YesterdayPrice != -1) {
                BlackPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(String.valueOf(YesterdayPrice), leftOnXAxis, ZeroGainOnYAxis, BlackPaint); //昨收价
                float UpGainOnYAxis = topOnYAxis + DisplayTool.dp2px(getContext(), 10);
                UpPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(String.valueOf(MaxValue), leftOnXAxis, UpGainOnYAxis, UpPaint); //最大价
                UpPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(UpGain.toString(), rightOnXAxis, UpGainOnYAxis, UpPaint); //涨幅
                DownPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(String.valueOf(MinValue), leftOnXAxis, btmWithoutTimeOnYAxis, DownPaint); //最小价
                DownPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(DownGain.toString(), rightOnXAxis, btmWithoutTimeOnYAxis, DownPaint); //跌幅
                float AveWidthOfItem = getMeasuredWidth() / DefItemSize;
                DrawPriceLine(canvas, topOnYAxis, btmWithoutTimeOnYAxis, AveWidthOfItem); //绘制折线
                DrawIndicateLine(canvas, IndicateLineX, IndicateLineY, leftOnXAxis, topOnYAxis, rightOnXAxis, btmWithoutTimeOnYAxis, AveWidthOfItem, 0, 0, 0, 0); //长按指示线
            }
        } else {
            //五天分时
            //计算相关宽度和X轴坐标
            float OneInFiveOfWidth = getMeasuredWidth() / 5f; //View宽度的五分一
            float TwoInFiveOnX = leftOnXAxis + OneInFiveOfWidth * 2f; //二分之一宽度X坐标点
            float ThreeInFiveOnX = leftOnXAxis + OneInFiveOfWidth * 3f; //三分之五宽度X坐标点
            float FourInFiveOnX = leftOnXAxis + OneInFiveOfWidth * 4f; //四分之五宽度X坐标点
            //绘制宫格线
            canvas.drawLine(leftOnXAxis, topOnYAxis, rightOnXAxis, topOnYAxis, GrayPaint); //顶横线
            canvas.drawLine(leftOnXAxis, quarterHeightOnY, rightOnXAxis, quarterHeightOnY, GrayPaint); //一横线
            canvas.drawLine(leftOnXAxis, halfHeightOnY, rightOnXAxis, halfHeightOnY, DottedPaint); //二横线
            canvas.drawLine(leftOnXAxis, threeQuarterHeightOnY, rightOnXAxis, threeQuarterHeightOnY, GrayPaint); //三横线
            canvas.drawLine(leftOnXAxis, btmWithoutTimeOnYAxis, rightOnXAxis, btmWithoutTimeOnYAxis, GrayPaint); //底横线
            canvas.drawLine(leftOnXAxis, topOnYAxis, leftOnXAxis, btmWithoutTimeOnYAxis, GrayPaint); //左竖线
            canvas.drawLine(OneInFiveOfWidth, topOnYAxis, OneInFiveOfWidth, btmWithoutTimeOnYAxis, GrayPaint); //一竖线
            canvas.drawLine(TwoInFiveOnX, topOnYAxis, TwoInFiveOnX, btmWithoutTimeOnYAxis, GrayPaint); //二竖线
            canvas.drawLine(ThreeInFiveOnX, topOnYAxis, ThreeInFiveOnX, btmWithoutTimeOnYAxis, GrayPaint); //三竖线
            canvas.drawLine(FourInFiveOnX, topOnYAxis, FourInFiveOnX, btmWithoutTimeOnYAxis, GrayPaint); //四竖线
            canvas.drawLine(rightOnXAxis, topOnYAxis, rightOnXAxis, btmWithoutTimeOnYAxis, GrayPaint); //右竖线
            //绘制时间
            BlackPaint.setTextAlign(Paint.Align.CENTER);
            float halfOfTime = OneInFiveOfWidth / 2f;
            if (hisData5 != null) {
                canvas.drawText(String.valueOf(hisData5.getHistoryTime()), halfOfTime, btmOnYAxis, BlackPaint); //1日
            }
            if (hisData4 != null) {
                canvas.drawText(String.valueOf(hisData4.getHistoryTime()), OneInFiveOfWidth + halfOfTime, btmOnYAxis, BlackPaint); //2日
            }
            if (hisData3 != null) {
                canvas.drawText(String.valueOf(hisData3.getHistoryTime()), TwoInFiveOnX + halfOfTime, btmOnYAxis, BlackPaint); //3日
            }
            if (hisData2 != null) {
                canvas.drawText(String.valueOf(hisData2.getHistoryTime()), ThreeInFiveOnX + halfOfTime, btmOnYAxis, BlackPaint); //4日
            }
            if (hisData1 != null) {
                canvas.drawText(String.valueOf(hisData1.getHistoryTime()), FourInFiveOnX + halfOfTime, btmOnYAxis, BlackPaint); //5日
            }
            //绘制折线
            float AveWidthOfItem = OneInFiveOfWidth / DefItemSize; //单位宽度
            float height = btmWithoutTimeOnYAxis - topOnYAxis; //可绘高度
            float AveHeightOfItem = height / (MaxValue - MinValue); //单位高度
            float LineStartX = AveWidthOfItem / 2f; //起始点
            for (int i = 0; i < 5; i++) {
                switch (i) {
                    case 0:
                        LineStartX = DrawFiveDayPriceLine(canvas, AveWidthOfItem, AveHeightOfItem, LineStartX, hisData5);
                        break;
                    case 1:
                        LineStartX = DrawFiveDayPriceLine(canvas, AveWidthOfItem, AveHeightOfItem, LineStartX, hisData4);
                        break;
                    case 2:
                        LineStartX = DrawFiveDayPriceLine(canvas, AveWidthOfItem, AveHeightOfItem, LineStartX, hisData3);
                        break;
                    case 3:
                        LineStartX = DrawFiveDayPriceLine(canvas, AveWidthOfItem, AveHeightOfItem, LineStartX, hisData2);
                        break;
                    case 4:
                        LineStartX = DrawFiveDayPriceLine(canvas, AveWidthOfItem, AveHeightOfItem, LineStartX, hisData1);
                        break;
                }
            }
            DrawIndicateLine(canvas, IndicateLineX, IndicateLineY, leftOnXAxis, topOnYAxis, rightOnXAxis, btmWithoutTimeOnYAxis, AveWidthOfItem,
                    OneInFiveOfWidth, TwoInFiveOnX, ThreeInFiveOnX, FourInFiveOnX); //长按指示线
        }
    }

    /*
     * 绘制实价-均价线
     * */
    private void DrawPriceLine(Canvas canvas, float topOnAxis, float btmOnAxis, float aveW) {
        float height = btmOnAxis - topOnAxis; //可绘高度
        float AveWidthOfItem = aveW; //每一数据点宽度
        float AveHeightOfItem = height / (MaxValue - MinValue); //一单位高度
        float LineStartX = AveWidthOfItem / 2f; //起始点
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (i == 0) {
                    float realDif = MaxValue - dataList.get(i).getPrice();
                    canvas.drawCircle(LineStartX, topOnAxis + AveHeightOfItem * realDif,
                            1, RealPricePaint); //实价起始点
                    float aveDif = MaxValue - dataList.get(i).getAvgPrice();
                    canvas.drawCircle(LineStartX, topOnAxis + AveHeightOfItem * aveDif,
                            1, AvePricePaint); //均价起始点
                } else {
                    float realDifPre = MaxValue - dataList.get(i - 1).getPrice();
                    float realDif = MaxValue - dataList.get(i).getPrice();
                    canvas.drawLine(LineStartX + AveWidthOfItem * (i - 1), topOnAxis + AveHeightOfItem * realDifPre,
                            LineStartX + AveWidthOfItem * i, topOnAxis + AveHeightOfItem * realDif,
                            RealPricePaint); //实价折线
                    float aveDifPre = MaxValue - dataList.get(i - 1).getAvgPrice();
                    float aveDif = MaxValue - dataList.get(i).getAvgPrice();
                    canvas.drawLine(LineStartX + AveWidthOfItem * (i - 1), topOnAxis + AveHeightOfItem * aveDifPre,
                            LineStartX + AveWidthOfItem * i, topOnAxis + AveHeightOfItem * aveDif,
                            AvePricePaint); //均价折线
                }
            }
        }
    }

    /*
     * 绘制五日实价-均价线
     * */
    private float DrawFiveDayPriceLine(Canvas canvas, float aveWidth, float aveHeight, float startX, HisTrendExtEntity entity) {
        float nextStart = startX;
        if (entity != null) {
            int sum = entity.getTrendDataModelList().size() - 1;
            for (int i = 0; i <= sum; i++) {
                if (i == 0) {
                    float realDif = MaxValue - entity.getTrendDataModelList().get(i).getPrice();
                    canvas.drawCircle(startX, aveHeight * realDif, 1, RealPricePaint); //实价起始点
                    float aveDif = MaxValue - entity.getTrendDataModelList().get(i).getAvgPrice();
                    canvas.drawCircle(startX, aveHeight * aveDif, 1, AvePricePaint); //均价起始点
                } else {
                    float realDifPre = MaxValue - entity.getTrendDataModelList().get(i - 1).getPrice();
                    float realDif = MaxValue - entity.getTrendDataModelList().get(i).getPrice();
                    float preX = nextStart;
                    nextStart = preX + aveWidth;
                    canvas.drawLine(preX, aveHeight * realDifPre, nextStart, aveHeight * realDif, RealPricePaint); //实价折线
                    float aveDifPre = MaxValue - entity.getTrendDataModelList().get(i - 1).getAvgPrice();
                    float aveDif = MaxValue - entity.getTrendDataModelList().get(i).getAvgPrice();
                    canvas.drawLine(preX, aveHeight * aveDifPre, nextStart, aveHeight * aveDif, AvePricePaint); //均价折线
                }
            }
        }
        return nextStart;
    }

    /*
     * 绘制指示线
     * */
    private void DrawIndicateLine(Canvas canvas, float indicateLineX, float indicateLineY, float left, float top, float right, float btm, float aveW,
                                  float oneInFive, float twoInFive, float threeInFive, float fourInFive) {
        if (enableLongPress && isLongPress) {
            if (isFiveDay) {
                if (indicateLineX <= left) {
                    canvas.drawLine(left, top, left, btm, BlackPaint); //限制越左边界指示竖线
                    if (longPressListener != null) {
                        double gain = (hisData5.getTrendDataModelList().get(0).getPrice() - hisData5.getPreClosePrice()) / hisData5.getPreClosePrice() * 100f;
                        longPressListener.onMinuteLongPress(hisData5.getPreClosePrice(), hisData5.getTrendDataModelList().get(0).getPrice(), hisData5.getTrendDataModelList().get(0).getAvgPrice(),
                                gain, true, hisData5.getTrendDataModelList().get(0).getTime(), hisData5.getTrendDataModelList().get(0).getTradeAmount(), YesterdayPrice);
                    }
                } else if (indicateLineX >= right) {
                    canvas.drawLine(right, top, right, btm, BlackPaint); //限制越右边界指示竖线
                    if (longPressListener != null) {
                        int lastIndex = hisData1.getTrendDataModelList().size() - 1;
                        double gain = (hisData1.getTrendDataModelList().get(lastIndex).getPrice() - hisData1.getPreClosePrice()) / hisData1.getPreClosePrice() * 100f;
                        longPressListener.onMinuteLongPress(hisData1.getPreClosePrice(), hisData1.getTrendDataModelList().get(lastIndex).getPrice(), hisData1.getTrendDataModelList().get(lastIndex).getAvgPrice(),
                                gain, true, hisData1.getTrendDataModelList().get(lastIndex).getTime(), hisData1.getTrendDataModelList().get(lastIndex).getTradeAmount(),
                                hisData1.getTrendDataModelList().get(lastIndex - 1).getPrice());
                    }
                } else {
                    HisTrendExtEntity hisTrendExtEntity = null;
                    int sum = (hisData5 == null ? 0 : hisData5.getTrendDataModelList().size()) + (hisData4 == null ? 0 : hisData4.getTrendDataModelList().size())
                            + (hisData3 == null ? 0 : hisData3.getTrendDataModelList().size()) + (hisData2 == null ? 0 : hisData2.getTrendDataModelList().size())
                            + (hisData1 == null ? 0 : hisData1.getTrendDataModelList().size());
                    int index = 0;
                    if (indicateLineX <= oneInFive) {
                        hisTrendExtEntity = hisData5;
                        index = (int) (indicateLineX / aveW);
                    } else if (indicateLineX <= twoInFive) {
                        hisTrendExtEntity = hisData4;
                        index = (int) ((indicateLineX - oneInFive) / aveW);
                    } else if (indicateLineX <= threeInFive) {
                        hisTrendExtEntity = hisData3;
                        index = (int) ((indicateLineX - twoInFive) / aveW);
                    } else if (indicateLineX <= fourInFive) {
                        hisTrendExtEntity = hisData2;
                        index = (int) ((indicateLineX - threeInFive) / aveW);
                    } else if (indicateLineX <= right) {
                        hisTrendExtEntity = hisData1;
                        index = (int) ((indicateLineX - fourInFive) / aveW);
                    }
                    canvas.drawLine(indicateLineX, top, indicateLineX, btm, BlackPaint); //指示竖线
                    if (index >= sum) {
                        return;
                    }
                    if (hisTrendExtEntity != null) {
                        double gain = (hisTrendExtEntity.getTrendDataModelList().get(index).getPrice() - hisTrendExtEntity.getPreClosePrice()) / hisTrendExtEntity.getPreClosePrice() * 100f;
                        if (index <= 0) {
                            longPressListener.onMinuteLongPress(hisTrendExtEntity.getPreClosePrice(), hisTrendExtEntity.getTrendDataModelList().get(0).getPrice(),
                                    hisTrendExtEntity.getTrendDataModelList().get(0).getAvgPrice(), gain, true,
                                    hisTrendExtEntity.getTrendDataModelList().get(0).getTime(), hisTrendExtEntity.getTrendDataModelList().get(0).getTradeAmount(), YesterdayPrice);
                        } else {
                            longPressListener.onMinuteLongPress(hisTrendExtEntity.getPreClosePrice(), hisTrendExtEntity.getTrendDataModelList().get(index).getPrice(),
                                    hisTrendExtEntity.getTrendDataModelList().get(index).getAvgPrice(), gain, true,
                                    hisTrendExtEntity.getTrendDataModelList().get(index).getTime(),
                                    hisTrendExtEntity.getTrendDataModelList().get(index).getTradeAmount(),
                                    hisTrendExtEntity.getTrendDataModelList().get(index - 1).getPrice());
                        }
                    }
                }
            } else {
                if (indicateLineX <= left) {
                    canvas.drawLine(left, top, left, btm, BlackPaint); //限制越左边界指示竖线
                    if (longPressListener != null) {
                        double gain = (dataList.get(0).getPrice() - YesterdayPrice) / YesterdayPrice * 100f;
                        longPressListener.onMinuteLongPress(YesterdayPrice, dataList.get(0).getPrice(), dataList.get(0).getAvgPrice(),
                                gain, true, dataList.get(0).getTime(), dataList.get(0).getTradeAmount(), YesterdayPrice);
                    }
                } else if (indicateLineX >= right) {
                    canvas.drawLine(right, top, right, btm, BlackPaint); //限制越右边界指示竖线
                    if (longPressListener != null) {
                        int lastIndex = dataList.size() - 1;
                        double gain = (dataList.get(lastIndex).getPrice() - YesterdayPrice) / YesterdayPrice * 100f;
                        longPressListener.onMinuteLongPress(YesterdayPrice, dataList.get(lastIndex).getPrice(), dataList.get(lastIndex).getAvgPrice(),
                                gain, true, dataList.get(lastIndex).getTime(), dataList.get(lastIndex).getTradeAmount(), dataList.get(lastIndex - 1).getPrice());
                    }
                } else {
                    int index = (int) (indicateLineX / aveW);
                    canvas.drawLine(indicateLineX, top, indicateLineX, btm, BlackPaint); //指示竖线
                    if (index >= dataList.size()) {
                        return;
                    }
                    double gain = (dataList.get(index).getPrice() - YesterdayPrice) / YesterdayPrice * 100f;
                    if (index <= 0) {
                        longPressListener.onMinuteLongPress(YesterdayPrice, dataList.get(0).getPrice(), dataList.get(0).getAvgPrice(),
                                gain, true, dataList.get(0).getTime(), dataList.get(0).getTradeAmount(), YesterdayPrice);
                    } else {
                        longPressListener.onMinuteLongPress(YesterdayPrice, dataList.get(index).getPrice(), dataList.get(index).getAvgPrice(),
                                gain, true, dataList.get(index).getTime(), dataList.get(index).getTradeAmount(), dataList.get(index - 1).getPrice());
                    }
                }
            }
            if (indicateLineY <= top) {
                canvas.drawLine(left, top, right, top, BlackPaint); //限制越顶边界指示横线
            } else if (indicateLineY >= btm) {
                canvas.drawLine(left, btm, right, btm, BlackPaint); //限制越底边界指示横线
            } else {
                canvas.drawLine(left, indicateLineY, right, indicateLineY, BlackPaint); //指示横线
            }
        }
    }


    /*
     * 初始化画笔
     * */
    private void InitPaint() {
        PrintLogD("初始画笔");
        //实价笔
        RealPricePaint = new Paint();
        RealPricePaint.setColor(RealPLineColor);
        RealPricePaint.setStyle(Paint.Style.FILL);
        RealPricePaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        RealPricePaint.setTextSize(DisplayTool.sp2px(getContext(), 14));
        //均价笔
        AvePricePaint = new Paint();
        AvePricePaint.setColor(AvePLineColor);
        AvePricePaint.setStyle(Paint.Style.FILL);
        AvePricePaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        AvePricePaint.setTextSize(DisplayTool.sp2px(getContext(), 14));
        //虚线笔
        DottedPaint = new Paint();
        DottedPaint.setColor(DottedLineColor);
        DottedPaint.setStyle(Paint.Style.FILL);
        DottedPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        DottedPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        DottedPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        //上涨笔
        UpPaint = new Paint();
        UpPaint.setColor(UpColor);
        UpPaint.setStyle(Paint.Style.FILL);
        UpPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        UpPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        //下跌笔
        DownPaint = new Paint();
        DownPaint.setColor(DownColor);
        DownPaint.setStyle(Paint.Style.FILL);
        DownPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        DownPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        //灰色笔
        GrayPaint = new Paint();
        GrayPaint.setColor(GrayColor);
        GrayPaint.setStyle(Paint.Style.FILL);
        GrayPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        GrayPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        //黑色笔
        BlackPaint = new Paint();
        BlackPaint.setColor(BlackColor);
        BlackPaint.setStyle(Paint.Style.FILL);
        BlackPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        BlackPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        Init();//初始化变量
    }

    private void Init() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                PrintLogD("手指按下");
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                PrintLogD("确认单击");
                return super.onSingleTapConfirmed(e);
            }

            //长按
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                if (enableLongPress) {
                    PrintLogD("确认触发长按");
                    isLongPress = true;
                }
            }

        });
    }

    /*
     * 设置数据集
     * */
    public void SetDataList(List<TrendDataModel> dataModels, float yesterdayPrice, float maxValue, float minValue) {
        if (dataList == null) {
            dataList = new ArrayList<TrendDataModel>();
        } else {
            if (dataList.size() == dataModels.size()) {
                return;
            }
        }
        dataList.clear();
        Log.d(getClass().getSimpleName(), "更新分时图数据");
        this.YesterdayPrice = yesterdayPrice;
        dataList.addAll(dataModels);
        CountGain(maxValue, minValue);
        if (minuteVolumeLink != null) {
            minuteVolumeLink.DataLink(dataList, this.YesterdayPrice);
        }
        invalidate();
    }

    /*
     * 计算涨跌幅
     * */
    public void CountGain(float maxValue, float minValue) {
        float SubDiffByMax = Math.abs(maxValue - YesterdayPrice);
        float SubDiffByMin = Math.abs(minValue - YesterdayPrice);
        if (SubDiffByMax > SubDiffByMin) {
            MaxValue = maxValue;
            MinValue = YesterdayPrice - SubDiffByMax;
        } else if (SubDiffByMax < SubDiffByMin) {
            MaxValue = YesterdayPrice + SubDiffByMin;
            MinValue = minValue;
        } else {
            MaxValue = maxValue;
            MinValue = minValue;
        }
        MaxValue = Float.valueOf(decimalFormat.format(MaxValue));
        MinValue = Float.valueOf(decimalFormat.format(MinValue));
        float gain = (MaxValue - YesterdayPrice) / YesterdayPrice * 100f;
        UpGain.replace(0, UpGain.length(), "+" + decimalFormat.format(gain) + "%");
        DownGain.replace(0, DownGain.length(), "-" + decimalFormat.format(gain) + "%");
    }


    private void PrintLogD(String log) {
        Log.d(getClass().getSimpleName(), log);
    }

    public void setMinuteVolumeLink(MinuteVolumeLink minuteVolumeLink) {
        this.minuteVolumeLink = minuteVolumeLink;
    }

    public float getDefItemSize() {
        return DefItemSize;
    }

    public void setDefItemSize(float defItemSize) {
        DefItemSize = defItemSize;
    }

    public void setEnableLongPress(boolean enableLongPress) {
        this.enableLongPress = enableLongPress;
    }

    public void setLongPressListener(OnMinuteLongPressListener longPressListener) {
        this.longPressListener = longPressListener;
    }

    public boolean isLongPress() {
        return isLongPress;
    }

    public void setFiveDay(boolean fiveDay) {
        isFiveDay = fiveDay;
    }

    public boolean isFiveDay() {
        return isFiveDay;
    }

    public void AddFiveDayMinuteData(HisTrendExtEntity data) {
        if (hisData1 == null) {
            hisData1 = data;
            MaxValue = hisData1.getMaxPrice();
            MinValue = hisData1.getMinPrice();
        } else {
            if (hisData2 == null) {
                hisData2 = data;
            } else if (hisData3 == null) {
                hisData3 = data;
            } else if (hisData4 == null) {
                hisData4 = data;
            } else if (hisData5 == null) {
                hisData5 = data;
            }
            MaxValue = Math.max(MaxValue, data.getMaxPrice());
            MinValue = Math.min(MinValue, data.getMinPrice());
        }
        Log.d(getClass().getSimpleName(), "更新五日分时图数据");
        isFiveDay = true;
        if (minuteVolumeLink != null) {
            minuteVolumeLink.FiveDataLink(hisData1, hisData2, hisData3, hisData4, hisData5);
        }
    }

    public void UpdateHisData1(HisTrendExtEntity data) {
        hisData1 = data;
        MaxValue = Math.max(MaxValue, data.getMaxPrice());
        MinValue = Math.min(MinValue, data.getMinPrice());
        if (minuteVolumeLink != null) {
            minuteVolumeLink.FiveDataLink(hisData1, hisData2, hisData3, hisData4, hisData5);
        }
    }

    public HisTrendExtEntity getHisData1() {
        return hisData1;
    }

    public HisTrendExtEntity getHisData2() {
        return hisData2;
    }

    public HisTrendExtEntity getHisData3() {
        return hisData3;
    }

    public HisTrendExtEntity getHisData4() {
        return hisData4;
    }

    public HisTrendExtEntity getHisData5() {
        return hisData5;
    }
}
