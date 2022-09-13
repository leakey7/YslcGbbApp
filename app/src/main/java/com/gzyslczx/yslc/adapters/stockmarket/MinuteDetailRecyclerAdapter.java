package com.gzyslczx.yslc.adapters.stockmarket;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.StockMarketActivity;
import com.yourui.sdk.message.listener.CommonTick;

import java.util.List;

public class MinuteDetailRecyclerAdapter extends BaseQuickAdapter<CommonTick, BaseViewHolder> {

    public MinuteDetailRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    public MinuteDetailRecyclerAdapter(int layoutResId, @Nullable List<CommonTick> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, CommonTick tick) {
        baseViewHolder.setText(R.id.MdetailTime, tick.getTime());
        if (tick.getPrice()> StockMarketActivity.PrePrice){
            baseViewHolder.setTextColor(R.id.MdetailPrice, ContextCompat.getColor(getContext(), R.color.red_up));
        }else if (tick.getPrice()<StockMarketActivity.PrePrice){
            baseViewHolder.setTextColor(R.id.MdetailPrice, ContextCompat.getColor(getContext(), R.color.green_down));
        }else {
            baseViewHolder.setTextColor(R.id.MdetailPrice, ContextCompat.getColor(getContext(), R.color.gray_666));
        }
        if (tick.getBuyOrSell()==0){
            baseViewHolder.setTextColor(R.id.MdetailVolue, ContextCompat.getColor(getContext(), R.color.green_down));
        }else if (tick.getBuyOrSell()==1){
            baseViewHolder.setTextColor(R.id.MdetailVolue, ContextCompat.getColor(getContext(), R.color.red_up));
        }else {
            baseViewHolder.setTextColor(R.id.MdetailVolue, ContextCompat.getColor(getContext(), R.color.gray_666));
        }
        baseViewHolder.setText(R.id.MdetailPrice, String.valueOf(tick.getPrice()));
        baseViewHolder.setText(R.id.MdetailVolue, String.valueOf((int)(tick.getVolume()/100)));
    }
}
