package com.gzyslczx.yslc;

import android.content.Intent;
import android.util.Log;

import com.gzyslczx.yslc.databinding.ActivityLaunchBinding;
import com.gzyslczx.yslc.events.GuBbTokenEvent;
import com.gzyslczx.yslc.events.NoticeLauncherInitEvent;
import com.gzyslczx.yslc.presenter.HomeActPresenter;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LaunchActivity extends BaseActivity<ActivityLaunchBinding> {

    private final String TAG = "LaunchAct";
    private HomeActPresenter mPresenter;
    private int count=0;

    @Override
    void InitParentLayout() {
        TransStatusTool.translucent(this);
        EventBus.getDefault().register(this);
        mPresenter = new HomeActPresenter();
        mPresenter.RequestToken(this, null, this);
    }

    @Override
    void InitView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnLauncherTokenEvent(GuBbTokenEvent event){
        if (event.isFLAG()){
            //获取Token后初始化部分数据
            //请求Icon
            mPresenter.ContactGuBbAdv(this, this,null, 1);
            //请求Banner
            mPresenter.ContactGuBbAdv(this, this,null, 7);
            //请求盘前特供
            mPresenter.ContactSpecialSupport(this, this,null);
            //请求名师动态
            mPresenter.ContactTeacherDyn(this, this, null);
            //请求App设置
            mPresenter.ContactAppSet(this, this, null);

        }else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NoticeLoadInLauncher(NoticeLauncherInitEvent event){
        AddCountInit();
    }

    private synchronized void AddCountInit(){
        count = ++count;
        Log.d(TAG, String.format("初始化事件:%d", count));
        if (count>=5){
            startActivity(new Intent(LaunchActivity.this, HomeActivity.class));
            finish();
        }
    }

}