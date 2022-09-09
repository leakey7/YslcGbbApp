package com.gzyslczx.yslc.adapters.fund;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.fund.bean.FundDetailGainData;

public class FundDetailGainListAdapter extends BaseQuickAdapter<FundDetailGainData, BaseViewHolder> {

    public FundDetailGainListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FundDetailGainData data) {
        baseViewHolder.setText(R.id.datesTag, data.getT());
        baseViewHolder.setText(R.id.datesValue, data.getV());
        if (data.isUp()){
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(),
                    R.drawable.red_up))
                    .centerCrop()
                    .into((ImageView) baseViewHolder.getView(R.id.datesImg));
            baseViewHolder.setTextColor(R.id.datesValue, ContextCompat.getColor(getContext(),
                    R.color.red_up));
        }else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(),
                    R.drawable.green_down))
                    .centerCrop()
                    .into((ImageView) baseViewHolder.getView(R.id.datesImg));
            baseViewHolder.setTextColor(R.id.datesValue, ContextCompat.getColor(getContext(),
                    R.color.green_down));
        }
    }
}
