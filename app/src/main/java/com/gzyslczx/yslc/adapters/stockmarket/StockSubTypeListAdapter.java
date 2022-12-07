package com.gzyslczx.yslc.adapters.stockmarket;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.tools.yourui.myviews.VolumeTypeConstance;

import java.util.ArrayList;
import java.util.List;

public class StockSubTypeListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<String> Types;
    private int Select = 0;

    public StockSubTypeListAdapter(int layoutResId) {
        super(layoutResId);
        Types = new ArrayList<>();
        Types.add("成交量");
        Types.add("KDJ");
        Types.add("MACD");
        setList(Types);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
       if (Select==getItemPosition(s)){
           baseViewHolder.setTextColor(R.id.SubTypeItem, ContextCompat.getColor(getContext(), R.color.main_red));
       }else {
           baseViewHolder.setTextColor(R.id.SubTypeItem, ContextCompat.getColor(getContext(), R.color.black_333));
       }
        baseViewHolder.setText(R.id.SubTypeItem, s);
    }

    public int getSelect() {
        return Select;
    }

    public void setSelect(int select) {
        Select = select;
    }

    public int getType(int position){
        switch (position){
            case 0:
                return VolumeTypeConstance.Volume;
            case 1:
                return VolumeTypeConstance.KDJ;
            case 2:
                return VolumeTypeConstance.MACD;
            default:
                return VolumeTypeConstance.Volume;
        }
    }

}
