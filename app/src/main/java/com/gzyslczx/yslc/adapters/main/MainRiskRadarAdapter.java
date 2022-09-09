package com.gzyslczx.yslc.adapters.main;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResMainRiskRadarObj;

public class MainRiskRadarAdapter extends BaseQuickAdapter<ResMainRiskRadarObj, BaseViewHolder> {

    public MainRiskRadarAdapter(int layoutResId) {
        super(layoutResId);
        addChildClickViewIds(R.id.RiskRadarStockName, R.id.RiskRadarLook);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResMainRiskRadarObj obj) {
        baseViewHolder.setText(R.id.RiskRadarStockName, obj.getStockName());
        baseViewHolder.setText(R.id.RiskRadarRiskName, obj.getRiskName());
        baseViewHolder.setText(R.id.RiskRadarRiskDes, obj.getRiskDesc());
    }
}
