package com.gzyslczx.yslc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.ActivityKlineBinding;
import com.gzyslczx.yslc.events.GuBbKLineTypeEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.fragments.kline.KLineFragment;
import com.gzyslczx.yslc.modes.response.ResKLineTypeObj;
import com.gzyslczx.yslc.presenter.KLinePresenter;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class KLineActivity extends BaseActivity<ActivityKlineBinding> implements View.OnClickListener {

    private final String TAG = "KLineAct";
    private KLinePresenter mPresenter;
    private TabLayoutMediator tabLayoutMediator;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityKlineBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        TransStatusTool.translucent(this);
        TransStatusTool.setStatusBarDarkMode(this);
    }


    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        int h = TransStatusTool.getStatusbarHeight(this) + DisplayTool.dp2px(this, 14);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mViewBinding.KLineToolBar.getLayoutParams();
        layoutParams.setMargins(0, h, 0, 0);
        mViewBinding.KLineToolBar.setLayoutParams(layoutParams);
        //点击回退
        SetupBackClicked();
        //点击Tab
        SetupTabClicked();
        //点击重试
        mViewBinding.ErrorNetLayout.noneBtn.setOnClickListener(this::onClick);
        mPresenter = new KLinePresenter();
        mViewBinding.KLineLoading.setVisibility(View.VISIBLE);
        mPresenter.RequestKLineType(this, this);
    }

    @Override
    protected void onDestroy() {
        if (tabLayoutMediator!=null) {
            tabLayoutMediator.detach();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    /*
    * 设置回退点击
    * */
    private void SetupBackClicked() {
        mViewBinding.KLineToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
     * 设置TAB点击事件
     * */
    private void SetupTabClicked(){
        mViewBinding.KLineTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position>=0) {
                    mViewBinding.KLinePager.setCurrentItem(position, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    /*
     * 初始化KLineFragment
     * */
    private List<BaseFragment> InitFragments(List<ResKLineTypeObj> objList){
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        for (ResKLineTypeObj obj : objList){
            Bundle bundle = new Bundle();
            bundle.putInt(KLineFragment.KLineKey, obj.getCateId());
            bundle.putInt(KLineFragment.KLineType, obj.getTypeInfo());
            KLineFragment kLineFragment =  new KLineFragment();
            kLineFragment.setArguments(bundle);
            fragments.add(kLineFragment);
        }
        return fragments;
    }

    /*
    * 接收K线秘籍类型
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnKLineTypeEvent(GuBbKLineTypeEvent event){
        Log.d(TAG, "接收到K线秘籍类型");
        if (event.isFlag()){
            TransStatusTool.setStatusBarDarkMode(this);
            mViewBinding.KLineParent.setBackground(ActivityCompat.getDrawable(this, R.drawable.klinebg));
            mViewBinding.KLineToolBar.setNavigationIcon(ActivityCompat.getDrawable(this, R.drawable.white_left));
            mViewBinding.KLineTitle.setTextColor(ActivityCompat.getColor(this, R.color.white));
            mViewBinding.ErrorNetLayout.getRoot().setVisibility(View.GONE);
            mViewBinding.KLinePager.setOffscreenPageLimit(event.getDataList().size());
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
            viewPagerAdapter.setmFragments(InitFragments(event.getDataList()));
            mViewBinding.KLinePager.setAdapter(viewPagerAdapter);
            tabLayoutMediator = new TabLayoutMediator(mViewBinding.KLineTab, mViewBinding.KLinePager, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    tab.setText(event.getDataList().get(position).getCateName());
                }
            });
            tabLayoutMediator.attach();
        }else {
            TransStatusTool.setStatusBarLightMode(this);
            mViewBinding.KLineParent.setBackgroundColor(ActivityCompat.getColor(this, R.color.white));
            mViewBinding.KLineToolBar.setNavigationIcon(ActivityCompat.getDrawable(this, R.drawable.black_left));
            mViewBinding.KLineTitle.setTextColor(ActivityCompat.getColor(this, R.color.black_333));
            mViewBinding.ErrorNetLayout.getRoot().setVisibility(View.VISIBLE);
            mViewBinding.ErrorNetLayout.noneText.setText(event.getError());
            mViewBinding.ErrorNetLayout.noneBtn.setText("点击重试");
        }
        mViewBinding.KLineLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.noneBtn){
            TransStatusTool.setStatusBarDarkMode(this);
            mViewBinding.KLineParent.setBackground(ActivityCompat.getDrawable(this, R.drawable.klinebg));
            mViewBinding.KLineToolBar.setNavigationIcon(ActivityCompat.getDrawable(this, R.drawable.white_left));
            mViewBinding.KLineTitle.setTextColor(ActivityCompat.getColor(this, R.color.white));
            mViewBinding.ErrorNetLayout.getRoot().setVisibility(View.GONE);
            mViewBinding.KLineLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestKLineType(this, this);
        }
    }
}