package com.gzyslczx.yslc;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gzyslczx.yslc.adapters.fund.FundDetailGainListAdapter;
import com.gzyslczx.yslc.adapters.fund.FundDetailGridAdapter;
import com.gzyslczx.yslc.adapters.fund.FundDetailListAdapter;
import com.gzyslczx.yslc.adapters.fund.bean.FundDetailGainData;
import com.gzyslczx.yslc.databinding.ActivityFundDetailBinding;
import com.gzyslczx.yslc.events.AddFundOptionEvent;
import com.gzyslczx.yslc.events.DeleteFundOptionEvent;
import com.gzyslczx.yslc.events.FundTongDetailEvent;
import com.gzyslczx.yslc.events.FundTongDetailIconEvent;
import com.gzyslczx.yslc.events.FundTongLoginEvent;
import com.gzyslczx.yslc.events.FundTongTokenEvent;
import com.gzyslczx.yslc.presenter.FundDetailPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FundDetailActivity extends BaseActivity<ActivityFundDetailBinding> implements View.OnClickListener, OnItemClickListener {

    private final String TAG = "FundDetailAct";
    private FundDetailPresenter mPresenter;
    public static final String CodeKey = "FundCode";
    private FundDetailGridAdapter mIconAdapter;
    private FundDetailGainListAdapter mGainAdapter;
    private FundDetailListAdapter mListAdapter;
    private String Code;
    private boolean isClickOption = false;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityFundDetailBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.main_blue));
        TransStatusTool.setStatusBarDarkMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        Code = getIntent().getStringExtra(CodeKey);
        mViewBinding.detailsDates.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mGainAdapter = new FundDetailGainListAdapter(R.layout.fund_detail_gains_item);
        mViewBinding.detailsDates.setAdapter(mGainAdapter);
        mIconAdapter = new FundDetailGridAdapter(this);
        mViewBinding.detailGrid.setAdapter(mIconAdapter);
        mViewBinding.detailsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new FundDetailListAdapter(R.layout.fund_detail_list_item);
        mListAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.detailsRecycler.setAdapter(mListAdapter);
        //点击回退
        mViewBinding.FundDetailLeft.setOnClickListener(this::onClick);
        //点击小图标
        SetupIconsClicked();
        //点击搜索
        mViewBinding.FundDetailSearchBg.setOnClickListener(this::onClick);
        mViewBinding.FundDetailSearchText.setOnClickListener(this::onClick);
        mViewBinding.FundDetailSearchImg.setOnClickListener(this::onClick);
        mViewBinding.detailsFundAdd.setOnClickListener(this::onClick);
        mPresenter = new FundDetailPresenter();
        mViewBinding.FundDetailLoading.setVisibility(View.VISIBLE);
        mPresenter.RequestFundTongToken(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isClickOption && !SpTool.Instance(this).IsFundTongLogin()){
            isClickOption = false;
        }
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.FundDetailLeft:
                finish();
                break;
            case R.id.FundDetailSearchBg:
            case R.id.FundDetailSearchText:
            case R.id.FundDetailSearchImg:
                Intent intent = new Intent(FundDetailActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.detailsFundAdd:
                if (SpTool.Instance(this).IsGuBbLogin()){
                    //股扒扒已登录
                    if (SpTool.Instance(this).IsFundTongLogin() && !TextUtils.isEmpty(Code)){
                        //基金通已经登录
                        if (mViewBinding.detailsFundAdd.getTag().toString().equals("FALSE")){
                            mPresenter.RequestAddFundOption(this, this, Code);
                        }else {
                            mPresenter.RequestDeleteFundOption(this, this, Code);
                        }
                    }else if (!SpTool.Instance(this).IsFundTongLogin()){
                        mPresenter.RequestFundTongLogin(this, this);
                    }
                }else {
                    isClickOption = true;
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
        }
    }
    /*
     * 设置图标点击事件
     * */
    private void SetupIconsClicked(){
        mViewBinding.detailGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent webIntent = new Intent(FundDetailActivity.this, WebActivity.class);
                webIntent.putExtra(WebActivity.WebKey, mIconAdapter.getData().get(i).getAppUrl()+mViewBinding.detailsFundCode.getText().toString());
                startActivity(webIntent);
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
            mPresenter.RequestDetails(this, this, Code);
            mPresenter.RequestFundTongDetailIcons(this, this);
        }else {
            Toast.makeText(this, event.getERROR(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 接收基金详情
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTongDetailEvent(FundTongDetailEvent event){
        Log.d(TAG, "接收到基金详情");
        if (event.isFlag()){
            //基金名称
            mViewBinding.detailsFundName.setText(event.getDateObj().getName());
            //基金代码
            mViewBinding.detailsFundCode.setText(event.getDateObj().getFCode());
            mViewBinding.FundDetailSearchText.setText(event.getDateObj().getFCode());
            //基金类型
            mViewBinding.detailsType.setText(event.getDateObj().getParentType());
            //单位净值
            mViewBinding.detailsUnit.setText(event.getDateObj().getUnitNet());
            //涨幅
            mViewBinding.detailsRate.setText(event.getDateObj().getProfitRate());
            //涨幅列
            List<FundDetailGainData> dateList = new ArrayList<FundDetailGainData>();
            dateList.add(new FundDetailGainData("周涨幅", event.getDateObj().getWeekRate()));
            dateList.add(new FundDetailGainData("月涨幅", event.getDateObj().getMonthRate()));
            dateList.add(new FundDetailGainData("季度涨幅", event.getDateObj().getMonthRate3()));
            dateList.add(new FundDetailGainData("半年涨幅", event.getDateObj().getMonthRate6()));
            dateList.add(new FundDetailGainData("年涨幅", event.getDateObj().getYearRate()));
            //涨幅
            mGainAdapter.setList(dateList);
            //十大持仓
            mListAdapter.setList(event.getDateObj().getListStock());
            //是否添加自选
            ShowFundOption(event.getDateObj().isSelected());
            if (isClickOption){
                isClickOption = false;
                if (!event.getDateObj().isSelected()){
                    mPresenter.RequestAddFundOption(this, this, event.getDateObj().getFCode());
                }
            }
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
        mViewBinding.FundDetailLoading.setVisibility(View.GONE);
    }

    /*
     * 接收基金详情小图标
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTongDetailIcon(FundTongDetailIconEvent event){
        Log.d(TAG, "接收到基金详情小图标");
        if (event.isFlag()){
            mIconAdapter.setData(event.getDataList());
            mIconAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebActivity.WebKey, ConnPath.StockDetailUrl+mListAdapter.getData().get(position).getStockcode());
        startActivity(intent);
    }

    /*
     * 接收基金通登录
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFundTongLoginEvent(FundTongLoginEvent event){
        Log.d(TAG, "接收到基金通登录");
        if (event.isFlag()){
            mViewBinding.FundDetailLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestDetails(this, this, Code);
        }
    }

    /*
    * 接收基金通添加自选
    * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnAddFundOptionEvent(AddFundOptionEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到基金通添加自选");
            ShowFundOption(true);
        }
    }

    /*
     * 接收基金通删除自选
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnDeleteFundOptionEvent(DeleteFundOptionEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到基金通删除自选");
            ShowFundOption(false);
        }
    }

    /*
    * 自选资金显示状态
    * */
    private void ShowFundOption(boolean isAdd){
        if (isAdd){
            mViewBinding.detailsFundAdd.setTextColor(ActivityCompat.getColor(this, R.color.gray_666));
            mViewBinding.detailsFundAdd.setText(getString(R.string.DeleteOption));
            mViewBinding.detailsFundAdd.setBackground(ActivityCompat.getDrawable(this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.detailsFundAdd.setTag("TRUE");
        }else {
            mViewBinding.detailsFundAdd.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
            mViewBinding.detailsFundAdd.setText(getString(R.string.AddOption));
            mViewBinding.detailsFundAdd.setBackground(ActivityCompat.getDrawable(this, R.drawable.red_border_focus_radius_10_shape));
            mViewBinding.detailsFundAdd.setTag("FALSE");
        }
    }

}