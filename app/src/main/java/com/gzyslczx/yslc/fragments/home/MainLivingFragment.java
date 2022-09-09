package com.gzyslczx.yslc.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gzyslczx.yslc.WebActivity;
import com.gzyslczx.yslc.adapters.main.MainLivingListAdapter;
import com.gzyslczx.yslc.adapters.main.bean.MainTeacherLivingData;
import com.gzyslczx.yslc.databinding.JustRecyclerviewBinding;
import com.gzyslczx.yslc.events.GuBbMainTeacherLivingEvent;
import com.gzyslczx.yslc.events.NoticeFocusPageRefreshEvent;
import com.gzyslczx.yslc.events.NoticeMainRefreshEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.MainFragmentPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainLivingFragment extends BaseFragment<JustRecyclerviewBinding> implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private MainLivingListAdapter mListAdapter;
    private final String TAG = "MainLivingFragment";
    private MainFragmentPresenter mPresenter;
    private boolean needInit = false;
    public static final String NeedInitKey = "InitKey";

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = JustRecyclerviewBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.JustRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new MainLivingListAdapter(new ArrayList<MainTeacherLivingData>());
        mListAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.JustRecycler.setAdapter(mListAdapter);
        mViewBinding.JustListRefresh.setOnRefreshListener(this);
        mPresenter = new MainFragmentPresenter();
        if (getArguments()!=null){
            needInit = getArguments().getBoolean(NeedInitKey, false);
            if (needInit){
                mViewBinding.JustListRefresh.setRefreshing(true);
                mPresenter.ContactTeacherLiving(getContext(), this);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 接收名师直播
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSetupTeacherLivingEvent(GuBbMainTeacherLivingEvent event){
        Log.d(TAG, "接收到名师直播");
        if (event.isFlag()){
            mListAdapter.setList(event.getLivingData());
            mViewBinding.JustRecycler.scrollToPosition(0);
        }else {
            Log.d(TAG, String.format("名师直播 Error=%s", event.getError()));
        }
        mViewBinding.JustListRefresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(getContext(), WebActivity.class);
        intent.putExtra(WebActivity.WebKey, mListAdapter.getData().get(position).getDataObj().getVideourl());
        intent.putExtra(WebActivity.WebVideoKey, true);
        startActivity(intent);
    }

    /*
     * 接收直播页面刷新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeRefresh(NoticeMainRefreshEvent event){
        if (event.getShowPage()==5){
            Log.d(TAG, "接收到直播页刷新");
            mViewBinding.JustListRefresh.setRefreshing(true);
            mPresenter.ContactTeacherLiving(getContext(), this);
        }
    }

    @Override
    public void onRefresh() {
        mViewBinding.JustListRefresh.setRefreshing(true);
        mPresenter.ContactTeacherLiving(getContext(), this);
    }
}
