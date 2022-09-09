package com.gzyslczx.yslc.fragments.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gzyslczx.yslc.adapters.search.SearchHisGridAdapter;
import com.gzyslczx.yslc.databinding.SearchHistoryFragmentBinding;
import com.gzyslczx.yslc.events.SearchHistoryEvent;
import com.gzyslczx.yslc.events.SearchPageChangeEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.SearchPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchHistoryFragment extends BaseFragment<SearchHistoryFragmentBinding> {

    private SearchHisGridAdapter mStockAdapter, mFundAdapter;
    private SearchPresenter mPresenter;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = SearchHistoryFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mStockAdapter = new SearchHisGridAdapter(getContext(), SearchHisGridAdapter.StockType);
        mFundAdapter = new SearchHisGridAdapter(getContext(), SearchHisGridAdapter.FundType);
        mViewBinding.SearchHisStockGrid.setAdapter(mStockAdapter);
        mViewBinding.SearchHisFundGrid.setAdapter(mFundAdapter);
        SearchStockHistoryClick();
        SearchFundHistoryClick();
        mPresenter = new SearchPresenter();
        mPresenter.RequestHistory(getContext(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 股票历史点击
     * */
    private void SearchStockHistoryClick(){
        mViewBinding.SearchHisStockGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchPageChangeEvent event = new SearchPageChangeEvent(1, mStockAdapter.getDataList().getStockList().get(i).getStockName());
                EventBus.getDefault().post(event);
            }
        });
    }

    /*
     * 基金历史点击
     * */
    private void SearchFundHistoryClick(){
        mViewBinding.SearchHisFundGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchPageChangeEvent event = new SearchPageChangeEvent(1, mStockAdapter.getDataList().getFundList().get(i).getFundName());
                EventBus.getDefault().post(event);
            }
        });
    }


    /*
    * 接收历史搜索
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSearchHistoryEvent(SearchHistoryEvent event){
        if (event.isFlag()){
            mViewBinding.SearchHisTag.setVisibility(View.VISIBLE);
            mViewBinding.SearchHisStock.setVisibility(View.VISIBLE);
            mViewBinding.SearchHisStockGrid.setVisibility(View.VISIBLE);
            mViewBinding.SearchHisFund.setVisibility(View.VISIBLE);
            mViewBinding.SearchHisFundGrid.setVisibility(View.VISIBLE);
            mStockAdapter.setDataList(event.getObjData());
            mStockAdapter.notifyDataSetChanged();
            mFundAdapter.setDataList(event.getObjData());
            mFundAdapter.notifyDataSetChanged();
        }else {
            mViewBinding.SearchHisTag.setVisibility(View.GONE);
            mViewBinding.SearchHisStock.setVisibility(View.GONE);
            mViewBinding.SearchHisStockGrid.setVisibility(View.GONE);
            mViewBinding.SearchHisFund.setVisibility(View.GONE);
            mViewBinding.SearchHisFundGrid.setVisibility(View.GONE);
        }
    }

}
