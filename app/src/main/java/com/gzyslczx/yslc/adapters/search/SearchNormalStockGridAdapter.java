package com.gzyslczx.yslc.adapters.search;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.SearchNormalStockItemBinding;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResSearchStock;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;

import java.util.List;

import cn.jpush.android.cache.Sp;

public class SearchNormalStockGridAdapter extends BaseAdapter {

    List<ResSearchStock> data;
    private Context context;
    private BaseFragment baseFragment;
    private StringBuilder searchText;

    public SearchNormalStockGridAdapter(Context context) {
        this.context = context;
        searchText = new StringBuilder();
    }

    public void setBaseFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
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
        if (data != null) {
            return data.size();
        }
        return 0;
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
        SearchNormalStockItemBinding binding;
        if (view == null) {
            binding = SearchNormalStockItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (SearchNormalStockItemBinding) view.getTag();
        }
        if (data != null && data.size()>0) {
            binding.StockName.setText(NormalActionTool.getColorSpannableString(data.get(i).getStockName(), searchText.toString(), ContextCompat.getColor(context, R.color.main_red)));
            binding.StockCode.setText(NormalActionTool.getColorSpannableString(data.get(i).getStockCode(), searchText.toString(), ContextCompat.getColor(context, R.color.main_red)));
            if (data.get(i).isFocus()) {
                binding.StockOption.setText("删自选");
                binding.StockOption.setTextColor(ContextCompat.getColor(context, R.color.gray_999));
                binding.StockOption.setBackground(ContextCompat.getDrawable(context, R.drawable.gray_focus_radius_10_shape));
            } else {
                binding.StockOption.setText("加自选");
                binding.StockOption.setTextColor(ContextCompat.getColor(context, R.color.main_red));
                binding.StockOption.setBackground(ContextCompat.getDrawable(context, R.drawable.red_border_focus_radius_10_shape));
            }
            binding.StockOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SpTool.Instance(context).IsGuBbLogin()) {
                        //添删自选
                        if (data.get(i).isFocus()) {
                            Log.d("DEL", "删自选");
                            NormalActionTool.DeleteOptionAction(context, baseFragment, (BaseActivity) baseFragment.getActivity(),
                                    new String[]{data.get(i).getStockCode()}, "DEL");
                        } else {
                            Log.d("ADD", "添加自选");
                            NormalActionTool.AddOptionAction(context, baseFragment, (BaseActivity) baseFragment.getActivity(),
                                    new String[]{data.get(i).getStockCode()}, "ADD");
                        }
                    }else {
                        NormalActionTool.LoginAction(context, baseFragment, (BaseActivity) baseFragment.getActivity(), data.get(i).getStockCode(), "ADD");
                    }
                }
            });
        }
        return view;
    }

    public List<ResSearchStock> getData() {
        return data;
    }

    public void setData(List<ResSearchStock> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            this.data.clear();
            if (data!=null) {
                this.data.addAll(data);
            }
        }
    }

}
