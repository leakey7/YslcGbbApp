package com.gzyslczx.yslc.adapters.stockmarket;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gzyslczx.yslc.databinding.RealTimeQuotesItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoreRealTimeGridAdapter extends BaseAdapter {

    private int type;
    public static final int NormalStyle = 1;
    public static final int KeChuangStyle = 2;
    private Context context;
    private List<String> valueList;
    private String DefValue="--";
    private final String[] NormalKey = new String[]{
            "涨停", "昨收", "涨跌",
            "跌停", "每手股", "五分涨速",
            "内盘", "委买", "市盈",
            "外盘", "委卖", "市净"};
    private final String[] KeChuanglKey = new String[]{
            "涨停", "昨收", "涨跌",
            "跌停", "每手股", "五分涨速",
            "内盘", "委买", "市盈",
            "外盘", "委卖", "市净",
            "盘后量", "成交量", "收盘价",
            "盘后额", "成交额"};

    public MoreRealTimeGridAdapter(int type, Context context) {
        this.type = type;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (type==NormalStyle){
            return NormalKey.length;
        }else if (type==KeChuangStyle){
            return KeChuanglKey.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (valueList!=null){
            if (type==NormalStyle && valueList.size()>=18) {
                return valueList.get(position);
            }else if (type==KeChuangStyle && valueList.size()>=24){
                return valueList.get(position);
            }
        }
        return DefValue;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RealTimeQuotesItemBinding binding;
        if (convertView == null) {
            binding = RealTimeQuotesItemBinding.inflate(LayoutInflater.from(context));
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (RealTimeQuotesItemBinding) convertView.getTag();
        }
        if (type==NormalStyle) {
            binding.LeftTag.setText(NormalKey[position]);
        }else {
            binding.LeftTag.setText(KeChuanglKey[position]);
        }
        if (valueList != null && valueList.size()>0) {
            binding.RightValue.setText(valueList.get(position));
        }else {
            binding.RightValue.setText(DefValue);
        }
        return convertView;
    }

    public List<String> getValueList() {
        return valueList;
    }

    public void setValueList(List<String> valueList) {
        if (this.valueList==null){
            this.valueList = new ArrayList<String>();
        }
        this.valueList.clear();
        this.valueList.addAll(valueList);
        notifyDataSetChanged();
    }
}
