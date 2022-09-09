package com.gzyslczx.yslc.adapters.fundtong;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResFundFindRankInfo;


public class FundFindRightListAdapter extends BaseQuickAdapter<ResFundFindRankInfo, BaseViewHolder> implements LoadMoreModule {

    public FundFindRightListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResFundFindRankInfo data) {
        baseViewHolder.setText(R.id.FundRightEva, data.getFinalScore());
        baseViewHolder.setText(R.id.FundRightNetWorth, data.getUnitNet());
        if (data.getMonthRate().substring(0, 1).equals("-")){
            baseViewHolder.setTextColor(R.id.FundRightMonthGains, ContextCompat.getColor(getContext(), R.color.green_down));
        }else {
            baseViewHolder.setTextColor(R.id.FundRightMonthGains, ContextCompat.getColor(getContext(), R.color.red_up));
        }
        baseViewHolder.setText(R.id.FundRightMonthGains, data.getMonthRate());

        if (data.getMonthRate3().substring(0, 1).equals("-")){
            baseViewHolder.setTextColor(R.id.FundRightQuarterGains, ContextCompat.getColor(getContext(), R.color.green_down));
        }else {
            baseViewHolder.setTextColor(R.id.FundRightQuarterGains, ContextCompat.getColor(getContext(), R.color.red_up));
        }
        baseViewHolder.setText(R.id.FundRightQuarterGains, data.getMonthRate3());

        if (data.getMonthRate6().substring(0, 1).equals("-")){
            baseViewHolder.setTextColor(R.id.FundRightHalfYearGains, ContextCompat.getColor(getContext(), R.color.green_down));
        }else {
            baseViewHolder.setTextColor(R.id.FundRightHalfYearGains, ContextCompat.getColor(getContext(), R.color.red_up));
        }
        baseViewHolder.setText(R.id.FundRightHalfYearGains, data.getMonthRate6());

        if (data.getYearRate().substring(0, 1).equals("-")){
            baseViewHolder.setTextColor(R.id.FundRightYearGains, ContextCompat.getColor(getContext(), R.color.green_down));
        }else {
            baseViewHolder.setTextColor(R.id.FundRightYearGains, ContextCompat.getColor(getContext(), R.color.red_up));
        }
        baseViewHolder.setText(R.id.FundRightYearGains, data.getYearRate());

    }
}
