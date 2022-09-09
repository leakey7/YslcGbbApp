package com.gzyslczx.yslc.fragments.fundtong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.FundFindFragmentBinding;
import com.gzyslczx.yslc.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class FundFindFragment extends BaseFragment<FundFindFragmentBinding> {

    private TabLayoutMediator tabLayoutMediator;
    private final String[] Tabs = new String[]{"行业", "概念"};

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = FundFindFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setmFragments(InitFragment());
        mViewBinding.FundFindPager.setAdapter(viewPagerAdapter);
        SetupTabPager();
        mViewBinding.FundFindPager.setUserInputEnabled(false);
        mViewBinding.FundFindPager.setOffscreenPageLimit(1);
    }

    @Override
    public void onDestroyView() {
        tabLayoutMediator.detach();
        super.onDestroyView();
    }

    /*
     * 初始化Fragment
     * */
    private List<BaseFragment> InitFragment(){
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        fragments.add(new FundTradeFragment());
        fragments.add(new FundConceptFragment());
        return fragments;
    }

    /*
     * 设置Tab和Pager
     * */
    private void SetupTabPager(){
        tabLayoutMediator = new TabLayoutMediator(mViewBinding.FundFindTab, mViewBinding.FundFindPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(Tabs[position]);
            }
        });
        tabLayoutMediator.attach();
    }

}
