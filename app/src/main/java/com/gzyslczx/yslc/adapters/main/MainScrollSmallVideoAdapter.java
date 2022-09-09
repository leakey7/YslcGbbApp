package com.gzyslczx.yslc.adapters.main;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResScrollSmallVideoInfo;
import com.gzyslczx.yslc.tools.DisplayTool;

public class MainScrollSmallVideoAdapter extends BaseQuickAdapter<ResScrollSmallVideoInfo, BaseViewHolder> implements LoadMoreModule {
    public MainScrollSmallVideoAdapter(int layoutResId) {
        super(layoutResId);
        addChildClickViewIds(R.id.SmallVideoFocus, R.id.SmallVideoPraise, R.id.SmallVideoShare, R.id.SmallVideoHeadImg);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResScrollSmallVideoInfo data) {
        Glide.with(getContext()).load(data.getAuthorImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                .apply(new RequestOptions().transform(new MultiTransformation<>(new FitCenter(),
                        new RoundedCorners(DisplayTool.dp2px(getContext(), 36)))))
                .into((ImageView) baseViewHolder.getView(R.id.SmallVideoHeadImg));
        baseViewHolder.setText(R.id.SmallVideoName, data.getAuthor());
        if (TextUtils.isEmpty(data.getDescribe())){
            baseViewHolder.setGone(R.id.SmallVideoDes, true);
        }else {
            baseViewHolder.setVisible(R.id.SmallVideoDes, true);
            baseViewHolder.setText(R.id.SmallVideoDes, data.getDescribe());
        }
        if (data.isFocus()){
            baseViewHolder.setBackgroundResource(R.id.SmallVideoFocus, R.drawable.gray_focus_radius_10_shape);
            baseViewHolder.setTextColor(R.id.SmallVideoFocus, ContextCompat.getColor(getContext(), R.color.gray_666));
            baseViewHolder.setText(R.id.SmallVideoFocus, getContext().getString(R.string.IsFocus));
        }else {
            baseViewHolder.setBackgroundResource(R.id.SmallVideoFocus, R.drawable.red_corner_10_shape);
            baseViewHolder.setTextColor(R.id.SmallVideoFocus, ContextCompat.getColor(getContext(), R.color.white));
            baseViewHolder.setText(R.id.SmallVideoFocus, getContext().getString(R.string.AddFocus));
        }
        if (data.isLike()){
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise)).fitCenter().into((ImageView) baseViewHolder.getView(R.id.SmallVideoPraise));
            baseViewHolder.setTextColor(R.id.SmallVideoPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
            baseViewHolder.setText(R.id.SmallVideoPraiseNum, String.valueOf(data.getPraise()));
        }else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise)).fitCenter().into((ImageView) baseViewHolder.getView(R.id.SmallVideoPraise));
            baseViewHolder.setTextColor(R.id.SmallVideoPraiseNum, ContextCompat.getColor(getContext(), R.color.white));
            baseViewHolder.setText(R.id.SmallVideoPraiseNum, getContext().getString(R.string.Praise));
        }
    }
}
