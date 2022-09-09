package com.gzyslczx.yslc.fragments.fundtong;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.Gson;
import com.gzyslczx.yslc.FundDetailActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.fundtong.FundFindLeftListAdapter;
import com.gzyslczx.yslc.adapters.fundtong.FundFindRightListAdapter;
import com.gzyslczx.yslc.adapters.fundtong.FundTongTradeLevelListAdapter;
import com.gzyslczx.yslc.adapters.fundtong.bean.FundTongTradLevelData;
import com.gzyslczx.yslc.databinding.FundTradeFragmentBinding;
import com.gzyslczx.yslc.events.FundTongFindDefaultEvent;
import com.gzyslczx.yslc.events.FundTongTradeIndexEvent;
import com.gzyslczx.yslc.events.FundTongTradeLevelEvent;
import com.gzyslczx.yslc.events.FundTradeListEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResFundTongTradeLevelObj;
import com.gzyslczx.yslc.modes.response.ResTradeIndexObj;
import com.gzyslczx.yslc.presenter.FundTongTradePres;
import com.gzyslczx.yslc.tools.customviews.LineChartView;
import com.gzyslczx.yslc.tools.customviews.ListPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FundTradeFragment extends BaseFragment<FundTradeFragmentBinding> implements View.OnClickListener, OnItemClickListener, OnLoadMoreListener {

    private final String TAG = "FundTongTrade";
    private FundTongTradePres mPresenter;
    private FundTongTradeLevelListAdapter mLevelListAdapter;
    private FundFindLeftListAdapter mLeftListAdapter;
    private FundFindRightListAdapter mRightListAdapter;
    private boolean LeftVScroll = false, RightVScroll = false;
    private ListPopup mLevelPopup;
    private LineChartView mChartView;
    private boolean isLoadMore=false;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = FundTradeFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mPresenter = new FundTongTradePres();
        SetupLeftRightScroll();
        SetupDownUpScroll();
        SetupLevelSelectClicked();
        mLevelListAdapter = new FundTongTradeLevelListAdapter(R.layout.trade_level_list_item);
        mLevelListAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.tradeFundLeftList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewBinding.tradeFundRightList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftListAdapter = new FundFindLeftListAdapter(R.layout.fund_find_left_list_item);
        mRightListAdapter = new FundFindRightListAdapter(R.layout.fund_find_right_list_item);
        mLeftListAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mRightListAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        SetupListItemClicked();
        mViewBinding.tradeFundLeftList.setAdapter(mLeftListAdapter);
        mViewBinding.tradeFundRightList.setAdapter(mRightListAdapter);
        mChartView = new LineChartView(getContext());
        mViewBinding.tradeChartFrame.addView(mChartView);
        mViewBinding.tradeChartFrame.post(new Runnable() {
            @Override
            public void run() {
                mLevelPopup = new ListPopup(getContext(), mLevelListAdapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 设置级别筛选点击
     * */
    private void SetupLevelSelectClicked() {
        mViewBinding.findA.setOnClickListener(this::onClick);
        mViewBinding.findB.setOnClickListener(this::onClick);
        mViewBinding.findC.setOnClickListener(this::onClick);
    }

    /*
     * 设置左右滑动联动
     * */
    private void SetupLeftRightScroll() {
        mViewBinding.tradeFundRightHeadScroll.setSubScroll(mViewBinding.tradeFundDataScroll);
        mViewBinding.tradeFundDataScroll.setSubScroll(mViewBinding.tradeFundRightHeadScroll);
    }

    /*
     *  设置上下滑动联动
     * */
    private void SetupDownUpScroll() {
        mViewBinding.tradeFundLeftList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    mViewBinding.tradeFundRightList.scrollBy(dx, dy);
                }
            }
        });
        mViewBinding.tradeFundRightList.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                    mViewBinding.tradeFundLeftList.scrollBy(dx, dy);
                }
            }
        });
    }

    /*
     * 列表点击事件
     * */
    private void SetupListItemClicked(){
        mLeftListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getContext(), FundDetailActivity.class);
                intent.putExtra(FundDetailActivity.CodeKey, mLeftListAdapter.getData().get(position).getFCode());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.findA:
                mPresenter.RequestLevelList(getContext(), this, 1, "");
                break;
            case R.id.findB:
                String codeA = (String) mViewBinding.findA.getTag();
                if (TextUtils.isEmpty(codeA)) {
                    Toast.makeText(getContext(), "请先筛选一级选项", Toast.LENGTH_SHORT).show();
                } else {
                    mPresenter.RequestLevelList(getContext(), this, 2, codeA);
                }
                break;
            case R.id.findC:
                String codeB = (String) mViewBinding.findB.getTag();
                if (TextUtils.isEmpty(codeB)) {
                    Toast.makeText(getContext(), "请先筛选二级选项", Toast.LENGTH_SHORT).show();
                } else {
                    mPresenter.RequestLevelList(getContext(), this, 3, codeB);
                }
                break;
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        String code = mLevelListAdapter.getData().get(position).getData().getCode();
        int level = mLevelListAdapter.getData().get(position).getData().getLevel();
        String name = mLevelListAdapter.getData().get(position).getData().getName();
        if (level == 1) {
            SetupSelectTextStyle(level, true, name, code);
            SetupSelectTextStyle(2, false, null, null);
            SetupSelectTextStyle(3, false, null, null);
        } else if (level == 2) {
            SetupSelectTextStyle(level, true, name, code);
            SetupSelectTextStyle(3, false, null, null);
        } else {
            SetupSelectTextStyle(level, true, name, code);
        }
        mLevelPopup.DismissListPopup();
    }

    @Override
    public void onLoadMore() {
        if (mLeftListAdapter.getLoadMoreModule().isLoading() && !mRightListAdapter.getLoadMoreModule().isLoading()) {
            mRightListAdapter.getLoadMoreModule().loadMoreToLoading();
        } else if (!mLeftListAdapter.getLoadMoreModule().isLoading() && mRightListAdapter.getLoadMoreModule().isLoading()) {
            mLeftListAdapter.getLoadMoreModule().loadMoreToLoading();
        }
        if (TextUtils.isEmpty((String) mViewBinding.findC.getTag())) {
            Toast.makeText(getContext(), "请先筛选各级选项", Toast.LENGTH_SHORT).show();
        } else if (!isLoadMore){
            isLoadMore=true;
            mPresenter.RequestFundFindList(getContext(), this, (String) mViewBinding.findC.getTag());
        }
    }

    /*
     * 设置筛选文字样式
     * */
    private void SetupSelectTextStyle(int level, boolean isSelected, String text, String code) {
        if (level == 1) {
            if (isSelected) {
                mViewBinding.findA.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF753E));
                mViewBinding.findA.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.trade_selected_shape));
                mViewBinding.findA.setText(text);
                mViewBinding.findA.setTag(code);
            } else {
                mViewBinding.findA.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
                mViewBinding.findA.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.trade_unselect_shape));
                mViewBinding.findA.setText(getString(R.string.FirstLevel));
                mViewBinding.findA.setTag(code);
            }
            return;
        }
        if (level == 2) {
            if (isSelected) {
                mViewBinding.findB.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF753E));
                mViewBinding.findB.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.trade_selected_shape));
                mViewBinding.findB.setText(text);
                mViewBinding.findB.setTag(code);
            } else {
                mViewBinding.findB.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
                mViewBinding.findB.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.trade_unselect_shape));
                mViewBinding.findB.setText(getString(R.string.SecondLevel));
                mViewBinding.findB.setTag(code);
            }
            return;
        }
        if (level == 3) {
            if (isSelected) {
                mViewBinding.findC.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_FF753E));
                mViewBinding.findC.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.trade_selected_shape));
                mViewBinding.findC.setText(text);
                mViewBinding.findC.setTag(code);
                mViewBinding.tradeLoading.setVisibility(View.VISIBLE);
                mPresenter.RequestTradeIndex(getContext(), this, code);
                mLeftListAdapter.getData().clear();
                mRightListAdapter.getData().clear();
                mViewBinding.tradeFundLeftList.removeAllViews();
                mViewBinding.tradeFundRightList.removeAllViews();
                mPresenter.RequestFundFindList(getContext(), this, code);
            } else {
                mViewBinding.findC.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_666));
                mViewBinding.findC.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.trade_unselect_shape));
                mViewBinding.findC.setText(getString(R.string.ThirdLevel));
                mViewBinding.findC.setTag(code);
            }
            return;
        }
    }

    /*
    * 接收默认行业数据
    * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OnFundTradeDefaultEvent(FundTongFindDefaultEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到默认行业数据");
            SetupSelectTextStyle(1, true, event.getData().getOneName(), event.getData().getCategoryOne());
            SetupSelectTextStyle(2, true, event.getData().getTwoName(), event.getData().getCategoryTwo());
            SetupSelectTextStyle(3, true, event.getData().getThreeName(), event.getData().getCategoryThree());
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 接收行业分级数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTradeLevelEvent(FundTongTradeLevelEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到行业分级数据");
            String code = null;
            if (event.getLevel() == 1) {
                code = (String) mViewBinding.findA.getTag();
            } else if (event.getLevel() == 2) {
                code = (String) mViewBinding.findB.getTag();
            } else {
                code = (String) mViewBinding.findC.getTag();
            }
            List<FundTongTradLevelData> data = new ArrayList<FundTongTradLevelData>();
            for (ResFundTongTradeLevelObj obj : event.getDataList()) {
                if (TextUtils.isEmpty(code)){
                    data.add(new FundTongTradLevelData(obj, false));
                }else {
                    if (code.equals(obj.getCode())) {
                        data.add(new FundTongTradLevelData(obj, true));
                    } else {
                        data.add(new FundTongTradLevelData(obj, false));
                    }
                }
            }
            mLevelListAdapter.setList(data);
            mLevelPopup.ShowListPopup(mViewBinding.findA);
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 接收行业指数数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTradeIndexEvent(FundTongTradeIndexEvent event){
        if (event.isFrag()){
            Log.d(TAG, "接收到行业指数数据");
            Log.d("行业指数", new Gson().toJson(event.getDataList()));
            float max = 0;
            mChartView.getChartDataList().clear();
            for (ResTradeIndexObj obj : event.getDataList()) {
                mChartView.AddData(obj.getClose(), obj.getPublishDate());
                max = Math.max(max, obj.getClose());
            }
            mChartView.setMaxV_ItemSize(max, event.getDataList().size());
            mChartView.isInitData();
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 接收行业列表数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTradeListEvent(FundTradeListEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到行业列表数据");
            if (event.isEnd()) {
                mLeftListAdapter.getLoadMoreModule().loadMoreEnd();
                mRightListAdapter.getLoadMoreModule().loadMoreEnd();
            } else {
                mLeftListAdapter.getLoadMoreModule().loadMoreComplete();
                mRightListAdapter.getLoadMoreModule().loadMoreComplete();
            }
            mLeftListAdapter.addData(event.getDataList());
            mRightListAdapter.addData(event.getDataList());
            if (isLoadMore){
                isLoadMore=false;
            }
        }else {
            mLeftListAdapter.getLoadMoreModule().loadMoreFail();
            mRightListAdapter.getLoadMoreModule().loadMoreFail();
            isLoadMore=false;
        }
        mViewBinding.tradeLoading.setVisibility(View.GONE);
    }


}
