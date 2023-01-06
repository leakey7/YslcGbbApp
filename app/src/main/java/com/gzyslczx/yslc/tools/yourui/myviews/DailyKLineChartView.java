package com.gzyslczx.yslc.tools.yourui.myviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.yourui.DailyMAEntity;
import com.yourui.sdk.message.use.StockKLine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DailyKLineChartView extends View {

    private int UpColor, DownColor, BlackColor, WhiteColor, GrayColor, MA5Color, MA10Color, MA20Color, MA30Color;
    private Paint UpPaint, DownPaint, BlackPaint, WhitePaint, GrayPaint, MA5Paint, MA10Paint, MA20Paint, MA30Paint;
    private List<StockKLine> DataList;
    private List<DailyMAEntity> maEntityList;
    private int[] ItemSizeLevel = new int[]{9, 11, 13, 15, 17, 19, 21, 24, 27, 31, 35, 40, 46, 54, 64, 76, 90, 106, 124, 144, 166, 190, 216, 250}; //缩放档位
    private int CurrentLevelIndex = 11; //当前缩放档位
    private final int MaxLevelIndex = 23;
    private GestureDetector mGestureDetector;
    private float ScrollSumDis = 0;
    private int MARecord=0;
    private DecimalFormat decimalFormat;
    private boolean isLongPress=false, enableLongPress=false; //长按
    private float X1=0, X2=0, Y1=0, Y2=0; //记录双指（X，Y）
    private boolean isDoublePress = false; //是否双指操作
    private int markStartIndexOfScale = 0; //记录右端Index
    private float IndicateLineX =0, IndicateLineY=0; //指示线(X,Y)
    private OnDailyLongPressListener longPressListener;
    private OnDailyStockLoadMoreListener loadMoreListener;
    private boolean isLoadMore = false;
    private boolean isLoadMoreEnd = false;
    private List<DailyVolumeLink> SubLinkList;

    public DailyKLineChartView(Context context) {
        super(context);
    }

    public DailyKLineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StockChartView);
        UpColor = typedArray.getColor(R.styleable.StockChartView_UpColor, ContextCompat.getColor(context, R.color.red_up));
        DownColor = typedArray.getColor(R.styleable.StockChartView_DownColor, ContextCompat.getColor(context, R.color.green_down));
        BlackColor = typedArray.getColor(R.styleable.StockChartView_BlackColor, ContextCompat.getColor(context, R.color.black_333));
        WhiteColor = typedArray.getColor(R.styleable.StockChartView_WhiteColor, ContextCompat.getColor(context, R.color.white));
        GrayColor = typedArray.getColor(R.styleable.StockChartView_GrayColor, ContextCompat.getColor(context, R.color.gray_FAF8F9));
        MA5Color = typedArray.getColor(R.styleable.StockChartView_MaFiveColor, ContextCompat.getColor(context, R.color.black_333));
        MA10Color = typedArray.getColor(R.styleable.StockChartView_MaTenColor, ContextCompat.getColor(context, R.color.orange_FF8C00));
        MA20Color = typedArray.getColor(R.styleable.StockChartView_MaTwentyColor, ContextCompat.getColor(context, R.color.yellow_FFCF00));
        MA30Color = typedArray.getColor(R.styleable.StockChartView_MaThirtyColor, ContextCompat.getColor(context, R.color.blue_2F7DFF));
        typedArray.recycle();
        InitPaint();
        decimalFormat = new DecimalFormat("#0.00");
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                PrintLogD("手指按下");
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                getParent().requestDisallowInterceptTouchEvent(true);
                PrintLogD("滑动"); //从左往右滑，滑动距离为负数，反之为正数。
                if (distanceX < 0) {
                    ScrollSumDis -= (int) distanceX;
                } else if (distanceX > 0) {
                    ScrollSumDis -= (int) distanceX;
                }
                if (SubLinkList!=null){
                    for (DailyVolumeLink link : SubLinkList) {
                        link.MoveLink(ScrollSumDis, markStartIndexOfScale);
                    }
                }
                invalidate();
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (enableLongPress) {
                    PrintLogD("确认长按");
                    isLongPress = true;
                    isDoublePress=false;
                }
                super.onLongPress(e);
            }
        });
    }

    private void PrintLogD(String log) {
        Log.d(getClass().getSimpleName(), log);
    }

    public DailyKLineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DailyKLineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawDailyKLineChart(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount()==2){
            //双指操作
            getParent().requestDisallowInterceptTouchEvent(true);
            if (!isDoublePress) {
                PrintLogD("确认双指");
                isDoublePress=true;
                isLongPress=false;
                X1= event.getX(0); //A指X点
                Y1= event.getY(0); //A指Y点
                X2= event.getX(1); //B指X点
                Y2= event.getY(1); //B指Y点
                if (longPressListener!=null){
                    longPressListener.onCancelLongPress();
                }
            }else {
                float x1 = event.getX(0);
                float y1 = event.getY(0);
                float x2 = event.getX(1);
                float y2 = event.getY(1);
                float DistanceX = Math.abs(X1 - X2);
                float distanceX = Math.abs(x1 - x2);
                float DistanceY = Math.abs(Y1 - Y2);
                float distanceY = Math.abs(y1 - y2);
                if (DistanceX - distanceX <= -60 || DistanceY - distanceY <= -60) {
                    //伸展
                    if (CurrentLevelIndex > 0) {
                        --CurrentLevelIndex;
                        PrintLogD(String.format("当前伸展ItemSize=%d", ItemSizeLevel[CurrentLevelIndex]));
                        X1 = x1;
                        Y1 = y1;
                        X2 = x2;
                        Y2= y2;
                        if (SubLinkList!=null){
                            for (DailyVolumeLink link : SubLinkList) {
                                link.ScaleLink(true, ItemSizeLevel[CurrentLevelIndex], markStartIndexOfScale);
                            }
                        }
                        invalidate();
                    } else {
                        PrintLogD(String.format("当前ItemSize=%d", ItemSizeLevel[CurrentLevelIndex]));
                        PrintLogD("当前为最小尺寸");
                    }
                } else if (DistanceX - distanceX >= 60 || DistanceY - distanceY >= 60) {
                    //收缩
                    if (CurrentLevelIndex<MaxLevelIndex) {
                        ++CurrentLevelIndex;
                        PrintLogD(String.format("当前收缩ItemSize=%d", ItemSizeLevel[CurrentLevelIndex]));
                        if (DataList.size()<ItemSizeLevel[CurrentLevelIndex]){
                            if (loadMoreListener!=null && !isLoadMore && !isLoadMoreEnd){
                                isLoadMore = true;
                                loadMoreListener.onLoadMoreDailyStock(); //通知加载更多
                                PrintLogD("通知加载更多");
                            }
                        }
                        X1 = x1;
                        Y1 = y1;
                        X2 = x2;
                        Y2 = y2;
                        if (markStartIndexOfScale+ItemSizeLevel[CurrentLevelIndex] > DataList.size()){
                            markStartIndexOfScale = DataList.size()-ItemSizeLevel[CurrentLevelIndex];
                        }
                        if (SubLinkList!=null){
                            for (DailyVolumeLink link : SubLinkList) {
                                link.ScaleLink(true, ItemSizeLevel[CurrentLevelIndex], markStartIndexOfScale);
                            }
                        }
                        invalidate();
                    } else {
                        PrintLogD(String.format("当前ItemSize=%d", ItemSizeLevel[CurrentLevelIndex]));
                        PrintLogD("当前为最大尺寸");
                    }
                }
            }
            return super.onTouchEvent(event);
        }else if (event.getPointerCount()==1){
            //单指操作
            if (isDoublePress){
                //双指状态
                isDoublePress = false;
                return super.onTouchEvent(event);
            }else if (isLongPress){
                //长按状态
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        PrintLogD("按下取消指示线");
                        isLongPress = false;
                        IndicateLineX=0;
                        IndicateLineY=0;
                        if (SubLinkList!=null){
                            for (DailyVolumeLink link : SubLinkList) {
                                link.LongPressLink(false, IndicateLineX, IndicateLineY);
                            }
                        }
                        if (longPressListener!=null){
                            longPressListener.onCancelLongPress();
                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        getParent().requestDisallowInterceptTouchEvent(true);
                        PrintLogD("长按后滑动");
                        IndicateLineX = event.getX();
                        IndicateLineY = event.getY();
                        if (SubLinkList!=null){
                            for (DailyVolumeLink link : SubLinkList) {
                                link.LongPressLink(true, IndicateLineX, IndicateLineY);
                            }
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
     * 绘制K图
     * */
    private void DrawDailyKLineChart(Canvas canvas) {
        PrintLogD("绘制K图");
        float topOnYAxis = 0; //顶部坐标Y点
        float leftOnXAxis = 0; //左端坐标X点
        float rightOnXAxis = leftOnXAxis + getMeasuredWidth(); //右端坐标X点
        float btmOnYAxis = topOnYAxis + getMeasuredHeight(); //底部坐标Y点
        float btmWithoutTimeOnYAxis = btmOnYAxis - DisplayTool.dp2px(getContext(), 10); //底部时间轴Y坐标
        float ChartViewHeight = btmWithoutTimeOnYAxis - topOnYAxis; //图可绘制高度
        //计算相关高度和Y轴坐标
        float quarterHeight = ChartViewHeight / 4f; //View高度的四分一
        float quarterHeightOnY = topOnYAxis + quarterHeight; //四分之一高度Y坐标点
        float halfHeightOnY = topOnYAxis + quarterHeight * 2f; //二分之一高度Y坐标点
        float threeQuarterHeightOnY = topOnYAxis + quarterHeight * 3f; //四分之三高度Y坐标点
        //绘制宫格线
        canvas.drawLine(leftOnXAxis, topOnYAxis, rightOnXAxis, topOnYAxis, GrayPaint); //顶横线
        canvas.drawLine(leftOnXAxis, quarterHeightOnY, rightOnXAxis, quarterHeightOnY, GrayPaint); //一横线
        canvas.drawLine(leftOnXAxis, halfHeightOnY, rightOnXAxis, halfHeightOnY, GrayPaint); //二横线
        canvas.drawLine(leftOnXAxis, threeQuarterHeightOnY, rightOnXAxis, threeQuarterHeightOnY, GrayPaint); //三横线
        canvas.drawLine(leftOnXAxis, btmWithoutTimeOnYAxis, rightOnXAxis, btmWithoutTimeOnYAxis, GrayPaint); //底横线
        if (DataList != null && DataList.size() > 0) {
            PrintLogD(String.format("数据总量：%d", DataList.size()));
            float ItemInterval = DisplayTool.dp2px(getContext(), 2); //间隔距离
            float AveWidthOfItem = (getMeasuredWidth() - ItemInterval * (ItemSizeLevel[CurrentLevelIndex] - 1)) / ItemSizeLevel[CurrentLevelIndex]; //每项平均宽度
            int scrollIndex; //滑动距离
            if (isDoublePress){
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
                if (DataList.size()>=ItemSizeLevel[CurrentLevelIndex]) {
                    dis = (AveWidthOfItem + ItemInterval) * (DataList.size() - ItemSizeLevel[CurrentLevelIndex]); //可发生的最大滑动距离
                }else {
                    dis = (AveWidthOfItem + ItemInterval) * (DataList.size()-((float) getMeasuredWidth()-AveWidthOfItem)); //可发生的最大滑动距离
                }
                PrintLogD("最大滑动距离:"+dis);
                if (dis>=0 && ScrollSumDis > dis) {
                    ScrollSumDis = dis;
                    if (loadMoreListener!=null && !isLoadMore && !isLoadMoreEnd){
                        isLoadMore = true;
                        loadMoreListener.onLoadMoreDailyStock(); //通知加载更多
                        PrintLogD("通知加载更多");
                    }
                }else if (dis<0){
                    ScrollSumDis=0;
                }
            }
            float Right = rightOnXAxis + ScrollSumDis; //适配平移距离最右侧坐标
            scrollIndex = Math.round((ScrollSumDis / (AveWidthOfItem + ItemInterval))); //适配平移距离后右侧Index
            markStartIndexOfScale = scrollIndex; //记录右侧Index，适配缩放
            int size = DataList.size()-1;
            int endIndex = scrollIndex + (ItemSizeLevel[CurrentLevelIndex] - 1); //适配平移距离后左侧Index
            if (endIndex>size){
                endIndex = size; //限制Index越界
            }
            double[] Max_Min_Dif = CountMaxValue(scrollIndex, endIndex); //计算最值
            float AveHeightOfItem = (float) (ChartViewHeight / Max_Min_Dif[2]); //每单位平均高度
            DrawTime(canvas, endIndex, scrollIndex, leftOnXAxis, rightOnXAxis, btmOnYAxis); //绘制时间
            if (ItemSizeLevel[CurrentLevelIndex] <= DataList.size()) {
                DrawKLine(canvas, AveWidthOfItem, AveHeightOfItem, Right, Max_Min_Dif[0], ItemInterval, scrollIndex, endIndex); //绘制蜡烛图
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
                    if (IndicateLineY > btmWithoutTimeOnYAxis) {
                        IndicateLineY = btmWithoutTimeOnYAxis;
                    }
                    DrawIndicateLine(canvas, IndicateLineX, IndicateLineY, leftOnXAxis, topOnYAxis, rightOnXAxis, btmWithoutTimeOnYAxis,
                            AveWidthOfItem, AveHeightOfItem, ItemInterval, endIndex, btmOnYAxis, Max_Min_Dif[0]); //绘制指示线
                } else {
                    if (longPressListener != null) {
                        longPressListener.onDailyLongPress(false, DataList.get(scrollIndex), maEntityList.get(scrollIndex));
                        if (SubLinkList != null) {
                            for (DailyVolumeLink link : SubLinkList) {
                                link.ShowDataOnLongPress(scrollIndex);
                            }
                        }
                    }
                }
            }else {
                DrawKLineOnLess(canvas, AveWidthOfItem, AveHeightOfItem, leftOnXAxis, Max_Min_Dif[0], ItemInterval, scrollIndex, endIndex);
                if (isLongPress) {
                    if (IndicateLineX < leftOnXAxis) {
                        IndicateLineX = leftOnXAxis;
                    }
                    float limitRight = leftOnXAxis + AveWidthOfItem + (AveWidthOfItem + ItemInterval) * (endIndex - scrollIndex);
                    if (IndicateLineX > limitRight) {
                        IndicateLineX = limitRight;
                    }
                    if (IndicateLineY < topOnYAxis) {
                        IndicateLineY = topOnYAxis;
                    }
                    if (IndicateLineY > btmWithoutTimeOnYAxis) {
                        IndicateLineY = btmWithoutTimeOnYAxis;
                    }
                    DrawIndicateLine(canvas, IndicateLineX, IndicateLineY, leftOnXAxis, topOnYAxis, rightOnXAxis, btmWithoutTimeOnYAxis,
                            AveWidthOfItem, AveHeightOfItem, ItemInterval, endIndex, btmOnYAxis, Max_Min_Dif[0]); //绘制指示线
                } else {
                    if (longPressListener != null) {
                        longPressListener.onDailyLongPress(false, DataList.get(scrollIndex), maEntityList.get(scrollIndex));
                        if (SubLinkList != null) {
                            for (DailyVolumeLink link : SubLinkList) {
                                link.ShowDataOnLongPress(scrollIndex);
                            }
                        }
                    }
                }
            }
        }

    }

    /*
     * 绘制时间
     * */
    private void DrawTime(Canvas canvas, int leftIndex, int rightIndex, float leftOnXAxis, float rightOnXAxis, float btmOnYAxis) {
        PrintLogD("绘制时间");
        String leftDate = String.valueOf(DataList.get(leftIndex).getDate());
        BlackPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(String.format("%s-%s-%s", leftDate.substring(0, 4), leftDate.substring(4, 6), leftDate.substring(6)),
                leftOnXAxis, btmOnYAxis, BlackPaint);
        String rightDate = String.valueOf(DataList.get(rightIndex).getDate());
        BlackPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format("%s-%s-%s", rightDate.substring(0, 4), rightDate.substring(4, 6), rightDate.substring(6)),
                rightOnXAxis, btmOnYAxis, BlackPaint);
    }

    /*
     * 绘制蜡烛图-数据量大于默认显示量
     * */
    private void DrawKLine(Canvas canvas, float AveWidth, float AveHeight, float RightAxis, double Max, float ItemInterval, int starIndex, int endIndex) {
        PrintLogD("绘制蜡烛图");
        float HalfAveWidth = AveWidth * 0.5f;
        float AveWidthWithInterval = (AveWidth + ItemInterval);
        for (int i = starIndex; i <=endIndex; i++) {
            float right = RightAxis - AveWidthWithInterval * i;
            float LineOnX = right - HalfAveWidth;
            if (DataList.get(i).getClosePrice() >= DataList.get(i).getOpenPrice()) {
                //收盘价高于开盘价-上涨笔
                canvas.drawRect(right - AveWidth, AveHeight * (float) (Max - DataList.get(i).getClosePrice()),
                        right, AveHeight * (float) (Max - DataList.get(i).getOpenPrice()), UpPaint);
                canvas.drawLine(LineOnX, AveHeight * (float) (Max - DataList.get(i).getHighPrice()),
                        LineOnX, AveHeight * (float) (Max - DataList.get(i).getLowPrice()), UpPaint);
            } else {
                //收盘价低于开盘价-下跌笔
                canvas.drawRect(right - AveWidth, AveHeight * (float) (Max - DataList.get(i).getOpenPrice()),
                        right, AveHeight * (float) (Max - DataList.get(i).getClosePrice()), DownPaint);
                canvas.drawLine(LineOnX, AveHeight * (float) (Max - DataList.get(i).getHighPrice()),
                        LineOnX, AveHeight * (float) (Max - DataList.get(i).getLowPrice()), DownPaint);
            }
            if (i==0){
                if (maEntityList.get(i).getMA5()!=-1) {
                    canvas.drawCircle(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA5()), 1, MA5Paint);
                }
                if (maEntityList.get(i).getMA10()!=-1) {
                    canvas.drawCircle(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA10()), 1, MA10Paint);
                }
                if (maEntityList.get(i).getMA20()!=-1) {
                    canvas.drawCircle(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA20()), 1, MA20Paint);
                }
                if (maEntityList.get(i).getMA30()!=-1) {
                    canvas.drawCircle(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA30()), 1, MA30Paint);
                }
            }else {
                float lastLineOnX = LineOnX+AveWidthWithInterval;
                if (maEntityList.get(i).getMA5()!=-1) {
                    canvas.drawLine(lastLineOnX, AveHeight * (float) (Max - maEntityList.get(i-1).getMA5()),
                            LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA5()), MA5Paint);
                }
                if (maEntityList.get(i).getMA10()!=-1) {
                    canvas.drawLine(lastLineOnX, AveHeight * (float) (Max - maEntityList.get(i-1).getMA10()),
                            LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA10()), MA10Paint);
                }
                if (maEntityList.get(i).getMA20()!=-1) {
                    canvas.drawLine(lastLineOnX, AveHeight * (float) (Max - maEntityList.get(i-1).getMA20()),
                            LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA20()), MA20Paint);
                }
                if (maEntityList.get(i).getMA30()!=-1) {
                    canvas.drawLine(lastLineOnX, AveHeight * (float) (Max - maEntityList.get(i-1).getMA30()),
                            LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA30()), MA30Paint);
                }
            }
        }
    }

    /*
    * 绘制蜡烛图-数据量少于默认显示量
    * */
    private void DrawKLineOnLess(Canvas canvas, float AveWidth, float AveHeight, float LeftAxis, double Max, float ItemInterval, int starIndex, int endIndex){
        PrintLogD("绘制蜡烛图");
        float HalfAveWidth = AveWidth * 0.5f;
        float AveWidthWithInterval = (AveWidth + ItemInterval);
        for (int i = endIndex; i >=starIndex; i--) {
            float left = LeftAxis + AveWidthWithInterval * Math.abs(i-endIndex);
            float LineOnX = left + HalfAveWidth;
            if (DataList.get(i).getClosePrice() >= DataList.get(i).getOpenPrice()) {
                //收盘价高于开盘价-上涨笔
                canvas.drawRect(left, AveHeight * (float) (Max - DataList.get(i).getClosePrice()),
                        left+AveWidth, AveHeight * (float) (Max - DataList.get(i).getOpenPrice()), UpPaint);
                canvas.drawLine(LineOnX, AveHeight * (float) (Max - DataList.get(i).getHighPrice()),
                        LineOnX, AveHeight * (float) (Max - DataList.get(i).getLowPrice()), UpPaint);
            } else {
                //收盘价低于开盘价-下跌笔
                canvas.drawRect(left, AveHeight * (float) (Max - DataList.get(i).getOpenPrice()),
                        left+AveWidth, AveHeight * (float) (Max - DataList.get(i).getClosePrice()), DownPaint);
                canvas.drawLine(LineOnX, AveHeight * (float) (Max - DataList.get(i).getHighPrice()),
                        LineOnX, AveHeight * (float) (Max - DataList.get(i).getLowPrice()), DownPaint);
            }
            if (i==0){
                if (maEntityList.get(i).getMA5()!=-1) {
                    canvas.drawCircle(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA5()), 1, MA5Paint);
                }
                if (maEntityList.get(i).getMA10()!=-1) {
                    canvas.drawCircle(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA10()), 1, MA10Paint);
                }
                if (maEntityList.get(i).getMA20()!=-1) {
                    canvas.drawCircle(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA20()), 1, MA20Paint);
                }
                if (maEntityList.get(i).getMA30()!=-1) {
                    canvas.drawCircle(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA30()), 1, MA30Paint);
                }
            }else {
                float nextLineOnX = LineOnX+AveWidthWithInterval;
                if (maEntityList.get(i).getMA5()!=-1) {
                    canvas.drawLine(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA5()),
                            nextLineOnX, AveHeight * (float) (Max - maEntityList.get(i-1).getMA5()), MA5Paint);
                }
                if (maEntityList.get(i).getMA10()!=-1) {
                    canvas.drawLine(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA10()),
                            nextLineOnX, AveHeight * (float) (Max - maEntityList.get(i-1).getMA10()), MA10Paint);
                }
                if (maEntityList.get(i).getMA20()!=-1) {
                    canvas.drawLine(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA20()),
                            nextLineOnX, AveHeight * (float) (Max - maEntityList.get(i-1).getMA20()), MA20Paint);
                }
                if (maEntityList.get(i).getMA30()!=-1) {
                    canvas.drawLine(LineOnX, AveHeight * (float) (Max - maEntityList.get(i).getMA30()),
                            nextLineOnX, AveHeight * (float) (Max - maEntityList.get(i-1).getMA30()), MA30Paint);
                }
            }
        }
    }

    /*
     * 绘制指示线
     * */
    private void DrawIndicateLine(Canvas canvas, float indicateLineX, float indicateLineY, float left, float top, float right, float btm,
                                  float aveOfWidth, float aveOfHeight, float ItemInterval, int endIndex, float allBtm, double MaxValue){
        if (indicateLineX>right){
            canvas.drawLine(right, top, right, btm, BlackPaint); //限制越右边界指示竖线
        }else if (indicateLineX<left){
            canvas.drawLine(left, top, left, btm, BlackPaint); //限制越左边界指示竖线
        }else {
            canvas.drawLine(indicateLineX, top, indicateLineX, btm, BlackPaint); //指示竖线
        }
        if (indicateLineY<=top){
            canvas.drawLine(left, top, right, top, BlackPaint); //限制越顶边界指示横线
        }else if (indicateLineY>=btm){
            canvas.drawLine(left, btm, right, btm, BlackPaint); //限制越底边界指示横线
        }else {
            canvas.drawLine(left, indicateLineY, right, indicateLineY, BlackPaint); //指示横线
        }
        DrawIndicateData(canvas, left, top, right, btm, allBtm, aveOfWidth, aveOfHeight, ItemInterval, indicateLineX, indicateLineY, endIndex, MaxValue);
    }
    /*
    * 绘制指示线数据
    * */
    private void DrawIndicateData(Canvas canvas, float left, float top, float right, float btm, float allBtm, float aveOfWidth, float aveOfHeight,
                                  float ItemInterval, float indicateLineX, float indicateLineY, int endIndex, double MaxValue){
        float AveWidthWithInterval = (aveOfWidth + ItemInterval);
        int index = (int) (indicateLineX / AveWidthWithInterval);
        PrintLogD(String.format("endIndex=%s,index=%s", endIndex, index));
        int TargetIndex = endIndex-index;
        if (DataList.size()<ItemSizeLevel[CurrentLevelIndex]){
            TargetIndex = DataList.size()-1-index;
        }
        if (longPressListener!=null){
            longPressListener.onDailyLongPress(true, DataList.get(TargetIndex), maEntityList.get(TargetIndex));
            if (SubLinkList!=null){
                for (DailyVolumeLink link : SubLinkList) {
                    link.ShowDataOnLongPress(TargetIndex);
                }
            }
        }
        String str = String.valueOf(DataList.get(TargetIndex).getDate());
        String date = String.format("%s-%s-%s", str.substring(0, 4), str.substring(4, 6), str.substring(6));
        float rectWidth = GrayPaint.measureText(date)+DisplayTool.dp2px(getContext(), 2);
        float rectHalfWidth = rectWidth/2f;
        if (indicateLineX<rectHalfWidth){
            canvas.drawRect(left, btm, left+rectWidth, allBtm, GrayPaint);
            BlackPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(date, left, allBtm, BlackPaint); //防左越界指示时间
        }else if (indicateLineX>right-rectHalfWidth){
            canvas.drawRect(right-rectWidth, btm, right, allBtm, GrayPaint);
            BlackPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(date, right, allBtm, BlackPaint); //防右越界指示时间
        }else {
            canvas.drawRect(indicateLineX-rectHalfWidth, btm, indicateLineX+rectHalfWidth, allBtm, GrayPaint);
            BlackPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(date, indicateLineX, allBtm, BlackPaint); //指示时间
        }
        BlackPaint.setTextAlign(Paint.Align.LEFT);
        if (indicateLineY<=top){
            float targetHeightValue = (float) (MaxValue-top / aveOfHeight);
            canvas.drawText(decimalFormat.format(targetHeightValue), left, top+DisplayTool.dp2px(getContext(), 10), BlackPaint); //防顶越界指示高度值
        }else if (indicateLineY>=btm){
            float targetHeightValue = (float) (MaxValue-btm / aveOfHeight);
            canvas.drawText(decimalFormat.format(targetHeightValue), left, btm-DisplayTool.dp2px(getContext(), 1), BlackPaint); //防底越界指示高度值
        }else {
            float targetHeightValue = (float) (MaxValue-indicateLineY / aveOfHeight);
            canvas.drawText(decimalFormat.format(targetHeightValue), left, indicateLineY-DisplayTool.dp2px(getContext(), 1), BlackPaint); //指示高度值
        }
    }

    private void InitPaint() {
        PrintLogD("初始化画笔");
        //上涨笔
        UpPaint = new Paint();
        UpPaint.setColor(UpColor);
        UpPaint.setStyle(Paint.Style.FILL);
        UpPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        UpPaint.setTextSize(DisplayTool.sp2px(getContext(), 14));
        //下跌笔
        DownPaint = new Paint();
        DownPaint.setColor(DownColor);
        DownPaint.setStyle(Paint.Style.FILL);
        DownPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        DownPaint.setTextSize(DisplayTool.sp2px(getContext(), 14));
        //黑色笔
        BlackPaint = new Paint();
        BlackPaint.setColor(BlackColor);
        BlackPaint.setStyle(Paint.Style.FILL);
        BlackPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        BlackPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        //灰色笔
        GrayPaint = new Paint();
        GrayPaint.setColor(GrayColor);
        GrayPaint.setStyle(Paint.Style.FILL);
        GrayPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        GrayPaint.setTextSize(DisplayTool.sp2px(getContext(), 14));
        //白色笔
        WhitePaint = new Paint();
        WhitePaint.setColor(WhiteColor);
        WhitePaint.setStyle(Paint.Style.FILL);
        WhitePaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        WhitePaint.setTextSize(DisplayTool.sp2px(getContext(), 14));
        //MA5笔
        MA5Paint = new Paint();
        MA5Paint.setColor(MA5Color);
        MA5Paint.setStyle(Paint.Style.FILL);
        MA5Paint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        MA5Paint.setTextSize(DisplayTool.sp2px(getContext(), 14));
        //MA10笔
        MA10Paint = new Paint();
        MA10Paint.setColor(MA10Color);
        MA10Paint.setStyle(Paint.Style.FILL);
        MA10Paint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        MA10Paint.setTextSize(DisplayTool.sp2px(getContext(), 14));
        //MA20笔
        MA20Paint = new Paint();
        MA20Paint.setColor(MA20Color);
        MA20Paint.setStyle(Paint.Style.FILL);
        MA20Paint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        MA20Paint.setTextSize(DisplayTool.sp2px(getContext(), 14));
        //MA30笔
        MA30Paint = new Paint();
        MA30Paint.setColor(MA30Color);
        MA30Paint.setStyle(Paint.Style.FILL);
        MA30Paint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        MA30Paint.setTextSize(DisplayTool.sp2px(getContext(), 14));

    }

    /*
     * 初始化数据集
     * */
    public void SetDataList(List<StockKLine> DataList) {
        PrintLogD("初始化数据集");
        this.DataList.clear();
        if (this.DataList.addAll(DataList)) {
            SetMA(true);
            if (SubLinkList!=null){
                for (DailyVolumeLink link : SubLinkList) {
                    link.ScaleLink(false, ItemSizeLevel[CurrentLevelIndex], markStartIndexOfScale);
                    link.DataLink(this.DataList);
                }
            }
        }
        invalidate();
    }

    /*
     * 添加数据
     * */
    public void AddData(List<StockKLine> DataList) {
        if (this.DataList == null) {
            this.DataList = new ArrayList<StockKLine>();
            SetDataList(DataList);
            return;
        }
        PrintLogD("加载更多数据");
        MARecord = this.DataList.size()-30;
        if (this.DataList.addAll(DataList)) {
            SetMA(false);
            if (SubLinkList!=null){
                for (DailyVolumeLink link : SubLinkList) {
                    link.ScaleLink(false, ItemSizeLevel[CurrentLevelIndex], markStartIndexOfScale);
                    link.DataLink(this.DataList);
                }
            }
        }
        isLoadMore = false;
        invalidate();
    }

    /*
     * MA赋值
     * */
    private void SetMA(boolean clear) {
        if (maEntityList == null) {
            maEntityList = new ArrayList<DailyMAEntity>();
        }
        if (clear) {
            maEntityList.clear();
            MARecord=0;
        }
        for (int i = MARecord; i < DataList.size(); i++) {
            DailyMAEntity entity = new DailyMAEntity();
            if (i <= DataList.size() - 5) {
                float ma5 = 0;
                for (int q = 0; q < 5; q++) {
                    ma5 += DataList.get(i + q).closePrice;
                }
                ma5 /= 5f;
                entity.setMA5(Float.parseFloat(decimalFormat.format(ma5)));
            }
            if (i <= DataList.size() - 10) {
                float ma10 = 0;
                for (int q = 0; q < 10; q++) {
                    ma10 += DataList.get(i + q).closePrice;
                }
                ma10 /= 10f;
                entity.setMA10(Float.parseFloat(decimalFormat.format(ma10)));
            }
            if (i <= DataList.size() - 20) {
                float ma20 = 0;
                for (int q = 0; q < 20; q++) {
                    ma20 += DataList.get(i + q).closePrice;
                }
                ma20 /= 20f;
                entity.setMA20(Float.parseFloat(decimalFormat.format(ma20)));
            }
            if (i <= DataList.size() - 30) {
                float ma30 = 0;
                for (int q = 0; q < 30; q++) {
                    ma30 += DataList.get(i + q).closePrice;
                }
                ma30 /= 30f;
                entity.setMA30(Float.parseFloat(decimalFormat.format(ma30)));
            }
            entity.setDate(DataList.get(i).date);
            if (this.maEntityList.size()>i){
                this.maEntityList.set(i,entity);
            }else {
                this.maEntityList.add(entity);
            }
        }
    }

    /*
     * 计算最值
     * */
    private double[] CountMaxValue(int startIndex, int endIndex) {
        PrintLogD(String.format("计算最值,开始Index=%d", startIndex));
        PrintLogD(String.format("计算最值,结尾Index=%d", endIndex));
        double maxValue = 0, minValue = Double.MAX_VALUE;
        for (int i = startIndex; i <= endIndex; i++) {
            if (i == startIndex) {
                maxValue = Math.max(DataList.get(i).getClosePrice(), DataList.get(i).getOpenPrice());
                maxValue = Math.max(maxValue, DataList.get(i).getHighPrice());
                minValue = Math.min(DataList.get(i).getClosePrice(), DataList.get(i).getOpenPrice());
                minValue = Math.min(minValue, DataList.get(i).getLowPrice());
            } else {
                maxValue = Math.max(maxValue, DataList.get(i).getOpenPrice());
                maxValue = Math.max(maxValue, DataList.get(i).getClosePrice());
                maxValue = Math.max(maxValue, DataList.get(i).getHighPrice());
                minValue = Math.min(minValue, DataList.get(i).getOpenPrice());
                minValue = Math.min(minValue, DataList.get(i).getClosePrice());
                minValue = Math.min(minValue, DataList.get(i).getLowPrice());
            }
            if (maEntityList.get(i).getMA5()!=-1){
                maxValue = Math.max(maxValue, maEntityList.get(i).getMA5());
                minValue = Math.min(minValue, maEntityList.get(i).getMA5());
            }
            if (maEntityList.get(i).getMA10()!=-1){
                maxValue = Math.max(maxValue, maEntityList.get(i).getMA10());
                minValue = Math.min(minValue, maEntityList.get(i).getMA10());
            }
            if (maEntityList.get(i).getMA20()!=-1){
                maxValue = Math.max(maxValue, maEntityList.get(i).getMA20());
                minValue = Math.min(minValue, maEntityList.get(i).getMA20());
            }
            if (maEntityList.get(i).getMA30()!=-1){
                maxValue = Math.max(maxValue, maEntityList.get(i).getMA30());
                minValue = Math.min(minValue, maEntityList.get(i).getMA30());
            }
        }
        return new double[]{maxValue, minValue, (maxValue - minValue)};
    }


    public void setEnableLongPress(boolean enableLongPress) {
        this.enableLongPress = enableLongPress;
    }

    public void setLongPressListener(OnDailyLongPressListener longPressListener) {
        this.longPressListener = longPressListener;
    }

    public void setLoadMoreListener(OnDailyStockLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setLoadMoreEnd(boolean loadMoreEnd) {
        isLoadMoreEnd = loadMoreEnd;
    }

    public void setSubLink(DailyVolumeLink dailyVolumeLink) {
        if (SubLinkList==null){
            SubLinkList = new ArrayList<>();
        }
        SubLinkList.add(dailyVolumeLink);
    }

}
