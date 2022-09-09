package com.gzyslczx.yslc.fragments.search;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.FundDetailActivity;
import com.gzyslczx.yslc.LabelArtActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.TeacherArtActivity;
import com.gzyslczx.yslc.WebActivity;
import com.gzyslczx.yslc.adapters.search.SearchMoreListAdapter;
import com.gzyslczx.yslc.adapters.search.bean.SearchMoreData;
import com.gzyslczx.yslc.databinding.SearchMoreFragmentBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangeOptionStateEvent;
import com.gzyslczx.yslc.events.NoticeSearchNormalEvent;
import com.gzyslczx.yslc.events.SearchMoreEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.SearchPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class SearchMoreFragment extends BaseFragment<SearchMoreFragmentBinding> implements OnItemChildClickListener, OnItemClickListener {

    private final String TAG = "SearchMoreFrag";
    private SearchPresenter mPresenter;
    private SearchMoreListAdapter mListAdapter;
    private String KeyWord;
    private int MoreType=99;
    private StringBuilder StockBuilder;
    private boolean isIntoWeb = false;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = SearchMoreFragmentBinding.inflate(inflater, container ,false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.SearchMoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mListAdapter = new SearchMoreListAdapter(new ArrayList<SearchMoreData>());
        mListAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mListAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.SearchMoreList.setAdapter(mListAdapter);
        mPresenter = new SearchPresenter();
        isIntoWeb = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isIntoWeb && !TextUtils.isEmpty(KeyWord) && MoreType!=99){
            mViewBinding.SearchMoreLoadBg.setVisibility(View.VISIBLE);
            mViewBinding.SearchMoreLoad.setVisibility(View.VISIBLE);
            switch (MoreType){
                case 1:
                    //更多股票
                    mPresenter.RequestMoreStock(getContext(), this, KeyWord);
                    break;
                case 2:
                    //更多基金
                    mPresenter.RequestMoreFund(getContext(), this, KeyWord);
                    break;
                case 3:
                    //更多相关内容
                    mPresenter.RequestMoreArticle(getContext(), this, KeyWord);
                    break;
            }
            isIntoWeb = false;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 点击列表子控件
    * */
    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        if (view.getId() == R.id.StockOption){
            if (SpTool.Instance(getContext()).IsGuBbLogin()){
                if (mListAdapter.getData().get(position).getStockData().isFocus()){
                    NormalActionTool.DeleteOptionAction(getContext(), this, (BaseActivity) getActivity(),
                            new String[]{mListAdapter.getData().get(position).getStockData().getStockCode()}, TAG);
                }else {
                    NormalActionTool.AddOptionAction(getContext(), this, (BaseActivity) getActivity(),
                            new String[]{mListAdapter.getData().get(position).getStockData().getStockCode()}, TAG);
                }
            }else {
                NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), mListAdapter.getData().get(position).getStockData().getStockCode(), TAG);
            }
        }
    }

    /*
    * 点击列表条目
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (adapter.getItemViewType(position) == SearchMoreData.ArticleType){
            Intent intent = null;
            if (mListAdapter.getData().get(position).getArtData().isColumn()){
                intent = new Intent(getContext(), LabelArtActivity.class);
                intent.putExtra(LabelArtActivity.LabelArtKey, mListAdapter.getData().get(position).getArtData().getNewsId());
            }else {
                intent = new Intent(getContext(), TeacherArtActivity.class);
                intent.putExtra(TeacherArtActivity.TeacherArtKey, mListAdapter.getData().get(position).getArtData().getNewsId());
            }
            startActivity(intent);
            return;
        }
        if (adapter.getItemViewType(position) == SearchMoreData.FundType){
            //基金详情
            Intent intent = new Intent(getContext(), FundDetailActivity.class);
            intent.putExtra(FundDetailActivity.CodeKey, mListAdapter.getData().get(position).getFundData().getFCode());
            startActivity(intent);
            return;
        }
        if (adapter.getItemViewType(position) == SearchMoreData.StockType){
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtra(WebActivity.WebKey, ConnPath.StockDetailUrl+mListAdapter.getData().get(position).getStockData().getStockCode());
            isIntoWeb = true;
            startActivity(intent);
        }
    }

    /*
     * 接收通知搜索
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeSearchEvent(NoticeSearchNormalEvent event){
        Log.d(TAG, "接收到通知搜索");
        if (event.isMore()) {
            mViewBinding.SearchMoreLoadBg.setVisibility(View.VISIBLE);
            mViewBinding.SearchMoreLoad.setVisibility(View.VISIBLE);
            KeyWord = event.getKeyWord();
            MoreType = event.getMoreType();
            mListAdapter.setSearchText(event.getKeyWord());
            switch (event.getMoreType()){
                case 1:
                    //更多股票
                    mPresenter.RequestMoreStock(getContext(), this, event.getKeyWord());
                    break;
                case 2:
                    //更多基金
                    mPresenter.RequestMoreFund(getContext(), this, event.getKeyWord());
                    break;
                case 3:
                    //更多相关内容
                    mPresenter.RequestMoreArticle(getContext(), this, event.getKeyWord());
                    break;
            }
        }
    }

    /*
    * 接收更多搜索结果
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMoreSearchEvent(SearchMoreEvent event){
        Log.d(TAG, "接收到更多搜索结果");
        if (event.isFlag()){
            mListAdapter.setList(event.getDataList());
        }else {
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
        }
        mViewBinding.SearchMoreLoadBg.setVisibility(View.GONE);
        mViewBinding.SearchMoreLoad.setVisibility(View.GONE);
    }

    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        if (event.isLogin()  && !TextUtils.isEmpty(event.getStockCode())){
            if (StockBuilder==null){
                StockBuilder = new StringBuilder();
            }
            StockBuilder.replace(0, StockBuilder.length(), event.getStockCode());
            mViewBinding.SearchMoreLoadBg.setVisibility(View.VISIBLE);
            mViewBinding.SearchMoreLoad.setVisibility(View.VISIBLE);
            NormalActionTool.AddOptionAction(getContext(), this, (BaseActivity) getActivity(), new String[]{event.getStockCode()}, TAG);
        }else {
            mViewBinding.SearchMoreLoadBg.setVisibility(View.VISIBLE);
            mViewBinding.SearchMoreLoad.setVisibility(View.VISIBLE);
            switch (MoreType) {
                case 1:
                    //更多股票
                    mPresenter.RequestMoreStock(getContext(), this, KeyWord);
                    break;
                case 2:
                    //更多基金
                    mPresenter.RequestMoreFund(getContext(), this, KeyWord);
                    break;
                case 3:
                    //更多相关内容
                    mPresenter.RequestMoreArticle(getContext(), this, KeyWord);
                    break;
            }
        }
    }

    /*
     * 接收添删自选
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeOptionStateEvent(GuBbChangeOptionStateEvent event){
        if (StockBuilder!=null && StockBuilder.length()>0){
            StockBuilder.delete(0, StockBuilder.length());
            switch (MoreType) {
                case 1:
                    //更多股票
                    mPresenter.RequestMoreStock(getContext(), this, KeyWord);
                    break;
                case 2:
                    //更多基金
                    mPresenter.RequestMoreFund(getContext(), this, KeyWord);
                    break;
                case 3:
                    //更多相关内容
                    mPresenter.RequestMoreArticle(getContext(), this, KeyWord);
                    break;
            }
        }else {
            if (event.isFlag() && MoreType == 1) {
                Log.d(TAG, "接收到添删自选");
                for (int i = 0; i < mListAdapter.getData().size(); i++) {
                    if (mListAdapter.getData().get(i).getStockData().getStockCode().equals(event.getStockCodes()[0])) {
                        mListAdapter.getData().get(i).getStockData().setFocus(event.isAddOption());
                        mListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }


}
