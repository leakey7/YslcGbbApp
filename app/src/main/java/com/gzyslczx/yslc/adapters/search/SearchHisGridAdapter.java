package com.gzyslczx.yslc.adapters.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gzyslczx.yslc.databinding.SearchHistoryItemBinding;
import com.gzyslczx.yslc.modes.response.ResSearchHisObj;

public class SearchHisGridAdapter extends BaseAdapter {
    private Context context;

    private ResSearchHisObj DataList;

    public static final int FundType = 1;
    public static final int StockType = 2;

    private int ItemType=-1;

    public SearchHisGridAdapter(Context context, int itemType) {
        this.context = context;
        ItemType = itemType;
    }

    @Override
    public int getCount() {
        if (ItemType == StockType && DataList!=null && DataList.getStockList()!=null){
            return DataList.getStockList().size();
        }
        if (ItemType == FundType && DataList!=null && DataList.getFundList()!=null){
            return DataList.getFundList().size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (ItemType == StockType){
            return DataList.getStockList().get(i);
        }
        if (ItemType == FundType){
            return DataList.getFundList().get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SearchHistoryItemBinding binding;
        if (view == null) {
            binding = SearchHistoryItemBinding.inflate(LayoutInflater.from(context));
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (SearchHistoryItemBinding) view.getTag();
        }
        if (DataList!=null) {
            if (ItemType == StockType && DataList.getStockList()!=null && DataList.getStockList().size()>0){
                binding.SearchHisText.setText(DataList.getStockList().get(i).getStockName());
            }
            if (ItemType == FundType && DataList.getFundList()!=null && DataList.getFundList().size()>0){
                binding.SearchHisText.setText(DataList.getFundList().get(i).getFundName());
            }
        }
        return view;
    }

    public ResSearchHisObj getDataList() {
        return DataList;
    }

    public void setDataList(ResSearchHisObj dataList) {
        DataList = dataList;
    }
}
