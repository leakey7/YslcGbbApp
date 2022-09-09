package com.gzyslczx.yslc.adapters.option;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.DefaultOptionItemBinding;
import com.gzyslczx.yslc.modes.response.ResDefaultOptionObj;

import java.util.List;

public class DefaultOptionGridAdapter extends BaseAdapter {

    private Context context;
    private List<ResDefaultOptionObj> data;

    public DefaultOptionGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return (data==null || data.size()>0) ? null : data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DefaultOptionItemBinding binding;
        if (view == null) {
            binding = DefaultOptionItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (DefaultOptionItemBinding) view.getTag();
        }
        if (data != null && data.size()>0 ) {
            if (TextUtils.isEmpty((String) binding.DefOptionItemSelect.getTag())) {
                binding.DefOptionItemName.setText(data.get(i).getStockName());
                binding.DefOptionItemCode.setText(data.get(i).getStockCode());
                binding.DefOptionItemType.setText(data.get(i).getSsswhy_Sjhy());
                if (data.get(i).isSelect()) {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.selected)).fitCenter().into(binding.DefOptionItemSelect);
                    binding.DefOptionItemSelect.setTag("TRUE");
                } else {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.unselect)).fitCenter().into(binding.DefOptionItemSelect);
                    binding.DefOptionItemSelect.setTag("FALSE");
                }
            }else {
                if (!data.get(i).isSelect() && binding.DefOptionItemSelect.getTag().equals("TRUE")){
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.unselect)).fitCenter().into(binding.DefOptionItemSelect);
                    binding.DefOptionItemSelect.setTag("FALSE");
                }
                if (data.get(i).isSelect() && binding.DefOptionItemSelect.getTag().equals("FALSE")){
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.selected)).fitCenter().into(binding.DefOptionItemSelect);
                    binding.DefOptionItemSelect.setTag("TRUE");
                }
            }
        }
        return view;
    }

    public List<ResDefaultOptionObj> getData() {
        return data;
    }

    public void setData(List<ResDefaultOptionObj> data) {
        this.data = data;
    }
}
