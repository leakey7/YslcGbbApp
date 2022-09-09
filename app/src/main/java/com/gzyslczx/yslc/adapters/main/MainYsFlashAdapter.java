package com.gzyslczx.yslc.adapters.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.main.bean.FlashData;

import java.util.List;

public class MainYsFlashAdapter extends BaseMultiItemQuickAdapter<FlashData, BaseViewHolder> implements LoadMoreModule {

    public MainYsFlashAdapter(@Nullable List<FlashData> data) {
        super(data);
        addItemType(FlashData.DateType, R.layout.flash_item_date_layout);
        addItemType(FlashData.InfoType, R.layout.flash_item_info_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FlashData data) {
        if (data.getItemType() == FlashData.DateType){
            baseViewHolder.setText(R.id.FlashItemDate, data.getData().getDate());
        }else {
            baseViewHolder.setText(R.id.FlashItemTime, data.getData().getInputtime());
            baseViewHolder.setText(R.id.FlashItemTitle, data.getData().getTitle());
            baseViewHolder.setText(R.id.FlashItemDes, data.getData().getContent());
        }
    }
}
