package com.gzyslczx.yslc.tools.yourui.myviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.tools.DisplayTool;

public class EntrustGainView extends View {

    private Paint RedPaint, GreenPaint, GrayPaint;
    private int RedColor, GreenColor, GrayColor;
    private double RedGain=0, GreenGain=0;

    public EntrustGainView(Context context) {
        super(context);
    }

    public EntrustGainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        PrintLogD("初始属性");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StockChartView);
        RedColor = typedArray.getColor(R.styleable.StockChartView_UpColor, ContextCompat.getColor(context, R.color.red_up));
        GreenColor = typedArray.getColor(R.styleable.StockChartView_DownColor, ContextCompat.getColor(context, R.color.green_down));
        GrayColor = typedArray.getColor(R.styleable.StockChartView_GrayColor, ContextCompat.getColor(context, R.color.gray_999));
        typedArray.recycle();
        InitPaint();
    }

    public EntrustGainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EntrustGainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void PrintLogD(String log){
        Log.d(getClass().getSimpleName(), log);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawGain(canvas);
    }

    private void InitPaint(){
        PrintLogD("初始画笔");
        //红色笔
        RedPaint = new Paint();
        RedPaint.setColor(RedColor);
        RedPaint.setStyle(Paint.Style.FILL);
        RedPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        RedPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        //绿色笔
        GreenPaint = new Paint();
        GreenPaint.setColor(GreenColor);
        GreenPaint.setStyle(Paint.Style.FILL);
        GreenPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        GreenPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
        //灰色笔
        GrayPaint = new Paint();
        GrayPaint.setColor(GrayColor);
        GrayPaint.setStyle(Paint.Style.FILL);
        GrayPaint.setStrokeWidth(DisplayTool.dp2px(getContext(), 1));
        GrayPaint.setTextSize(DisplayTool.sp2px(getContext(), 10));
    }

    private void DrawGain(Canvas canvas){
        float LeftOnX = 0;
        float TopOnY = 0;
        float RightOnX = LeftOnX+getMeasuredWidth();
        float BtmOnY = TopOnY+getMeasuredHeight();
        if (RedGain==0 && GreenGain==0){
            canvas.drawRect(LeftOnX, TopOnY, RightOnX, BtmOnY, GrayPaint);
        }else {
            float redWidth = (float) (getMeasuredWidth()*RedGain);
            float greenWidth = getMeasuredWidth()-redWidth;
            canvas.drawRect(LeftOnX, TopOnY, LeftOnX+redWidth, BtmOnY, RedPaint); //红色比例
            canvas.drawRect(RightOnX-greenWidth, TopOnY, RightOnX, BtmOnY, GreenPaint); //绿色比例
        }
    }

    public void SetGain(double red, double green){
        this.RedGain = red;
        this.GreenGain = green;
        invalidate();
    }

}
