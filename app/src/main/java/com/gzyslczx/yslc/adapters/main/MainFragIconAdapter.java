package com.gzyslczx.yslc.adapters.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.IcomItemBinding;
import com.gzyslczx.yslc.modes.response.ResAdvObj;

import java.util.List;

public class MainFragIconAdapter extends BaseAdapter {

    private Context context;
    private List<ResAdvObj> dataList;

    public MainFragIconAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (dataList!=null && dataList.size()>0){
            return dataList.size();
        }
        return 10;
    }

    @Override
    public Object getItem(int i) {
        if (dataList!=null && dataList.size()>0){
            return dataList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        IcomItemBinding binding;
        if (view == null) {
            binding = IcomItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (IcomItemBinding) view.getTag();
        }
        if (dataList==null){
            Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.thump_shape)).dontAnimate().fitCenter().into(binding.IconImg);
        }else {
            Glide.with(context).load(dataList.get(i).getImg()).fitCenter().into(binding.IconImg);
            binding.IconText.setText(dataList.get(i).getTitle());
        }
        return view;
    }

    public List<ResAdvObj> getDataList() {
        return dataList;
    }

    public void setDataList(List<ResAdvObj> dataList) {
        this.dataList = dataList;
    }
}
