package com.gzyslczx.yslc;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gzyslczx.yslc.adapters.kline.KLineVideoRecoListAdapter;
import com.gzyslczx.yslc.databinding.ActivityKlineVideoBinding;
import com.gzyslczx.yslc.events.GuBbKLineDetailEvent;
import com.gzyslczx.yslc.events.GuBbKLinePraiseEvent;
import com.gzyslczx.yslc.events.NoticeKLineLearnEvent;
import com.gzyslczx.yslc.presenter.KLinePresenter;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.OnVideoPlayState;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class KLineVideoActivity extends BaseActivity<ActivityKlineVideoBinding> implements View.OnClickListener, OnVideoPlayState, OnItemClickListener {

    private final String TAG = "KLineVideo";
    public static final String KLVideoID = "KLineVID";
    public static final String KLVideoPos = "KLineVPosition";
    public static final String KLVideoCID = "KLineVCID";
    private KLinePresenter mPresenters;
    private int VideoID, CID, Position;
    private KLineVideoRecoListAdapter mListAdapter;
    private TXVodPlayer mPlayer;
    private StringBuilder PlayUrl;
    private boolean ReConnect=false, Request;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityKlineVideoBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        VideoID =  getIntent().getIntExtra(KLVideoID, -1);
        Position = getIntent().getIntExtra(KLVideoPos, -1);
        CID = getIntent().getIntExtra(KLVideoCID, -1);
        mPlayer = new TXVodPlayer(this);
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mPlayer.setAutoPlay(true);
        //播放监听
        SetupPlayerListener();
        mPlayer.setPlayerView(mViewBinding.KLVideoView);
        mViewBinding.KLVideoBar.SetNormal(this);
        mViewBinding.KLVideoList.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new KLineVideoRecoListAdapter(R.layout.k_line_video_reco_item);
        mViewBinding.KLVideoList.setAdapter(mListAdapter);
        //回退点击
        SetupBackClicked();
        //重连点击
        mViewBinding.KLVideoTip.setOnClickListener(this::onClick);
        //推荐列表点击
        mListAdapter.setOnItemClickListener(this::onItemClick);
        //点赞点击
        mViewBinding.KLVideoPraise.setOnClickListener(this::onClick);
        mViewBinding.KLVideoPraiseImg.setOnClickListener(this::onClick);
        mPresenters = new KLinePresenter();
        PlayUrl = new StringBuilder();
        mViewBinding.KLVideoLoad.setVisibility(View.VISIBLE);
        mPresenters.RequestKLineVideo(this, this, VideoID);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        VideoID =  intent.getIntExtra(KLVideoID, -1);
        mViewBinding.KLVideoLoad.setVisibility(View.VISIBLE);
        mPresenters.RequestKLineVideo(this, this, VideoID);
    }

    @Override
    protected void onDestroy() {
        mPlayer.stopPlay(true);
        mViewBinding.KLVideoView.onDestroy();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer.isPlaying()){
            mViewBinding.KLVideoBar.NoticeChangeState(true);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == newConfig.ORIENTATION_LANDSCAPE) {
            //满屏播放
            setupScreenFullPlay();
        } else if (newConfig.orientation == newConfig.ORIENTATION_PORTRAIT) {
            //小屏播放
            setupScreenSmallPlay();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            ChangeScreenWithVisi(ChangeScreen());
            return true;
        }
        return super.onKeyUp(keyCode, event);

    }

    /*
     * 小屏播放控件位置设置
     * */
    private void setupScreenSmallPlay() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mViewBinding.ParentLayout);
        //设置播放控件位置
        set.setDimensionRatio(mViewBinding.KLVideoView.getId(), "h,1.6:1");
        set.connect(mViewBinding.KLVideoView.getId(), ConstraintSet.TOP, mViewBinding.KLVideoTitle.getId(), ConstraintSet.BOTTOM,
                DisplayTool.dp2px(this, 14));
        set.clear(mViewBinding.KLVideoView.getId(), ConstraintSet.BOTTOM);
        TransitionManager.beginDelayedTransition(mViewBinding.ParentLayout);
        set.applyTo(mViewBinding.ParentLayout);
    }

    /*
     * 满屏播放控件位置设置
     * */
    private void setupScreenFullPlay() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mViewBinding.ParentLayout);
        //设置播放控件位置
        set.setDimensionRatio(mViewBinding.KLVideoView.getId(), "null");
        set.connect(mViewBinding.KLVideoView.getId(), ConstraintSet.TOP, mViewBinding.ParentLayout.getId(), ConstraintSet.TOP, 0);
        set.connect(mViewBinding.KLVideoView.getId(), ConstraintSet.BOTTOM, mViewBinding.ParentLayout.getId(), ConstraintSet.BOTTOM);
        TransitionManager.beginDelayedTransition(mViewBinding.ParentLayout);
        set.applyTo(mViewBinding.ParentLayout);
    }

    /*
     * 设置播放监听
     * */
    private void SetupPlayerListener(){
        mPlayer.setVodListener(new ITXVodPlayListener() {
            @Override
            public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle param) {
                if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                    // 播放进度, 单位是秒
                    int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                    mViewBinding.KLVideoBar.SetCurrentProgress(progress);
                    // 视频总长, 单位是秒
                    int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                    mViewBinding.KLVideoBar.SetMaxProgress(duration);
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_END){
                    mViewBinding.KLVideoBar.ChangePlayState(true);
                }
                if (event ==  TXLiveConstants.PLAY_EVT_PLAY_LOADING){
                    mViewBinding.KLVideoTip.setVisibility(View.VISIBLE);
                    mViewBinding.KLVideoTip.setText("加载中...");
                }
                if (event == TXLiveConstants.PLAY_EVT_VOD_LOADING_END){
                    mViewBinding.KLVideoTip.setVisibility(View.GONE);
                }
                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT){
                    ReConnect = true;
                    mViewBinding.KLVideoTip.setVisibility(View.VISIBLE);
                    mViewBinding.KLVideoTip.setText("视频获取失败，点击重试");
                }
            }

            @Override
            public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {
            }
        });
        //点击播放屏幕收放进度条
        mViewBinding.KLVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewBinding.KLVideoBar.getTag().equals("VISI")){
                    mViewBinding.KLVideoBar.setTag("GONE");
                    mViewBinding.KLVideoBar.SetProgressOut();
                    mViewBinding.KLVideoBar.setVisibility(View.GONE);
                }else {
                    mViewBinding.KLVideoBar.setTag("VISI");
                    mViewBinding.KLVideoBar.SetProgressIn();
                    mViewBinding.KLVideoBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /*
     * 设置回退点击
     * */
    private void SetupBackClicked(){
        mViewBinding.KLVideoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.KLVideoTip:
                //点击重连
                if (ReConnect){
                    ReConnect=false;
                    mPlayer.startVodPlay(PlayUrl.toString());
                }
                if (Request){
                    Request = false;
                    mPresenters.RequestKLineVideo(this, this, VideoID);
                }
                break;
            case R.id.KLVideoPraise:
            case R.id.KLVideoPraiseImg:
                //点赞
                if (SpTool.Instance(this).IsGuBbLogin()){
                    mPresenters.RequestKLineLearnOrPraise(this, this, VideoID, 1, 3, Position, CID);
                }else {
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
        }
    }

    @Override
    public void OnStop() {
        mPlayer.pause();
    }

    @Override
    public void OnStart() {
        mPlayer.resume();
    }

    @Override
    public void OnReStart() {
        mPlayer.startVodPlay(PlayUrl.toString());
    }

    @Override
    public void OnFullScreen() {
        ChangeScreenWithVisi(ChangeScreen());
    }

    @Override
    public void OnSmallScreen() {
        ChangeScreenWithVisi(ChangeScreen());
    }

    @Override
    public void ChangeProgress(int progress) {
        mPlayer.seek(progress);
    }

    /*
     * 更换播放屏大小隐藏非必要控件
     * */
    private void ChangeScreenWithVisi(int state) {
        if (state==0){
            //小屏
            Log.d(TAG, "切换小屏");
            mViewBinding.KLVideoToolBar.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoTitle.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoRead.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoReadTag.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoReadNum.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoShare.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoShareImg.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoPraise.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoPraiseImg.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoClassListTag.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoList.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoLine.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoRedLine.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoRisk.setVisibility(View.VISIBLE);
            mViewBinding.KLVideoBar.setPadding(0, 0, 0, 0);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mViewBinding.KLVideoView.getLayoutParams();
            layoutParams.setMargins(0, DisplayTool.dp2px(this, 14), 0, 0);
            mViewBinding.KLVideoView.setLayoutParams(layoutParams);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(mViewBinding.KLVideoCons);
            constraintSet.setDimensionRatio(mViewBinding.KLVideoView.getId(),"h,1.6:1");
            constraintSet.applyTo(mViewBinding.KLVideoCons);
        }else {
            //满屏
            Log.d(TAG, "切换大屏");
            mViewBinding.KLVideoToolBar.setVisibility(View.GONE);
            mViewBinding.KLVideoTitle.setVisibility(View.GONE);
            mViewBinding.KLVideoRead.setVisibility(View.GONE);
            mViewBinding.KLVideoReadTag.setVisibility(View.GONE);
            mViewBinding.KLVideoReadNum.setVisibility(View.GONE);
            mViewBinding.KLVideoShare.setVisibility(View.GONE);
            mViewBinding.KLVideoShareImg.setVisibility(View.GONE);
            mViewBinding.KLVideoPraise.setVisibility(View.GONE);
            mViewBinding.KLVideoPraiseImg.setVisibility(View.GONE);
            mViewBinding.KLVideoClassListTag.setVisibility(View.GONE);
            mViewBinding.KLVideoList.setVisibility(View.GONE);
            mViewBinding.KLVideoLine.setVisibility(View.GONE);
            mViewBinding.KLVideoRedLine.setVisibility(View.GONE);
            mViewBinding.KLVideoRisk.setVisibility(View.GONE);
            int size = DisplayTool.dp2px(this, TransStatusTool.getStatusbarHeight(this));
            mViewBinding.KLVideoBar.setPadding(size, 0, size, 0);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mViewBinding.KLVideoView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            mViewBinding.KLVideoView.setLayoutParams(layoutParams);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(mViewBinding.KLVideoCons);
            DisplayMetrics dm = new DisplayMetrics();;
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int DEVICE_WIDTH=dm.widthPixels;
            int DEVICE_HEIGHT=dm.heightPixels;
            constraintSet.setDimensionRatio(mViewBinding.KLVideoView.getId(),String.format("h,%d:%d", DEVICE_HEIGHT, DEVICE_WIDTH));
            constraintSet.applyTo(mViewBinding.KLVideoCons);
        }
    }

    /*
    * 点击推荐列表
    * */
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Intent intent = new Intent(this, KLineVideoActivity.class);
        intent.putExtra(KLVideoID, mListAdapter.getData().get(position).getId());
        startActivity(intent);
    }

    /*
    * 接收到视频详情
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnKLineVideoDetailEvent(GuBbKLineDetailEvent event){
        Log.d(TAG, "接收到K线秘籍视频详情");
        if (event.isFlag()){
            VideoID =  event.getDetailsObj().getVideoDetail().getId();
            mViewBinding.KLVideoTitle.setText(event.getDetailsObj().getVideoDetail().getTitle());
            PlayUrl.replace(0, PlayUrl.length(), event.getDetailsObj().getVideoDetail().getVideoUrl());
            mViewBinding.KLVideoReadNum.setText(String.format("%s次", event.getDetailsObj().getVideoDetail().getPlayTimes()));
            if (event.getDetailsObj().getVideoDetail().isLikes()){
                Glide.with(KLineVideoActivity.this).load(ActivityCompat.getDrawable(KLineVideoActivity.this, R.drawable.is_praise))
                        .fitCenter().into(mViewBinding.KLVideoPraiseImg);
                mViewBinding.KLVideoPraise.setText(event.getDetailsObj().getVideoDetail().getLikes());
            }else {
                Glide.with(KLineVideoActivity.this).load(ActivityCompat.getDrawable(KLineVideoActivity.this, R.drawable.un_praise))
                        .fitCenter().into(mViewBinding.KLVideoPraiseImg);
                mViewBinding.KLVideoPraise.setText(getString(R.string.Praise));
            }
            if (!TextUtils.isEmpty(PlayUrl)){
                mViewBinding.KLVideoTip.setVisibility(View.GONE);
                mPlayer.startVodPlay(PlayUrl.toString());
                mViewBinding.KLVideoBar.NoticeChangeState(false);
            }
            Log.d(TAG, "视频："+event.getDetailsObj().getVideoDetail().getCategoryId());
            if (!event.getDetailsObj().getVideoDetail().isLearn()) {
                NoticeKLineLearnEvent noticeKLineLearnEvent = new NoticeKLineLearnEvent(true, 1, event.getDetailsObj().getVideoDetail().getId(), CID);
                EventBus.getDefault().post(noticeKLineLearnEvent);
            }
            mListAdapter.setList(event.getDetailsObj().getVideoList());
            if (TextUtils.isEmpty(event.getDetailsObj().getVideoDetail().getFxsm())){
                mViewBinding.KLVideoRisk.setText(getString(R.string.VideoWarm));
            }else {
                mViewBinding.KLVideoRisk.setText(event.getDetailsObj().getVideoDetail().getFxsm());
            }
        }else {
            Toast.makeText(KLineVideoActivity.this, event.getError(), Toast.LENGTH_SHORT).show();
        }
        mViewBinding.KLVideoLoad.setVisibility(View.GONE);
    }

    /*
    * 收到点赞更新
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnGuBbKLinePraiseEvent(GuBbKLinePraiseEvent event){
        if (event.isFlag()){
            Log.d(TAG, "接收到K线秘籍视频点赞更新");
            if (event.isPraise()){
                Glide.with(this).load(ActivityCompat.getDrawable(this, R.drawable.is_praise))
                        .fitCenter().into(mViewBinding.KLVideoPraiseImg);
                mViewBinding.KLVideoPraise.setText(String.valueOf(event.getPraiseNum()));
            }else {
                Glide.with(this).load(ActivityCompat.getDrawable(this, R.drawable.un_praise))
                        .fitCenter().into(mViewBinding.KLVideoPraiseImg);
                mViewBinding.KLVideoPraise.setText(getString(R.string.Praise));
            }
        }
    }

}