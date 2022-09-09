package com.gzyslczx.yslc.tools.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.tools.DateTool;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

public class MyMonthCalendar extends MonthView {

    private Paint mSelectCirclePaint, mSchemeCirclePaint;
    private float mRadius;

    public MyMonthCalendar(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        super.onPreviewHook();
        mRadius = Math.min(mItemHeight, mItemWidth)/4f;
        mSelectCirclePaint = new Paint();
        mSelectCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSelectCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.main_red));
        mSchemeCirclePaint = new Paint();
        mSchemeCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSchemeCirclePaint.setColor(ContextCompat.getColor(getContext(), R.color.pink_FFECE8));
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        if (!calendar.isWeekend()) {
            float cX = x + (mItemWidth / 2f);
            float cY = y + (mItemHeight / 2f);
            canvas.drawCircle(cX, cY, mRadius, mSelectCirclePaint);
        }
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        if (!calendar.isWeekend()) {
            float cX = x + (mItemWidth / 2f);
            float cY = y + (mItemHeight / 2f);
            canvas.drawCircle(cX, cY, mRadius, mSchemeCirclePaint);
        }
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baseLineY = mTextBaseLine+y;
        int cX = x+mItemWidth/2;
        //判断当前日期是否在本月中
        boolean isInRange = isInRange(calendar);
        //判断当前日期是否可用
        boolean isEnable = !onCalendarIntercept(calendar);
        //如果该日期被选中，添加选中样式
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cX, baseLineY, mSelectTextPaint);
        }
        //如果没被选中但是是标注日期
        else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cX, baseLineY, mSchemeTextPaint);
        }
        //其他
        else {
            canvas.drawText(String.valueOf(calendar.getDay()), cX, baseLineY, calendar.isCurrentDay() ? mCurDayTextPaint :
                    calendar.isCurrentMonth() && isInRange && isEnable ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
