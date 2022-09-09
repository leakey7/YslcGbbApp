package com.gzyslczx.yslc.fragments.kline;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.KLineArticleActivity;
import com.gzyslczx.yslc.KLineVideoActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.adapters.kline.KLineAdapter;
import com.gzyslczx.yslc.adapters.kline.bean.KLineData;
import com.gzyslczx.yslc.databinding.KLineFragmentBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbKLineLearnEvent;
import com.gzyslczx.yslc.events.GuBbKLineListEvent;
import com.gzyslczx.yslc.events.GuBbKLinePraiseEvent;
import com.gzyslczx.yslc.events.NoticeKLineLearnEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.KLineListPresenter;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class KLineFragment extends BaseFragment<KLineFragmentBinding> implements OnLoadMoreListener, OnItemClickListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = "KLFragment";
    public static final String KLineKey = "KLineId";
    public static final String KLineType = "KLineType";
    //cid:分类ID， type：1=视频；2=文章
    private int cid, type;
    private KLineListPresenter mPresenter;
    private KLineAdapter mAdapter;


    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = KLineFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        cid = getArguments().getInt(KLineKey);
        type = getArguments().getInt(KLineType);
        mViewBinding.KLineList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new KLineAdapter(new ArrayList<KLineData>());
        //加载更多监听
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        //类别条目点击
        mAdapter.setOnItemClickListener(this::onItemClick);
        mViewBinding.KLineList.setAdapter(mAdapter);
        //加入学习点击
        mViewBinding.KLineAdd.setOnClickListener(this::onClick);
        //刷新监听
        mViewBinding.KLineRefresh.setOnRefreshListener(this::onRefresh);
        mPresenter = new KLineListPresenter();
        mViewBinding.KLineRefresh.setRefreshing(true);
        if (type==1){
            //视频列表
            mPresenter.RequestKLineVideoList(getContext(), this, type, cid);
        }else if (type==2){
            //文章列表
            mPresenter.RequestKLineArtList(getContext(), this, cid, type);
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    /*
    * 列表点击
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

        if (mViewBinding.KLineAdd.getVisibility()==View.VISIBLE){
            //未加入学习
            if (SpTool.Instance(getContext()).IsGuBbLogin()){
                //已登录
                Toast.makeText(getContext(), "请先加入学习", Toast.LENGTH_SHORT).show();
            }else {
                //未登录
                Toast.makeText(getContext(), "请先登录后加入学习", Toast.LENGTH_SHORT).show();
                NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
            }
        }else {
            //已加入学习
            if (type==1){
                //视频
            Intent videoIntent = new Intent(getContext(), KLineVideoActivity.class);
            videoIntent.putExtra(KLineVideoActivity.KLVideoID, mAdapter.getData().get(position).getvListData().getId());
            videoIntent.putExtra(KLineVideoActivity.KLVideoPos, position);
            videoIntent.putExtra(KLineVideoActivity.KLVideoCID, cid);
            Log.d(TAG, "视频"+cid);
            startActivity(videoIntent);
            }else if (type==2){
                //文章
            Intent artIntent = new Intent(getContext(), KLineArticleActivity.class);
            artIntent.putExtra(KLineArticleActivity.KLineArtID, mAdapter.getData().get(position).getaListData().getId());
            artIntent.putExtra(KLineArticleActivity.KLineArtPos, position);
            artIntent.putExtra(KLineArticleActivity.KLineArtCID, cid);
            startActivity(artIntent);
            }
        }
    }

    /*
    * 加载更多
    * */
    @Override
    public void onLoadMore() {
        if (type==1){
            //视频列表
            mPresenter.RequestKLineVideoList(getContext(), this, type, cid);
        }else if (type==2){
            //文章列表
            mPresenter.RequestKLineArtList(getContext(), this, cid, type);
        }
    }

    //点击事件
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.KLineAdd){
            //点击加入学习
            if (SpTool.Instance(getContext()).IsGuBbLogin()) {
                if (type == 1) {
                    //视频列表
                    mPresenter.RequestKLineLearnOrPraise(getContext(), this, cid, 0, 1);
                } else if (type == 2) {
                    //文章列表
                    mPresenter.RequestKLineLearnOrPraise(getContext(), this, cid, 0, 2);
                }
            }else {
                NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
            }
        }
    }

    /*
    * 接收K线秘籍资讯list
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnKLineListEvent(GuBbKLineListEvent event){
        if (event.isFlag() && cid==event.getCId() && type == event.getContentType()){
            Log.d(TAG, String.format("接收K线秘籍资讯%s", event.getInfo().getCateName()));
            mViewBinding.ErrorTip.setVisibility(View.GONE);
            mViewBinding.KLineName.setText(event.getInfo().getCateName());
            mViewBinding.KLineRead.setText(String.valueOf(event.getInfo().getLearnNum()));
            if (type==1){
                mViewBinding.KLineSum.setText(String.format("/%d", event.getInfo().getVideoNum()));
            }else {
                mViewBinding.KLineSum.setText(String.format("/%d", event.getInfo().getArtNum()));
            }
            mViewBinding.KLineTheme.setText(event.getInfo().getTheme());
            mViewBinding.KLineDes.setText(event.getInfo().getDesc());
            if (event.getInfo().isJoinLearn()){
                mViewBinding.KLineAdd.setVisibility(View.GONE);
                mViewBinding.KLineIsLearn.setVisibility(View.VISIBLE);
            }else {
                mViewBinding.KLineAdd.setVisibility(View.VISIBLE);
            }
            if (mViewBinding.KLineRefresh.isRefreshing()){
                mAdapter.setList(event.getDataList());
                mViewBinding.KLineRefresh.setRefreshing(false);
            }else if (mAdapter.getLoadMoreModule().isLoading()){
                mAdapter.addData(event.getDataList());
                if (event.isEnd()){
                    mAdapter.getLoadMoreModule().loadMoreEnd();
                }else {
                    mAdapter.getLoadMoreModule().loadMoreComplete();
                }
            }
        }else if (!event.isFlag() && cid==event.getCId() && type == event.getContentType()){
            if (mViewBinding.KLineRefresh.isRefreshing()) {
                mViewBinding.KLineRefresh.setRefreshing(false);
                mViewBinding.ErrorTip.setText(String.format("%s，可下拉刷新", event.getError()));
                mViewBinding.ErrorTip.setVisibility(View.VISIBLE);
                return;
            }else if (mAdapter.getLoadMoreModule().isLoading()){
                mAdapter.getLoadMoreModule().loadMoreFail();
                return;
            }
        }
    }

    /*
    * 刷新
    * */
    @Override
    public void onRefresh() {
        mPresenter.setCurrentPage(1);
        if (type==1){
            //视频列表
            mPresenter.RequestKLineVideoList(getContext(), this, type, cid);
        }else if (type==2){
            //文章列表
            mPresenter.RequestKLineArtList(getContext(), this, cid, type);
        }
    }

    /*
    * 接收登录状态更新
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        Log.d(TAG, "接收到登录更新");
        mPresenter.setCurrentPage(1);
        mViewBinding.KLineRefresh.setRefreshing(true);
        if (type==1){
            //视频列表
            mPresenter.RequestKLineVideoList(getContext(), this, type, cid);
        }else if (type==2){
            //文章列表
            mPresenter.RequestKLineArtList(getContext(), this, cid, type);
        }
    }

    /*
    * 接收学习状态更新
    * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnUpdateKLineLearnEvent(GuBbKLineLearnEvent event){
        if (event.isFlag() && cid==event.getCid()){
            Log.d(TAG, "接收到学习状态更新");
            mViewBinding.KLineAdd.setVisibility(View.GONE);
            mViewBinding.KLineIsLearn.setVisibility(View.VISIBLE);
        }
    }

    /*
    * 接收学习状态更新
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeLearnEvent(NoticeKLineLearnEvent event){
        Log.d(TAG, "接收到学习状态更新");
        if (event.isLearn() && event.getCId() == cid && event.getType() == type){
            if (type==1){
                //视频
                for (int i=0; i<mAdapter.getData().size(); i++){
                    if (mAdapter.getData().get(i).getvListData().getId() == event.getId()){
                        if (!mAdapter.getData().get(i).getvListData().isLearn()) {
                            mAdapter.getData().get(i).getvListData().setLearn(true);
                            mAdapter.notifyDataSetChanged();
                            int oldRead = Integer.parseInt(mViewBinding.KLineRead.getText().toString());
                            mViewBinding.KLineRead.setText(String.valueOf(oldRead + 1));
                        }
                    }
                }
            }else if (type==2){
                //文章
                for (int i=0; i<mAdapter.getData().size(); i++){
                    if (mAdapter.getData().get(i).getaListData().getId() == event.getId()){
                        if (!mAdapter.getData().get(i).getaListData().isLearn()) {
                            mAdapter.getData().get(i).getaListData().setLearn(true);
                            mAdapter.notifyDataSetChanged();
                            int oldRead = Integer.parseInt(mViewBinding.KLineRead.getText().toString());
                            mViewBinding.KLineRead.setText(String.valueOf(oldRead + 1));
                        }
                    }
                }
            }
        }
    }

    /*
     * 收到点赞更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnGuBbKLinePraiseEvent(GuBbKLinePraiseEvent event){
        if (event.isFlag()){
            if (event.getCid() == cid){
                Log.d(TAG, "接收到K线秘籍点赞更新");
                if (type==1){
                    for (KLineData video : mAdapter.getData()){
                        if (video.getvListData().getId() == event.getId()){
                            mAdapter.getData().get(event.getPosition()).getvListData().setLikes(event.getPraiseNum());
                        }
                    }
                }else if (type==2){
                    for (KLineData article : mAdapter.getData()){
                        if (article.getaListData().getId() == event.getId()){
                            mAdapter.getData().get(event.getPosition()).getaListData().setLikes(event.getPraiseNum());
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
