package com.gzyslczx.yslc.adapters.option;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResMyOptionObj;

import java.util.List;

public class ManageOptionListAdapter extends BaseQuickAdapter<ResMyOptionObj, BaseViewHolder> implements DraggableModule {


    public ManageOptionListAdapter(int layoutResId, @Nullable List<ResMyOptionObj> data) {
        super(layoutResId, data);
        addChildClickViewIds(R.id.ManageOpSetTopImg);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResMyOptionObj resMyOptionObj) {
        baseViewHolder.setText(R.id.ManageOpListNameData, resMyOptionObj.getStockName());
        baseViewHolder.setText(R.id.ManageOpListCode, resMyOptionObj.getStockCode());
        if (resMyOptionObj.isFinanc()){
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.finance)).fitCenter().into((ImageView) baseViewHolder.getView(R.id.ManageOpListNameImg));
        }else {
            baseViewHolder.setVisible(R.id.ManageOpListNameImg, false);
        }
        if (resMyOptionObj.isSelected()){
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.stock_checked)).fitCenter().into((ImageView) baseViewHolder.getView(R.id.ManageOpListCheck));
        }else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.stock_uncheck)).fitCenter().into((ImageView) baseViewHolder.getView(R.id.ManageOpListCheck));
        }
    }
}
