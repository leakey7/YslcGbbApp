package com.gzyslczx.yslc.adapters.fundtong;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResMyFundOptionObj;

public class FundOptionLeftListAdapter extends BaseQuickAdapter<ResMyFundOptionObj, BaseViewHolder> {

    public FundOptionLeftListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResMyFundOptionObj data) {
        baseViewHolder.setText(R.id.FundOptionLeftDataName, data.getName());
        baseViewHolder.setText(R.id.FundOptionLeftDataCode, data.getFCode());
    }
}
