package com.gzyslczx.yslc.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SmallVideoActivity;
import com.gzyslczx.yslc.TeacherSelfActivity;
import com.gzyslczx.yslc.adapters.main.SmallVideoFragAdapter;
import com.gzyslczx.yslc.databinding.JustRecyclerviewBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.GuBbMainRecoAndVideoListEvent;
import com.gzyslczx.yslc.events.NoticeMainRefreshEvent;
import com.gzyslczx.yslc.events.NoticePraiseUpdateEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResScrollSmallVideoInfo;
import com.gzyslczx.yslc.presenter.MainRecoAndVideoPresenter;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SmallVideoFragment extends BaseFragment<JustRecyclerviewBinding> implements OnItemChildClickListener, OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private final String TAG = "SmallVideoFrag";
    private SmallVideoFragAdapter mAdapter;
    private MainRecoAndVideoPresenter mPresenter;
    private int PraisePos = -1;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = JustRecyclerviewBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.JustRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new SmallVideoFragAdapter(R.layout.small_video_fragment_item);
        mAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mAdapter.setOnItemClickListener(this::onItemClick);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mViewBinding.JustRecycler.setAdapter(mAdapter);
        mViewBinding.JustListRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.main_red));
        mViewBinding.JustListRefresh.setOnRefreshListener(this::onRefresh);
        mPresenter = new MainRecoAndVideoPresenter();
        mViewBinding.JustListRefresh.setRefreshing(true);
//        mPresenter.RequestRecommendList(getContext(), this, (BaseActivity) getActivity(), 3);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 刷洗
    * */
    @Override
    public void onRefresh() {
        NoticeMainRefreshEvent event = new NoticeMainRefreshEvent(4);
        EventBus.getDefault().post(event);
    }

    /*
    * 列表条目子控件点击
    * */
    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        if (view.getId() == R.id.SVideoPraiseImg || view.getId()==R.id.SVideoPraiseNum){
            //点击了点赞
            if (SpTool.Instance(getContext()).IsGuBbLogin()){
                //已登录，点赞操作
                NormalActionTool.PraiseAction(getContext(), this, (BaseActivity) getActivity(), position,
                        mAdapter.getData().get(position).getInfo().getNewsId(), true, TAG);
                PraisePos = position;
            }else {
                //未登录，跳转至登录界面
                NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
            }
        }else if (view.getId() == R.id.SVideoHeadImg){
            //点击了头像
            Intent TeacherIntent = new Intent(getContext(), TeacherSelfActivity.class);
            TeacherIntent.putExtra(TeacherSelfActivity.TIDKey, mAdapter.getData().get(position).getInfo().getUserId());
            startActivity(TeacherIntent);
        }
    }

    /*
    * 列表条目点击
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
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

    /*
    * 加载更多
    * */
    @Override
    public void onLoadMore() {
        mPresenter.RequestRecommendList(getContext(), this, (BaseActivity) getActivity(), 3);
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
                    if (mAdapter.getData().get(i).getInfo().getNewsId().equals(event.getNID())){
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
     * 接收视频列表
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRecommendListEvent(GuBbMainRecoAndVideoListEvent event){
        if (event.isFlag() && event.getListType()==3){
            Log.d(TAG, String.format("接收到视频列表%d", 3));
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
            }else {
                //非刷新，加载更多数据
                mAdapter.addData(event.getDataList());
            }
        }else if (!event.isFlag() && event.getListType()==3){
            Toast.makeText(getContext(), event.getError(), Toast.LENGTH_SHORT).show();
            if (mViewBinding.JustListRefresh.isRefreshing()){
                mViewBinding.JustListRefresh.setRefreshing(false);
            }
            if (mAdapter.getLoadMoreModule().isLoading()) {
                mAdapter.getLoadMoreModule().loadMoreFail();
            }
        }
        mPresenter.setCurrentPage(event.getCurrentPage());
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
     * 接收视频页面刷新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeRefresh(NoticeMainRefreshEvent event){
        if (event.getShowPage()==4){
            Log.d(TAG, "接收到小视频页刷新");
            mPresenter.setCurrentPage(1);
            mViewBinding.JustListRefresh.setRefreshing(true);
            mPresenter.RequestRecommendList(getContext(), this, (BaseActivity) getActivity(), 3);
            return;
        }
    }

}
