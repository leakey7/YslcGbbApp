package com.gzyslczx.yslc.adapters.msgbox;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResMsgBoxObj;
import com.gzyslczx.yslc.tools.DisplayTool;

public class MsgBoxListAdapter extends BaseQuickAdapter<ResMsgBoxObj, BaseViewHolder> {

    private boolean IsLogin;

    public MsgBoxListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResMsgBoxObj data) {
        if (IsLogin){
            Glide.with(getContext()).load(data.getMsgImg())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                            new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                    .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                    .into((ImageView) baseViewHolder.getView(R.id.MsgBoxItemHeadImg));
            baseViewHolder.setText(R.id.MsgBoxItemClass, data.getMsgTypName());
            baseViewHolder.setText(R.id.MsgBoxItemDes, data.getMsgContent());
            baseViewHolder.setText(R.id.MsgBoxItemTime, data.getDateInfo());
            baseViewHolder.setVisible(R.id.MsgBoxItemTime,true);
            if (data.getMsgNum().equals("0")){
                baseViewHolder.setGone(R.id.MsgBoxItemNum,true);
            }else {
                baseViewHolder.setText(R.id.MsgBoxItemNum, data.getMsgNum());
                baseViewHolder.setVisible(R.id.MsgBoxItemNum,true);
            }
        }else {
            Glide.with(getContext()).load(data.getMsgImg())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                    new RoundedCorners(DisplayTool.dp2px(getContext(), 44)))))
                    .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.thump_shape))
                    .into((ImageView) baseViewHolder.getView(R.id.MsgBoxItemHeadImg));
            baseViewHolder.setText(R.id.MsgBoxItemClass, data.getMsgTypName());
            baseViewHolder.setText(R.id.MsgBoxItemDes, getContext().getString(R.string.NoMsgWhiteLogin));
            baseViewHolder.setGone(R.id.MsgBoxItemNum,true);
            baseViewHolder.setGone(R.id.MsgBoxItemTime,true);
        }
    }

    public void setLogin(boolean login) {
        IsLogin = login;
    }

    public boolean isLogin() {
        return IsLogin;
    }
}
