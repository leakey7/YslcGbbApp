package com.gzyslczx.yslc.adapters.msgbox;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.modes.response.ResMsgInfoListInfo;

public class MsgInfoListAdapter extends BaseQuickAdapter<ResMsgInfoListInfo, BaseViewHolder> implements LoadMoreModule {

    public MsgInfoListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ResMsgInfoListInfo data) {
        baseViewHolder.setText(R.id.MsgItemTitle, data.getTitle());
        baseViewHolder.setText(R.id.MsgItemDes, data.getContent());
        if (data.getRead()==0){
            //未读
            baseViewHolder.setVisible(R.id.MsgItemNews, true);
        }else {
            baseViewHolder.setGone(R.id.MsgItemNews, true);
        }
        baseViewHolder.setText(R.id.MsgItemTime, data.getAddDate());
    }
}
