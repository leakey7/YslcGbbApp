package com.gzyslczx.yslc.adapters.stockmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gzyslczx.yslc.databinding.StockMarketValueItemBinding;

import java.util.ArrayList;
import java.util.List;

public class StockMarketValueGridAdapter extends BaseAdapter {

    private List<String> values;
    private Context context;
    private int DefSize = 9;
    private String DefValues = "--";
    private final String[] DefKey = new String[]{
            "最高", "总量", "总额",
            "最低", "现手", "换手",
            "今开", "振幅", "量比", };

    public StockMarketValueGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (values!=null){
            return values.size();
        }
        return DefSize;
    }

    @Override
    public Object getItem(int position) {
        if (values!=null){
            return values.get(position);
        }
        return DefValues;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StockMarketValueItemBinding binding;
        if (convertView == null) {
            binding = StockMarketValueItemBinding.inflate(LayoutInflater.from(context));
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (StockMarketValueItemBinding) convertView.getTag();
        }
        binding.TagText.setText(DefKey[position]);
        if (values != null && values.size()>0) {
            binding.ValueText.setText(values.get(position));
        }else {
            binding.ValueText.setText(DefValues);
        }
        return convertView;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        if (this.values==null){
            this.values = new ArrayList<String>();
        }
        this.values.clear();
        this.values.addAll(values);
        notifyDataSetChanged();
    }

}
