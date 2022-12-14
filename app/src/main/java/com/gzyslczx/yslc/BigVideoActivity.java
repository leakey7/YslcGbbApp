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
        //????????????
        SetupPlayerListener();
        //???????????????
        mViewBinding.FVideoView.setOnClickListener(this::onClick);
        //????????????
        mViewBinding.FVideoViewTip.setOnClickListener(this::onClick);
        //????????????
        SetupBackClicked();
        //????????????
        mViewBinding.FVideoHis.setOnClickListener(this::onClick);
        //????????????
        mViewBinding.FVideoFocus.setOnClickListener(this::onClick);
        //????????????
        mViewBinding.FVideoPraise.setOnClickListener(this::onClick);
        //????????????
        mViewBinding.FVideoShare.setOnClickListener(this::onClick);
        //?????????????????????
        mPlayer.setPlayerView(mViewBinding.FVideoView);
        //?????????????????????
        mViewBinding.FVideoViewBar.SetNormal(this);
        FVideoID = new StringBuilder();
        PlayUrl = new StringBuilder();
        TeacherID = new StringBuilder();
        animationDrawable = (AnimationDrawable) mViewBinding.FVideoPraise.getBackground();
        mHistoryAdapter = new BigVideoHistoryAdapter(this);
        FVideoID.replace(0, FVideoID.length(), getIntent().getStringExtra(BidVideoKey));
        VideoPos = getIntent().getIntExtra(TeacherVideoPos, -1);
        mViewBinding.FVideoViewTip.setVisibility(View.VISIBLE);
        mViewBinding.FVideoViewTip.setText("?????????");
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
        mViewBinding.FVideoViewTip.setText("?????????");
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
            //????????????
            SetupScreenFullPlay();
        } else if (newConfig.orientation == newConfig.ORIENTATION_PORTRAIT) {
            //????????????
            SetupScreenSmallPlay();
        }
    }

    /*
     * ??????????????????
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
     * ????????????
     * */
    private void SetupPlayerListener() {
        mPlayer.setVodListener(new ITXVodPlayListener() {
            @Override
            public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle param) {
                if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                    // ????????????, ????????????
                    int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                    mViewBinding.FVideoViewBar.SetCurrentProgress(progress);
                    // ????????????, ????????????
                    int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                    mViewBinding.FVideoViewBar.SetMaxProgress(duration);
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    mViewBinding.FVideoViewBar.ChangePlayState(true);
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                    mViewBinding.FVideoViewTip.setVisibility(View.VISIBLE);
                    mViewBinding.FVideoViewTip.setText("?????????...");
                }
                if (event == TXLiveConstants.PLAY_EVT_VOD_LOADING_END) {
                    mViewBinding.FVideoViewTip.setVisibility(View.GONE);
                }
                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                    ReConnect = true;
                    mViewBinding.FVideoViewTip.setVisibility(View.VISIBLE);
                    mViewBinding.FVideoViewTip.setText("?????????????????????????????????");
                    mViewBinding.FVideoViewTip.setClickable(true);
                }
            }

            @Override
            public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {
            }
        });
    }

    /*
     * GridView????????????
     * */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mHistoryPopup.dismiss();
        Intent intent = new Intent(this, BigVideoActivity.class);
        intent.putExtra(BidVideoKey, mHistoryAdapter.getDataList().get(i).getNewsId());
        startActivity(intent);
    }

    /*
     * ????????????
     * */
    @Override
    public void OnStop() {
        mPlayer.pause();
    }

    /*
     * ????????????
     * */
    @Override
    public void OnStart() {
        mPlayer.resume();
    }

    /*
     * ????????????
     * */
    @Override
    public void OnReStart() {
        mPlayer.startVodPlay(PlayUrl.toString());
    }

    /*
     * ????????????
     * */
    @Override
    public void OnFullScreen() {
        ChangeScreenWithVisi(ChangeScreen());
    }

    /*
     * ????????????
     * */
    @Override
    public void OnSmallScreen() {
        ChangeScreenWithVisi(ChangeScreen());
    }

    /*
     * ??????????????????
     * */
    @Override
    public void ChangeProgress(int progress) {
        mPlayer.seek(progress);
    }

    /*
     * ??????????????????????????????
     * */
    private void SetupScreenFullPlay() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mViewBinding.ParentLayout);
        //????????????????????????
        set.setDimensionRatio(mViewBinding.FVideoView.getId(), "null");
        set.connect(mViewBinding.FVideoView.getId(), ConstraintSet.TOP, mViewBinding.ParentLayout.getId(), ConstraintSet.TOP, 0);
        set.connect(mViewBinding.FVideoView.getId(), ConstraintSet.BOTTOM, mViewBinding.ParentLayout.getId(), ConstraintSet.BOTTOM);
        TransitionManager.beginDelayedTransition(mViewBinding.ParentLayout);
        set.applyTo(mViewBinding.ParentLayout);
    }

    /*
     * ??????????????????????????????
     * */
    private void SetupScreenSmallPlay() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mViewBinding.ParentLayout);
        //????????????????????????
        set.setDimensionRatio(mViewBinding.FVideoView.getId(), "h,1.6:1");
        set.connect(mViewBinding.FVideoView.getId(), ConstraintSet.TOP, mViewBinding.FVideoTitle.getId(), ConstraintSet.BOTTOM,
                DisplayTool.dp2px(this, 14));
        set.clear(mViewBinding.FVideoView.getId(), ConstraintSet.BOTTOM);
        TransitionManager.beginDelayedTransition(mViewBinding.ParentLayout);
        set.applyTo(mViewBinding.ParentLayout);
    }

    /*
     * ??????????????????????????????????????????
     * */
    private void ChangeScreenWithVisi(int state) {
        if (state == 0) {
            //??????
            Log.d(TAG, "????????????");
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
            //??????
            Log.d(TAG, "????????????");
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
     * ????????????
     * */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FVideoView:
                //?????????????????????????????????
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
                    //?????????????????????
                    Log.d(TAG, "?????????????????????");
                    Request = false;
                    isConnect = true;
                    mPresenter.RequestDetail(this, this, FVideoID.toString());
                } else if (ReConnect) {
                    //???????????????????????????????????????
                    Log.d(TAG, "????????????????????????");
                    ReConnect = false;
                    mPlayer.startVodPlay(PlayUrl.toString());
                }
                mViewBinding.FVideoViewTip.setText("????????????...");
                mViewBinding.FVideoViewTip.setClickable(false);
                break;
            case R.id.FVideoHis:
                //????????????
                if (!isConnect) {
                    ShowHistoryView(false);
                }
                break;
            case R.id.FVideoFocus:
                //????????????
                if (SpTool.Instance(this).IsGuBbLogin() && !TextUtils.isEmpty(TeacherID)) {
                    //????????????????????????
                    NormalActionTool.FocusAction(this, null, this, TeacherID.toString(), true, TAG);
                } else if (!SpTool.Instance(this).IsGuBbLogin()) {
                    //????????????????????????
                    NormalActionTool.LoginAction(this, null, this, null, TAG);
                }
                break;
            case R.id.FVideoPraise:
                //????????????
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
                //????????????
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
     * ??????????????????
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
     * ??????????????????
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
     * ??????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeFocusEvent(GuBbChangeFocusEvent event) {
        if (event.isFlag()) {
            Log.d(TAG, "?????????????????????");
            ChangeFocusState(event.isFocus());
        }
    }

    /*
     * ??????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDetailEvent(GuBbTeacherDetailEvent event) {
        Log.d(TAG, "?????????????????????");
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
            mViewBinding.FVideoViewTip.setText("????????????,????????????");
            mViewBinding.FVideoViewTip.setClickable(true);
        }
    }

    /*
     * ????????????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event) {
        //??????????????????
        Log.d(TAG, "?????????????????????");
        mPresenter.RequestDetail(this, this, FVideoID.toString());
        if (mPlayer.isPlaying()) {
            ChangeLoginState = true;
        }
    }

}