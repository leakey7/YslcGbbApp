package com.gzyslczx.yslc.adapters.option;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResMyOptionObj;

public class MyOptionValueListAdapter extends BaseQuickAdapter<ResMyOptionObj, BaseViewHolder> {

    public MyOptionValueListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResMyOptionObj resMyOptionObj) {
        baseViewHolder.setText(R.id.MyOpBelongData, resMyOptionObj.getSsswhy_Sjhy());
        baseViewHolder.setText(R.id.MyOpNorthData, resMyOptionObj.getNothCgRatio());
        baseViewHolder.setText(R.id.MyOpFinanceData, resMyOptionObj.getRzRation());
        baseViewHolder.setText(R.id.MyOpNetProfitData, resMyOptionObj.getXsjll());
    }
}
