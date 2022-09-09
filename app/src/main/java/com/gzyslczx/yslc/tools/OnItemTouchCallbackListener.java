package com.gzyslczx.yslc.tools;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface OnItemTouchCallbackListener {
    boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target);
    void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction);
    void onFinishMove(int EndPos);
}
