package com.gzyslczx.stockmarketlibrary;

import android.graphics.Canvas;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;

public abstract class LineAdapter {

    private HashMap<String, ILine> lineMap;
    private float MAX=Float.MIN_VALUE, MIN=Float.MAX_VALUE;

    public LineAdapter() {
        lineMap = new HashMap<>();
    }

    public int getLineItemSize(){
        return lineMap.size();
    }

    public void addLine(ILine iLine){
        lineMap.put(iLine.getTag(), iLine);
        MAX = Math.max(MAX, iLine.getMaxValue());
        MIN = Math.min(MIN, iLine.getMinValue());
    }

    public void DrawLine(Canvas canvas, HashSet<String> lineTag, float AveHeight, float AveWidth, float HalfOfAveWidth, String TAG){
        /*
         * 绘制线段
         * */
        for (String tag : lineTag) { //遍历线段条目
            //获取线段数据
            if (lineMap.get(tag) != null && lineMap.size()>0) {
                for (int i = 0; i < lineMap.get(tag).getLine().size(); i++) {
                    lineMap.get(tag).getLine().get(i).setXValue(i); //设置X点值
                    lineMap.get(tag).getLine().get(i).setXPoint(HalfOfAveWidth + AveWidth * i); //设置X点坐标位置
                    lineMap.get(tag).getLine().get(i).setYPoint((MAX - lineMap.get(tag).getLine().get(i).getYValue()) / AveHeight); //设置Y点坐标位置
                    if (i == 0) {
                        //线段的端点
                        canvas.drawCircle(lineMap.get(tag).getLine().get(i).getXPoint(), lineMap.get(tag).getLine().get(i).getYPoint(), 1f,
                                lineMap.get(tag).getPaint());
                    } else {
                        //上一点和下一点连成小线段
                        canvas.drawLine(lineMap.get(tag).getLine().get(i - 1).getXPoint(), lineMap.get(tag).getLine().get(i - 1).getYPoint(),
                                lineMap.get(tag).getLine().get(i).getXPoint(), lineMap.get(tag).getLine().get(i).getYPoint(), lineMap.get(tag).getPaint());
                    }
                }
            } else {
                Log.e(TAG, String.format("不存在线段条目:%s", tag));
            }
        }
    }

    public ILine getILine(String tag){
        return lineMap.get(tag);
    }

    public float getMAX() {
        return MAX;
    }

    public float getMIN() {
        return MIN;
    }
}
