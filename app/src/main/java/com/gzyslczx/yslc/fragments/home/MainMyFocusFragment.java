package com.gzyslczx.yslc.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.BigVideoActivity;
import com.gzyslczx.yslc.LabelArtActivity;
import com.gzyslczx.yslc.LabelSelfActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SmallVideoActivity;
import com.gzyslczx.yslc.TeacherArtActivity;
import com.gzyslczx.yslc.TeacherSelfActivity;
import com.gzyslczx.yslc.adapters.main.MainMyFocusAdapter;
import com.gzyslczx.yslc.adapters.main.MainRecoAdapter;
import com.gzyslczx.yslc.adapters.main.bean.MainMyFocusData;
import com.gzyslczx.yslc.adapters.main.bean.MainRecoData;
import com.gzyslczx.yslc.databinding.MainMyFocusFragmentBinding;
import com.gzyslczx.yslc.databinding.NoneDataItemBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.GuBbMainMyFocusEvent;
import com.gzyslczx.yslc.events.MainFocusPageChangeEvent;
import com.gzyslczx.yslc.events.MyFocusInfoListEvent;
import com.gzyslczx.yslc.events.NoticeFocusPageRefreshEvent;
import com.gzyslczx.yslc.events.NoticeFocusUpdateEvent;
import com.gzyslczx.yslc.events.NoticePraiseUpdateEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.modes.response.ResMyFocusObj;
import com.gzyslczx.yslc.modes.response.ResScrollSmallVideoInfo;
import com.gzyslczx.yslc.presenter.MainFocusPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.jpush.android.cache.Sp;

public class MainMyFocusFragment extends BaseFragment<MainMyFocusFragmentBinding> implements OnItemChildClickListener, View.OnClickListener, OnLoadMoreListener,
        OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = "MyFocusFrag";

    private MainFocusPresenter mPresenter;
    //关注列表适配去
    private MainMyFocusAdapter mWithoutLoginAdapter;
    private MainRecoAdapter mAdapter;
    //空数据视图
    private NoneDataItemBinding mNoneDataBinding;
    private SharePopup sharePopup;
    private int PraisePos = -1;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = MainMyFocusFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        mPresenter = new MainFocusPresenter();
        SetupAboutViews(SpTool.Instance(getContext()).IsGuBbLogin());
        mViewBinding.MyFocusRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewBinding.MyFocusMore.setOnClickListener(this::onClick);
        mViewBinding.MyFocusRefresh.setEnabled(false);
//        if (!SpTool.Instance(getContext()).IsGuBbLogin()) {
//            //未登录时列表适配器
//            mWithoutLoginAdapter = new MainMyFocusAdapter(new ArrayList<MainMyFocusData>());
//            mWithoutLoginAdapter.setEmptyView(InitNoneDataView());
//            mViewBinding.MyFocusRecycler.setAdapter(mWithoutLoginAdapter);
//            mPresenter.RequestMyFocus(getContext(), this);
//        }else {
//            //登录时列表适配器
//            mAdapter = new MainRecoAdapter(new ArrayList<MainRecoData>());
//            mAdapter.setOnItemClickListener(this::onItemClick);
//            mAdapter.setOnItemChildClickListener(this::onItemChildClick);
//            mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
//            mAdapter.setEmptyView(InitNoneDataView());
//            mViewBinding.MyFocusRecycler.setAdapter(mAdapter);
//            mViewBinding.MyFocusRefresh.setRefreshing(true);
//            mPresenter.RequestMyFocusInfoList(getContext(), this);
//        }
        mAdapter = new MainRecoAdapter(new ArrayList<MainRecoData>());
        mAdapter.setOnItemClickListener(this::onItemClick);
        mAdapter.setOnItemChildClickListener(this::onItemChildClick);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
        mAdapter.setEmptyView(InitNoneDataView());
        mViewBinding.MyFocusRecycler.setAdapter(mAdapter);
        mViewBinding.MyFocusRefresh.setRefreshing(true);
        mPresenter.RequestMyFocusInfoList(getContext(), this);
    }

    @Override
    public void onDestroyView() {
        if (sharePopup!=null){
            sharePopup.Clear();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    /*
    * 根据登录状态显示相关控件
    * */
    private void SetupAboutViews(boolean isLogin){
        if (isLogin){
            Log.d(TAG, "已登录股扒扒");
            mViewBinding.MyFocusYs.setVisibility(View.VISIBLE);
            mViewBinding.MyFocusMore.setVisibility(View.VISIBLE);
        }else {
            Log.d(TAG, "未登录股扒扒");
            mViewBinding.MyFocusYs.setVisibility(View.GONE);
            mViewBinding.MyFocusMore.setVisibility(View.GONE);
        }
    }

    /*
     * 接收未登录时我的关注
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMyFocusEventWithoutLogin(GuBbMainMyFocusEvent event){
//        if (event.isFlag()){
//            mWithoutLoginAdapter.setList(event.getDataList());
//            mViewBinding.MyFocusRecycler.scrollToPosition(0);
//            mWithoutLoginAdapter.getLoadMoreModule().loadMoreEnd();
//        }else {
//            mWithoutLoginAdapter.getData().clear();
//            mWithoutLoginAdapter.notifyDataSetChanged();
//        }
//        mViewBinding.MyFocusRefresh.setRefreshing(false);
    }


    /*
    * 接收我的关注
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMyFocusEvent(MyFocusInfoListEvent event){
        Log.d(TAG, "接收到我的关注");
        if (event.isFlag()){
            if (event.isNoData()){
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
            }else {
                if (event.isEnd()){
                    mAdapter.addData(event.getDataList());
                    mAdapter.getLoadMoreModule().loadMoreEnd();
                }else {
                    if (mPresenter.getCurrentPage()==1){
                        mAdapter.setList(event.getDataList());
                        mViewBinding.MyFocusRecycler.scrollToPosition(0);
                    }else {
                        mAdapter.addData(event.getDataList());
                    }
                    mPresenter.AddCurrentPage();
                    mAdapter.getLoadMoreModule().loadMoreComplete();
                }
            }
        }else {
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
        }
        if (mViewBinding.MyFocusRefresh.isRefreshing()) {
            mViewBinding.MyFocusRefresh.setRefreshing(false);
        }
    }

    /*
    * 接收到关注通知更新
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void NoticeUpdateFocusEvent(NoticeFocusUpdateEvent event){
        if (SpTool.Instance(getContext()).IsGuBbLogin()) {
            if (!event.isFocus()) {
                Log.d(TAG, "接收到通知关注更新");
                mPresenter.setCurrentPage(1);
                mViewBinding.MyFocusRefresh.setRefreshing(true);
                mPresenter.RequestMyFocusInfoList(getContext(), this);
                ResMyFocusObj resMyFocusObj = new ResMyFocusObj(event.getID(), null, null, null, null, event.isFocus());
                GuBbChangeFocusEvent focusEvent = new GuBbChangeFocusEvent(true, resMyFocusObj, event.isFocus(), event.isTeacher());
                EventBus.getDefault().post(focusEvent);
                //我的关注接收更新后变为+关注的名师或栏目
//                if (event.isTeacher()) {
//                    //更新名师关注
//                    ResMyFocusObj resMyFocusObj = null;
//                    for (int i = 0; i < mAdapter.getData().size(); i++) {
//                        MainRecoData data = mAdapter.getData().get(i);
//                        if (data.getItemType() == MainMyFocusData.TeacherItem && data.getInfo().getUserId().equals(event.getID())) {
//                            if (resMyFocusObj==null) {
//                                resMyFocusObj=new ResMyFocusObj(event.getID(), data.getInfo().getAuthor(), data.getInfo().getAuthorImg(), null, null, event.isFocus());
//                                GuBbChangeFocusEvent focusEvent = new GuBbChangeFocusEvent(true, resMyFocusObj, event.isFocus(), true);
//                                EventBus.getDefault().post(focusEvent);
//                            }
//                            mAdapter.removeAt(i);
//                        }
//                    }
//                } else {
//                    //更新栏目关注
//                    ResMyFocusObj resMyFocusObj = null;
//                    for (int i = 0; i < mAdapter.getData().size(); i++) {
//                        MainRecoData data = mAdapter.getData().get(i);
//                        if (data.getItemType() == MainMyFocusData.LabelItem && data.getInfo().getLabelId().equals(event.getID())) {
//                            if (resMyFocusObj==null) {
//                                resMyFocusObj = new ResMyFocusObj(event.getID(), data.getInfo().getLabelName(), data.getInfo().getLabelHeadImg(), null, null, event.isFocus());
//                                GuBbChangeFocusEvent focusEvent = new GuBbChangeFocusEvent(true, resMyFocusObj, event.isFocus(), false);
//                                EventBus.getDefault().post(focusEvent);
//                            }
//                            mAdapter.removeAt(i);
//                            break;
//                        }
//                    }
//                }
            }
        }
    }


    /*
    * 接收关注更新
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeFocusEvent(GuBbChangeFocusEvent event){
        Log.d(TAG, "接收到关注更新");
        if (event.isFocus()){
            mPresenter.setCurrentPage(1);
            mPresenter.RequestMyFocusInfoList(getContext(), this);
        }
    }

    /*
    * 接收登录状态更新
    * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        SetupAboutViews(event.isLogin());
//        if (!SpTool.Instance(getContext()).IsGuBbLogin()) {
//            //未登录时列表适配器
//            if (mWithoutLoginAdapter==null) {
//                mWithoutLoginAdapter = new MainMyFocusAdapter(new ArrayList<MainMyFocusData>());
//                mWithoutLoginAdapter.setEmptyView(InitNoneDataView());
//                mViewBinding.MyFocusRecycler.setAdapter(mWithoutLoginAdapter);
//            }
//            mViewBinding.MyFocusRefresh.setRefreshing(true);
//            mPresenter.RequestMyFocus(getContext(), this);
//        }else {
//            //登录时列表适配器
//            if (mAdapter==null) {
//                mAdapter = new MainRecoAdapter(new ArrayList<MainRecoData>());
//                mAdapter.setOnItemClickListener(this::onItemClick);
//                mAdapter.setOnItemChildClickListener(this::onItemChildClick);
//                mAdapter.getLoadMoreModule().setOnLoadMoreListener(this::onLoadMore);
//                mAdapter.setEmptyView(InitNoneDataView());
//                mViewBinding.MyFocusRecycler.setAdapter(mAdapter);
//            }
//            mViewBinding.MyFocusRefresh.setRefreshing(true);
//            mPresenter.RequestMyFocusInfoList(getContext(), this);
//        }
        mViewBinding.MyFocusRefresh.setRefreshing(true);
        mPresenter.RequestMyFocusInfoList(getContext(), this);
    }

    /*
    * 列表条目子控件点击
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
            case R.id.FocusTState:
            case R.id.FocusLState:
                if (!SpTool.Instance(getContext()).IsGuBbLogin()){
                    NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
                }
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
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.MyFocusMore || view.getId()==R.id.noneBtn){
            //点击更多关注
            if (SpTool.Instance(getContext()).IsGuBbLogin()) {
                MainFocusPageChangeEvent event = new MainFocusPageChangeEvent(1);
                EventBus.getDefault().post(event);
            }else {
                NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
            }
        }
    }

    /*
    * 初始化空数据视图
    * */
    private View InitNoneDataView(){
        mNoneDataBinding = NoneDataItemBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.none_data_item, null));
        mNoneDataBinding.noneText.setText(R.string.NoneFocusTip);
        mNoneDataBinding.noneBtn.setText(R.string.MoreFocus);
        mNoneDataBinding.noneBtn.setOnClickListener(this::onClick);
        return mNoneDataBinding.getRoot();
    }

    /*
     * 接收我的关注切换
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnNoticeRefresh(NoticeFocusPageRefreshEvent event){
        if (event.getFocusPage()==0){
            Log.d(TAG, "接收到我的关注页刷新");
            SetupAboutViews(SpTool.Instance(getContext()).IsGuBbLogin());
            mViewBinding.MyFocusRefresh.setRefreshing(true);
            mPresenter.setCurrentPage(1);
            mPresenter.RequestMyFocusInfoList(getContext(), this);
        }
    }

    @Override
    public void onLoadMore() {
        mPresenter.RequestMyFocusInfoList(getContext(), this);
    }

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

    @Override
    public void onRefresh() {

    }
}
