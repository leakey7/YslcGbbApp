package com.gzyslczx.yslc;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.gzyslczx.yslc.adapters.main.MainScrollSmallVideoAdapter;
import com.gzyslczx.yslc.databinding.ActivitySmallVideoBinding;
import com.gzyslczx.yslc.databinding.ScrollSmallVideItemBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangePraiseEvent;
import com.gzyslczx.yslc.events.GuBbScrollSmallVideoEvent;
import com.gzyslczx.yslc.modes.response.ResScrollSmallVideoInfo;
import com.gzyslczx.yslc.presenter.GuBbSmallVideoPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.OnScrollVideoListener;
import com.gzyslczx.yslc.tools.ScrollVideoManager;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SmallVideoActivity extends BaseActivity<ActivitySmallVideoBinding> implements OnScrollVideoListener, OnLoadMoreListener, OnItemChildClickListener,
        ITXVodPlayListener, View.OnClickListener {

    private final String TAG = "ScrollSVideoAct";
    private GuBbSmallVideoPresenter mPresenter;
    private ScrollVideoManager scrollVideoManager;
    private MainScrollSmallVideoAdapter mAdapter;
    private TXVodPlayer mPlayer;
    public static final String FirstVideo = "No1Video";
    private ResScrollSmallVideoInfo No1Video;
    private TXCloudVideoView lastVideo;
    private int lastVideoPos, PraisePos = -1;
    private boolean isChange = false, isInit=true;
    private SharePopup sharePopup;


    @Override
    void InitParentLayout() {
        mViewBinding = ActivitySmallVideoBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.black));
        TransStatusTool.setStatusBarDarkMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mViewBinding.SVideoLeft.getLayoutParams();
        layoutParams.topMargin = TransStatusTool.getStatusbarHeight(this)+ DisplayTool.dp2px(this, 14);
        mViewBinding.SVideoLeft.setLayoutParams(layoutParams);
        scrollVideoManager = new ScrollVideoManager(this, LinearLayoutManager.VERTICAL, false);
        scrollVideoManager.setListener(this);
        mViewBinding.SVideoList.setLayoutManager(scrollVideoManager);
        mAdapter = new MainScrollSmallVideoAdapter(R.layout.scroll_small_vide_item);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mViewBinding.SVideoList.setAdapter(mAdapter);
        mViewBinding.SVideoList.getItemAnimator().setChangeDuration(0);
        mPlayer = new TXVodPlayer(this);
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mPlayer.setAutoPlay(false);
        mPlayer.setVodListener(this);
        mPresenter = new GuBbSmallVideoPresenter();
        No1Video = (ResScrollSmallVideoInfo) getIntent().getSerializableExtra(FirstVideo);
        //点击回退
        mViewBinding.SVideoLeft.setOnClickListener(this::onClick);
        //点击重连
        mViewBinding.SVideoTip.setOnClickListener(this::onClick);
        //点击重播
        mViewBinding.SVideoRestart.setOnClickListener(this::onClick);
        if (No1Video!=null) {
            mViewBinding.SVideoLoading.setVisibility(View.VISIBLE);
            mPresenter.RequestScrollVideoList(this, this, No1Video.getNewsId());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lastVideo!=null) {
            PlayVideo(lastVideoPos, lastVideo);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StopVideo();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPlayer.stopPlay(false);
        if (lastVideo!=null) {
            lastVideo.onDestroy();
        }
        if (sharePopup!=null){
            sharePopup.Clear();
        }
    }

    /*
    * 列表条目子控件点击
    * */
    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        switch (view.getId()) {
            case R.id.SmallVideoHeadImg:
                //点击头像
                Intent intent = new Intent(this, TeacherSelfActivity.class);
                intent.putExtra(TeacherSelfActivity.TIDKey, mAdapter.getData().get(position).getUserId());
                startActivity(intent);
                break;
            case R.id.SmallVideoFocus:
                //点击关注
                NormalActionTool.FocusAction(this, null, this, mAdapter.getData().get(position).getUserId(), true, TAG);
                break;
            case R.id.SmallVideoPraise:
                //点赞
                PraisePos = position;
                NormalActionTool.PraiseAction(this, null, this, position, mAdapter.getData().get(position).getNewsId(), true, TAG);
                break;
            case R.id.SmallVideoShare:
            case R.id.SmallVideoShareTag:
                if (sharePopup == null){
                    sharePopup = new SharePopup(this, mViewBinding.getRoot(), true);
                }
                sharePopup.setUrlPath(ConnPath.SmallVideoShareUrl+mAdapter.getData().get(position).getNewsId());
                sharePopup.setTitle(mAdapter.getData().get(position).getTitle());
                sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    /*
    * 加载更多
    * */
    @Override
    public void onLoadMore() {
        mPresenter.RequestScrollVideoList(this, this, No1Video.getNewsId());
    }

    @Override
    public void onInitComplete(ScrollSmallVideItemBinding view) {
        if (lastVideo==null) {
            PlayVideo(0, view.SmallVideo);
            lastVideo = view.SmallVideo;
            lastVideoPos = 0;
            Log.d(TAG, "onInitComplete");
        }
    }

    @Override
    public void onPageRelease(boolean isNext, int position, ScrollSmallVideItemBinding view) {
        if (!isChange) {
            StopVideo();
            view.SmallVideo.onDestroy();
            Log.d(TAG, "onPageRelease");
        }else {
            isChange = false;
        }
    }

    @Override
    public void onPageSelect(int position, boolean isBottom, ScrollSmallVideItemBinding view) {
        PlayVideo(position, view.SmallVideo);
        lastVideo = view.SmallVideo;
        lastVideoPos = position;
        Log.d(TAG, "onPageSelect");
    }

    /*
     * 开始播放
     * */
    private void PlayVideo(int position, TXCloudVideoView videoView){
        mPlayer.setPlayerView(videoView);
        mPlayer.setAutoPlay(true);
        mPlayer.startVodPlay(mAdapter.getData().get(position).getFileUrl());
    }

    /*
     * 停止播放
     * */
    private void StopVideo(){
        mPlayer.setAutoPlay(false);
        mPlayer.stopPlay(true);
    }

    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
        if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            // 播放进度, 单位是秒
//            int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
//            mViewBinding.FVideoViewBar.SetCurrentProgress(progress);
            // 视频总长, 单位是秒
//            int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
//            mViewBinding.FVideoViewBar.SetMaxProgress(duration);
        }
        if (event == TXLiveConstants.PLAY_EVT_PLAY_END){
            mViewBinding.SVideoRestart.setVisibility(View.VISIBLE);
        }
        if (event ==  TXLiveConstants.PLAY_EVT_PLAY_LOADING){
            if (mViewBinding.SVideoLoading.getVisibility() == View.GONE) {
                mViewBinding.SVideoLoading.setVisibility(View.VISIBLE);
            }
            if (mViewBinding.SVideoRestart.getVisibility() == View.VISIBLE) {
                mViewBinding.SVideoRestart.setVisibility(View.GONE);
            }
        }
        if (event == TXLiveConstants.PLAY_EVT_VOD_LOADING_END){
            if (mViewBinding.SVideoLoading.getVisibility() == View.VISIBLE) {
                mViewBinding.SVideoLoading.setVisibility(View.GONE);
            }
        }
        if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT){
            mViewBinding.SVideoTip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SVideoLeft:
                finish();
                break;
            case R.id.SVideoTip:
                if (mAdapter.getData().size()<=1){
                    mViewBinding.SVideoLoading.setVisibility(View.VISIBLE);
                    mPresenter.RequestScrollVideoList(this, this, No1Video.getNewsId());
                }else {
                    mPlayer.setAutoPlay(true);
                    mPlayer.startVodPlay(mAdapter.getData().get(lastVideoPos).getFileUrl());
                }
                mViewBinding.SVideoTip.setVisibility(View.GONE);
                break;
            case R.id.SVideoRestart:
                mPlayer.setAutoPlay(true);
                mPlayer.startVodPlay(mAdapter.getData().get(lastVideoPos).getFileUrl());
                mViewBinding.SVideoRestart.setVisibility(View.GONE);
                break;
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
        isInit = true;
        mPresenter.RequestScrollVideoList(this, this, No1Video.getNewsId());
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
                    if (mAdapter.getData().get(i).getUserId().equals(event.getFocusObj().getTid())){
                        mAdapter.getData().get(i).setFocus(event.isFocus());
                        isChange = true;
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /*
     * 接收点赞更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangePraiseEvent(GuBbChangePraiseEvent event){
        if (PraisePos!=-1 && event.isFlag()){
            Log.d(TAG, "接收到点赞更新");
            mAdapter.getData().get(PraisePos).setLike(event.isPraise());
            if (event.isPraise()){
                mAdapter.getData().get(PraisePos).setPraise(event.getPraiseNum());
            }
            isChange = true;
            mAdapter.notifyDataSetChanged();
            PraisePos = -1;
        }
    }

    /*
    * 接收小视频列表
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnScrollSmallVideoEvent(GuBbScrollSmallVideoEvent event){
        Log.d(TAG, "接收到小视频列表");
        if (event.isFlag()){
            if (event.isEnd()){
                mAdapter.getLoadMoreModule().loadMoreEnd();
            }else {
                mAdapter.getLoadMoreModule().loadMoreComplete();
            }
            if (isInit){
                isInit=false;
                event.getDataList().add(0, No1Video);
                mAdapter.setList(event.getDataList());
            }else {
                mAdapter.addData(event.getDataList());
            }
        }else {
            mViewBinding.SVideoLoading.setVisibility(View.GONE);
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
            mAdapter.getLoadMoreModule().loadMoreFail();
        }
        mViewBinding.SVideoLoading.setVisibility(View.GONE);
    }

}