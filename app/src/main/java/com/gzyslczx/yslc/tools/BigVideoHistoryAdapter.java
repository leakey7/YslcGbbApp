package com.gzyslczx.yslc.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.BigVideoHistoryItemBinding;
import com.gzyslczx.yslc.modes.response.ResTHisVideoList;

import java.util.List;

public class BigVideoHistoryAdapter extends BaseAdapter {

    private List<ResTHisVideoList> dataList;
    private Context context;

    public BigVideoHistoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (dataList!=null && dataList.size()>0){
            return  dataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (dataList!=null && dataList.size()>0){
            return  dataList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BigVideoHistoryItemBinding binding;
        if (view == null) {
            binding = BigVideoHistoryItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (BigVideoHistoryItemBinding) view.getTag();
        }
        if (dataList != null) {
            Glide.with(context).load(dataList.get(i).getImg()).placeholder(ContextCompat.getDrawable(context, R.drawable.thump_shape))
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(DisplayTool.dp2px(context, 6)))))
                    .into(binding.HisVideoImg);
            binding.HisVideoName.setText(dataList.get(i).getTitle());
        }
        return view;
    }

    public void setDataList(List<ResTHisVideoList> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public List<ResTHisVideoList> getDataList() {
        return dataList;
    }

}
