package com.gzyslczx.stockmarketlibrary;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ILine {

    private List<IPoint<?>> line;
    private float MaxValue=Float.MIN_VALUE, MinValue=Float.MAX_VALUE;
    private int color;
    private String tag;
    private Paint paint;

    public ILine(@NonNull String tag, @ColorInt int lineColor) {
        this.line = new ArrayList<>();
        this.tag = tag;
        this.color = lineColor;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2f);
        paint.setColor(this.color);
    }

    /*
    * 重置线段
    * */
    public void SetLine(List<IPoint<?>> iPointList){
        this.line.clear();
        this.line.addAll(iPointList);
        CountMaxAndMinValue(0, this.line.size());
    }

    /*
    * 加点
    * */
    public void AddPoint(IPoint<?> iPoint){
        this.line.add(iPoint);
        CountMaxAndMinValue(this.line.size()-1, this.line.size());
    }

    /*
    * 加线段
    * */
    public void AddLine(List<IPoint<?>> iPointList){
        int start = this.line.size();
        this.line.addAll(iPointList);
        CountMaxAndMinValue(start, this.line.size());
    }

    /*
    * 获取点
    * */
    public IPoint GetPoint(int i){
        if (i<0){
            return null;
        }
        return line.size()>i ? line.get(i) : null;
    }

    /*
    * 计算最大值和最小值
    * */
    public boolean CountMaxAndMinValue(int start, int end){
        if (start>=0 && start<=end && end<=this.line.size()) {
            for (int i = start; i < end; i++) {
                MaxValue = Math.max(MaxValue, line.get(i).getYValue());
                MinValue = Math.min(MinValue, line.get(i).getYValue());
            }
            return true;
        }
        return false;
    }

    public List<IPoint<?>> getLine() {
        return line;
    }

    public float getMaxValue() {
        return MaxValue;
    }

    public float getMinValue() {
        return MinValue;
    }

    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public void setDotted(boolean dotted) {
        if (dotted){
            paint.setPathEffect(new DashPathEffect(new float[]{6, 6}, 0));
        }
    }

    private void SetLinePaint(Paint.Style style, float width){
        paint.setStyle(style);
        paint.setStrokeWidth(width);
    }

    public Paint getPaint() {
        return paint;
    }
}
