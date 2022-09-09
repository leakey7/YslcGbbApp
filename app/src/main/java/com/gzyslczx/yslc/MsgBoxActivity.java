package com.gzyslczx.yslc;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.gzyslczx.yslc.adapters.msgbox.MsgBoxListAdapter;
import com.gzyslczx.yslc.databinding.ActivityMsgBoxBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.MsgBoxStyleEvent;
import com.gzyslczx.yslc.presenter.MsgBoxPresenter;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MsgBoxActivity extends BaseActivity<ActivityMsgBoxBinding> implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = "MsgBoxAct";
    private MsgBoxPresenter mPresenter;
    private MsgBoxListAdapter mAdapter;


    @Override
    void InitParentLayout() {
        mViewBinding = ActivityMsgBoxBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this,R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.MsgBoxRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MsgBoxListAdapter(R.layout.msg_box_item);
        mAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.MsgBoxRecycler.setAdapter(mAdapter);
        SetupBackClicked();
        mViewBinding.MsgBoxRefresh.setOnRefreshListener(this::onRefresh);
        mPresenter = new MsgBoxPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setLogin(SpTool.Instance(this).IsGuBbLogin());
        mViewBinding.MsgBoxRefresh.setRefreshing(true);
        mPresenter.RequestMsgBoxStyle(this, this);
    }

    private void SetupBackClicked() {
        mViewBinding.MsgBoxToolBar.setNavigationOnClickListener(new View.OnClickListener() {
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

    /*
    * 接收消息盒子类型
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMsgBoxStyleEvent(MsgBoxStyleEvent event){
        if (event.isFlag()){
            mAdapter.setList(event.getDataList());
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT);
        }
        mViewBinding.MsgBoxRefresh.setRefreshing(false);
    };

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (mAdapter.isLogin()){
            Intent intent = new Intent(this, MsgInfoActivity.class);
            intent.putExtra(MsgInfoActivity.MsgTitle, mAdapter.getData().get(position).getMsgTypName());
            intent.putExtra(MsgInfoActivity.MsgStyleID, mAdapter.getData().get(position).getMsgTypId());
            startActivity(intent);
        }else {
            NormalActionTool.LoginAction(this, null, this, null, TAG);
        }
    }

    @Override
    public void onRefresh() {
        mAdapter.setLogin(SpTool.Instance(this).IsGuBbLogin());
        mPresenter.RequestMsgBoxStyle(this, this);
    }


    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        mAdapter.setLogin(event.isLogin());
        mViewBinding.MsgBoxRefresh.setRefreshing(true);
        mPresenter.RequestMsgBoxStyle(this, this);
    }
}