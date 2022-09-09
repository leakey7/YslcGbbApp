package com.gzyslczx.yslc.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.YsFlashDetailActivity;
import com.gzyslczx.yslc.adapters.main.MainYsFlashAdapter;
import com.gzyslczx.yslc.adapters.main.bean.FlashData;
import com.gzyslczx.yslc.databinding.JustRecyclerviewBinding;
import com.gzyslczx.yslc.events.GuBbYsFlashEvent;
import com.gzyslczx.yslc.events.NoticeMainRefreshEvent;
import com.gzyslczx.yslc.events.NoticeYSFlashRefreshEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResFlashObj;
import com.gzyslczx.yslc.presenter.YsFlashPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class YsFlashListFragment extends BaseFragment<JustRecyclerviewBinding> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private final String TAG = "YsFlashListFrag";
    public static final String YsFlashKey = "YsFlashType";
    private int ListType=-2;
    private MainYsFlashAdapter mListAdapter;
    private YsFlashPresenter mPresenter;
    private int current=1;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = JustRecyclerviewBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        ListType = getArguments().getInt(YsFlashKey, -2);
        mViewBinding.JustRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new MainYsFlashAdapter(new ArrayList<FlashData>());
        mListAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //上拉加载更多
        mListAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        //点击列表条目
        mListAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.JustRecycler.setAdapter(mListAdapter);
        mPresenter = new YsFlashPresenter();
        mViewBinding.JustListRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.blue_2B7FF9));
        mViewBinding.JustListRefresh.setRefreshing(true);
        mPresenter.RequestYsFlashList(getContext(), this, current, ListType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 加载更多
    * */
    @Override
    public void onLoadMore() {
        mPresenter.RequestYsFlashList(getContext(), this, current, ListType);
    }

    /*
    * 刷新
    * */
    @Override
    public void onRefresh() {
    }

    /*
    * 列表栏目点击
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        ResFlashObj data = mListAdapter.getData().get(position).getData();
        Bundle bundle = new Bundle();
        bundle.putString(YsFlashDetailActivity.FID, data.getId());
        bundle.putString(YsFlashDetailActivity.FDate, data.getDate());
        bundle.putString(YsFlashDetailActivity.FTime, data.getInputtime());
        bundle.putString(YsFlashDetailActivity.FContent, data.getContent());
        Intent intent = new Intent();
        intent.putExtra(YsFlashDetailActivity.DesKey,bundle);
        intent.setClass(getContext(), YsFlashDetailActivity.class);
        startActivity(intent);
    }

    /*
    * 接收越声快讯列表信息
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnYsFlashListEvent(GuBbYsFlashEvent event){
        if (event.isFlag() && ListType == event.getListType()){
            if (current==1){
                mListAdapter.setList(event.getDataList());
                mViewBinding.JustRecycler.scrollToPosition(0);
            }else if (current>1){
                mListAdapter.addData(event.getDataList());
            }
            mListAdapter.getLoadMoreModule().loadMoreComplete();
            current++;
            Log.d(TAG, "接收到越声快讯列表信息");
        }else if (!event.isFlag() && ListType == event.getListType()){
            mListAdapter.getLoadMoreModule().loadMoreFail();
        }
        if (mViewBinding.JustListRefresh.isRefreshing()) {
            mViewBinding.JustListRefresh.setRefreshing(false);
            mViewBinding.JustListRefresh.setEnabled(false);
        }
    }

    /*
     * 接收越声快讯列表刷新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeRefresh(NoticeYSFlashRefreshEvent event){
        if (event.getYsFlashType()==ListType){
            Log.d(TAG, String.format("接收到越声快讯列表刷新%d", ListType));
            mPresenter.ClearStringBuilder();
            current=1;
            mViewBinding.JustListRefresh.setRefreshing(true);
            mPresenter.RequestYsFlashList(getContext(), this, current, ListType);;
        }
    }

}
