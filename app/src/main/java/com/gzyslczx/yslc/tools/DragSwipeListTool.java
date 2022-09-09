package com.gzyslczx.yslc.tools;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DragSwipeListTool extends ItemTouchHelper.Callback {

    private OnItemTouchCallbackListener onItemTouchCallbackListener;

    private boolean canDrag = true;
    private boolean canSwipe = true;
    private int endPosition;

    public DragSwipeListTool(OnItemTouchCallbackListener onItemTouchCallbackListener) {
        this.onItemTouchCallbackListener = onItemTouchCallbackListener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int dragFlags = 0;
        int swipeFlags = 0;
        if (layoutManager instanceof GridLayoutManager) {
            // 如果是Grid布局，则不能滑动，只能上下左右拖动
            dragFlags =
                    ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        } else if (layoutManager instanceof LinearLayoutManager) {
            // 如果是纵向Linear布局，则能上下拖动，左右滑动
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
            } else {
                // 如果是横向Linear布局，则能左右拖动，上下滑动
                swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
        }
        //法指定可进行的操作
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        endPosition = target.getAdapterPosition();
        return onItemTouchCallbackListener.onMove(recyclerView, viewHolder, target);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        //滑动
        onItemTouchCallbackListener.onSwiped(viewHolder, direction);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ACTION_STATE_IDLE){
            onItemTouchCallbackListener.onFinishMove(endPosition);
        }
    }

    /*
    * 是否可滑动
    * */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return canSwipe;
    }

    /*
     * 是否可拖动
     * */
    @Override
    public boolean isLongPressDragEnabled() {
        return canDrag;
    }

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    public void setCanSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }
}
