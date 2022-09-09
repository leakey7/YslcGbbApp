package com.gzyslczx.yslc.tools.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.gzyslczx.yslc.tools.interfaces.OnScrollListenerForHorizontal;

public class MyHorScroll extends HorizontalScrollView {

    private HorizontalScrollView subScroll;
    private OnScrollListenerForHorizontal listener;

    public MyHorScroll(Context context) {
        super(context);
    }

    public MyHorScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSubScroll(HorizontalScrollView subScroll) {
        this.subScroll = subScroll;
    }

    public MyHorScroll(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (subScroll!=null){
            subScroll.scrollTo(l ,t);
        }
        if (listener!=null) {
            listener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setListener(OnScrollListenerForHorizontal listener) {
        this.listener = listener;
    }
}
