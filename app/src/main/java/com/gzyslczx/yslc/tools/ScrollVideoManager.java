package com.gzyslczx.yslc.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.gzyslczx.yslc.databinding.ScrollSmallVideItemBinding;

public class ScrollVideoManager extends LinearLayoutManager {

    private PagerSnapHelper mPagerSnapHelper;
    private OnScrollVideoListener listener;
    private RecyclerView mRecyclerView;
    private int mDrift;//位移，用来判断移动方向

    public ScrollVideoManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    public ScrollVideoManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPagerSnapHelper = new PagerSnapHelper();
    }

    /*
     * 一次滑动一页
     * */
    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        this.mRecyclerView = view;
        mRecyclerView.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    /*
     * 滑动状态的改变
     * 缓慢拖拽-> SCROLL_STATE_DRAGGING
     * 快速滚动-> SCROLL_STATE_SETTLING
     * 空闲状态-> SCROLL_STATE_IDLE
     * */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                View viewIdle = mPagerSnapHelper.findSnapView(this);
                if (viewIdle != null) {
                    int positionIdle = getPosition(viewIdle);
                    if (listener != null && getChildCount() == 1) {
                        listener.onPageSelect(positionIdle, positionIdle == getItemCount() - 1, ScrollSmallVideItemBinding.bind(viewIdle));
                    }
                }
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                View viewDrag = mPagerSnapHelper.findSnapView(this);
                if (viewDrag != null) {
                    int positionDrag = getPosition(viewDrag);
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                View viewSettling = mPagerSnapHelper.findSnapView(this);
                if (viewSettling != null) {
                    int positionSettling = getPosition(viewSettling);
                }
                break;
        }
    }

    /*
     * 竖直方向滑动
     * */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    /*
     * 水平方向滑动
     * */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {

        /*
         * Item依附Widow
         * */
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (listener != null && getChildCount() == 1) {
                listener.onInitComplete(ScrollSmallVideItemBinding.bind(view));
            }
        }

        /*
         * Item脱离Window
         * */
        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {
            if (mDrift >= 0) {
                if (listener != null)
                    try {
                        listener.onPageRelease(true, getPosition(view), ScrollSmallVideItemBinding.bind(view));
                    }catch (Exception e){
                        Log.d("SVideoManagerNext", e.getMessage());
                    }

            } else {
                if (listener != null)
                    try {
                        listener.onPageRelease(false, getPosition(view), ScrollSmallVideItemBinding.bind(view));
                    }catch (Exception e){
                        Log.d("SVideoManagerLast", e.getMessage());
                    }
            }
        }
    };

    public void setListener(OnScrollVideoListener listener) {
        this.listener = listener;
    }
}
