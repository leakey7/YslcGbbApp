package com.gzyslczx.yslc.adapters.search;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.SearchNormalFundItemBinding;
import com.gzyslczx.yslc.modes.response.ResSearchFund;
import com.gzyslczx.yslc.tools.NormalActionTool;

import java.util.List;

public class SearchNormalFundGridAdapter extends BaseAdapter {
    List<ResSearchFund> data;
    private Context context;
    private StringBuilder searchText;

    public SearchNormalFundGridAdapter(Context context) {
        this.context = context;
        searchText = new StringBuilder();
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    public void setSearchText(String searchText) {
        if (TextUtils.isEmpty(searchText)){
            this.searchText.delete(0, this.searchText.length());
            return;
        }
        this.searchText.replace(0, this.searchText.length(), searchText);
    }

    @Override
    public Object getItem(int i) {
        if (data != null && data.size() > 0) {
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
        SearchNormalFundItemBinding binding;
        if (view == null) {
            binding = SearchNormalFundItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (SearchNormalFundItemBinding) view.getTag();
        }
        if (data != null && data.size()>0) {
            binding.FundName.setText(NormalActionTool.getColorSpannableString(data.get(i).getName(), searchText.toString(), ContextCompat.getColor(context, R.color.main_red)));
            binding.FundCode.setText(NormalActionTool.getColorSpannableString(data.get(i).getFCode(), searchText.toString(), ContextCompat.getColor(context, R.color.main_red)));
        }
        return view;
    }

    public List<ResSearchFund> getData() {
        return data;
    }

    public void setData(List<ResSearchFund> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.clear();
            if (data!=null){
                this.data.addAll(data);
            }
        }
    }
}
