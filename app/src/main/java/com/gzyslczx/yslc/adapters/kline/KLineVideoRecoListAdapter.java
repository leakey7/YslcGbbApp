package com.gzyslczx.yslc.adapters.kline;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResKLDetailRec;

public class KLineVideoRecoListAdapter extends BaseQuickAdapter<ResKLDetailRec, BaseViewHolder> {


    public KLineVideoRecoListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResKLDetailRec data) {
        Glide.with(getContext()).load(data.getPicUrl()).fitCenter().into((ImageView) baseViewHolder.getView(R.id.KLineRecoItemImg));
        baseViewHolder.setText(R.id.KLineRecoItemTitle, data.getTitle());
        baseViewHolder.setText(R.id.KLineRecoItemRead, data.getPlayTimes());
        baseViewHolder.setText(R.id.KLineRecoItemPraise, data.getLikes());
        if (data.isLearn()){
            baseViewHolder.setVisible(R.id.KLineRecoItemLearn, true);
        }else {
            baseViewHolder.setVisible(R.id.KLineRecoItemLearn, false);
        }
    }
}
