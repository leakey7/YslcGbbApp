package com.gzyslczx.yslc.adapters.fund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.gzyslczx.yslc.databinding.FundDetailGridItemBinding;
import com.gzyslczx.yslc.modes.response.ResAdvObj;

import java.util.List;

public class FundDetailGridAdapter extends BaseAdapter {

    private List<ResAdvObj> data;
    private Context context;

    public FundDetailGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (data!=null){
            return data.size();
        }
        return 0;
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
        FundDetailGridItemBinding binding;
        if (view == null) {
            binding = FundDetailGridItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (FundDetailGridItemBinding) view.getTag();
        }
        if (data != null && data.size()>0) {
            binding.mainGridText.setText(data.get(i).getTitle());
            Glide.with(context).load(data.get(i).getImg()).fitCenter().into(binding.mainGridImg);
        }
        return view;
    }

    public void setData(List<ResAdvObj> data) {
        this.data = data;
    }

    public List<ResAdvObj> getData() {
        return data;
    }
}
