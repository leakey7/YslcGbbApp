package com.gzyslczx.yslc.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.google.gson.Gson;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.BigVideoActivity;
import com.gzyslczx.yslc.LabelArtActivity;
import com.gzyslczx.yslc.LabelSelfActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SmallVideoActivity;
import com.gzyslczx.yslc.TeacherArtActivity;
import com.gzyslczx.yslc.TeacherSelfActivity;
import com.gzyslczx.yslc.adapters.main.MainRecoAdapter;
import com.gzyslczx.yslc.adapters.main.bean.MainRecoData;
import com.gzyslczx.yslc.databinding.JustRecyclerviewBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.GuBbMainRecoAndVideoListEvent;
import com.gzyslczx.yslc.events.NoticeMainRefreshEvent;
import com.gzyslczx.yslc.events.NoticePraiseUpdateEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResScrollSmallVideoInfo;
import com.gzyslczx.yslc.presenter.MainRecoAndVideoPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainRecoFragment extends BaseFragment<JustRecyclerviewBinding> implements OnItemChildClickListener, OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private final String TAG = "MainRecoFrag";

    private MainRecoAdapter mAdapter;
    private MainRecoAndVideoPresenter mPresenter;
    private int PraisePos = -1;
    private SharePopup sharePopup;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = JustRecyclerviewBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.JustRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MainRecoAdapter(new ArrayList<MainRecoData>());
        mAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mAdapter.setOnItemClickListener(this::onItemClick);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mViewBinding.JustRecycler.setAdapter(mAdapter);
        mViewBinding.JustListRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.main_red));
        mViewBinding.JustListRefresh.setOnRefreshListener(this::onRefresh);
        mPresenter = new MainRecoAndVideoPresenter();
        mViewBinding.JustListRefresh.setRefreshing(true);
        mPresenter.RequestRecommendList(getContext(), this, (BaseActivity) getActivity(), 1);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sharePopup!=null){
            sharePopup.Clear();
        }
    }

    /*
    * 条目子控件点击
    * */
    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        switch (view.getId()){
            case R.id.JTextHeadImg:
            case R.id.APicHeadImg:
            case R.id.MPicHeadImg:
            case R.id.HVHeadImg:
            case R.id.VVHeadImg:
                //点击了头像
                if (mAdapter.getData().get(position).isTeacherItem()){
                    //名师类，跳转该名师个人页
                    Intent TeacherIntent = new Intent(getContext(), TeacherSelfActivity.class);
                    TeacherIntent.putExtra(TeacherSelfActivity.TIDKey, mAdapter.getData().get(position).getInfo().getUserId());
                    startActivity(TeacherIntent);
                }else {
                    //栏目类，跳转该栏目专栏页
                    Intent LabelIntent = new Intent(getContext(), LabelSelfActivity.class);
                    LabelIntent.putExtra(LabelSelfActivity.LNameKey, mAdapter.getData().get(position).getInfo().getNewLabel());
                    startActivity(LabelIntent);
                }
                break;
            case R.id.JTextFocus:
            case R.id.APicFocus:
            case R.id.MPicFocus:
            case R.id.HVFocus:
            case R.id.VVFocus:
                //点击了关注
                if (SpTool.Instance(getContext()).IsGuBbLogin()) {
                    if (mAdapter.getData().get(position).isTeacherItem()) {
                        //已登录，关注名师
                        NormalActionTool.FocusAction(getContext(), this, (BaseActivity) getActivity(),
                                mAdapter.getData().get(position).getInfo().getUserId(), true, TAG);
                    } else {
                        //已登录，关注栏目
                        NormalActionTool.FocusAction(getContext(), this, (BaseActivity) getActivity(),
                                mAdapter.getData().get(position).getInfo().getLabelId(), false, TAG);
                    }
                }else {
                    //未登录，跳转至登录界面
                    NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
                }
                break;
            case R.id.JTextPraiseImg:
            case R.id.JTextPraiseNum:
            case R.id.APicPraiseImg:
            case R.id.APicPraiseNum:
            case R.id.MPicPraiseImg:
            case R.id.MPicPraiseNum:
            case R.id.HVPraiseImg:
            case R.id.HVPraiseNum:
            case R.id.VVPraiseImg:
            case R.id.VVPraiseNum:
                //点击了点赞
                if (SpTool.Instance(getContext()).IsGuBbLogin()){
                    //已登录，点赞操作
                    Log.d(TAG, new Gson().toJson(mAdapter.getData().get(position)));
                    NormalActionTool.PraiseAction(getContext(), this, (BaseActivity) getActivity(), position,
                            mAdapter.getData().get(position).getInfo().getNewsId(), mAdapter.getData().get(position).isTeacherItem(), TAG);
                    PraisePos = position;
                }else {
                    //未登录，跳转至登录界面
                    NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
                }
                break;
            case R.id.JTextShare:
            case R.id.JTextShareImg:
            case R.id.APicShare:
            case R.id.APicShareImg:
            case R.id.MPicShare:
            case R.id.MPicShareImg:
            case R.id.HVShare:
            case R.id.HVShareImg:
            case R.id.VVShare:
            case R.id.VVShareImg:
                //点击分享
                if (sharePopup==null){
                    sharePopup = new SharePopup(getContext(), mViewBinding.getRoot(), true);
                }
                if (mAdapter.getData().get(position).isTeacherItem()){
                    //名师类
                    if (mAdapter.getData().get(position).getItemType()==MainRecoData.BVideo){
                        //名师大视频类
                        sharePopup.setUrlPath(ConnPath.BigVideoShareUrl+mAdapter.getData().get(position).getInfo().getNewsId());
                    }else if (mAdapter.getData().get(position).getItemType()==MainRecoData.SVideo){
                        //名师小视频类
                        sharePopup.setUrlPath(ConnPath.SmallVideoShareUrl+mAdapter.getData().get(position).getInfo().getNewsId());
                    }else {
                        //名师文章类
                        sharePopup.setUrlPath(ConnPath.TeacherArticleShareUrl+mAdapter.getData().get(position).getInfo().getNewsId());
                    }

                }else {
                    //栏目类
                    sharePopup.setUrlPath(ConnPath.NormalLabelShareUrl+mAdapter.getData().get(position).getInfo().getNewsId());
                }
                sharePopup.setTitle(mAdapter.getData().get(position).getInfo().getTitle());
                sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                break;

        }
    }

    /*
    * 接收点赞更新
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangePraiseEvent(GuBbChangePraiseEvent event){
        if (PraisePos!=-1 && event.isFlag()){
            Log.d(TAG, "接收到点赞更新");
            mAdapter.getData().get(PraisePos).getInfo().setLike(event.isPraise());
            if (event.isPraise()){
                mAdapter.getData().get(PraisePos).getInfo().setPraise(event.getPraiseNum());
            }
            mAdapter.notifyItemRangeChanged(PraisePos,1);
            PraisePos = -1;
        }
    }

    /*
    * 接收通知点赞更新
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticePraiseUpdateEvent(NoticePraiseUpdateEvent event){
        if (PraisePos==-1 && event.isFlag()){
            Log.d(TAG, "接收到通知点赞更新");
            if (event.isTeacher()){
                //更新名师点赞
                for (int i=0; i<mAdapter.getData().size(); i++){
                    if (mAdapter.getData().get(i).isTeacherItem() && mAdapter.getData().get(i).getInfo().getNewsId().equals(event.getNID())){
                        mAdapter.getData().get(i).getInfo().setLike(event.isPraise());
                        if (event.isPraise()) {
                            mAdapter.getData().get(i).getInfo().setPraise(event.getPraiseNum());
                        }
                        mAdapter.notifyItemRangeChanged(i,1);
                        break;
                    }
                }
            }else {
                //更新栏目点赞
                for (int i=0; i<mAdapter.getData().size(); i++){
                    if (!mAdapter.getData().get(i).isTeacherItem() && mAdapter.getData().get(i).getInfo().getNewsId().equals(event.getNID())){
                        mAdapter.getData().get(i).getInfo().setLike(event.isPraise());
                        if (event.isPraise()) {
                            mAdapter.getData().get(i).getInfo().setPraise(event.getPraiseNum());
                        }
                        mAdapter.notifyItemRangeChanged(i,1);
                        break;
                    }
                }
            }
        }
    }


    /*
    * 接收推荐列表
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRecommendListEvent(GuBbMainRecoAndVideoListEvent event){
        Log.d(TAG, "接收到推荐列表");
        if (event.isFlag() && event.getListType()==1){
            if (mAdapter.getLoadMoreModule().isLoading()){
                if (event.isListPageEnd()){
                    mAdapter.getLoadMoreModule().loadMoreEnd();
                }else {
                    mAdapter.getLoadMoreModule().loadMoreComplete();
                }
            }
            if (mViewBinding.JustListRefresh.isRefreshing()){
                //刷新，初始化数据
                mAdapter.setList(event.getDataList());
                mViewBinding.JustRecycler.scrollToPosition(0);
                mViewBinding.JustListRefresh.setRefreshing(false);
                mViewBinding.JustListRefresh.setEnabled(false);
            }else {
                //非刷新，加载更多数据
                mAdapter.addData(event.getDataList());
            }
        }else if (!event.isFlag() && event.getListType()==1){
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
            if (mViewBinding.JustListRefresh.isRefreshing()){
                mViewBinding.JustListRefresh.setRefreshing(false);
                mViewBinding.JustListRefresh.setEnabled(false);
            }
            if (mAdapter.getLoadMoreModule().isLoading()) {
                mAdapter.getLoadMoreModule().loadMoreFail();
            }
        }
    }

    /*
    * 接收更新关注
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnFocusChangeEvent(GuBbChangeFocusEvent event){
        Log.d(TAG, "接收到关注更新");
        if (event.isFlag()){
            if (event.isTeacher()){
                //更新名师关注
                for (int i=0; i<mAdapter.getData().size(); i++){
                    if (mAdapter.getData().get(i).isTeacherItem()
                            && mAdapter.getData().get(i).getInfo().getUserId().equals(event.getFocusObj().getTid())){
                        mAdapter.getData().get(i).getInfo().setFocus(event.isFocus());
                    }
                }
            }else {
                //更新栏目关注
                for (int i=0; i<mAdapter.getData().size(); i++){
                    if (!mAdapter.getData().get(i).isTeacherItem()
                            && mAdapter.getData().get(i).getInfo().getLabelId().equals(event.getFocusObj().getTid())){
                        mAdapter.getData().get(i).getInfo().setFocus(event.isFocus());
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /*
    * 加载更多
    * */
    @Override
    public void onLoadMore() {
        mPresenter.RequestRecommendList(getContext(), this, (BaseActivity) getActivity(), 1);
    }

    /*
    * 刷新
    * */
    @Override
    public void onRefresh() {
        mPresenter.setCurrentPage(1);
        mPresenter.RequestRecommendList(getContext(), this, (BaseActivity) getActivity(), 1);
    }

    /*
    * 列表条目点击
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (mAdapter.getData().get(position).isTeacherItem()){
            //名师类
            int contentType = mAdapter.getData().get(position).getInfo().getContentType();
            // contentType：1.文章，2.观点，3.语音，4.视频
            if (contentType==1 || contentType==2){
                //文章或观点
                Intent tArtIntent = new Intent(getContext(), TeacherArtActivity.class);
                tArtIntent.putExtra(TeacherArtActivity.TeacherArtKey, mAdapter.getData().get(position).getInfo().getNewsId());
                tArtIntent.putExtra(TeacherArtActivity.TeacherArtPos, position);
                startActivity(tArtIntent);
            }else if (contentType==3){
                //语音
                Log.d(TAG, "语音");
            }else {
                //视频
                int videoType = mAdapter.getData().get(position).getInfo().getVideoType();
                //videoType：0为横屏视频、1为竖屏小视频
                if (videoType==0){
                    Intent intent = new Intent(getContext(), BigVideoActivity.class);
                    intent.putExtra(BigVideoActivity.BidVideoKey, mAdapter.getData().get(position).getInfo().getNewsId());
                    intent.putExtra(BigVideoActivity.TeacherVideoPos, position);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), SmallVideoActivity.class);
                    ResScrollSmallVideoInfo info = new ResScrollSmallVideoInfo();
                    info.setAuthor(mAdapter.getData().get(position).getInfo().getAuthor());
                    info.setAuthorImg(mAdapter.getData().get(position).getInfo().getAuthorImg());
                    info.setDescribe(mAdapter.getData().get(position).getInfo().getDescribe());
                    info.setFocus(mAdapter.getData().get(position).getInfo().isFocus());
                    info.setFileUrl(mAdapter.getData().get(position).getInfo().getFileUrl());
                    info.setNewsId(mAdapter.getData().get(position).getInfo().getNewsId());
                    info.setUserId(mAdapter.getData().get(position).getInfo().getUserId());
                    info.setTitle(mAdapter.getData().get(position).getInfo().getTitle());
                    info.setPraise(mAdapter.getData().get(position).getInfo().getPraise());
                    info.setLike(mAdapter.getData().get(position).getInfo().isLike());
                    intent.putExtra(SmallVideoActivity.FirstVideo, info);
                    startActivity(intent);
                }
            }
        }else {
            //栏目类
            int contentType = mAdapter.getData().get(position).getInfo().getContentType();
            // contentType：1.文章，2.观点，3.语音，4.视频
            if (contentType==1 || contentType==2){
                //文章或观点
                Intent lArtIntent = new Intent(getContext(), LabelArtActivity.class);
                lArtIntent.putExtra(LabelArtActivity.LabelArtKey, mAdapter.getData().get(position).getInfo().getNewsId());
                lArtIntent.putExtra(LabelArtActivity.LabelArtPos, position);
                startActivity(lArtIntent);
            }else if (contentType==3){
                //语音
                Log.d(TAG, "语音");
            }else {
                //视频
                int videoType = mAdapter.getData().get(position).getInfo().getVideoType();
                //videoType：0为横屏视频、1为竖屏小视频
                if (videoType==0){
                    Intent intent = new Intent(getContext(), BigVideoActivity.class);
                    intent.putExtra(BigVideoActivity.BidVideoKey, mAdapter.getData().get(position).getInfo().getNewsId());
                    startActivity(intent);
                }else {

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
        Log.d(TAG, "接收到登录更新");
        mPresenter.setCurrentPage(1);
        mViewBinding.JustListRefresh.setRefreshing(true);
        mPresenter.RequestRecommendList(getContext(), this, (BaseActivity) getActivity(), 1);
    }

    /*
     * 接收推荐页面刷新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeRefresh(NoticeMainRefreshEvent event){
        if (event.getShowPage()==1){
            Log.d(TAG, "接收到推荐页刷新");
            mPresenter.setCurrentPage(1);
            mViewBinding.JustListRefresh.setRefreshing(true);
            mPresenter.RequestRecommendList(getContext(), this, (BaseActivity) getActivity(), 1);
        }
    }

}
