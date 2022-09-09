package com.gzyslczx.yslc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.ActivityHomeBinding;
import com.gzyslczx.yslc.events.GuBbHomeChangePageEvent;
import com.gzyslczx.yslc.events.GuBbTokenOnPushEvent;
import com.gzyslczx.yslc.events.MainPageStateEvent;
import com.gzyslczx.yslc.events.NoticeBtmBarHidden;
import com.gzyslczx.yslc.events.NoticeHiddenBtmBarEvent;
import com.gzyslczx.yslc.events.UpdatePushEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.fragments.home.LivingFragment;
import com.gzyslczx.yslc.fragments.home.MainFragment;
import com.gzyslczx.yslc.fragments.home.MineFragment;
import com.gzyslczx.yslc.fragments.home.VipFragment;
import com.gzyslczx.yslc.fragments.option.OptionFragment;
import com.gzyslczx.yslc.presenter.HomePresenter;
import com.gzyslczx.yslc.tools.SecretCodeTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.jigunagpush.ResponsePush;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    private final String TAG = "HomeAct";
    public static final String PushKey = "ToPush";
    public static final String PushTitle = "ToPushTitle";
    /*
    * 0：展开 1：收缩
    * */
    private int MainFragmentState = 0;
    private List<BaseFragment> mFragments;

    private HomePresenter mPresenter;
    private boolean isInit = true;
    private boolean isHiddenBtmBar = false;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        TransStatusTool.translucent(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.HomeActBtmBar.setItemIconTintList(null);
        mViewBinding.HomeActBtmBar.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        mViewBinding.HomeActPager.setUserInputEnabled(false);
        mViewBinding.HomeActPager.setOffscreenPageLimit(4);
        viewPagerAdapter = new ViewPagerAdapter(this);
        InitFragments();
        viewPagerAdapter.setmFragments(mFragments);
        SetupBtmBarOnClick();
        mPresenter = new HomePresenter();
        OnPushIntent(getIntent());
        if (!TextUtils.isEmpty(getIntent().getStringExtra(PushKey)) && !TextUtils.isEmpty(getIntent().getStringExtra(PushTitle))) {
            String push = getIntent().getStringExtra(PushKey);
            String title = getIntent().getStringExtra(PushTitle);
            Intent intent = SecretCodeTool.AnalysisSecretCode(this, push);
            if (intent != null) {
                mPresenter.RequestToken(this, null, this, intent);
            }
            mPresenter.RequestUpdatePush(this, this, push, title);
        }else {
            mPresenter.RequestToken(this, null, this, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInit) {
            if (SpTool.Instance(this).GetHiddenLiveOrder()) {
                mViewBinding.HomeActBtmBar.getMenu().findItem(R.id.BtmBarLiving).setTitle("名师");
            } else {
                mViewBinding.HomeActBtmBar.getMenu().findItem(R.id.BtmBarLiving).setTitle("直播");
            }
            isInit = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        OnPushIntent(intent);
        if (!TextUtils.isEmpty(intent.getStringExtra(PushKey)) && !TextUtils.isEmpty(intent.getStringExtra(PushTitle))) {
            String push = getIntent().getStringExtra(PushKey);
            String title = getIntent().getStringExtra(PushTitle);
            Intent pIntent = SecretCodeTool.AnalysisSecretCode(this, push);
            if (pIntent != null) {
                mPresenter.RequestToken(this, null, this, pIntent);
            }
            mPresenter.RequestUpdatePush(this, this, push, title);
        }
    }

    /*
    * 初始化Fragment
    * */
    private void InitFragments(){
        mFragments = new ArrayList<BaseFragment>();
        mFragments.add(new MainFragment());
        mFragments.add(new LivingFragment());
        mFragments.add(new VipFragment());
        mFragments.add(new OptionFragment());
        mFragments.add(new MineFragment());
    }

    /*
    * 接受主页Fragment滚动状态改变状态栏样式
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeMainFragmentStatusBar(MainPageStateEvent event){
        Log.d(TAG, "接收到首页Fragment滚动变化");
        MainFragmentState = event.getState();
    }

    /*
    * 设置底部Bar点击事件
    * */
    private void SetupBtmBarOnClick(){
        mViewBinding.HomeActBtmBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.BtmBarHome:
                        mViewBinding.HomeActPager.setCurrentItem(0, false);
                        if (MainFragmentState==0){
                            TransStatusTool.setStatusBarDarkMode(HomeActivity.this);
                        }else {
                            TransStatusTool.setStatusBarLightMode(HomeActivity.this);
                        }
                        break;
                    case R.id.BtmBarLiving:
                        TransStatusTool.setStatusBarLightMode(HomeActivity.this);
                        mViewBinding.HomeActPager.setCurrentItem(1, false);
                        break;
                    case R.id.BtmBarVip:
                        TransStatusTool.setStatusBarLightMode(HomeActivity.this);
                        mViewBinding.HomeActPager.setCurrentItem(2, false);
                        break;
                    case R.id.BtmBarOptional:
                        TransStatusTool.setStatusBarLightMode(HomeActivity.this);
                        mViewBinding.HomeActPager.setCurrentItem(3, false);
                        break;
                    case R.id.BtmBarMine:
                        TransStatusTool.setStatusBarLightMode(HomeActivity.this);
                        mViewBinding.HomeActPager.setCurrentItem(4, false);
                        break;
                }
                return true;
            }
        });
    }

    /*
    * 接收切换页面
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangePageEvent(GuBbHomeChangePageEvent event){
        Log.d(TAG, "接收到切换Home子页面");
        switch (event.getCurrentPage()){
            case 0:
                mViewBinding.HomeActBtmBar.setSelectedItemId(R.id.BtmBarHome);
                break;
            case 1:
                mViewBinding.HomeActBtmBar.setSelectedItemId(R.id.BtmBarLiving);
                break;
            case 2:
                mViewBinding.HomeActBtmBar.setSelectedItemId(R.id.BtmBarVip);
                break;
            case 3:
                mViewBinding.HomeActBtmBar.setSelectedItemId(R.id.BtmBarOptional);
                break;
            case 4:
                mViewBinding.HomeActBtmBar.setSelectedItemId(R.id.BtmBarMine);
                break;
        }
    }

    /*
    * 接收推送更新
    * */
    public void OnPushUpdateEvent(UpdatePushEvent event){
        Log.d(TAG, String.format("IsSuccess:%b;Msg=$s", event.isFlag(), event.getMsg()));
    }

    /*
    * 接收推送Intent
    * */
    private void OnPushIntent(Intent pushIntent){
        if(getIntent()!=null){
            //华为通道
            if (pushIntent.getData()!=null){
                //华为通道
                String HUAWEI = pushIntent.getData().toString();
                ResponsePush res = new Gson().fromJson(HUAWEI, ResponsePush.class);
                if (res.getN_extras()!=null){
                    Intent intent = SecretCodeTool.AnalysisSecretCode(this, res.getN_extras().getUrl());
                    if (intent!=null){
                        mPresenter.RequestToken(this, null, this, intent);
                        mPresenter.RequestUpdatePush(this, this, res.getN_extras().getUrl(), res.getN_title());
                    }
                }
            }
            if (pushIntent.getExtras()!=null){
                //小米、vivo、OPPO、FCM、魅族、极光通道使用
                String NO_HUAWEI = pushIntent.getExtras().getString("JMessageExtra");
                if (!TextUtils.isEmpty(NO_HUAWEI)) {
                    ResponsePush res = new Gson().fromJson(NO_HUAWEI, ResponsePush.class);
                    if (res.getN_extras()!=null){
                        Intent intent = SecretCodeTool.AnalysisSecretCode(this, res.getN_extras().getUrl());
                        if (intent!=null){
                            mPresenter.RequestToken(this, null, this, intent);
                            mPresenter.RequestUpdatePush(this, this, res.getN_extras().getUrl(), res.getN_title());
                        }
                    }
                }
            }
        }
    }

    /*
    * 接收隐藏底部导航栏
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnHiddenBtmBarEvent(NoticeHiddenBtmBarEvent event){
        if (event.isHidden()){
            mViewBinding.HomeActBtmBar.setVisibility(View.GONE);
        }else {
            if (!isHiddenBtmBar) {
                mViewBinding.HomeActBtmBar.setVisibility(View.VISIBLE);
            }
            mViewBinding.HomeActBtmBar.setSelectedItemId(R.id.BtmBarHome);
            mViewBinding.HomeActBtmBar.setSelectedItemId(R.id.BtmBarVip);
        }
    }


    /*
     * 接收导航栏显示或隐藏
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnNoticeHiddenBtmBar(NoticeBtmBarHidden event){
        //更新登录显示
        Log.d(TAG, "接收到导航栏显示或隐藏");
        if (event.getState()==0){
            mViewBinding.HomeActBtmBar.setVisibility(View.VISIBLE);
            isHiddenBtmBar = false;
        }else {
            mViewBinding.HomeActBtmBar.setVisibility(View.GONE);
            isHiddenBtmBar = true;
        }
    }

    /*
    * 点击虚拟导航栏回退键
    * */

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setMessage("确定退出并关闭APP吗？");
            builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /*
    * Token过期更新后跳转推送
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTokenPush(GuBbTokenOnPushEvent event){
        if (event.isFLAG()){
            mViewBinding.HomeActPager.setAdapter(viewPagerAdapter);
            if (event.getIntent()!=null) {
                startActivity(event.getIntent());
            }
        }else {
            Log.d(TAG, event.getERROR());
        }
    }

}