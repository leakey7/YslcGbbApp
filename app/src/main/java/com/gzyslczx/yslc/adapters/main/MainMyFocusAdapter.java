package com.gzyslczx.yslc.adapters.main;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.main.bean.MainMyFocusData;
import com.gzyslczx.yslc.tools.DisplayTool;

import java.util.List;

public class MainMyFocusAdapter extends BaseMultiItemQuickAdapter<MainMyFocusData, BaseViewHolder> {

    public MainMyFocusAdapter(@Nullable List<MainMyFocusData> data) {
        super(data);
        addItemType(MainMyFocusData.TeacherItem, R.layout.focus_teacher_item);
        addItemType(MainMyFocusData.LabelItem, R.layout.focus_label_item);
        addChildClickViewIds(R.id.FocusTState, R.id.FocusLState, R.id.FocusTHeadImg, R.id.FocusLHeadImg);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MainMyFocusData data) {
        if (data.getItemType()==MainMyFocusData.TeacherItem){
            Glide.with(getContext()).load(data.getData().getImg())
                    .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                    .into((ImageView) baseViewHolder.getView(R.id.FocusTHeadImg));
            baseViewHolder.setText(R.id.FocusTName, data.getData().getName());
            baseViewHolder.setText(R.id.FocusTDes, data.getData().getDesc());
            baseViewHolder.setText(R.id.FocusTCode, data.getData().getRemark());
            if (data.getData().isFocus()){
                baseViewHolder.setText(R.id.FocusTState, getContext().getString(R.string.IsFocus));
                baseViewHolder.setTextColor(R.id.FocusTState, ContextCompat.getColor(getContext(), R.color.gray_666));
                baseViewHolder.setBackgroundResource(R.id.FocusTState, R.drawable.gray_focus_radius_10_shape);
            }else {
                baseViewHolder.setText(R.id.FocusTState, getContext().getString(R.string.AddFocus));
                baseViewHolder.setTextColor(R.id.FocusTState, ContextCompat.getColor(getContext(), R.color.main_red));
                baseViewHolder.setBackgroundResource(R.id.FocusTState, R.drawable.red_border_focus_radius_10_shape);
            }
        }else {
            Glide.with(getContext()).load(data.getData().getImg())
                    .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                    .into((ImageView) baseViewHolder.getView(R.id.FocusLHeadImg));
            baseViewHolder.setText(R.id.FocusLName, data.getData().getName());
            baseViewHolder.setText(R.id.FocusLDes, data.getData().getDesc());
            if (data.getData().isFocus()){
                baseViewHolder.setText(R.id.FocusLState, getContext().getString(R.string.IsFocus));
                baseViewHolder.setTextColor(R.id.FocusLState, ContextCompat.getColor(getContext(), R.color.gray_666));
                baseViewHolder.setBackgroundResource(R.id.FocusLState, R.drawable.gray_focus_radius_10_shape);
            }else {
                baseViewHolder.setText(R.id.FocusLState, getContext().getString(R.string.AddFocus));
                baseViewHolder.setTextColor(R.id.FocusLState, ContextCompat.getColor(getContext(), R.color.main_red));
                baseViewHolder.setBackgroundResource(R.id.FocusLState, R.drawable.red_border_focus_radius_10_shape);
            }
        }
    }
}
