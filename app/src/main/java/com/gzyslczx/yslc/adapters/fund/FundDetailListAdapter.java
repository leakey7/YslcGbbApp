package com.gzyslczx.yslc.adapters.fund;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResFundDetailList;

public class FundDetailListAdapter extends BaseQuickAdapter<ResFundDetailList, BaseViewHolder> {
    public FundDetailListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResFundDetailList data) {
        baseViewHolder.setText(R.id.sName, data.getStockname());
        baseViewHolder.setText(R.id.sCode, data.getStockcode());
        baseViewHolder.setText(R.id.nRate, data.getHoldRate());
    }
}
