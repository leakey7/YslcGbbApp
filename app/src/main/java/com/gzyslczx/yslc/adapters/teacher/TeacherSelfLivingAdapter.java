package com.gzyslczx.yslc.adapters.teacher;

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
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.teacher.bean.TeacherSelfLivingData;
import com.gzyslczx.yslc.tools.DisplayTool;

import java.util.List;

public class TeacherSelfLivingAdapter extends BaseMultiItemQuickAdapter<TeacherSelfLivingData, BaseViewHolder> implements LoadMoreModule {

    public TeacherSelfLivingAdapter(@Nullable List<TeacherSelfLivingData> data) {
        super(data);
        addItemType(TeacherSelfLivingData.OnLiving, R.layout.teacher_self_living_on_item);
        addItemType(TeacherSelfLivingData.OnSub, R.layout.teacher_self_living_sub_item);
        addItemType(TeacherSelfLivingData.LivingEnd, R.layout.teacher_self_living_sub_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, TeacherSelfLivingData data) {
        switch (data.getItemType()){
            case TeacherSelfLivingData.OnLiving:
                Glide.with(getContext()).load(data.getTHeadImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.TSelfLVHeadImg));
                Glide.with(getContext()).load(data.getLivingData().getImg()).centerCrop()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.TSelfLVImg));
                baseViewHolder.setText(R.id.TSelfLVName, data.getTName());
                baseViewHolder.setText(R.id.TSelfLVTime, data.getLivingData().getDateInfo());
                baseViewHolder.setText(R.id.TSelfLVTitle, data.getLivingData().getTitle());
                baseViewHolder.setText(R.id.TSelfLVNumber, String.valueOf(data.getLivingData().getReadNumber()));
                break;
            case TeacherSelfLivingData.OnSub:
                Glide.with(getContext()).load(data.getTHeadImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.TSelfSubHeadImg));
                Glide.with(getContext()).load(data.getLivingData().getImg()).centerCrop()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.TSelfSubImg));
                baseViewHolder.setText(R.id.TSelfSubName, data.getTName());
                baseViewHolder.setText(R.id.TSelfSubTime, data.getLivingData().getDateInfo());
                baseViewHolder.setText(R.id.TSelfSubTitle, data.getLivingData().getTitle());
                break;
            case TeacherSelfLivingData.LivingEnd:
                baseViewHolder.setGone(R.id.TSelfSubTag, true);
                baseViewHolder.setGone(R.id.TSelfSubTagImg, true);
                Glide.with(getContext()).load(data.getTHeadImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                        .into((ImageView) baseViewHolder.getView(R.id.TSelfSubHeadImg));
                Glide.with(getContext()).load(data.getLivingData().getImg()).centerCrop()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.TSelfSubImg));
                baseViewHolder.setText(R.id.TSelfSubName, data.getTName());
                baseViewHolder.setText(R.id.TSelfSubTime, data.getLivingData().getDateInfo());
                baseViewHolder.setText(R.id.TSelfSubTitle, data.getLivingData().getTitle());
                break;
            default:
                break;
        }
    }
}
