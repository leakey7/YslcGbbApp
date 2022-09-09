package com.gzyslczx.yslc;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.databinding.ActivitySearchBinding;
import com.gzyslczx.yslc.events.NoticeSearchNormalEvent;
import com.gzyslczx.yslc.events.SearchPageChangeEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.fragments.search.SearchHistoryFragment;
import com.gzyslczx.yslc.fragments.search.SearchMoreFragment;
import com.gzyslczx.yslc.fragments.search.SearchNormalFragment;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> implements View.OnClickListener {


    @Override
    void InitParentLayout() {
        mViewBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setmFragments(InitFragments());
        mViewBinding.SearchPagers.setAdapter(viewPagerAdapter);
        mViewBinding.SearchPagers.setUserInputEnabled(false);
        mViewBinding.SearchPagers.setOffscreenPageLimit(2);
        //点击回退
        mViewBinding.SearchBack.setOnClickListener(this::onClick);
        //点击搜索
        mViewBinding.SearchIng.setOnClickListener(this::onClick);
        //点击清空
        mViewBinding.SearchCancel.setOnClickListener(this::onClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 初始化Fragment
     * */
    private List<BaseFragment> InitFragments() {
        List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
        fragmentList = new ArrayList<BaseFragment>();
        //历史搜索
        fragmentList.add(new SearchHistoryFragment());
        //模糊搜索
        fragmentList.add(new SearchNormalFragment());
        //更多搜索
        fragmentList.add(new SearchMoreFragment());
        return fragmentList;
    }


    /*
    * 接收页面变更
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSearchPageChangeEvent(SearchPageChangeEvent event){
        switch (event.getType()){
            case 0:
                mViewBinding.SearchPagers.setCurrentItem(0, false);
                break;
            case 1:
                //模糊搜索
                mViewBinding.SearchContent.setText(event.getKeyWord());
                NoticeSearchNormalEvent normalEvent = new NoticeSearchNormalEvent(event.getKeyWord(), false);
                EventBus.getDefault().post(normalEvent);
                mViewBinding.SearchPagers.setCurrentItem(1, false);
                break;
            case 2:
                //更多股票
                mViewBinding.SearchContent.setText(event.getKeyWord());
                NoticeSearchNormalEvent moreStock = new NoticeSearchNormalEvent(event.getKeyWord(), true);
                moreStock.setMoreType(1);
                EventBus.getDefault().post(moreStock);
                mViewBinding.SearchPagers.setCurrentItem(2, false);
                break;
            case 3:
                //更多基金
                mViewBinding.SearchContent.setText(event.getKeyWord());
                NoticeSearchNormalEvent moreFund = new NoticeSearchNormalEvent(event.getKeyWord(), true);
                moreFund.setMoreType(2);
                EventBus.getDefault().post(moreFund);
                mViewBinding.SearchPagers.setCurrentItem(2, false);
                break;
            case 4:
                //更多相关内容
                mViewBinding.SearchContent.setText(event.getKeyWord());
                NoticeSearchNormalEvent moreAbout = new NoticeSearchNormalEvent(event.getKeyWord(), true);
                moreAbout.setMoreType(3);
                EventBus.getDefault().post(moreAbout);
                mViewBinding.SearchPagers.setCurrentItem(2, false);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SearchBack:
                if (mViewBinding.SearchPagers.getCurrentItem()==2){
                    mViewBinding.SearchPagers.setCurrentItem(1,false);
                }else {
                    finish();
                }
                break;
            case R.id.SearchIng:
                if (TextUtils.isEmpty(mViewBinding.SearchContent.getText())) {
                    Toast.makeText(SearchActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                } else {
                    NoticeSearchNormalEvent event = new NoticeSearchNormalEvent(mViewBinding.SearchContent.getText().toString().trim(), false);
                    EventBus.getDefault().post(event);
                    mViewBinding.SearchPagers.setCurrentItem(1, false);
                }
                break;
            case R.id.SearchCancel:
                mViewBinding.SearchContent.setText("");
                break;
        }
    }
}