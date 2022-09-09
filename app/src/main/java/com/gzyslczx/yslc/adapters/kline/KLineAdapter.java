package com.gzyslczx.yslc.adapters.kline;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.kline.bean.KLineData;
import com.gzyslczx.yslc.tools.DisplayTool;

import java.util.List;

public class KLineAdapter extends BaseMultiItemQuickAdapter<KLineData, BaseViewHolder> implements LoadMoreModule {

    public KLineAdapter(@Nullable List<KLineData> data) {
        super(data);
        addItemType(KLineData.UnLoginType, R.layout.k_line_unlogin_item);
        addItemType(KLineData.IsLoginType, R.layout.k_line_islogin_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, KLineData kLineData) {
        if (kLineData.getItemType()==KLineData.UnLoginType){
            if (kLineData.getContentType()==1){
                //视频
                baseViewHolder.setText(R.id.KLUnLoginTitle, kLineData.getvListData().getTitle());
                baseViewHolder.setText(R.id.KLUnLoginRead, String.valueOf(kLineData.getvListData().getPlayTimes()));
                baseViewHolder.setText(R.id.KLUnLoginPrice, String.valueOf(kLineData.getvListData().getLikes()));
            }else {
                //文章
                baseViewHolder.setText(R.id.KLUnLoginTitle, kLineData.getaListData().getTitle());
                baseViewHolder.setText(R.id.KLUnLoginRead, String.valueOf(kLineData.getaListData().getReadTimes()));
                baseViewHolder.setText(R.id.KLUnLoginPrice, String.valueOf(kLineData.getaListData().getLikes()));
            }
        }else {
            if (kLineData.getContentType()==1){
                //视频
                Glide.with(getContext())
                        .load(kLineData.getvListData().getPicUrl())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .override(DisplayTool.dp2px(getContext(), 160), DisplayTool.dp2px(getContext(), 90))
                        .centerCrop()
                        .dontAnimate()
                        .into((ImageView) baseViewHolder.getView(R.id.KLineImg));
                baseViewHolder.setText(R.id.KLineTitle, kLineData.getvListData().getTitle());
                baseViewHolder.setText(R.id.KLineRead, String.valueOf(kLineData.getvListData().getPlayTimes()));
                baseViewHolder.setText(R.id.KLinePrice, String.valueOf(kLineData.getvListData().getLikes()));
                if (kLineData.getvListData().isLearn()){
                    baseViewHolder.setVisible(R.id.KLineStudy, true);
                }else {
                    baseViewHolder.setGone(R.id.KLineStudy, true);
                }
            }else {
                //文章
                Glide.with(getContext()).load(kLineData.getaListData().getPicUrl())
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                        .centerCrop().into((ImageView) baseViewHolder.getView(R.id.KLineImg));
                baseViewHolder.setText(R.id.KLineTitle, kLineData.getaListData().getTitle());
                baseViewHolder.setText(R.id.KLineRead, String.valueOf(kLineData.getaListData().getReadTimes()));
                baseViewHolder.setText(R.id.KLinePrice, String.valueOf(kLineData.getaListData().getLikes()));
                if (kLineData.getaListData().isLearn()){
                    baseViewHolder.setVisible(R.id.KLineStudy, true);
                }else {
                    baseViewHolder.setGone(R.id.KLineStudy, true);
                }
            }
        }
    }
}
