package com.gzyslczx.yslc.fragments.home;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.LivingFragmentBinding;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.MainFragmentPresenter;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import java.util.ArrayList;
import java.util.List;

public class LivingFragment extends BaseFragment<LivingFragmentBinding> {

    private final String TAG = "LivingFragment";
    private TabLayoutMediator mTabLayoutMediator;
    private String[] mTabs = new String[]{"视频", "小视频", "直播"};
    private final int defTabTextSize = 16;
    private MainFragmentPresenter mPresenter;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = LivingFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mViewBinding.LiveFragTab.getLayoutParams();
        layoutParams.topMargin = TransStatusTool.getStatusbarHeight(getContext());
        mViewBinding.LiveFragTab.setLayoutParams(layoutParams);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setmFragments(InitFragments());
        mViewBinding.LiveFragPages.setAdapter(viewPagerAdapter);
        mViewBinding.LiveFragPages.setOffscreenPageLimit(mTabs.length-1);
        InitTab();
        mPresenter = new MainFragmentPresenter();
//        mainFragPres.ReqTeacherLiveList();
    }

    @Override
    public void onDestroy() {
        mViewBinding.LiveFragPages.unregisterOnPageChangeCallback(pageChangeCallback);
        if (mTabLayoutMediator!=null){
            mTabLayoutMediator.detach();
        }
        super.onDestroy();
    }

    /*
     * 初始化Tab
     * */
    private void InitTab() {
        //自定义TabItem
        mTabLayoutMediator = new TabLayoutMediator(mViewBinding.LiveFragTab, mViewBinding.LiveFragPages, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView tabView = new TextView(getContext());
                tabView.setTextSize(defTabTextSize);
                tabView.setText(mTabs[position]);
                tabView.setGravity(Gravity.CENTER);
                tab.setCustomView(tabView);
            }
        });
        mTabLayoutMediator.attach();
        //绑定页面切换监听
        mViewBinding.LiveFragPages.registerOnPageChangeCallback(pageChangeCallback);
        //设置默认页是推荐页
        mViewBinding.LiveFragPages.setCurrentItem(2);
    }
    /*
     * Tab切换事件
     * */
    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            int tabCount = mViewBinding.LiveFragTab.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = mViewBinding.LiveFragTab.getTabAt(i);
                TextView customView = (TextView) tab.getCustomView();
                if (tab.getPosition() == position) {
                    customView.setTextColor(ContextCompat.getColor(getContext(), R.color.black_333));
                } else {
                    customView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_999));
                }
            }
        }

    };

    /*
     * 添加Fragment
     * */
    private List<BaseFragment> InitFragments() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        //视频
        MainVideoFragment HVideoFragment = new MainVideoFragment();
        Bundle HVBundle = new Bundle();
        HVBundle.putInt(MainVideoFragment.VideoTypeKey, 2);
        HVideoFragment.setArguments(HVBundle);
        fragments.add(HVideoFragment);
        //小视频
        fragments.add(new SmallVideoFragment());
        //直播
        if (!SpTool.Instance(getContext()).GetHiddenLiveOrder()) {
            fragments.add(new MainLivingFragment());
        }
        return fragments;
    }

}
