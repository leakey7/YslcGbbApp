package com.gzyslczx.yslc.fragments.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.JustViewpagerBinding;
import com.gzyslczx.yslc.events.MainFocusPageChangeEvent;
import com.gzyslczx.yslc.events.NoticeFocusPageRefreshEvent;
import com.gzyslczx.yslc.events.NoticeMainRefreshEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainFocusFragment extends BaseFragment<JustViewpagerBinding> {

    private final String TAG = "MainFocusFrag";
    private List<BaseFragment> fragments;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = JustViewpagerBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        InitFragments();
        viewPagerAdapter.setmFragments(fragments);
        mViewBinding.JustPager.setAdapter(viewPagerAdapter);
        mViewBinding.JustPager.setUserInputEnabled(false);
        mViewBinding.JustPager.setOffscreenPageLimit(1);
    }

    /*
    * 初始化Fragments
    * */
    private void InitFragments(){
        fragments = new ArrayList<BaseFragment>();
        fragments.add(new MainMyFocusFragment());
        fragments.add(new MainMoreFocusFragment());
    }

    /*
    * 接收关注Fragment页面切换
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void PageChangeEvent(MainFocusPageChangeEvent event){
        Log.d(TAG, "接收到关注页切换");
        mViewBinding.JustPager.setCurrentItem(event.getPageNum(), false);
    }

    /*
     * 接收关注页面刷新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeRefresh(NoticeMainRefreshEvent event){
        if (event.getShowPage()==0){
            Log.d(TAG, "接收到关注页刷新");
            NoticeFocusPageRefreshEvent refreshEvent = new NoticeFocusPageRefreshEvent(mViewBinding.JustPager.getCurrentItem());
            EventBus.getDefault().post(refreshEvent);
        }
    }

}
