package com.gzyslczx.yslc.adapters.search;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.SearchAboutItemBinding;
import com.gzyslczx.yslc.modes.response.ResSearchAbout;
import com.gzyslczx.yslc.tools.NormalActionTool;

import java.util.List;

public class SearchAboutGridAdapter extends BaseAdapter {
    private Context context;
    private List<ResSearchAbout> data;
    private StringBuilder searchText;

    public SearchAboutGridAdapter(Context context) {
        this.context = context;
        searchText = new StringBuilder();
    }

    public void setSearchText(String searchText) {
        if (TextUtils.isEmpty(searchText)){
            this.searchText.delete(0, this.searchText.length());
            return;
        }
        this.searchText.replace(0, this.searchText.length(), searchText);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data == null ? null : data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SearchAboutItemBinding binding;
        if (view == null) {
            binding = SearchAboutItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (SearchAboutItemBinding) view.getTag();
        }
        if (data!=null) {
            binding.aboutTitle.setText(NormalActionTool.getColorSpannableString(data.get(i).getTitle(), searchText.toString(), ContextCompat.getColor(context, R.color.main_red)));
            binding.aboutTitleDes.setText(NormalActionTool.getColorSpannableString(data.get(i).getDesc(), searchText.toString(), ContextCompat.getColor(context, R.color.main_red)));
            binding.aboutTime.setText(data.get(i).getDateInfo());
        }
        return view;
    }

    public List<ResSearchAbout> getData() {
        return data;
    }

    public void setData(List<ResSearchAbout> data) {
        if (this.data==null){
            this.data = data;
        }else {
            this.data.clear();
            if (data!=null){
                this.data.addAll(data);
            }
        }
    }
}
