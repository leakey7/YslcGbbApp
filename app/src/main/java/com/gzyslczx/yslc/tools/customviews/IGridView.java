package com.gzyslczx.yslc.tools.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class IGridView extends GridView {
    public IGridView(Context context) {
        super(context);
    }

    public IGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
