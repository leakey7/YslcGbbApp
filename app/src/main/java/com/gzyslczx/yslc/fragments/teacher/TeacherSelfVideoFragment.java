package com.gzyslczx.yslc.fragments.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.BigVideoActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.TeacherSelfActivity;
import com.gzyslczx.yslc.adapters.teacher.TeacherSelfAdapter;
import com.gzyslczx.yslc.adapters.teacher.bean.TeacherSelfData;
import com.gzyslczx.yslc.databinding.OnlyjustRecyclerviewBinding;
import com.gzyslczx.yslc.databinding.NoneDataItemBinding;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.TeacherSelfInfoRefreshEvent;
import com.gzyslczx.yslc.events.TeacherSelfListEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.TeacherSelfPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class TeacherSelfVideoFragment extends BaseFragment<OnlyjustRecyclerviewBinding> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
        OnItemChildClickListener, OnItemClickListener {

    private final String TAG = "TSelfVideoFrag";
    private TeacherSelfAdapter mAdapter;
    private int CurrentPage = 1;
    private TeacherSelfPresenter mPresenter;
    private String Tid;
    private NoneDataItemBinding mNoneDataBinding;
    private SharePopup sharePopup;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = OnlyjustRecyclerviewBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        Tid = getArguments().getString(TeacherSelfActivity.TIDKey);
        mViewBinding.JustListRefresh.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.main_red));
        //设置刷新监听
        mViewBinding.JustListRefresh.setOnRefreshListener(this::onRefresh);
        mViewBinding.JustRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TeacherSelfAdapter(new ArrayList<TeacherSelfData>());
        //设置列表子控件点击
        mAdapter.setOnItemChildClickListener(this::onItemChildClick);
        //设置列表条目点击
        mAdapter.setOnItemClickListener(this::onItemClick);
        //设置加载更多监听
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mAdapter.setEmptyView(InitNoneDataView());
        mViewBinding.JustRecycler.setAdapter(mAdapter);
        EventBus.getDefault().register(this);
        mPresenter = new TeacherSelfPresenter();
        mViewBinding.JustListRefresh.setRefreshing(true);
        //type==3代表视频类资讯
        mPresenter.RequestTeacherSelf(getContext(), this, (BaseActivity) getActivity(), 3, CurrentPage, Tid, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sharePopup!=null){
            sharePopup.Clear();
        }
    }

    /*
     * 接收名师视频资讯
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnTeacherSelfListEvent(TeacherSelfListEvent event){
        if (event.getType()==3) {
            Log.d(TAG, "接收到名师视频资讯");
            if (event.isFlag() && event.getDataList()!=null && event.getDataList().size()>0) {
                //接收到全部类型资讯，type==3代表视频类资讯
                if (CurrentPage == 1) {
                    mAdapter.setList(event.getDataList());
                } else {
                    mAdapter.addData(event.getDataList());
                }
                if (event.isEnd()) {
                    mAdapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    mAdapter.getLoadMoreModule().loadMoreComplete();
                    CurrentPage++;
                }
            } else {
                mAdapter.getLoadMoreModule().loadMoreFail();
                if (CurrentPage==1){
                    mNoneDataBinding.noneText.setVisibility(View.VISIBLE);
                    mNoneDataBinding.noneImg.setVisibility(View.VISIBLE);
                    mNoneDataBinding.noneText.setText("暂无数据");
                }
            }
            if (mViewBinding.JustListRefresh.isRefreshing()) {
                mViewBinding.JustListRefresh.setRefreshing(false);
            }
        }
    }

    /*
     * 刷新
     * */
    @Override
    public void onRefresh() {
        if (TextUtils.isEmpty(Tid)){
            Log.d(TAG, getString(R.string.get_teacher_id_error));
            mViewBinding.JustListRefresh.setRefreshing(false);
        }else {
            TeacherSelfInfoRefreshEvent event = new TeacherSelfInfoRefreshEvent(3);
            EventBus.getDefault().post(event);
            CurrentPage = 1;
            mPresenter.RequestTeacherSelf(getContext(), this, (BaseActivity) getActivity(), 3, CurrentPage, Tid, false);
        }
    }

    /*
     * 加载更多
     * */
    @Override
    public void onLoadMore() {
        if (TextUtils.isEmpty(Tid)){
            Log.d(TAG, getString(R.string.get_teacher_id_error));
            mAdapter.getLoadMoreModule().loadMoreFail();
        }else {
            mPresenter.RequestTeacherSelf(getContext(), this, (BaseActivity) getActivity(), 3, CurrentPage, Tid, false);
        }
    }

    /*
     * 初始化无数据视图
     * */
    private View InitNoneDataView(){
        mNoneDataBinding = NoneDataItemBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.none_data_item, null));
        mNoneDataBinding.noneBtn.setVisibility(View.GONE);
        mNoneDataBinding.noneText.setVisibility(View.GONE);
        mNoneDataBinding.noneImg.setVisibility(View.GONE);
        return mNoneDataBinding.getRoot();
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        switch (view.getId()){
            case R.id.LSelfJTextPraiseImg:
            case R.id.LSelfJTextPraiseNum:
            case R.id.LSelfAPicPraiseImg:
            case R.id.LSelfAPicPraiseNum:
            case R.id.LSelfMPicPraiseImg:
            case R.id.LSelfMPicPraiseNum:
            case R.id.LSelfHVPraiseImg:
            case R.id.LSelfHVPraiseNum:
            case R.id.LSelfVVPraiseImg:
            case R.id.LSelfVVPraiseNum:
                //点击了点赞
                if (SpTool.Instance(getContext()).IsGuBbLogin()){
                    //已登录，点赞操作
                    NormalActionTool.PraiseAction(getContext(), this, (BaseActivity) getActivity(), position,
                            mAdapter.getData().get(position).getListData().getNewsId(), true, TAG);
                }else {
                    //未登录，跳转至登录界面
                    NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
                }
                break;
            case R.id.LSelfJTextShare:
            case R.id.LSelfJTextShareImg:
            case R.id.LSelfAPicShare:
            case R.id.LSelfAPicShareImg:
            case R.id.LSelfMPicShare:
            case R.id.LSelfMPicShareImg:
            case R.id.LSelfHVShare:
            case R.id.LSelfHVShareImg:
            case R.id.LSelfVVShare:
            case R.id.LSelfVVShareImg:
                //点击分享
                if (sharePopup==null){
                    sharePopup = new SharePopup(getContext(), mViewBinding.getRoot(), true);
                }
                sharePopup.setUrlPath(ConnPath.BigVideoShareUrl+mAdapter.getData().get(position).getListData().getNewsId());
                sharePopup.setTitle(mAdapter.getData().get(position).getListData().getTitle());
                sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    /*
     * 接收点赞更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangePraiseEvent(GuBbChangePraiseEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到点赞更新");
            mAdapter.getData().get(event.getPosition()).getListData().setLike(event.isPraise());
            if (event.isPraise()){
                mAdapter.getData().get(event.getPosition()).getListData().setPraise(event.getPraiseNum());
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(getContext(), BigVideoActivity.class);
        intent.putExtra(BigVideoActivity.BidVideoKey, mAdapter.getData().get(position).getListData().getNewsId());
        intent.putExtra(BigVideoActivity.TeacherVideoPos, position);
        startActivity(intent);
    }
}
