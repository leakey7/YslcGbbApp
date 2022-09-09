package com.gzyslczx.yslc.adapters.main;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.main.bean.MainTeacherLivingData;
import com.gzyslczx.yslc.tools.DisplayTool;

import java.util.List;

public class MainTeacherLivingAdapter extends BaseMultiItemQuickAdapter<MainTeacherLivingData, BaseViewHolder> {

    private int DefWidth;

    public MainTeacherLivingAdapter(@Nullable List<MainTeacherLivingData> data) {
        super(data);
        addItemType(MainTeacherLivingData.OnLiving, R.layout.main_teacher_living_onlive_item);
        addItemType(MainTeacherLivingData.OnSub, R.layout.main_teacher_living_sub_item);
        addItemType(MainTeacherLivingData.PlayBack, R.layout.main_teacher_living_palyback_item);
        addItemType(MainTeacherLivingData.VipLiving, R.layout.main_teacher_living_vip_item);
    }

    public void setDefWidth(int defWidth) {
        DefWidth = defWidth;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MainTeacherLivingData data) {
        switch (getItemViewType(getItemPosition(data))){
            case MainTeacherLivingData.OnLiving:
                ImageView OnLivingImg = baseViewHolder.getView(R.id.OnLivingImg);
                ConstraintLayout.LayoutParams OnLivingLayoutParams = (ConstraintLayout.LayoutParams) OnLivingImg.getLayoutParams();
                OnLivingLayoutParams.width = DefWidth;
                OnLivingImg.setLayoutParams(OnLivingLayoutParams);
                Glide.with(getContext()).load(data.getDataObj().getImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .skipMemoryCache(true)
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new FitCenter(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.OnLivingImg));
                Glide.with(getContext())
                        .asGif()
                        .load(R.drawable.living)
                        .error(ContextCompat.getDrawable(getContext(),R.drawable.live_img))
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into((ImageView) baseViewHolder.getView(R.id.OnLivingTagImg));
                break;
            case MainTeacherLivingData.OnSub:
                ImageView OnSubImg = baseViewHolder.getView(R.id.SubLiveImg);
                ConstraintLayout.LayoutParams OnSubLayoutParams = (ConstraintLayout.LayoutParams) OnSubImg.getLayoutParams();
                OnSubLayoutParams.width = DefWidth;
                OnSubImg.setLayoutParams(OnSubLayoutParams);
                Glide.with(getContext()).load(data.getDataObj().getImg()).centerCrop()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .skipMemoryCache(true)
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.SubLiveImg));
                baseViewHolder.setText(R.id.SubLiveTime, String.format("%s-%s", data.getDataObj().getStarttime(), data.getDataObj().getEndtime()));
                break;
            case MainTeacherLivingData.PlayBack:
                ImageView PlayBackImg = baseViewHolder.getView(R.id.PlayBackLiveImg);
                ConstraintLayout.LayoutParams PlayBackLayoutParams = (ConstraintLayout.LayoutParams) PlayBackImg.getLayoutParams();
                PlayBackLayoutParams.width = DefWidth;
                PlayBackImg.setLayoutParams(PlayBackLayoutParams);
                Glide.with(getContext()).load(data.getDataObj().getImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .skipMemoryCache(true)
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new FitCenter(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.PlayBackLiveImg));
                break;
            case MainTeacherLivingData.VipLiving:
                Glide.with(getContext()).load(data.getDataObj().getImg()).centerCrop()
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .skipMemoryCache(true)
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new FitCenter(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 6)))))
                        .into((ImageView) baseViewHolder.getView(R.id.VIPLiveImg));
                break;
            default:
                break;
        }
    }
}
