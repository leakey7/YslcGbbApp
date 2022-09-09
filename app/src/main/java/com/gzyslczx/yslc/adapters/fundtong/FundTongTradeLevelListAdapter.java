package com.gzyslczx.yslc.adapters.fundtong;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.fundtong.bean.FundTongTradLevelData;


public class FundTongTradeLevelListAdapter extends BaseQuickAdapter<FundTongTradLevelData, BaseViewHolder> {

    public FundTongTradeLevelListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FundTongTradLevelData levelData) {
        if (levelData.isSelect()){
            baseViewHolder.setVisible(R.id.TradeLevelFundImg, true);
            baseViewHolder.setTextColor(R.id.TradeLevelFundName, ContextCompat.getColor(getContext(), R.color.orange_FF753E));
        }else {
            baseViewHolder.setGone(R.id.TradeLevelFundImg, true);
            baseViewHolder.setTextColor(R.id.TradeLevelFundName, ContextCompat.getColor(getContext(), R.color.black_333));
        }
        baseViewHolder.setText(R.id.TradeLevelFundName, levelData.getData().getName());
    }
}
