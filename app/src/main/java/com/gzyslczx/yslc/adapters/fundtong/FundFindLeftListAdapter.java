package com.gzyslczx.yslc.adapters.fundtong;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResFundFindRankInfo;


public class FundFindLeftListAdapter extends BaseQuickAdapter<ResFundFindRankInfo, BaseViewHolder> implements LoadMoreModule {

    public FundFindLeftListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResFundFindRankInfo data) {
        baseViewHolder.setText(R.id.FundLeftName, data.getName());
        baseViewHolder.setText(R.id.FundLeftCode, data.getFCode());
        if (TextUtils.isEmpty(data.getDayRate())){
            baseViewHolder.setText(R.id.FundLeftHeavy, data.getRate());
        }else {
            baseViewHolder.setText(R.id.FundLeftHeavy, data.getDayRate());
        }
    }
}
