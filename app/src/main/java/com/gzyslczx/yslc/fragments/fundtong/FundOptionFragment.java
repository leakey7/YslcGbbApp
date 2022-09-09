package com.gzyslczx.yslc.fragments.fundtong;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gzyslczx.yslc.FundDetailActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SearchActivity;
import com.gzyslczx.yslc.adapters.fundtong.FundOptionLeftListAdapter;
import com.gzyslczx.yslc.adapters.fundtong.FundOptionRightAdapter;
import com.gzyslczx.yslc.databinding.FundOptionFragmentLayoutBinding;
import com.gzyslczx.yslc.databinding.NoneDataItemBinding;
import com.gzyslczx.yslc.events.FundTongLoginEvent;
import com.gzyslczx.yslc.events.MineFundOptionEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.FundOptionPresenter;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FundOptionFragment extends BaseFragment<FundOptionFragmentLayoutBinding> implements View.OnClickListener, OnItemClickListener {

    private final String TAG = "FOptionFragment";
    private FundOptionPresenter mPresenter;
    private boolean LeftVScroll = false, RightVScroll = false;
    private FundOptionLeftListAdapter mLeftListAdapter;
    private FundOptionRightAdapter mRightAdapter;
    //空数据视图
    private NoneDataItemBinding mNoneDataBinding;
    private int NoneState = 0;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = FundOptionFragmentLayoutBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.FundOptionTopHScrollChild.FundTongDayImg.setVisibility(View.GONE);
        mViewBinding.FundOptionTopHScrollChild.FundTongWeekRiseImg.setVisibility(View.GONE);
        mViewBinding.FundOptionTopHScrollChild.FundTongMonthRiseImg.setVisibility(View.GONE);
        mViewBinding.FundOptionTopHScrollChild.FundTongQuarterRiseImg.setVisibility(View.GONE);
        mViewBinding.FundOptionTopHScrollChild.FundTongHalfYearRiseImg.setVisibility(View.GONE);
        mViewBinding.FundOptionTopHScrollChild.FundTongYearRiseImg.setVisibility(View.GONE);
        mViewBinding.FundOptionTopHScrollChild.FundTongFValueImg.setVisibility(View.GONE);
        mViewBinding.FundOptionTopHScrollChild.FundTongFValueBg.setVisibility(View.GONE);
        mViewBinding.FundOptionTopHScrollChild.FundTongFValue.setVisibility(View.GONE);
        SetupLeftRightScroll();
        SetupDownUpScroll();
        mViewBinding.FundOptionLeftList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftListAdapter = new FundOptionLeftListAdapter(R.layout.fund_option_left_list_item);
        mLeftListAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.FundOptionLeftList.setAdapter(mLeftListAdapter);
        mViewBinding.FundOptionRightList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new FundOptionRightAdapter(R.layout.fund_tong_rightlist_rank_list);
        mViewBinding.FundOptionRightList.setAdapter(mRightAdapter);
        mPresenter = new FundOptionPresenter();
        InitNoneDataView();
    }

    /*
     * 初始化空数据视图
     * */
    private void InitNoneDataView(){
        mNoneDataBinding = NoneDataItemBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.none_data_item, null));
        mViewBinding.FundOptionNoneData.addView(mNoneDataBinding.getRoot());
        mNoneDataBinding.noneBtn.setOnClickListener(this::onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpTool.Instance(getContext()).IsFundTongLogin()) {
            mViewBinding.FundOptionLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestMineFundOption(getContext(), this);
        }
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
        mViewBinding.FundOptionTopHScroll.setSubScroll(mViewBinding.FundOptionDataScroll);
        mViewBinding.FundOptionDataScroll.setSubScroll(mViewBinding.FundOptionTopHScroll);
    }

    /*
     *  设置上下滑动联动
     * */
    private void SetupDownUpScroll() {
        mViewBinding.FundOptionLeftList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    mViewBinding.FundOptionRightList.scrollBy(dx, dy);
                }
            }
        });
        mViewBinding.FundOptionRightList.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                    mViewBinding.FundOptionLeftList.scrollBy(dx, dy);
                }
            }
        });
    }

    /*
    * 接收自选基金列表
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMineFundOptionEvent(MineFundOptionEvent event){
        Log.d(TAG, "接收到自选基金列表");
        if (event.isFlag()){
            if (event.getDataList()!=null && event.getDataList().size()>0){
                //初始化列表
                mLeftListAdapter.setList(event.getDataList());
                mRightAdapter.setList(event.getDataList());
                mViewBinding.FundOptionNoneData.setVisibility(View.GONE);
            }else {
                mLeftListAdapter.getData().clear();
                mRightAdapter.getData().clear();
                mLeftListAdapter.notifyDataSetChanged();
                mRightAdapter.notifyDataSetChanged();
                //列表空
                mNoneDataBinding.noneText.setText("亲，您尚未添加自选基金哟~");
                mNoneDataBinding.noneBtn.setText("点击添加自选基金");
                NoneState=1;
                mViewBinding.FundOptionNoneData.setVisibility(View.VISIBLE);
            }
        }else {
            //无数据或请求失败
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
            mNoneDataBinding.noneText.setText("网络不给力...");
            mNoneDataBinding.noneBtn.setText("点击刷新");
            NoneState=0;
            mViewBinding.FundOptionNoneData.setVisibility(View.VISIBLE);
        }
        mViewBinding.FundOptionLoading.setVisibility(View.GONE);
    }

    /*
     * 接收基金通登录
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTongLoginEvent(FundTongLoginEvent event){
        Log.d(TAG, "接收到基金通登录");
        if (event.isFlag()){
            mViewBinding.FundOptionLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestMineFundOption(getContext(), this);
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.noneBtn){
            if (NoneState==1) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }else {
                mViewBinding.FundOptionNoneData.setVisibility(View.GONE);
                mViewBinding.FundOptionLoading.setVisibility(View.VISIBLE);
                mPresenter.RequestMineFundOption(getContext(), this);
            }
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(getContext(), FundDetailActivity.class);
        intent.putExtra(FundDetailActivity.CodeKey, mLeftListAdapter.getData().get(position).getFCode());
        startActivity(intent);
    }
}
