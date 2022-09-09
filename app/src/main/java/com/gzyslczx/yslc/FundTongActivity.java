package com.gzyslczx.yslc;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.material.navigation.NavigationBarView;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.ActivityFundTongBinding;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.fragments.fundtong.FundFindFragment;
import com.gzyslczx.yslc.fragments.fundtong.FundOptionFragment;
import com.gzyslczx.yslc.fragments.fundtong.FundTongFragment;
import com.gzyslczx.yslc.tools.TransStatusTool;

import java.util.ArrayList;
import java.util.List;

public class FundTongActivity extends BaseActivity<ActivityFundTongBinding> {

    private final String TAG = "FundTongAct";

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityFundTongBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.main_blue));
        TransStatusTool.setStatusBarDarkMode(this);
    }

    @Override
    void InitView() {
        mViewBinding.FundTongBtmBar.setItemIconTintList(null);
        mViewBinding.FundTongBtmBar.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        SetupBackClicked();
        SetupBtmBarClicked();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setmFragments(InitFragment());
        mViewBinding.FundTongPager.setAdapter(viewPagerAdapter);
        mViewBinding.FundTongPager.setOffscreenPageLimit(1);
        mViewBinding.FundTongPager.setUserInputEnabled(false);
    }



    /*
     * 初始化Fragment
     * */
    private List<BaseFragment> InitFragment(){
        List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
        fragmentList.add(new FundTongFragment());
        fragmentList.add(new FundFindFragment());
        fragmentList.add(new FundOptionFragment());
        return fragmentList;
    }


    /*
     * 设置底部Bar点击事件
     * */
    private void SetupBtmBarClicked() {
        mViewBinding.FundTongBtmBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.FundTongFind) {
                    mViewBinding.FundTongPager.setCurrentItem(1, false);
                }else if (item.getItemId() == R.id.FundTongHome){
                    mViewBinding.FundTongPager.setCurrentItem(0, false);
                }else {
                    mViewBinding.FundTongPager.setCurrentItem(2, false);
                }
                return true;
            }
        });
    }


    /*
     * 设置回退点击
     * */
    private void SetupBackClicked() {
        mViewBinding.FundTongLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}