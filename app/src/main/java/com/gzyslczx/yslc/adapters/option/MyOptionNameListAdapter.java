package com.gzyslczx.yslc.adapters.option;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResMyOptionObj;

public class MyOptionNameListAdapter extends BaseQuickAdapter<ResMyOptionObj, BaseViewHolder> implements DraggableModule {

    public MyOptionNameListAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResMyOptionObj resMyOptionObj) {
        baseViewHolder.setText(R.id.MyOpNameData, resMyOptionObj.getStockName());
        baseViewHolder.setText(R.id.MyOpCode, resMyOptionObj.getStockCode());
        if (resMyOptionObj.isFinanc()) {
            baseViewHolder.setVisible(R.id.MyOpNameImg, true);
        } else {
            baseViewHolder.setGone(R.id.MyOpNameImg, true);
        }
    }



}
