package com.gzyslczx.yslc.adapters.fundtong;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResFundTongRankInfo;

import java.util.List;

public class FundTongRankRightAdapter extends BaseQuickAdapter<ResFundTongRankInfo, BaseViewHolder> implements LoadMoreModule {


    public FundTongRankRightAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResFundTongRankInfo resFundTongRankInfo) {
        if (isUp(resFundTongRankInfo.getProfitRate())){
            baseViewHolder.setTextColor(R.id.FundTongDayValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongDayValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongDayValue, resFundTongRankInfo.getProfitRate());

        if (isUp(resFundTongRankInfo.getWeekRate())){
            baseViewHolder.setTextColor(R.id.FundTongWeekValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongWeekValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongWeekValue, resFundTongRankInfo.getWeekRate());

        if (isUp(resFundTongRankInfo.getMonthRate())){
            baseViewHolder.setTextColor(R.id.FundTongMonthValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongMonthValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongMonthValue, resFundTongRankInfo.getMonthRate());

        if (isUp(resFundTongRankInfo.getMonthRate3())){
            baseViewHolder.setTextColor(R.id.FundTongQuarterValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongQuarterValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongQuarterValue, resFundTongRankInfo.getMonthRate3());

        if (isUp(resFundTongRankInfo.getMonthRate6())){
            baseViewHolder.setTextColor(R.id.FundTongHalfYearValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongHalfYearValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongHalfYearValue, resFundTongRankInfo.getMonthRate6());

        if (isUp(resFundTongRankInfo.getYearRate())){
            baseViewHolder.setTextColor(R.id.FundTongYearValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongYearValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongYearValue, resFundTongRankInfo.getYearRate());

        if (isUp(resFundTongRankInfo.getNetValue())){
            baseViewHolder.setTextColor(R.id.FundTongDataValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongDataValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongDataValue, String.format("%s亿元",resFundTongRankInfo.getNetValue()));

//        baseViewHolder.setText(R.id.FundTongDateValue, resFundTongRankInfo.getDate());
    }


    private boolean isUp(String value){
        if (value.substring(0, 1).equals("-")){
            return false;
        }
        return true;
    }

}
