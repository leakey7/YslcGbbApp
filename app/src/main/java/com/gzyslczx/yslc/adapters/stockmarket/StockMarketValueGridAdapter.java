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
    private String[] DefValues = new String[]{"--", "--", "--", "--", "--", "--", "--", "--", "--"};

    public StockMarketValueGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (values!=null){
            return values.size();
        }
        return DefValues.length;
    }

    @Override
    public Object getItem(int position) {
        if (values!=null){
            return values.get(position);
        }
        return DefValues[position];
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
        switch (position){
            case 0:
                binding.TagText.setText("高");
                break;
            case 1:
                binding.TagText.setText("量比");
                break;
            case 2:
                binding.TagText.setText("金额");
                break;
            case 3:
                binding.TagText.setText("低");
                break;
            case 4:
                binding.TagText.setText("换手");
                break;
            case 5:
                binding.TagText.setText("流通");
                break;
            case 6:
                binding.TagText.setText("开");
                break;
            case 7:
                binding.TagText.setText("振幅");
                break;
            case 8:
                binding.TagText.setText("市盈");
                break;
        }

        if (values != null && values.size()>0) {
            binding.ValueText.setText(values.get(position));
        }else {
            binding.ValueText.setText(DefValues[position]);
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
