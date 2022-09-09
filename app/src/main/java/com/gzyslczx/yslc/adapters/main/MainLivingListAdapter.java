package com.gzyslczx.yslc.adapters.main;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.main.bean.MainTeacherLivingData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainLivingListAdapter extends BaseMultiItemQuickAdapter<MainTeacherLivingData, BaseViewHolder> {

    public MainLivingListAdapter(@Nullable List<MainTeacherLivingData> data) {
        super(data);
        addItemType(MainTeacherLivingData.OnLiving, R.layout.main_living_onlive_item);
        addItemType(MainTeacherLivingData.OnSub, R.layout.main_living_sub_item);
        addItemType(MainTeacherLivingData.PlayBack, R.layout.main_living_playback_item);
        addItemType(MainTeacherLivingData.VipLiving, R.layout.main_living_vip_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MainTeacherLivingData data) {
        switch (data.getItemType()){
            case MainTeacherLivingData.OnLiving:
                Glide.with(getContext()).load(data.getDataObj().getImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .centerCrop().into((ImageView) baseViewHolder.getView(R.id.OnLiveImg));
                Glide.with(getContext())
                        .asGif()
                        .load(R.drawable.living)
                        .error(ContextCompat.getDrawable(getContext(),R.drawable.live_img))
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into((ImageView) baseViewHolder.getView(R.id.OnLiveTagImg));
                break;
            case MainTeacherLivingData.OnSub:
                Glide.with(getContext()).load(data.getDataObj().getImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .centerCrop().into((ImageView) baseViewHolder.getView(R.id.OnLiveSubImg));
                baseViewHolder.setText(R.id.OnLiveSubTime, String.format("%s-%s", data.getDataObj().getStarttime(), data.getDataObj().getEndtime()));
                break;
            case MainTeacherLivingData.PlayBack:
                Glide.with(getContext()).load(data.getDataObj().getImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .centerCrop().into((ImageView) baseViewHolder.getView(R.id.OnLivePlayBackImg));
                break;
            case MainTeacherLivingData.VipLiving:
                Glide.with(getContext()).load(data.getDataObj().getImg())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .centerCrop().into((ImageView) baseViewHolder.getView(R.id.OnLiveVIPImg));
                break;
            default:
                break;
        }
    }
}
