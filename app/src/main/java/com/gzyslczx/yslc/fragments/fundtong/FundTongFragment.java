package com.gzyslczx.yslc.fragments.fundtong;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.FundDetailActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SearchActivity;
import com.gzyslczx.yslc.WebActivity;
import com.gzyslczx.yslc.adapters.ViewPagerAdapter;
import com.gzyslczx.yslc.adapters.fundtong.FundTongRankLeftAdapter;
import com.gzyslczx.yslc.adapters.fundtong.FundTongRankRightAdapter;
import com.gzyslczx.yslc.databinding.FundTongFragmentBinding;
import com.gzyslczx.yslc.events.FundTongHomeAdvEvent;
import com.gzyslczx.yslc.events.FundTongHomeIconTabEvent;
import com.gzyslczx.yslc.events.FundTongHomeRankEvent;
import com.gzyslczx.yslc.events.FundTongLoginEvent;
import com.gzyslczx.yslc.events.FundTongTokenEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResFundTongIconObj;
import com.gzyslczx.yslc.modes.response.ResFundTongIconTab;
import com.gzyslczx.yslc.presenter.FundTongPresenter;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.customviews.LoadingPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FundTongFragment extends BaseFragment<FundTongFragmentBinding> implements OnLoadMoreListener, View.OnClickListener, OnItemClickListener {

    private final String TAG = "FundTongFrag";
    private FundTongPresenter mPresenter;
    //    private FundTongIconAdapter mIconAdapter;
    private LoadingPopup loadingPopup;
    private int IconLocation = 0, TabLocation = 0;
    private FundTongRankLeftAdapter mLeftAdapter;
    private FundTongRankRightAdapter mRightAdapter;
    private boolean LeftVScroll = false, RightVScroll = false;
//    private NormalDialog mDialog;
//    private NormalDialogLayoutBinding mDialogBinding;
    private int sort = 1;
    List<ResFundTongIconObj> mFirstTabList;
    private boolean IsInit=false;
    private String[] ChartTabs = new String[]{"1个月", "3个月", "6个月", "1年", "2年", "3年", "5年"};
    private TabLayoutMediator tabLayoutMediator;


    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = FundTongFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
//        mIconAdapter = new FundTongIconAdapter(getContext());
//        mViewBinding.FundTongIcon.setAdapter(mIconAdapter);
        mLeftAdapter = new FundTongRankLeftAdapter(R.layout.fund_tong_leftlist_rank_item);
        mRightAdapter = new FundTongRankRightAdapter(R.layout.fund_tong_rightlist_rank_list);
        mViewBinding.FundTongLeftList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewBinding.FundTongRightList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mLeftAdapter.setOnItemClickListener(this::onItemClick);
        mRightAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mViewBinding.FundTongLeftList.setAdapter(mLeftAdapter);
        mViewBinding.FundTongRightList.setAdapter(mRightAdapter);
        InitHuShenChartPager();
        mPresenter = new FundTongPresenter();
//        SetupIconClicked();
        SetupFirstTabClicked();
        SetupTabClicked();
        SetupLeftRightScroll();
        SetupDownUpScroll();
//        SetupQuestionClicked();
        SetupSortClicked();
        mViewBinding.FundTongSelector.setOnClickListener(this::onClick);
        mViewBinding.FundTongSearchText.setOnClickListener(this::onClick);
        if (loadingPopup == null) {
            loadingPopup = new LoadingPopup();
        }
        if (SpTool.Instance(getContext()).IsGuBbLogin()) {
            if (mFirstTabList == null) {
                mViewBinding.FundTongFirstTab.post(new Runnable() {
                    @Override
                    public void run() {
                        loadingPopup.ShowLoading(getContext(), mViewBinding.getRoot(), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //点击重试
                                loadingPopup.DisRetry();
                                mPresenter.RequestFundTongToken(getContext(), FundTongFragment.this);
                            }
                        });
                        mPresenter.RequestFundTongToken(getContext(), FundTongFragment.this);
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (IsInit && !SpTool.Instance(getContext()).IsGuBbLogin()){
            getActivity().finish();
        }else if (!IsInit && !SpTool.Instance(getContext()).IsGuBbLogin()){
            IsInit=true;
            NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tabLayoutMediator.detach();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 初始化沪深三百对比
    * */
    private void InitHuShenChartPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.setmFragments(InitChartFragment());
        mViewBinding.FundTongChartPager.setAdapter(viewPagerAdapter);
        tabLayoutMediator = new TabLayoutMediator(mViewBinding.FundTongChatTab, mViewBinding.FundTongChartPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(getContext());
                textView.setWidth(DisplayTool.dp2px(getContext(), 40));
                textView.setHeight(DisplayTool.dp2px(getContext(), 24));
                textView.setText(ChartTabs[position]);
                textView.setTextSize(10);
                textView.setGravity(Gravity.CENTER);
                tab.setCustomView(textView);
            }
        });
        tabLayoutMediator.attach();
        //绑定页面切换监听
        mViewBinding.FundTongChartPager.registerOnPageChangeCallback(pageChangeCallback);
        //设置默认页是推荐页
        mViewBinding.FundTongChartPager.setCurrentItem(0, false);
        mViewBinding.FundTongChartPager.setUserInputEnabled(false);
        mViewBinding.FundTongChartPager.setOffscreenPageLimit(6);
    }

    /*
     * Tab切换事件
     * */
    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            int tabCount = mViewBinding.FundTongChatTab.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = mViewBinding.FundTongChatTab.getTabAt(i);
                TextView customView = (TextView) tab.getCustomView();
                if (tab.getPosition() == position) {
                    customView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    customView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.blue_radius_4_shape));
                } else {
                    customView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_999));
                    customView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gray_radius_4_shape));
                }
            }
        }
    };

    //初始化ChartFragment
    private List<BaseFragment> InitChartFragment(){
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        for (int i=1; i<=ChartTabs.length; i++){
            FundTongHSChatFragment fragment = new FundTongHSChatFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(FundTongHSChatFragment.ChartKey, i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        return fragments;
    }

    /*
     * 排行榜正倒序点击
     * */
    private void SetupSortClicked() {
        mViewBinding.FundTongRightScroll.FundTongDayImg.setOnClickListener(this::onClick);
        mViewBinding.FundTongRightScroll.FundTongWeekRiseImg.setOnClickListener(this::onClick);
        mViewBinding.FundTongRightScroll.FundTongMonthRiseImg.setOnClickListener(this::onClick);
        mViewBinding.FundTongRightScroll.FundTongQuarterRiseImg.setOnClickListener(this::onClick);
        mViewBinding.FundTongRightScroll.FundTongHalfYearRiseImg.setOnClickListener(this::onClick);
        mViewBinding.FundTongRightScroll.FundTongYearRise.setOnClickListener(this::onClick);
        mViewBinding.FundTongRightScroll.FundTongFValueImg.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.FundTongSearchText:
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.FundTongDayImg:
            case R.id.FundTongWeekRiseImg:
            case R.id.FundTongMonthRiseImg:
            case R.id.FundTongQuarterRiseImg:
            case R.id.FundTongHalfYearRiseImg:
            case R.id.FundTongYearRiseImg:
            case R.id.FundTongFValueImg:
                if (sort==1){
                    sort=0;
                }else {
                    sort=1;
                }
                ChangeSortImg();
                loadingPopup.Show(mViewBinding.getRoot());
                mPresenter.RequestFundRankList(getContext(), this, true, mFirstTabList.get(IconLocation).getAdId(),
                        mFirstTabList.get(IconLocation).getTList().get(TabLocation).getTId(), sort);
                break;
            case R.id.FundTongSelector:
                Intent webIntent = new Intent(getContext(), WebActivity.class);
                webIntent.putExtra(WebActivity.WebKey, (String) mViewBinding.FundTongSelector.getTag());
                startActivity(webIntent);
                break;

        }
    }

    /*
     * 排序指示图变化
     * */
    private void ChangeSortImg(){
        if (sort==1){
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.da_xiao)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongDayImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.da_xiao)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongWeekRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.da_xiao)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongMonthRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.da_xiao)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongQuarterRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.da_xiao)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongHalfYearRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.da_xiao)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongYearRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.da_xiao)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongFValueImg);
        }else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.xiao_da)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongDayImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.xiao_da)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongWeekRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.xiao_da)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongMonthRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.xiao_da)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongQuarterRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.xiao_da)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongHalfYearRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.xiao_da)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongYearRiseImg);
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.xiao_da)).fitCenter().into(mViewBinding.FundTongRightScroll.FundTongFValueImg);
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(getContext(), FundDetailActivity.class);
        intent.putExtra(FundDetailActivity.CodeKey, mLeftAdapter.getData().get(position).getFCode());
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        if (mLeftAdapter.getLoadMoreModule().isLoading() && !mRightAdapter.getLoadMoreModule().isLoading()) {
            mRightAdapter.getLoadMoreModule().loadMoreToLoading();
        } else if (!mLeftAdapter.getLoadMoreModule().isLoading() && mRightAdapter.getLoadMoreModule().isLoading()) {
            mLeftAdapter.getLoadMoreModule().loadMoreToLoading();
        }
        mPresenter.RequestFundRankList(getContext(), this, false, mFirstTabList.get(IconLocation).getAdId(),
                mFirstTabList.get(IconLocation).getTList().get(TabLocation).getTId(), sort);
    }

    /*
     * 设置左右滑动联动
     * */
    private void SetupLeftRightScroll() {
        mViewBinding.FundTongBtmScroll.setSubScroll(mViewBinding.FundTongDataScroll);
        mViewBinding.FundTongDataScroll.setSubScroll(mViewBinding.FundTongBtmScroll);
    }

    /*
     *  设置上下滑动联动
     * */
    private void SetupDownUpScroll() {
        mViewBinding.FundTongLeftList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_TOUCH_SCROLL) {
                    LeftVScroll = true;
                    RightVScroll = false;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!RightVScroll) {
                    mViewBinding.FundTongRightList.scrollBy(dx, dy);
                }
            }
        });
        mViewBinding.FundTongRightList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_TOUCH_SCROLL) {
                    RightVScroll = true;
                    LeftVScroll = false;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx == 0 && !LeftVScroll) {
                    mViewBinding.FundTongLeftList.scrollBy(dx, dy);
                }
            }
        });
    }

    /*
     * 设置FirstTab点击事件
     * */
    private void SetupFirstTabClicked(){
        mViewBinding.FundTongFirstTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                IconLocation = mViewBinding.FundTongFirstTab.getSelectedTabPosition();
                SetupTabItem(mFirstTabList.get(IconLocation).getTList());
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
     * 设置Tab
     * */
    private void SetupTabItem(List<ResFundTongIconTab> TabData) {
        if (TabData.size() > 0) {
            mViewBinding.FundTongTab.removeAllTabs();
            for (int i = 0; i < TabData.size(); i++) {
                TabLayout.Tab tab = mViewBinding.FundTongTab.newTab();
                tab.setText(TabData.get(i).getTypeName());
                mViewBinding.FundTongTab.addTab(tab, i);
            }
            mViewBinding.FundTongTab.selectTab(mViewBinding.FundTongTab.getTabAt(0));
        }
    }

    /*
     * 设置Tab点击
     * */
    private void SetupTabClicked() {
        mViewBinding.FundTongTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabLocation = mViewBinding.FundTongTab.getSelectedTabPosition();
                loadingPopup.Show(mViewBinding.getRoot());
                sort = 1;
                ChangeSortImg();
                mPresenter.RequestFundRankList(getContext(), FundTongFragment.this, true, mFirstTabList.get(IconLocation).getAdId(),
                        mFirstTabList.get(IconLocation).getTList().get(TabLocation).getTId(), sort);
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
     * 接收基金通Token
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTongTokenEvent(FundTongTokenEvent event){
        Log.d(TAG, "接收到基金通Token");
        if (event.isFLAG()){
            if (SpTool.Instance(getContext()).IsFundTongLogin()){
                mPresenter.RequestFundAdv(getContext(), this);
                mPresenter.RequestFundIconTab(getContext(), this);
                mPresenter.RequestDefaultFind(getContext(), this);
            }else {
                mPresenter.RequestFundTongLogin(getContext(), this);
            }
        }else {
            Toast.makeText(getContext(), event.getERROR(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 接收基金通登录
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTongLoginEvent(FundTongLoginEvent event){
        Log.d(TAG, "接收到基金通登录");
        if (event.isFlag()){
            mPresenter.RequestFundAdv(getContext(), this);
            mPresenter.RequestFundIconTab(getContext(), this);
            mPresenter.RequestDefaultFind(getContext(), this);
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 接收基金通首页广告图
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTongHomeAdvEvent(FundTongHomeAdvEvent event){
        Log.d(TAG, "接收到基金通首页广告图");
        if (event.isFlag()){
            Glide.with(getContext()).load(event.getData().getResultObj().get(0).getImg()).fitCenter().into(mViewBinding.FundTongSelector);
            mViewBinding.FundTongSelector.setTag(event.getData().getResultObj().get(0).getAppUrl());
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 接收基金通首页IconTab
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTongHomeIconTabEvent(FundTongHomeIconTabEvent event){
        Log.d(TAG, "接收到基金通首页IconTab");
        if (event.isFlag()){
            mFirstTabList = event.getDataList();
            SetupFirstTab(event.getDataList());
            IconLocation = 0;
            SetupTabItem(event.getDataList().get(0).getTList());
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 初始化Tab
    * */
    private void SetupFirstTab(List<ResFundTongIconObj> FirstTabData){
        if (FirstTabData.size() > 0) {
            mViewBinding.FundTongFirstTab.removeAllTabs();
            for (int i = 0; i < FirstTabData.size(); i++) {
                TabLayout.Tab tab = mViewBinding.FundTongFirstTab.newTab();
                tab.setText(FirstTabData.get(i).getTitle());
                mViewBinding.FundTongFirstTab.addTab(tab, i);
            }
            mViewBinding.FundTongFirstTab.selectTab(mViewBinding.FundTongFirstTab.getTabAt(0));
        }
    }

    /*
     * 接收基金通首页排行榜
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTongHomeRankEvent(FundTongHomeRankEvent event){
        Log.d(TAG, "接收到基金通首页排行榜");
        if (event.isFlag()){
            if (event.isEnd()) {
                mLeftAdapter.getLoadMoreModule().loadMoreEnd();
                mRightAdapter.getLoadMoreModule().loadMoreEnd();
            } else {
                mLeftAdapter.getLoadMoreModule().loadMoreComplete();
                mRightAdapter.getLoadMoreModule().loadMoreComplete();
            }
            if (event.isInit()) {
                mLeftAdapter.setList(event.getDataList());
                mRightAdapter.setList(event.getDataList());
            } else {
                mLeftAdapter.addData(event.getDataList());
                mRightAdapter.addData(event.getDataList());
            }
        }else {
            mLeftAdapter.getLoadMoreModule().loadMoreFail();
            mRightAdapter.getLoadMoreModule().loadMoreFail();
        }
        if (loadingPopup != null) {
            loadingPopup.Dismiss();
        }
    }

    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        if (event.isLogin()){
            loadingPopup.Show(mViewBinding.getRoot());
            mPresenter.RequestFundTongToken(getContext(), FundTongFragment.this);
        }
    }

}
