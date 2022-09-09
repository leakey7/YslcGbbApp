package com.gzyslczx.yslc.fragments.option;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SearchActivity;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.OptionFragLayoutBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.NoticeOptionPageChangeEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class OptionFragment extends BaseFragment<OptionFragLayoutBinding> {

    private final String TAG = "OptionFragment";

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = OptionFragLayoutBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mViewBinding.OptionToolBar.getLayoutParams();
        layoutParams.setMargins(0, TransStatusTool.getStatusbarHeight(getContext()), 0, 0);
        mViewBinding.OptionToolBar.setLayoutParams(layoutParams);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setmFragments(InitFragments());
        mViewBinding.OptionPagers.setAdapter(viewPagerAdapter);
        mViewBinding.OptionPagers.setUserInputEnabled(false);
        //点击搜索
        SetupSearchClicked();
        if (SpTool.Instance(getContext()).IsGuBbLogin()){
            mViewBinding.OptionPagers.setCurrentItem(0,false);
        }else {
            mViewBinding.OptionPagers.setCurrentItem(1, false);
        }
        Log.d(TAG, "当前页:"+mViewBinding.OptionPagers.getCurrentItem());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 初始化Fragments
    * */
    private List<BaseFragment> InitFragments(){
        List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
        fragmentList.add(new MyOptionFragment());
        fragmentList.add(new DefaultOptionFragment());
        return fragmentList;
    }

    /*
     * 跳转搜索点击
     * */
    private void SetupSearchClicked(){
        mViewBinding.OptionToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.black_search){
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    /*
    * 接收通知切换自选页面
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeOptionChangePageEvent(NoticeOptionPageChangeEvent event){
        Log.d(TAG, "接收到通知切换自选页面:"+event.getCurrentPage());
        mViewBinding.OptionPagers.setCurrentItem(event.getCurrentPage(), false);
    }

    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        if (event.isLogin()){
            //已经登录，切换到
            mViewBinding.OptionPagers.setCurrentItem(0);
        }else {
            mViewBinding.OptionPagers.setCurrentItem(1);
        }
    }

}
