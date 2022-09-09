package com.gzyslczx.yslc.adapters.kline;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResKLDetailRec;

public class KLineArticleRecoListAdapter extends BaseQuickAdapter<ResKLDetailRec, BaseViewHolder> {

    public KLineArticleRecoListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResKLDetailRec data) {
        baseViewHolder.setText(R.id.KLArtRecTitle, data.getTitle());
        baseViewHolder.setText(R.id.KLArtRecReadNum, data.getReadTimes());
        if (data.isLikes()){
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.is_praise)).fitCenter()
                    .into((ImageView) baseViewHolder.getView(R.id.KLArtRecPraiseImg));
            baseViewHolder.setText(R.id.KLArtRecPraiseNum, data.getLikes());
        }else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.un_praise)).fitCenter()
                    .into((ImageView) baseViewHolder.getView(R.id.KLArtRecPraiseImg));
            baseViewHolder.setText(R.id.KLArtRecPraiseNum, getContext().getString(R.string.Praise));
        }
    }
}
