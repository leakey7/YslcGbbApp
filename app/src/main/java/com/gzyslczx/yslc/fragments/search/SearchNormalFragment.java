package com.gzyslczx.yslc.fragments.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.FundDetailActivity;
import com.gzyslczx.yslc.LabelArtActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.TeacherArtActivity;
import com.gzyslczx.yslc.WebActivity;
import com.gzyslczx.yslc.adapters.search.SearchAboutGridAdapter;
import com.gzyslczx.yslc.adapters.search.SearchNormalFundGridAdapter;
import com.gzyslczx.yslc.adapters.search.SearchNormalStockGridAdapter;
import com.gzyslczx.yslc.databinding.SearchNormalFragmentBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangeOptionStateEvent;
import com.gzyslczx.yslc.events.NoticeSearchNormalEvent;
import com.gzyslczx.yslc.events.SearchNormalEvent;
import com.gzyslczx.yslc.events.SearchPageChangeEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.SearchPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchNormalFragment extends BaseFragment<SearchNormalFragmentBinding> implements View.OnClickListener {

    private final String TAG = "SearchFrag";
    private SearchNormalStockGridAdapter mStockAdapter;
    private SearchNormalFundGridAdapter mFundAdapter;
    private SearchAboutGridAdapter mAboutAdapter;
    private SearchPresenter mPresenter;
    private StringBuilder stringBuilder, StockCodeBuilder;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = SearchNormalFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mStockAdapter = new SearchNormalStockGridAdapter(getContext());
        mStockAdapter.setBaseFragment(this);
        mFundAdapter = new SearchNormalFundGridAdapter(getContext());
        mAboutAdapter = new SearchAboutGridAdapter(getContext());
        mViewBinding.SearchNorStockGrid.setAdapter(mStockAdapter);
        mViewBinding.SearchNorFundGrid.setAdapter(mFundAdapter);
        mViewBinding.SearchNorAboutGrid.setAdapter(mAboutAdapter);
        //点击更多股票
        mViewBinding.SearchNorStockMore.setOnClickListener(this::onClick);
        //点击更多基金
        mViewBinding.SearchNorFundMore.setOnClickListener(this::onClick);
        //点击更多相关内容
        mViewBinding.SearchNorAboutMore.setOnClickListener(this::onClick);
        //点击GridView条目
        SetupGridListClicked();
        mPresenter = new SearchPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stringBuilder!=null && stringBuilder.length()>0){
            mViewBinding.SearchNorLoadingBg.setVisibility(View.VISIBLE);
            mViewBinding.SearchNorLoading.setVisibility(View.VISIBLE);
            mStockAdapter.setSearchText(stringBuilder.toString().trim());
            mFundAdapter.setSearchText(stringBuilder.toString().trim());
            mAboutAdapter.setSearchText(stringBuilder.toString().trim());
            mPresenter.RequestNormal(getContext(), this, stringBuilder.toString().trim());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SearchNorStockMore:
                //点击更多股票
                SearchPageChangeEvent changeStock = new SearchPageChangeEvent(2, stringBuilder.toString());
                EventBus.getDefault().post(changeStock);
                break;
            case R.id.SearchNorFundMore:
                //点击更多基金
                SearchPageChangeEvent changeFund = new SearchPageChangeEvent(3, stringBuilder.toString());
                EventBus.getDefault().post(changeFund);
                break;
            case R.id.SearchNorAboutMore:
                //点击更多相关内容
                SearchPageChangeEvent changeAbout = new SearchPageChangeEvent(4, stringBuilder.toString());
                EventBus.getDefault().post(changeAbout);
                break;
        }
    }

    /*
     * 设置相关内容条目点击
     * */
    private void SetupGridListClicked() {
        mViewBinding.SearchNorAboutGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = null;
                if (mAboutAdapter.getData().get(i).isColumn()) {
                    intent = new Intent(getContext(), LabelArtActivity.class);
                    intent.putExtra(LabelArtActivity.LabelArtKey, mAboutAdapter.getData().get(i).getNewsId());
                } else {
                    intent = new Intent(getContext(), TeacherArtActivity.class);
                    intent.putExtra(TeacherArtActivity.TeacherArtKey, mAboutAdapter.getData().get(i).getNewsId());
                }
                startActivity(intent);
            }
        });
        mViewBinding.SearchNorFundGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //基金详情
                Intent intent = new Intent(getContext(), FundDetailActivity.class);
                intent.putExtra(FundDetailActivity.CodeKey, mFundAdapter.getData().get(i).getFCode());
                startActivity(intent);
            }
        });
        mViewBinding.SearchNorStockGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra(WebActivity.WebKey, ConnPath.StockDetailUrl+mStockAdapter.getData().get(i).getStockCode());
                startActivity(intent);
            }
        });
    }

    /*
    * 接收通知搜索
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeSearchEvent(NoticeSearchNormalEvent event){
        Log.d(TAG, "接收到通知搜索");
        if (!event.isMore()) {
            mViewBinding.SearchNorLoadingBg.setVisibility(View.VISIBLE);
            mViewBinding.SearchNorLoading.setVisibility(View.VISIBLE);
            if (stringBuilder==null){
                stringBuilder = new StringBuilder();
            }
            stringBuilder.replace(0, stringBuilder.length(), event.getKeyWord());
            mStockAdapter.setSearchText(event.getKeyWord());
            mFundAdapter.setSearchText(event.getKeyWord());
            mAboutAdapter.setSearchText(event.getKeyWord());
            mPresenter.RequestNormal(getContext(), this, event.getKeyWord());
        }
    }

    /*
    * 接收搜索结果
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNormalSearchEvent(SearchNormalEvent event){
        Log.d(TAG, "接收到搜索结果");
        if (event.isFlag()){
            if (event.getDataObj().getStockList()==null || event.getDataObj().getStockList().size()<1){
                mViewBinding.SearchNorStock.setVisibility(View.GONE);
                mViewBinding.SearchNorStockGrid.setVisibility(View.GONE);
                mViewBinding.SearchNorStockMore.setVisibility(View.GONE);
            }else {
                mViewBinding.SearchNorStock.setVisibility(View.VISIBLE);
                mViewBinding.SearchNorStockGrid.setVisibility(View.VISIBLE);
                mViewBinding.SearchNorStockMore.setVisibility(View.VISIBLE);
                mStockAdapter.setData(event.getDataObj().getStockList());
                mStockAdapter.notifyDataSetChanged();
            }
            if (event.getDataObj().getFundList()==null || event.getDataObj().getFundList().size()<1){
                mViewBinding.SearchNorFund.setVisibility(View.GONE);
                mViewBinding.SearchNorFundGrid.setVisibility(View.GONE);
                mViewBinding.SearchNorFundMore.setVisibility(View.GONE);
            }else {
                mViewBinding.SearchNorFund.setVisibility(View.VISIBLE);
                mViewBinding.SearchNorFundGrid.setVisibility(View.VISIBLE);
                mViewBinding.SearchNorFundMore.setVisibility(View.VISIBLE);
                mFundAdapter.setData(event.getDataObj().getFundList());
                mFundAdapter.notifyDataSetChanged();
            }
            if (event.getDataObj().getArtList()==null || event.getDataObj().getArtList().size()<1){
                mViewBinding.SearchNorAbout.setVisibility(View.GONE);
                mViewBinding.SearchNorAboutGrid.setVisibility(View.GONE);
                mViewBinding.SearchNorAboutMore.setVisibility(View.GONE);
            }else {
                mViewBinding.SearchNorAbout.setVisibility(View.VISIBLE);
                mViewBinding.SearchNorAboutGrid.setVisibility(View.VISIBLE);
                mViewBinding.SearchNorAboutMore.setVisibility(View.VISIBLE);
                mAboutAdapter.setData(event.getDataObj().getArtList());
                mAboutAdapter.notifyDataSetChanged();
            }
            mViewBinding.SearchNorScroll.scrollTo(0, 0);
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
        mViewBinding.SearchNorLoadingBg.setVisibility(View.GONE);
        mViewBinding.SearchNorLoading.setVisibility(View.GONE);
    }

    /*
     * 接收添删自选
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeOptionStateEvent(GuBbChangeOptionStateEvent event){
        if (StockCodeBuilder!=null && StockCodeBuilder.length()>0){
            StockCodeBuilder.delete(0, StockCodeBuilder.length());
            mPresenter.RequestNormal(getContext(), this, stringBuilder.toString().trim());
        }else {
            if (event.isFlag()){
                Log.d(TAG, "接收到添删自选");
                for (int i=0; i<mStockAdapter.getData().size(); i++){
                    if (mStockAdapter.getData().get(i).getStockCode().equals(event.getStockCodes()[0])){
                        mStockAdapter.getData().get(i).setFocus(event.isAddOption());
                        mStockAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        if (event.isLogin() && !TextUtils.isEmpty(event.getStockCode())){
            if (StockCodeBuilder==null){
                StockCodeBuilder = new StringBuilder();
            }
            StockCodeBuilder.replace(0, stringBuilder.length(), event.getStockCode());
            mViewBinding.SearchNorLoadingBg.setVisibility(View.VISIBLE);
            mViewBinding.SearchNorLoading.setVisibility(View.VISIBLE);
            mStockAdapter.setSearchText(stringBuilder.toString().trim());
            mFundAdapter.setSearchText(stringBuilder.toString().trim());
            mAboutAdapter.setSearchText(stringBuilder.toString().trim());
            NormalActionTool.AddOptionAction(getContext(), this, (BaseActivity) getActivity(), new String[]{event.getStockCode()}, TAG);
        }else {
            if (stringBuilder != null && stringBuilder.length() > 0) {
                mViewBinding.SearchNorLoadingBg.setVisibility(View.VISIBLE);
                mViewBinding.SearchNorLoading.setVisibility(View.VISIBLE);
                mStockAdapter.setSearchText(stringBuilder.toString().trim());
                mFundAdapter.setSearchText(stringBuilder.toString().trim());
                mAboutAdapter.setSearchText(stringBuilder.toString().trim());
                mPresenter.RequestNormal(getContext(), this, stringBuilder.toString().trim());
            }
        }
    }

}
