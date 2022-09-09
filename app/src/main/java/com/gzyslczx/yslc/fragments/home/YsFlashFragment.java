package com.gzyslczx.yslc.fragments.home;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.YsFlashFragmentBinding;
import com.gzyslczx.yslc.events.NoticeMainRefreshEvent;
import com.gzyslczx.yslc.events.NoticeYSFlashRefreshEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.tools.DisplayTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class YsFlashFragment extends BaseFragment<YsFlashFragmentBinding> {

    private final String TAG = "YsFlashFragment";
    private TabLayoutMediator tabLayoutMediator;

    private final String[] TAB = new String[]{"全部", "综合", "股票", "理财", "债券", "新产业", "商品外汇"};

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = YsFlashFragmentBinding.inflate(inflater, container, false);
        InitView();

    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setmFragments(InitFragments());
        mViewBinding.FlashPager.setAdapter(viewPagerAdapter);
        mViewBinding.FlashPager.setUserInputEnabled(false);
        mViewBinding.FlashPager.setOffscreenPageLimit(TAB.length);
        tabLayoutMediator = new TabLayoutMediator(mViewBinding.FlashTab, mViewBinding.FlashPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(getContext());
                int h = DisplayTool.dp2px(getContext(), 4);
                int v = DisplayTool.dp2px(getContext(), 2);
                textView.setPadding(h, v, h, v);
                textView.setTextSize(12);
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.flash_tab_selector));
                textView.setText(TAB[position]);
                tab.setCustomView(textView);
            }
        });
        tabLayoutMediator.attach();
        mViewBinding.FlashPager.registerOnPageChangeCallback(onPageChangeCallback);
        mViewBinding.FlashPager.setCurrentItem(0);
    }
    //自定义Tab样式
    private ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            int tabCount = mViewBinding.FlashTab.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = mViewBinding.FlashTab.getTabAt(i);
                TextView customView = (TextView) tab.getCustomView();
                if (tab.getPosition() == position) {
                    customView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    customView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_999));
                }
            }
        }
    };

    /*
     * 初始化Fragment集
     * */
    private List<BaseFragment> InitFragments(){
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        for (int i=-1; i<=5; i++){
            YsFlashListFragment listPageFrag = new YsFlashListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(YsFlashListFragment.YsFlashKey, i);
            listPageFrag.setArguments(bundle);
            fragments.add(listPageFrag);
        }
        return fragments;
    }

    /*
     * 接收越声快讯页面刷新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeRefresh(NoticeMainRefreshEvent event){
        if (event.getShowPage()==2){
            Log.d(TAG, "接收到越声快讯页刷新");
            NoticeYSFlashRefreshEvent refreshEvent = new NoticeYSFlashRefreshEvent(mViewBinding.FlashPager.getCurrentItem()-1);
            EventBus.getDefault().post(refreshEvent);
        }
    }
}
