package com.gzyslczx.yslc.adapters.fundtong;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResMyFundOptionObj;

public class FundOptionRightAdapter extends BaseQuickAdapter<ResMyFundOptionObj, BaseViewHolder> {
    public FundOptionRightAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResMyFundOptionObj data) {
        if (isUp(data.getProfitRate())){
            baseViewHolder.setTextColor(R.id.FundTongDayValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongDayValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongDayValue, data.getProfitRate());

        if (isUp(data.getWeekRate())){
            baseViewHolder.setTextColor(R.id.FundTongWeekValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongWeekValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongWeekValue, data.getWeekRate());

        if (isUp(data.getMonthRate())){
            baseViewHolder.setTextColor(R.id.FundTongMonthValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongMonthValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongMonthValue, data.getMonthRate());

        if (isUp(data.getMonthRate3())){
            baseViewHolder.setTextColor(R.id.FundTongQuarterValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongQuarterValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongQuarterValue, data.getMonthRate3());

        if (isUp(data.getMonthRate6())){
            baseViewHolder.setTextColor(R.id.FundTongHalfYearValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongHalfYearValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongHalfYearValue, data.getMonthRate6());

        if (isUp(data.getYearRate())){
            baseViewHolder.setTextColor(R.id.FundTongYearValue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.FundTongYearValue, ContextCompat.getColor(getContext(), R.color.green_down));
        }
        baseViewHolder.setText(R.id.FundTongYearValue, data.getYearRate());

        baseViewHolder.setGone(R.id.FundTongDataValue, true);
    }

    private boolean isUp(String value){
        if (value.substring(0, 1).equals("-")){
            return false;
        }
        return true;
    }

}
