package com.gzyslczx.yslc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.gzyslczx.yslc.databinding.ActivityBigVideoBinding;
import com.gzyslczx.yslc.databinding.GridViewLayoutBinding;
import com.gzyslczx.yslc.events.GuBbChangeFocusEvent;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbTeacherDetailEvent;
import com.gzyslczx.yslc.presenter.TeacherArticlePresenter;
import com.gzyslczx.yslc.tools.BigVideoHistoryAdapter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.OnVideoPlayState;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BigVideoActivity extends BaseActivity<ActivityBigVideoBinding> implements OnVideoPlayState, AdapterView.OnItemClickListener, View.OnClickListener {

    private final String TAG = "BidVideoAct";
    private TeacherArticlePresenter mPresenter;
    public static final String BidVideoKey = "BVideID";
    public static final String TeacherVideoPos = "TVideoPos";
    private StringBuilder FVideoID, PlayUrl, TeacherID;
    private int VideoPos;
    private TXVodPlayer mPlayer;
    private boolean ReConnect = false, Request = false, ChangeLoginState = false, isConnect = true;
    private GridViewLayoutBinding mHistoryBinding;
    private PopupWindow mHistoryPopup;
    private BigVideoHistoryAdapter mHistoryAdapter;
    private AnimationDrawable animationDrawable;
    private SharePopup sharePopup;
    private boolean isPraise=false;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityBigVideoBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.black));
        TransStatusTool.setStatusBarDarkMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        mPlayer = new TXVodPlayer(this);
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mPlayer.setAutoPlay(true);
        //播放监听
        SetupPlayerListener();
        //收放进度条
        mViewBinding.FVideoView.setOnClickListener(this::onClick);
        //点击重试
        mViewBinding.FVideoViewTip.setOnClickListener(this::onClick);
        //回退点击
        SetupBackClicked();
        //往期点击
        mViewBinding.FVideoHis.setOnClickListener(this::onClick);
        //关注点击
        mViewBinding.FVideoFocus.setOnClickListener(this::onClick);
        //点赞点击
        mViewBinding.FVideoPraise.setOnClickListener(this::onClick);
        //分享点击
        mViewBinding.FVideoShare.setOnClickListener(this::onClick);
        //初始化播放控件
        mPlayer.setPlayerView(mViewBinding.FVideoView);
        //关联视频进度条
        mViewBinding.FVideoViewBar.SetNormal(this);
        FVideoID = new StringBuilder();
        PlayUrl = new StringBuilder();
        TeacherID = new StringBuilder();
        animationDrawable = (AnimationDrawable) mViewBinding.FVideoPraise.getBackground();
        mHistoryAdapter = new BigVideoHistoryAdapter(this);
        FVideoID.replace(0, FVideoID.length(), getIntent().getStringExtra(BidVideoKey));
        VideoPos = getIntent().getIntExtra(TeacherVideoPos, -1);
        mViewBinding.FVideoViewTip.setVisibility(View.VISIBLE);
        mViewBinding.FVideoViewTip.setText("连接中");
        mPresenter = new TeacherArticlePresenter();
        isConnect = true;
        mPresenter.RequestDetail(this, this, FVideoID.toString());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mPlayer.isPlaying()) {
            mPlayer.stopPlay(false);
        }
        FVideoID.replace(0, FVideoID.length(), intent.getStringExtra(BidVideoKey));
        isPraise=false;
        isConnect = true;
        mPresenter.RequestDetail(this, this, FVideoID.toString());
        mViewBinding.FVideoViewTip.setVisibility(View.VISIBLE);
        mViewBinding.FVideoViewBar.ReSetMax();
        mViewBinding.FVideoViewTip.setText("连接中");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            ChangeScreenWithVisi(ChangeScreen());
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mPlayer.stopPlay(true);
        mViewBinding.FVideoView.onDestroy();
        super.onDestroy();
        if (sharePopup != null) {
            sharePopup.Clear();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer.isPlaying()) {
            mViewBinding.FVideoViewBar.NoticeChangeState(true);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == newConfig.ORIENTATION_LANDSCAPE) {
            //满屏播放
            SetupScreenFullPlay();
        } else if (newConfig.orientation == newConfig.ORIENTATION_PORTRAIT) {
            //小屏播放
            SetupScreenSmallPlay();
        }
    }

    /*
     * 设置回退点击
     * */
    private void SetupBackClicked() {
        mViewBinding.FVideoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    /*
     * 播放监听
     * */
    private void SetupPlayerListener() {
        mPlayer.setVodListener(new ITXVodPlayListener() {
            @Override
            public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle param) {
                if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                    // 播放进度, 单位是秒
                    int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                    mViewBinding.FVideoViewBar.SetCurrentProgress(progress);
                    // 视频总长, 单位是秒
                    int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                    mViewBinding.FVideoViewBar.SetMaxProgress(duration);
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    mViewBinding.FVideoViewBar.ChangePlayState(true);
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                    mViewBinding.FVideoViewTip.setVisibility(View.VISIBLE);
                    mViewBinding.FVideoViewTip.setText("加载中...");
                }
                if (event == TXLiveConstants.PLAY_EVT_VOD_LOADING_END) {
                    mViewBinding.FVideoViewTip.setVisibility(View.GONE);
                }
                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                    ReConnect = true;
                    mViewBinding.FVideoViewTip.setVisibility(View.VISIBLE);
                    mViewBinding.FVideoViewTip.setText("获取视频失败，点击重试");
                    mViewBinding.FVideoViewTip.setClickable(true);
                }
            }

            @Override
            public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {
            }
        });
    }

    /*
     * GridView条目点击
     * */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mHistoryPopup.dismiss();
        Intent intent = new Intent(this, BigVideoActivity.class);
        intent.putExtra(BidVideoKey, mHistoryAdapter.getDataList().get(i).getNewsId());
        startActivity(intent);
    }

    /*
     * 播放停止
     * */
    @Override
    public void OnStop() {
        mPlayer.pause();
    }

    /*
     * 播放开始
     * */
    @Override
    public void OnStart() {
        mPlayer.resume();
    }

    /*
     * 播放重开
     * */
    @Override
    public void OnReStart() {
        mPlayer.startVodPlay(PlayUrl.toString());
    }

    /*
     * 设置满屏
     * */
    @Override
    public void OnFullScreen() {
        ChangeScreenWithVisi(ChangeScreen());
    }

    /*
     * 设置小屏
     * */
    @Override
    public void OnSmallScreen() {
        ChangeScreenWithVisi(ChangeScreen());
    }

    /*
     * 更新播放进度
     * */
    @Override
    public void ChangeProgress(int progress) {
        mPlayer.seek(progress);
    }

    /*
     * 满屏播放控件位置设置
     * */
    private void SetupScreenFullPlay() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mViewBinding.ParentLayout);
        //设置播放控件位置
        set.setDimensionRatio(mViewBinding.FVideoView.getId(), "null");
        set.connect(mViewBinding.FVideoView.getId(), ConstraintSet.TOP, mViewBinding.ParentLayout.getId(), ConstraintSet.TOP, 0);
        set.connect(mViewBinding.FVideoView.getId(), ConstraintSet.BOTTOM, mViewBinding.ParentLayout.getId(), ConstraintSet.BOTTOM);
        TransitionManager.beginDelayedTransition(mViewBinding.ParentLayout);
        set.applyTo(mViewBinding.ParentLayout);
    }

    /*
     * 小屏播放控件位置设置
     * */
    private void SetupScreenSmallPlay() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mViewBinding.ParentLayout);
        //设置播放控件位置
        set.setDimensionRatio(mViewBinding.FVideoView.getId(), "h,1.6:1");
        set.connect(mViewBinding.FVideoView.getId(), ConstraintSet.TOP, mViewBinding.FVideoTitle.getId(), ConstraintSet.BOTTOM,
                DisplayTool.dp2px(this, 14));
        set.clear(mViewBinding.FVideoView.getId(), ConstraintSet.BOTTOM);
        TransitionManager.beginDelayedTransition(mViewBinding.ParentLayout);
        set.applyTo(mViewBinding.ParentLayout);
    }

    /*
     * 更换播放屏大小隐藏非必要控件
     * */
    private void ChangeScreenWithVisi(int state) {
        if (state == 0) {
            //小屏
            Log.d(TAG, "切换小屏");
            mViewBinding.FVideoToolBar.setVisibility(View.VISIBLE);
            mViewBinding.FVideoTitle.setVisibility(View.VISIBLE);
            mViewBinding.FVideoReadNum.setVisibility(View.VISIBLE);
            mViewBinding.FVideoReadTag.setVisibility(View.VISIBLE);
            mViewBinding.FVideoToolBarCons.setVisibility(View.VISIBLE);
            mViewBinding.FVideoHeadImg.setVisibility(View.VISIBLE);
            mViewBinding.FVideoName.setVisibility(View.VISIBLE);
            mViewBinding.FVideoFocus.setVisibility(View.VISIBLE);
            mViewBinding.FVideoHis.setVisibility(View.VISIBLE);
            mViewBinding.FVideoRisk.setVisibility(View.VISIBLE);
            mViewBinding.FVideoTalk.setVisibility(View.VISIBLE);
            mViewBinding.FVideoPraise.setVisibility(View.VISIBLE);
            mViewBinding.FVideoShare.setVisibility(View.VISIBLE);
            mViewBinding.FVideoViewBar.setPadding(0, 0, 0, 0);
        } else {
            //满屏
            Log.d(TAG, "切换大屏");
            mViewBinding.FVideoToolBar.setVisibility(View.GONE);
            mViewBinding.FVideoTitle.setVisibility(View.GONE);
            mViewBinding.FVideoReadNum.setVisibility(View.GONE);
            mViewBinding.FVideoReadTag.setVisibility(View.GONE);
            mViewBinding.FVideoToolBarCons.setVisibility(View.GONE);
            mViewBinding.FVideoHeadImg.setVisibility(View.GONE);
            mViewBinding.FVideoName.setVisibility(View.GONE);
            mViewBinding.FVideoFocus.setVisibility(View.GONE);
            mViewBinding.FVideoHis.setVisibility(View.GONE);
            mViewBinding.FVideoRisk.setVisibility(View.GONE);
            mViewBinding.FVideoTalk.setVisibility(View.GONE);
            mViewBinding.FVideoPraise.setVisibility(View.GONE);
            mViewBinding.FVideoShare.setVisibility(View.GONE);
            int size = DisplayTool.dp2px(this, TransStatusTool.getStatusbarHeight(this));
            mViewBinding.FVideoViewBar.setPadding(size, 0, size, 0);
        }
    }

    /*
     * 点击事件
     * */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FVideoView:
                //点击播放屏幕收放进度条
                if (mViewBinding.FVideoViewBar.getTag().equals("VISI")) {
                    mViewBinding.FVideoViewBar.setTag("GONE");
                    mViewBinding.FVideoViewBar.SetProgressOut();
                    mViewBinding.FVideoViewBar.setVisibility(View.GONE);
                } else {
                    mViewBinding.FVideoViewBar.setTag("VISI");
                    mViewBinding.FVideoViewBar.SetProgressIn();
                    mViewBinding.FVideoViewBar.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.FVideoViewTip:
                if (Request) {
                    //请求失败下重试
                    Log.d(TAG, "请求失败下重试");
                    Request = false;
                    isConnect = true;
                    mPresenter.RequestDetail(this, this, FVideoID.toString());
                } else if (ReConnect) {
                    //请求成功但获取视频失败重连
                    Log.d(TAG, "获取视频失败重连");
                    ReConnect = false;
                    mPlayer.startVodPlay(PlayUrl.toString());
                }
                mViewBinding.FVideoViewTip.setText("正在重试...");
                mViewBinding.FVideoViewTip.setClickable(false);
                break;
            case R.id.FVideoHis:
                //点击往期
                if (!isConnect) {
                    ShowHistoryView(false);
                }
                break;
            case R.id.FVideoFocus:
                //点击关注
                if (SpTool.Instance(this).IsGuBbLogin() && !TextUtils.isEmpty(TeacherID)) {
                    //已登录，关注操作
                    NormalActionTool.FocusAction(this, null, this, TeacherID.toString(), true, TAG);
                } else if (!SpTool.Instance(this).IsGuBbLogin()) {
                    //未登录，前往登录
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
            case R.id.FVideoPraise:
                //点击点赞
                if (SpTool.Instance(this).IsGuBbLogin()) {
                    if (FVideoID.length()>0 && !isPraise) {
                        NormalActionTool.PraiseAction(this, null, this, VideoPos, FVideoID.toString(), true, TAG);
                    }
                    if (animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    animationDrawable.start();
                } else {
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
            case R.id.FVideoShare:
                //点击分享
                if (FVideoID != null && !TextUtils.isEmpty(FVideoID.toString()))
                    if (sharePopup == null) {
                        sharePopup = new SharePopup(this, mViewBinding.getRoot(), true);
                    }
                sharePopup.setUrlPath(ConnPath.BigVideoShareUrl + FVideoID.toString());
                sharePopup.setTitle(mViewBinding.FVideoTitle.getText().toString());
                sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    /*
     * 显示往期列表
     * */
    private void ShowHistoryView(boolean update) {
        if (update) {
            if (mHistoryPopup == null) {
                mHistoryBinding = GridViewLayoutBinding.bind(getLayoutInflater().inflate(R.layout.grid_view_layout, null));
                mHistoryPopup = new PopupWindow(mHistoryBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mHistoryBinding.gridView.setAdapter(mHistoryAdapter);
                mHistoryBinding.gridView.setOnItemClickListener(this::onItemClick);
                mHistoryPopup.setFocusable(true);
                return;
            }
            return;
        }
        if (mHistoryPopup != null) {
            mHistoryPopup.showAtLocation(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
        }
    }

    /*
     * 显示关注状态
     * */
    private void ChangeFocusState(boolean isFocus) {
        if (isFocus) {
            mViewBinding.FVideoFocus.setBackground(ActivityCompat.getDrawable(this, R.drawable.gray_focus_radius_10_shape));
            mViewBinding.FVideoFocus.setTextColor(ActivityCompat.getColor(this, R.color.gray_666));
            mViewBinding.FVideoFocus.setText(getString(R.string.IsFocus));
        } else {
            mViewBinding.FVideoFocus.setBackground(ActivityCompat.getDrawable(this, R.drawable.red_corner_10_shape));
            mViewBinding.FVideoFocus.setTextColor(ActivityCompat.getColor(this, R.color.white));
            mViewBinding.FVideoFocus.setText(getString(R.string.AddFocus));
        }
        mViewBinding.FVideoFocus.setVisibility(View.VISIBLE);
    }

    /*
     * 接收关注更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeFocusEvent(GuBbChangeFocusEvent event) {
        if (event.isFlag()) {
            Log.d(TAG, "接收到关注更新");
            ChangeFocusState(event.isFocus());
        }
    }

    /*
     * 接收资讯详情
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDetailEvent(GuBbTeacherDetailEvent event) {
        Log.d(TAG, "接收到资讯详情");
        if (event.isFlag()) {
            if (ChangeLoginState) {
                ChangeLoginState = false;
                ChangeFocusState(event.getData().isFocus());
                isPraise=event.getData().isLike();
            } else {
                mViewBinding.FVideoViewTip.setVisibility(View.GONE);
                PlayUrl.replace(0, PlayUrl.length(), event.getData().getFileUrl());
                mPlayer.startVodPlay(PlayUrl.toString());
                mViewBinding.FVideoViewBar.NoticeChangeState(false);
                Glide.with(this).load(event.getData().getAuthorImg())
                        .placeholder(ActivityCompat.getDrawable(this, R.drawable.head_img))
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new FitCenter(),
                                new RoundedCorners(DisplayTool.dp2px(this, 32)))))
                        .into(mViewBinding.FVideoHeadImg);
                mViewBinding.FVideoName.setText(event.getData().getAuthor());
                mViewBinding.FVideoTitle.setText(event.getData().getTitle());
                mViewBinding.FVideoReadNum.setText(String.valueOf(event.getData().getReading()));
                mViewBinding.FVideoRisk.setText(event.getData().getRiskTips());
                isPraise=event.getData().isLike();
                if (event.getData().getVideoList() != null && event.getData().getVideoList().size() > 0) {
                    Log.d(TAG, new Gson().toJson(event.getData().getVideoList()));
                    mViewBinding.FVideoHis.setVisibility(View.VISIBLE);
                    mHistoryAdapter.setDataList(event.getData().getVideoList());
                    ShowHistoryView(true);
                } else {
                    mViewBinding.FVideoHis.setVisibility(View.GONE);
                }
                ChangeFocusState(event.getData().isFocus());
                TeacherID.replace(0, TeacherID.length(), event.getData().getUserId());
            }
            isConnect = false;
        } else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
            Request = true;
            mViewBinding.FVideoViewTip.setVisibility(View.VISIBLE);
            mViewBinding.FVideoViewTip.setText("连接异常,点击重试");
            mViewBinding.FVideoViewTip.setClickable(true);
        }
    }

    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event) {
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        mPresenter.RequestDetail(this, this, FVideoID.toString());
        if (mPlayer.isPlaying()) {
            ChangeLoginState = true;
        }
    }

}