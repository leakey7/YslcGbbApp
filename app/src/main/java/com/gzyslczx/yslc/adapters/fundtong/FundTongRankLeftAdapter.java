package com.gzyslczx.yslc.adapters.fundtong;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResFundTongRankInfo;

import java.util.List;

public class FundTongRankLeftAdapter extends BaseQuickAdapter<ResFundTongRankInfo, BaseViewHolder> implements LoadMoreModule {


    public FundTongRankLeftAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResFundTongRankInfo resFundTongRankInfo) {
        baseViewHolder.setText(R.id.FundTongLeftName, resFundTongRankInfo.getName());
        baseViewHolder.setText(R.id.FundTongLeftCode, resFundTongRankInfo.getFCode());
        int pos = getData().indexOf(resFundTongRankInfo);
        if (pos==0 || pos==1 || pos==2){
            baseViewHolder.setVisible(R.id.FundTongLeftSortImg, true);
            if (pos==0){
                Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.no_1)).fitCenter()
                        .into((ImageView) baseViewHolder.getView(R.id.FundTongLeftSortImg));
            }else if (pos==1){
                Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.no_2)).fitCenter()
                        .into((ImageView) baseViewHolder.getView(R.id.FundTongLeftSortImg));
            }else {
                Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.no_3)).fitCenter()
                        .into((ImageView) baseViewHolder.getView(R.id.FundTongLeftSortImg));
            }
            baseViewHolder.setVisible(R.id.FundTongLeftNumber, false);
        }else {
            baseViewHolder.setVisible(R.id.FundTongLeftSortImg, false);
            baseViewHolder.setVisible(R.id.FundTongLeftNumber, true);
            baseViewHolder.setText(R.id.FundTongLeftNumber, String.valueOf(pos+1));
        }

    }
}
