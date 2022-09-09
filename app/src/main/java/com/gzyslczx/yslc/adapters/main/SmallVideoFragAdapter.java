package com.gzyslczx.yslc.adapters.main;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.main.bean.MainRecoData;
import com.gzyslczx.yslc.modes.response.ResMainRecoInfo;
import com.gzyslczx.yslc.tools.DisplayTool;

public class SmallVideoFragAdapter extends BaseQuickAdapter<MainRecoData, BaseViewHolder> implements LoadMoreModule {

    public SmallVideoFragAdapter(int layoutResId) {
        super(layoutResId);
        addChildClickViewIds(R.id.SVideoPraiseImg, R.id.SVideoPraiseNum, R.id.SVideoHeadImg);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MainRecoData data) {
        Glide.with(getContext()).load(data.getInfo().getAuthorImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.head_img))
                .apply(new RequestOptions().transform(new MultiTransformation<>(new FitCenter(), new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                .into((ImageView) baseViewHolder.getView(R.id.SVideoHeadImg));
        Glide.with(getContext()).load(data.getInfo().getImg()).placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(getContext(), 8)))))
                .into((ImageView) baseViewHolder.getView(R.id.SVideoImg));
        baseViewHolder.setText(R.id.SVideoName, data.getInfo().getAuthor());
        baseViewHolder.setText(R.id.SVideoTitle, data.getInfo().getTitle());
        if (data.getInfo().isLike()){
            baseViewHolder.setTextColor(R.id.SVideoPraiseNum, ContextCompat.getColor(getContext(), R.color.main_red));
            baseViewHolder.setText(R.id.SVideoPraiseNum, String.valueOf(data.getInfo().getPraise()));
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(),R.drawable.is_praise))
                    .fitCenter().into((ImageView) baseViewHolder.getView(R.id.SVideoPraiseImg));
        }else {
            baseViewHolder.setTextColor(R.id.SVideoPraiseNum, ContextCompat.getColor(getContext(), R.color.gray_999));
            baseViewHolder.setText(R.id.SVideoPraiseNum, getContext().getString(R.string.Praise));
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(),R.drawable.un_praise))
                    .fitCenter().into((ImageView) baseViewHolder.getView(R.id.SVideoPraiseImg));
        }
    }
}
