package com.gzyslczx.yslc.fragments.teacher;

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
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.BigVideoActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.TeacherSelfActivity;
import com.gzyslczx.yslc.adapters.teacher.TeacherSelfLivingAdapter;
import com.gzyslczx.yslc.adapters.teacher.bean.TeacherSelfLivingData;
import com.gzyslczx.yslc.databinding.OnlyjustRecyclerviewBinding;
import com.gzyslczx.yslc.databinding.NoneDataItemBinding;
import com.gzyslczx.yslc.events.TeacherSelfLivingEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.TeacherSelfPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class TeacherSelfLivingFragment extends BaseFragment<OnlyjustRecyclerviewBinding> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, OnItemClickListener {

    private final String TAG = "TSelfLivingFrag";
    private TeacherSelfPresenter mPresenter;
    private TeacherSelfLivingAdapter mAdapter;
    private int CurrentPage=1;
    private String Tid;
    private NoneDataItemBinding mNoneDataBinding;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = OnlyjustRecyclerviewBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        Tid = getArguments().getString(TeacherSelfActivity.TIDKey);
        mViewBinding.JustListRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.main_red));
        //设置刷新监听
        mViewBinding.JustListRefresh.setOnRefreshListener(this::onRefresh);
        mViewBinding.JustRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TeacherSelfLivingAdapter(new ArrayList<TeacherSelfLivingData>());
        //设置加载更多监听
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        //设置列表点击
        mAdapter.setOnItemClickListener(this::onItemClick);
        mAdapter.setEmptyView(InitNoneDataView());
        mViewBinding.JustRecycler.setAdapter(mAdapter);
        EventBus.getDefault().register(this);
        mPresenter = new TeacherSelfPresenter();
        mViewBinding.JustListRefresh.setRefreshing(true);
        //type=2代表观点资讯
        mPresenter.RequestLivingList(getContext(), this, (BaseActivity) getActivity(), CurrentPage, Tid);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 接收名师个人直播列
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnTeacherSelfLivingListEvent(TeacherSelfLivingEvent event){
        Log.d(TAG, "接收到名师个人直播列");
        if (event.isFlag() && event.getDataList()!=null && event.getDataList().size()>0){
            if (CurrentPage==1){
                mAdapter.setList(event.getDataList());
            }else {
                mAdapter.addData(event.getDataList());
            }
            if (event.isEnd()){
                mAdapter.getLoadMoreModule().loadMoreEnd();
            }else {
                CurrentPage++;
                mAdapter.getLoadMoreModule().loadMoreComplete();
            }
        }else {
            if (CurrentPage==1){
                mNoneDataBinding.noneText.setVisibility(View.VISIBLE);
                mNoneDataBinding.noneImg.setVisibility(View.VISIBLE);
                mNoneDataBinding.noneText.setText("暂无数据");
            }else {
                mAdapter.getLoadMoreModule().loadMoreFail();
            }
        }
        if (mViewBinding.JustListRefresh.isRefreshing()){
            mViewBinding.JustListRefresh.setRefreshing(false);
        }
    }

    /*
    * 刷新
    * */
    @Override
    public void onRefresh() {
        CurrentPage=1;
        mPresenter.RequestLivingList(getContext(), this, (BaseActivity) getActivity(), CurrentPage, Tid);
    }

    /*
    * 加载更多
    * */
    @Override
    public void onLoadMore() {
        mPresenter.RequestLivingList(getContext(), this, (BaseActivity) getActivity(), CurrentPage, Tid);
    }

    /*
     * 初始化无数据视图
     * */
    private View InitNoneDataView(){
        mNoneDataBinding = NoneDataItemBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.none_data_item, null));
        mNoneDataBinding.noneBtn.setVisibility(View.GONE);
        mNoneDataBinding.noneText.setVisibility(View.GONE);
        mNoneDataBinding.noneImg.setVisibility(View.GONE);
        return mNoneDataBinding.getRoot();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(getContext(), BigVideoActivity.class);
        Log.d("直播ID", mAdapter.getData().get(position).getLivingData().getLiveId());
        intent.putExtra(BigVideoActivity.BidVideoKey, mAdapter.getData().get(position).getLivingData().getLiveId());
        startActivity(intent);
    }
}
