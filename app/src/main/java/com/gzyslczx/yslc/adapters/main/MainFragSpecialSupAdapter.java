package com.gzyslczx.yslc.adapters.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.gzyslczx.yslc.databinding.MainFragSupListItemBinding;
import com.gzyslczx.yslc.modes.response.ResMainSupObj;
import com.gzyslczx.yslc.tools.DisplayTool;

import java.util.List;

public class MainFragSpecialSupAdapter extends BaseAdapter {

    private Context context;
    private List<ResMainSupObj> data;

    public MainFragSpecialSupAdapter(Context context) {
        this.context = context;
    }

    public List<ResMainSupObj> getData() {
        return data;
    }

    public void setData(List<ResMainSupObj> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        if (data!=null){
            return data.size();
        }
        return 3;
    }

    @Override
    public Object getItem(int i) {
        if (data!=null && data.size()>0){
            return data.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MainFragSupListItemBinding binding;
        if (view == null) {
            binding = MainFragSupListItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DisplayTool.dp2px(context, 100));
            view.setLayoutParams(layoutParams);
        } else {
            binding = (MainFragSupListItemBinding) view.getTag();
        }
        if (data!=null) {
            binding.MainSupListItemName.setText(data.get(i).getStockName());
            binding.MainSupListItemCode.setText(data.get(i).getStockCode());
        }
        return view;
    }
}
