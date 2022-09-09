
package com.gzyslczx.yslc;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.gzyslczx.yslc.adapters.msgbox.MsgInfoListAdapter;
import com.gzyslczx.yslc.databinding.ActivityMsgInfoBinding;
import com.gzyslczx.yslc.events.MsgInfoListEvent;
import com.gzyslczx.yslc.events.UpdateMsgListEvent;
import com.gzyslczx.yslc.presenter.MsgInfoPresenter;
import com.gzyslczx.yslc.tools.SecretCodeTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MsgInfoActivity extends BaseActivity<ActivityMsgInfoBinding> implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnLoadMoreListener {

    private final String TAG = "MsgInfoAct";
    private MsgInfoListAdapter mAdapter;
    private MsgInfoPresenter mPresenter;
    public static final String MsgTitle = "MsgName";
    public static final String MsgStyleID= "MsgId";
    private int MsgTypeId;
    private String MsgInfoName;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityMsgInfoBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        MsgInfoName = getIntent().getStringExtra(MsgTitle);
        MsgTypeId = getIntent().getIntExtra(MsgStyleID, -1);
        mViewBinding.MsgInfoRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MsgInfoListAdapter(R.layout.msg_item_layout);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.MsgInfoRecycler.setAdapter(mAdapter);
        SetupBackListener();
        mViewBinding.MsgInfoRefresh.setOnRefreshListener(this::onRefresh);
        mPresenter = new MsgInfoPresenter();
        mViewBinding.MsgInfoRefresh.setRefreshing(true);
        mPresenter.RequestMsgInfo(this, this, MsgTypeId);
        mViewBinding.MsgInfoToolBarTitle.setText(MsgInfoName);
    }

    private void SetupBackListener() {
        mViewBinding.MsgInfoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        mPresenter.setCurrentPage(1);
        mPresenter.RequestMsgInfo(this, this, MsgTypeId);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Log.d(TAG, "AppUrl="+mAdapter.getData().get(position).getUrl());
        Intent intent = SecretCodeTool.AnalysisSecretCode(this, mAdapter.getData().get(position).getUrl());
        if (intent!=null){
            startActivity(intent);
            if (mAdapter.getData().get(position).getRead()==0){
                mPresenter.RequestUpdateMsgInfo(this, this, mAdapter.getData().get(position).getId(), position);
            }
        }
    }

    @Override
    public void onLoadMore() {
        mPresenter.RequestMsgInfo(this, this, MsgTypeId);
    }

    /*
    * 接收消息列表
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMsgInfoListEvent (MsgInfoListEvent event){
        if (event.isFlag()){
            if (mPresenter.getCurrentPage()==1){
                mAdapter.setList(event.getDataList());
            }else {
                mAdapter.addData(event.getDataList());
            }
            if (event.isEnd()){
                mAdapter.getLoadMoreModule().loadMoreEnd();
            }else {
                mAdapter.getLoadMoreModule().loadMoreComplete();
                mPresenter.AddCurrentPage();
            }
        }else {
            mAdapter.getLoadMoreModule().loadMoreFail();
        }
        if (mViewBinding.MsgInfoRefresh.isRefreshing()){
            mViewBinding.MsgInfoRefresh.setRefreshing(false);
        }
    }

    /*
    * 接收更新消息列表
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnUpdateMsgInfoListEvent (UpdateMsgListEvent event){
        if (event.isFlag()){
            mAdapter.getData().get(event.getPos()).setRead(1);
            mAdapter.notifyDataSetChanged();
        }
    }
}