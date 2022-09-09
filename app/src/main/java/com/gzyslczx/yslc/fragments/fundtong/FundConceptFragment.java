package com.gzyslczx.yslc.fragments.fundtong;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.gzyslczx.yslc.ConceptSelectorActivity;
import com.gzyslczx.yslc.FundDetailActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.fundtong.FundFindLeftListAdapter;
import com.gzyslczx.yslc.adapters.fundtong.FundFindRightListAdapter;
import com.gzyslczx.yslc.databinding.FundConceptFragmentBinding;
import com.gzyslczx.yslc.events.FundConceptIndexEvent;
import com.gzyslczx.yslc.events.FundConceptListEvent;
import com.gzyslczx.yslc.events.FundTongFindDefaultEvent;
import com.gzyslczx.yslc.events.NoticeConceptUpdateEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResTradeIndexObj;
import com.gzyslczx.yslc.presenter.FundTongConceptPres;
import com.gzyslczx.yslc.tools.customviews.LineChartView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FundConceptFragment extends BaseFragment<FundConceptFragmentBinding> implements OnLoadMoreListener, OnItemClickListener, View.OnClickListener {

    private final String TAG = "ConceptFrag";
    private FundTongConceptPres mPresenter;
    private FundFindLeftListAdapter mLeftListAdapter;
    private FundFindRightListAdapter mRightListAdapter;
    private boolean LeftVScroll = false, RightVScroll = false;
    public static final int ResultCde = 500;
    public static final String CodeKey = "ConceptCode";
    private LineChartView mChartView;
    private boolean isInitList = true;
    private boolean isLoadMore =false;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = FundConceptFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        mPresenter = new FundTongConceptPres();
        EventBus.getDefault().register(this);
        mViewBinding.conceptFundLeftList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewBinding.conceptFundRightList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftListAdapter = new FundFindLeftListAdapter(R.layout.fund_find_left_list_item);
        mRightListAdapter = new FundFindRightListAdapter(R.layout.fund_find_right_list_item);
        mLeftListAdapter.setOnItemClickListener(this::onItemClick);
        mLeftListAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mRightListAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mViewBinding.conceptFundLeftList.setAdapter(mLeftListAdapter);
        mViewBinding.conceptFundRightList.setAdapter(mRightListAdapter);
        mChartView = new LineChartView(getContext());
        mViewBinding.conceptChartFrame.addView(mChartView);
        SetupLeftRightScroll();
        SetupDownUpScroll();
        mViewBinding.conceptSelector.setOnClickListener(this::onClick);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
     * 设置左右滑动联动
     * */
    private void SetupLeftRightScroll() {
        mViewBinding.conceptFundRightHeadScroll.setSubScroll(mViewBinding.conceptFundDataScroll);
        mViewBinding.conceptFundDataScroll.setSubScroll(mViewBinding.conceptFundRightHeadScroll);
    }

    /*
     *  设置上下滑动联动
     * */
    private void SetupDownUpScroll() {
        mViewBinding.conceptFundLeftList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    mViewBinding.conceptFundRightList.scrollBy(dx, dy);
                }
            }
        });
        mViewBinding.conceptFundRightList.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                    mViewBinding.conceptFundLeftList.scrollBy(dx, dy);
                }
            }
        });
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(getContext(), FundDetailActivity.class);
        intent.putExtra(FundDetailActivity.CodeKey, mLeftListAdapter.getData().get(position).getFCode());
        startActivity(intent);
    }

    @Override
    public void onLoadMore() {
        if (!isLoadMore){
            isLoadMore=true;
            mPresenter.RequestFundFindList(getContext(), this, (String) mViewBinding.conceptSelectName.getTag());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.conceptSelector){
            Intent intent = new Intent(getContext(), ConceptSelectorActivity.class);
            startActivity(intent);
        }
    }

    /*
     * 接收默认概念数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OnFundConceptDefaultEvent(FundTongFindDefaultEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到默行概念数据");
            mViewBinding.conceptLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestConceptIndex(getContext(), this, event.getData().getGnCode());
            mPresenter.RequestFundFindList(getContext(), this, event.getData().getGnCode());
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 接收概念指数数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundConceptIndexEvent(FundConceptIndexEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到概念指数数据");
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
    * 接收概念列表数据
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundConceptListEvent(FundConceptListEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到概念列表数据");
            if (isInitList){
                mLeftListAdapter.setList(event.getDataList());
                mRightListAdapter.setList(event.getDataList());
                isInitList=false;
            }else if (isLoadMore){
                isLoadMore = false;
                mLeftListAdapter.addData(event.getDataList());
                mRightListAdapter.addData(event.getDataList());
                if (event.isEnd()) {
                    mLeftListAdapter.getLoadMoreModule().loadMoreEnd();
                    mRightListAdapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    mLeftListAdapter.getLoadMoreModule().loadMoreComplete();
                    mRightListAdapter.getLoadMoreModule().loadMoreComplete();
                }
            }
            mViewBinding.conceptSelectName.setText(event.getDataList().get(0).getGnName());
            mViewBinding.conceptSelectName.setTag(event.getDataList().get(0).getCode());
        }else {
            mLeftListAdapter.getLoadMoreModule().loadMoreFail();
            mRightListAdapter.getLoadMoreModule().loadMoreFail();
            isLoadMore = false;
        }
        mViewBinding.conceptLoading.setVisibility(View.GONE);
    }

    /*
     * 接收更新概念数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OnFundConceptUpdateEvent(NoticeConceptUpdateEvent event){
        mPresenter.setCurrentPage(1);
        isInitList = true;
        mViewBinding.conceptLoading.setVisibility(View.VISIBLE);
        mPresenter.RequestConceptIndex(getContext(), this, event.getCode());
        mPresenter.RequestFundFindList(getContext(), this, event.getCode());
    }

}
