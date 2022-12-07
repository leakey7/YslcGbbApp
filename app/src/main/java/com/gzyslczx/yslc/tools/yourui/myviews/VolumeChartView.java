package com.gzyslczx.yslc.tools.yourui.myviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.presenter.yourui.YRBasePresenter;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.yourui.sdk.message.kline.KlineKDJ;
import com.yourui.sdk.message.kline.KlineMACD;
import com.yourui.sdk.message.kline.KlineVOL;
import com.yourui.sdk.message.use.StockKLine;
import com.yourui.sdk.message.use.TrendDataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolumeChartView extends View implements MinuteVolumeLink, DailyVolumeLink {

    private int UpColor, DownColor, GrayColor, BlackColor, DottedLineColor, EqualColor, KColor, DColor, JColor;
    private Paint UpPaint, DownPaint, GrayPaint, BlackPaint, DottedPaint, EqualPaint, KPaint, DPaint, JPaint;
    private List<TrendDataModel> dataList;
    private List<StockKLine> kLineList;
    private KlineKDJ klineKDJ;
    private KlineMACD klineMACD;
    private KlineVOL klineVOL;
    private float DefItemSize = 241; //默认数据量
    private long MaxValue = 0;
    private float YesterdayPrice = -1;
    private int type; //图类型
    private float IndicateLineX, IndicateLineY; //指示线（X,Y）
    private boolean isLongPress = false;
    private float ScrollSumDis = 0;
    private boolean isDoublePress = false; //是否双指操作
    private int markStartIndexOfScale = 0; //记录右端Index
    private OnDailyLongPressListener dailyLongPressListener;


    public VolumeChartView(Context context) {
        super(context);
    }

    public VolumeChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        PrintLogD("初始属性");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StockChartView);
        UpColor = typedArray.getColor(R.styleable.StockChartView_UpColor, ContextCompat.getColor(context, R.color.red_up));
        DownColor = typedArray.getColor(R.styleable.StockChartView_DownColor, ContextCompat.getColor(context, R.color.green_down));
        GrayColor = typedArray.getColor(R.styleable.StockChartView_GrayColor, ContextCompat.getColor(context, R.color.gray_eee));
        BlackColor = typedArray.getColor(R.styleable.StockChartView_BlackColor, ContextCompat.getColor(context, R.color.black));
        EqualColor = typedArray.getColor(R.styleable.StockChartView_equalColor, ContextCompat.getColor(context, R.color.gray_A9));
        DottedLineColor = typedArray.getColor(R.styleable.StockChartView_DottedLineColor, ContextCompat.getColor(context, R.color.gray_999));
        KColor = typedArray.getColor(R.styleable.StockChartView_KColor, ContextCompat.getColor(context, R.color.black));
        DColor = typedArray.getColor(R.styleable.StockChartView_DColor, ContextCompat.getColor(context, R.color.orange_FF8C00));
        JColor = typedArray.getColor(R.styleable.StockChartView_JColor, ContextCompat.getColor(context, R.color.pink_FF69B4));
        type = typedArray.getInteger(R.styleable.StockChartView_VolumeType, VolumeTypeConstance.Volume);
        typedArray.recycle();
        InitPaint();
    }

    public VolumeChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VolumeChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void PrintLogD(String log) {
        Log.d(getClass().getSimpleName(), log);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawChart(canvas);
    }

    private void DrawChart(Canvas canvas) {
        PrintLogD("绘制副图");
        float topOnYAxis = 0; //顶部坐标Y点
        float leftOnXAxis = 0; //左端坐标X点
        float rightOnXAxis = leftOnXAxis + getMeasuredWidth(); //右端坐标X点
        float btmOnYAxis = topOnYAxis + getMeasuredHeight(); //底部坐标Y点
        //计算相关宽度和X轴坐标
        float quarterWidth = getMeasuredWidth() / 4f; //View宽度的四分一
        float quarterWidthOnX = leftOnXAxis + quarterWidth; //四分之一宽度X坐标点
        float halfWidthOnX = leftOnXAxis + quarterWidth * 2f; //二分之一宽度X坐标点
        float threeQuarterWidthOnX = leftOnXAxis + quarterWidth * 3f; //四分之三宽度X坐标点
        //计算相关高度和Y轴坐标
        float quarterHeight = (btmOnYAxis - topOnYAxis) / 4f; //View高度的四分一
        float halfHeightOnY = topOnYAxis + quarterHeight * 2f; //二分之一高度Y坐标点
        //绘制宫格线
        canvas.drawLine(leftOnXAxis, topOnYAxis, rightOnXAxis, topOnYAxis, GrayPaint); //顶横线
        if (type != VolumeTypeConstance.MACD) {
            canvas.drawLine(leftOnXAxis, halfHeightOnY, rightOnXAxis, halfHeightOnY, DottedPaint); //中横线
        }
        canvas.drawLine(leftOnXAxis, btmOnYAxis, rightOnXAxis, btmOnYAxis, GrayPaint); //底横线
        canvas.drawLine(leftOnXAxis, topOnYAxis, leftOnXAxis, btmOnYAxis, GrayPaint); //左竖线
        canvas.drawLine(quarterWidthOnX, topOnYAxis, quarterWidthOnX, btmOnYAxis, GrayPaint); //一竖线
        canvas.drawLine(halfWidthOnX, topOnYAxis, halfWidthOnX, btmOnYAxis, GrayPaint); //二竖线
        canvas.drawLine(threeQuarterWidthOnX, topOnYAxis, threeQuarterWidthOnX, btmOnYAxis, GrayPaint); //三竖线
        canvas.drawLine(rightOnXAxis, topOnYAxis, rightOnXAxis, btmOnYAxis, GrayPaint); //右竖线
        //绘制分时柱状图
        if (YesterdayPrice != -1) {
            if (type == VolumeTypeConstance.Volume) {
                float MaxVolumeOnYAxis = topOnYAxis + DisplayTool.dp2px(getContext(), 10);
                BlackPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(String.format("%s手", MaxValue), rightOnXAxis, MaxVolumeOnYAxis, BlackPaint); //最大价
                DrawColumnChart(canvas, topOnYAxis, btmOnYAxis, leftOnXAxis); //绘制成交量
            }
            DrawIndicateLine(canvas, leftOnXAxis, rightOnXAxis, IndicateLineX, topOnYAxis, btmOnYAxis); //长按指示线
        }
        //绘制K线副图
        if (kLineList != null && kLineList.size() > 0) {
            PrintLogD(String.format("数据总量：%d", kLineList.size()));
            float ItemInterval = DisplayTool.dp2px(getContext(), 2); //间隔距离
            float AveWidthOfItem = (getMeasuredWidth() - ItemInterval * (DefItemSize - 1)) / DefItemSize; //每项平均宽度
            int scrollIndex;
            if (isDoublePress) {
                //缩放时，适配平移距离
                scrollIndex = markStartIndexOfScale;
                ScrollSumDis = (AveWidthOfItem + ItemInterval) * scrollIndex;
                isDoublePress = false;
            }
            if (ScrollSumDis < 0) {
                //阻止右端越界
                ScrollSumDis = 0;
            } else {
                //阻止左端越界
                float dis;
                if (kLineList.size() >= DefItemSize) {
                    dis = (AveWidthOfItem + ItemInterval) * (kLineList.size() - DefItemSize); //可发生的最大滑动距离
                } else {
                    dis = (AveWidthOfItem + ItemInterval) * kLineList.size() - ((float) getMeasuredWidth() - AveWidthOfItem); //可发生的最大滑动距离
                }
                if (dis > 0 && ScrollSumDis > dis) {
                    ScrollSumDis = dis;
//                        if (loadMoreListener!=null && !isLoadMore && !isLoadMoreEnd){
//                            isLoadMore = true;
//                            loadMoreListener.onLoadMoreDailyStock(); //通知加载更多
//                        }
                } else if (dis < 0) {
                    ScrollSumDis = 0;
                }
            }
            float Right = rightOnXAxis + ScrollSumDis; //适配平移距离最右侧坐标
            scrollIndex = Math.round((ScrollSumDis / (AveWidthOfItem + ItemInterval))); //适配平移距离后右侧Index
            markStartIndexOfScale = scrollIndex; //记录右侧Index，适配缩放
            int size = kLineList.size() - 1;
            int endIndex = (int) (scrollIndex + (DefItemSize - 1)); //适配平移距离后左侧Index
            if (endIndex > size) {
                endIndex = size; //限制Index越界
            }
            double[] Max_Min_Dif = CountMaxValueOnDaily(scrollIndex, endIndex); //计算最值
            float AveHeightOfItem = (float) (getMeasuredHeight() / Max_Min_Dif[2]); //每单位平均高度
//                DrawTime(canvas, endIndex, scrollIndex, leftOnXAxis, rightOnXAxis, btmOnYAxis); //绘制时间
            if (kLineList.size() >= DefItemSize) {
                DrawSubOnDaily(canvas, AveWidthOfItem, AveHeightOfItem, Right, Max_Min_Dif[0], ItemInterval, scrollIndex, endIndex); //绘制副图
                if (isLongPress) {
                    if (IndicateLineX > rightOnXAxis) {
                        IndicateLineX = rightOnXAxis;
                    }
                    float limitLeft = rightOnXAxis - AveWidthOfItem - (AveWidthOfItem + ItemInterval) * (endIndex - scrollIndex);
                    if (IndicateLineX < limitLeft) {
                        IndicateLineX = limitLeft;
                    }
                    if (IndicateLineY < topOnYAxis) {
                        IndicateLineY = topOnYAxis;
                    }
                    if (IndicateLineY > btmOnYAxis) {
                        IndicateLineY = btmOnYAxis;
                    }
                    DrawIndicateLineOnDaily(canvas, IndicateLineX, IndicateLineY, leftOnXAxis, topOnYAxis, rightOnXAxis, btmOnYAxis,
                            AveWidthOfItem, AveHeightOfItem, ItemInterval, endIndex, btmOnYAxis, Max_Min_Dif[0]); //绘制指示线
                }
//            else {
//                    if (longPressListener!=null){
//                        longPressListener.onDailyLongPress(false, DataList.get(scrollIndex), maEntityList.get(scrollIndex));
//                    }
//            }
            }else {
                DrawSubOnDailyLess(canvas, AveWidthOfItem, AveHeightOfItem, leftOnXAxis, Max_Min_Dif[0], ItemInterval, scrollIndex, endIndex );
            }
        }

    }

    /*
     * 绘制柱状图
     * */
    private void DrawColumnChart(Canvas canvas, float topOnAxis, float btmOnAxis, float leftOnAxis) {
        float height = btmOnAxis - topOnAxis; //可绘高度
        float AveWidthOfItem = getMeasuredWidth() / DefItemSize; //每一数据点宽度
        float AveHeightOfItem = height / MaxValue; //一单位高度
        float LineStartX = leftOnAxis; //起始点
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (i == 0) {
                    JudgeColumnColor(YesterdayPrice, dataList.get(i).getPrice(), canvas, LineStartX, LineStartX + AveWidthOfItem,
                            btmOnAxis - AveHeightOfItem * dataList.get(i).getTradeAmount(), btmOnAxis);
                } else {
                    JudgeColumnColor(dataList.get(i - 1).getPrice(), dataList.get(i).getPrice(), canvas,
                            LineStartX + AveWidthOfItem * i, LineStartX + AveWidthOfItem * (i + 1),
                            btmOnAxis - AveHeightOfItem * dataList.get(i).getTradeAmount(), btmOnAxis);
                }
            }
        }

    }

    /*
     * 区分成交量柱状图颜色
     * */
    private void JudgeColumnColor(float oldPrice, float newPrice, Canvas canvas, float left, float right, float top, float btm) {
        if (oldPrice == newPrice) {
            canvas.drawRect(left, top, right, btm, EqualPaint); //平等柱状图
            return;
        }
        if (oldPrice < newPrice) {
            canvas.drawRect(left, top, right, btm, UpPaint); //上涨柱状图
            return;
        }
        if (oldPrice > newPrice) {
            canvas.drawRect(left, top, right, btm, DownPaint); //下跌柱状图
            return;
        }
    }

    /*
     * 绘制指示线
     * */
    private void DrawIndicateLine(Canvas canvas, float left, float right, float indicateLineX, float top, float btm) {
        if (isLongPress) {
            if (indicateLineX < left) {
                canvas.drawLine(left, top, left, btm, BlackPaint); //防止左越界指示竖线
            } else if (indicateLineX > right) {
                canvas.drawLine(right, top, right, btm, BlackPaint); //防止右越界指示竖线
            }
            canvas.drawLine(indicateLineX, top, indicateLineX, btm, BlackPaint); //指示竖线
        }
    }

    /*
     * 计算最高值
     * */
    private double[] CountMaxValueOnDaily(int startIndex, int endIndex) {
        PrintLogD(String.format("计算最值,开始Index=%d", startIndex));
        PrintLogD(String.format("计算最值,结尾Index=%d", endIndex));
        double maxValue = 0, minValue = Double.MAX_VALUE;
        int size = this.kLineList.size() - 1;
        for (int i = startIndex; i <= endIndex; i++) {
            int q = Math.abs(size - i);
            if (i == startIndex) {
                if (type == VolumeTypeConstance.KDJ) {
                    maxValue = Math.max(klineKDJ.getKData(q), klineKDJ.getDData(q));
                    maxValue = Math.max(maxValue, klineKDJ.getJData(q));
                    minValue = Math.min(klineKDJ.getKData(q), klineKDJ.getDData(q));
                    minValue = Math.min(minValue, klineKDJ.getJData(q));
                } else if (type == VolumeTypeConstance.MACD) {
                    maxValue = Math.max(klineMACD.getMACD(q), klineMACD.getDea(q));
                    maxValue = Math.max(klineMACD.getDIFF(q), maxValue);
                    minValue = Math.min(klineMACD.getMACD(q), klineMACD.getDea(q));
                    minValue = Math.min(klineMACD.getDIFF(q), minValue);
                } else if (type == VolumeTypeConstance.Volume){
                    maxValue = kLineList.get(q).getVolume();
                    minValue = 0;
                }
            } else {
                if (type == VolumeTypeConstance.KDJ) {
                    maxValue = Math.max(maxValue, klineKDJ.getKData(q));
                    maxValue = Math.max(maxValue, klineKDJ.getDData(q));
                    maxValue = Math.max(maxValue, klineKDJ.getJData(q));

                    minValue = Math.min(minValue, klineKDJ.getKData(q));
                    minValue = Math.min(minValue, klineKDJ.getDData(q));
                    minValue = Math.min(minValue, klineKDJ.getJData(q));
                } else if (type == VolumeTypeConstance.MACD) {
                    maxValue = Math.max(maxValue, klineMACD.getMACD(q));
                    maxValue = Math.max(maxValue, klineMACD.getDea(q));
                    maxValue = Math.max(maxValue, klineMACD.getDIFF(q));

                    minValue = Math.min(minValue, klineMACD.getMACD(q));
                    minValue = Math.min(minValue, klineMACD.getDea(q));
                    minValue = Math.min(minValue, klineMACD.getDIFF(q));
                }else if (type == VolumeTypeConstance.Volume) {
                    maxValue = Math.max(maxValue, kLineList.get(q).getVolume());
                }
            }
        }
        return new double[]{maxValue, minValue, (maxValue - minValue)};
    }

    /*
     * 绘制K线副图
     * */
    private void DrawSubOnDaily(Canvas canvas, float AveWidth, float AveHeight, float RightAxis, double Max, float ItemInterval, int starIndex, int endIndex) {
        if (type == VolumeTypeConstance.KDJ) {
            PrintLogD("绘制KDJ");
            float HalfAveWidth = AveWidth * 0.5f;
            float AveWidthWithInterval = (AveWidth + ItemInterval);
            int size = this.kLineList.size() - 1;
            for (int i = starIndex; i <= endIndex; i++) {
                int q = Math.abs(size - i);
                float right = RightAxis - AveWidthWithInterval * i;
                float LineOnX = right - HalfAveWidth;
                if (i == 0) {
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineKDJ.getKData(q))), 1, KPaint);
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineKDJ.getDData(q))), 1, DPaint);
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineKDJ.getJData(q))), 1, JPaint);
                } else {
                    float lastLineOnX = LineOnX + AveWidthWithInterval;
                    canvas.drawLine(lastLineOnX, (float) (AveHeight * (Max - klineKDJ.getKData(q + 1))),
                            LineOnX, (float) (AveHeight * (Max - klineKDJ.getKData(q))), KPaint);
                    canvas.drawLine(lastLineOnX, (float) (AveHeight * (Max - klineKDJ.getDData(q + 1))),
                            LineOnX, (float) (AveHeight * (Max - klineKDJ.getDData(q))), DPaint);
                    canvas.drawLine(lastLineOnX, AveHeight * (float) (Max - klineKDJ.getJData(q + 1)),
                            LineOnX, (float) (AveHeight * (Max - klineKDJ.getJData(q))), JPaint);
                }
            }
        } else if (type == VolumeTypeConstance.MACD) {
            PrintLogD("绘制MACD");
            float HalfAveWidth = AveWidth * 0.5f;
            float AveWidthWithInterval = (AveWidth + ItemInterval);
            int size = this.kLineList.size() - 1;
            for (int i = starIndex; i <= endIndex; i++) {
                int q = Math.abs(size - i);
                float right = RightAxis - AveWidthWithInterval * i;
                float LineOnX = right - HalfAveWidth;
                float MacdOnY = (float) (AveHeight * (Max - klineMACD.getMACD(q)));
                float MacdOnZero = (float) (AveHeight * Max);
                float left = right-AveWidth;
                if (klineMACD.getMACD(q) > 0) {
                    canvas.drawRect(left, MacdOnY, right, MacdOnZero, UpPaint);
                } else if (klineMACD.getMACD(q) < 0) {
                    canvas.drawRect(left, MacdOnZero, right, MacdOnY, DownPaint);
                } else {
                    canvas.drawRect(left, MacdOnY, right, MacdOnZero, EqualPaint);
                }
                if (i == 0) {
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineMACD.getDIFF(q))), 1, KPaint);
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineMACD.getDea(q))), 1, DPaint);
                } else {
                    float lastLineOnX = LineOnX + AveWidthWithInterval;
                    canvas.drawLine(lastLineOnX, (float) (AveHeight * (Max - klineMACD.getDIFF(q + 1))),
                            LineOnX, (float) (AveHeight * (Max - klineMACD.getDIFF(q))), KPaint);
                    canvas.drawLine(lastLineOnX, (float) (AveHeight * (Max - klineMACD.getDea(q + 1))),
                            LineOnX, (float) (AveHeight * (Max - klineMACD.getDea(q))), DPaint);
                }
            }
        }else if (type == VolumeTypeConstance.Volume){
            PrintLogD("绘制VOL");
            float HalfAveWidth = AveWidth * 0.5f;
            float AveWidthWithInterval = (AveWidth + ItemInterval);
            int size = this.kLineList.size() - 1;
            for (int i = starIndex; i <= endIndex; i++) {
                int q = Math.abs(size - i);
                float right = RightAxis - AveWidthWithInterval * i;
                float left = right-AveWidth;
                float top = (float) (AveHeight * (Max - kLineList.get(q).getVolume()));
                if (q==0){
                    JudgeColumnColor(kLineList.get(q).getVolume(), kLineList.get(q).getVolume(), canvas, left, right, top, getMeasuredHeight());
                }else {
                    JudgeColumnColor(kLineList.get(q-1).getVolume(), kLineList.get(q).getVolume(), canvas, left, right, top, getMeasuredHeight());
                }
            }
        }

    }

    private void DrawSubOnDailyLess(Canvas canvas, float AveWidth, float AveHeight, float LeftAxis, double Max, float ItemInterval, int starIndex, int endIndex){
        if (type == VolumeTypeConstance.KDJ) {
            PrintLogD("绘制KDJ");
            float HalfAveWidth = AveWidth * 0.5f;
            float AveWidthWithInterval = (AveWidth + ItemInterval);
//            int size = this.kLineList.size() - 1;
            for (int i = starIndex; i <= endIndex; i++) {
                float left = LeftAxis + AveWidthWithInterval * i;
                float LineOnX = left + HalfAveWidth;
                if (i == 0) {
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineKDJ.getKData(i))), 1, KPaint);
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineKDJ.getDData(i))), 1, DPaint);
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineKDJ.getJData(i))), 1, JPaint);
                } else {
                    float nextLineOnX = LineOnX + AveWidthWithInterval;
                    canvas.drawLine(LineOnX, (float) (AveHeight * (Max - klineKDJ.getKData(i))),
                            nextLineOnX, (float) (AveHeight * (Max - klineKDJ.getKData(i+1))), KPaint);
                    canvas.drawLine(LineOnX, (float) (AveHeight * (Max - klineKDJ.getDData(i))),
                            nextLineOnX, (float) (AveHeight * (Max - klineKDJ.getDData(i+1))), DPaint);
                    canvas.drawLine(LineOnX, AveHeight * (float) (Max - klineKDJ.getJData(i)),
                            nextLineOnX, (float) (AveHeight * (Max - klineKDJ.getJData(i+1))), JPaint);
                }
            }
        } else if (type == VolumeTypeConstance.MACD) {
            PrintLogD("绘制MACD");
            float HalfAveWidth = AveWidth * 0.5f;
            float AveWidthWithInterval = (AveWidth + ItemInterval);
//            int size = this.kLineList.size() - 1;
            for (int i = starIndex; i <= endIndex; i++) {
                float left = LeftAxis + AveWidthWithInterval * i;
                float LineOnX = left + HalfAveWidth;
                float MacdOnY = (float) (AveHeight * (Max - klineMACD.getMACD(i)));
                float MacdOnZero = (float) (AveHeight * Max);
                float right = left+AveWidth;
                if (klineMACD.getMACD(i) > 0) {
                    canvas.drawRect(left, MacdOnY, right, MacdOnZero, UpPaint);
                } else if (klineMACD.getMACD(i) < 0) {
                    canvas.drawRect(left, MacdOnZero, right, MacdOnY, DownPaint);
                } else {
                    canvas.drawRect(left, MacdOnY, right, MacdOnZero, EqualPaint);
                }
                if (i == 0) {
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineMACD.getDIFF(i))), 1, KPaint);
                    canvas.drawCircle(LineOnX, (float) (AveHeight * (Max - klineMACD.getDea(i))), 1, DPaint);
                } else {
                    float nextLineOnX = LineOnX + AveWidthWithInterval;
                    canvas.drawLine(LineOnX, (float) (AveHeight * (Max - klineMACD.getDIFF(i))),
                            nextLineOnX, (float) (AveHeight * (Max - klineMACD.getDIFF(i+1))), KPaint);
                    canvas.drawLine(LineOnX, (float) (AveHeight * (Max - klineMACD.getDea(i))),
                            nextLineOnX, (float) (AveHeight * (Max - klineMACD.getDea(i+1))), DPaint);
                }
            }
        }else if (type == VolumeTypeConstance.Volume){
            PrintLogD("绘制VOL");
            float HalfAveWidth = AveWidth * 0.5f;
            float AveWidthWithInterval = (AveWidth + ItemInterval);
            for (int i = starIndex; i <= endIndex; i++) {
                float left = LeftAxis + AveWidthWithInterval * i;
                float right = left+AveWidth;
                float top = (float) (AveHeight * (Max - kLineList.get(i).getVolume()));
                if (i==0){
                    JudgeColumnColor(kLineList.get(i).getVolume(), kLineList.get(i).getVolume(), canvas, left, right, top, getMeasuredHeight());
                }else {
                    JudgeColumnColor(kLineList.get(i-1).getVolume(), kLineList.get(i).getVolume(), canvas, left, right, top, getMeasuredHeight());
                }
            }
        }
    }


    /*
     * 绘制指示线
     * */
    private void DrawIndicateLineOnDaily(Canvas canvas, float indicateLineX, float indicateLineY, float left, float top, float right, float btm,
                                         float aveOfWidth, float aveOfHeight, float ItemInterval, int endIndex, float allBtm, double MaxValue) {
        if (indicateLineX > right) {
            canvas.drawLine(right, top, right, btm, BlackPaint); //限制越右边界指示竖线
        } else if (indicateLineX < left) {
            canvas.drawLine(left, top, left, btm, BlackPaint); //限制越左边界指示竖线
        } else {
            canvas.drawLine(indicateLineX, top, indicateLineX, btm, BlackPaint); //指示竖线
        }
//        DrawIndicateData(canvas, left, top, right, btm, allBtm, aveOfWidth, aveOfHeight, ItemInterval, indicateLineX, indicateLineY, endIndex, MaxValue);
    }


    /*
     * 初始画笔
     * */
    private void InitPaint() {
        PrintLogD("初始画笔");
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
        //虚线笔
        DottedPaint = new Paint();
        DottedPaint.setColor(DottedLineColor);
        DottedPaint.setStyle(Paint.Style.FILL);
        DottedPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        DottedPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        DottedPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        //相等笔
        EqualPaint = new Paint();
        EqualPaint.setColor(EqualColor);
        EqualPaint.setStyle(Paint.Style.FILL);
        EqualPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        EqualPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        //K笔
        KPaint = new Paint();
        KPaint.setColor(KColor);
        KPaint.setStyle(Paint.Style.FILL);
        KPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        KPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        //D笔
        DPaint = new Paint();
        DPaint.setColor(DColor);
        DPaint.setStyle(Paint.Style.FILL);
        DPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        DPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        //J笔
        JPaint = new Paint();
        JPaint.setColor(JColor);
        JPaint.setStyle(Paint.Style.FILL);
        JPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        JPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
    }


    /*
     * 分时数据联动
     * */
    @Override
    public void DataLink(List<TrendDataModel> dataModels, float yesterdayPrice) {
        if (dataList == null) {
            dataList = new ArrayList<TrendDataModel>();
        }
        dataList.clear();
        dataList.addAll(dataModels);
        for (TrendDataModel trendDataModel : dataList) {
            MaxValue = Math.max(MaxValue, trendDataModel.getTradeAmount());
        }
        this.YesterdayPrice = yesterdayPrice;
        invalidate();
    }

    /*
     * K线数据联动
     * */
    @Override
    public void DataLink(List<StockKLine> dataModels) {
        setkLineList(dataModels);
        invalidate();
    }

    @Override
    public void LongPressLink(boolean isMove, float moveX, float moveY) {
        this.isLongPress = isMove;
        IndicateLineX = moveX;
        IndicateLineY = moveY;
        invalidate();
    }

    @Override
    public void ScaleLink(boolean invalidate, int itemSize, int markScaleIndex) {
        setDefItemSize(itemSize);
        this.markStartIndexOfScale = markScaleIndex;
        if (invalidate) {
            isDoublePress = true;
            invalidate();
        }
        isLongPress = false;
    }

    @Override
    public void MoveLink(float moveDistance, int markScaleIndex) {
        ScrollSumDis = moveDistance;
        markStartIndexOfScale = markScaleIndex;
        isLongPress = false;
        invalidate();
    }

    @Override
    public void ShowDataOnLongPress(int targetIndex) {
        if (dailyLongPressListener != null) {
            if (kLineList != null) {
                if (klineKDJ != null && type == VolumeTypeConstance.KDJ) {
                    int i = Math.abs(kLineList.size() - 1 - targetIndex);
                    dailyLongPressListener.onKDJLongPress(klineKDJ.getKData(i), klineKDJ.getDData(i), klineKDJ.getJData(i));
                } else if (klineMACD != null && type == VolumeTypeConstance.MACD) {
                    int i = Math.abs(kLineList.size() - 1 - targetIndex);
                    dailyLongPressListener.onMACDLongPress(klineMACD.getMACD(i), klineMACD.getDIFF(i), klineMACD.getDea(i));
                }
            }
        }
    }

    public float getDefItemSize() {
        return DefItemSize;
    }

    public void setDefItemSize(float defItemSize) {
        DefItemSize = defItemSize;
    }

    public void setkLineList(List<StockKLine> kLineList) {
        if (this.kLineList == null) {
            this.kLineList = new ArrayList<StockKLine>();
        }
        this.kLineList.clear();
        if (this.kLineList.addAll(kLineList)) {
            Collections.reverse(this.kLineList);
            if (type == VolumeTypeConstance.KDJ) {
                PrintLogD("更新KDJ数据");
                klineKDJ = YRBasePresenter.Create().GetKLineKDJ(this.kLineList);
                return;
            }
            if (type == VolumeTypeConstance.MACD) {
                PrintLogD("更新MACD数据");
                klineMACD = YRBasePresenter.Create().GetKLineMACD(this.kLineList);
                return;
            }
            if (type == VolumeTypeConstance.Volume) {
                PrintLogD("更新成交量数据");

                return;
            }
        }
    }

    public void setDailyLongPressListener(OnDailyLongPressListener dailyLongPressListener) {
        this.dailyLongPressListener = dailyLongPressListener;
    }

    public int getUpColor() {
        return UpColor;
    }

    public int getDownColor() {
        return DownColor;
    }

    public int getEqualColor() {
        return EqualColor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        if (type == VolumeTypeConstance.KDJ) {
            PrintLogD("更新KDJ数据");
            klineKDJ = YRBasePresenter.Create().GetKLineKDJ(this.kLineList);
            invalidate();
            return;
        }
        if (type == VolumeTypeConstance.MACD) {
            PrintLogD("更新MACD数据");
            klineMACD = YRBasePresenter.Create().GetKLineMACD(this.kLineList);
            invalidate();
            return;
        }
        if (type == VolumeTypeConstance.Volume) {
            PrintLogD("更新成交量数据");

            invalidate();
            return;
        }
    }
}
