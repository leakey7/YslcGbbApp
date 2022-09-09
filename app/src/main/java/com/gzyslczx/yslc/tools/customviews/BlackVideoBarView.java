package com.gzyslczx.yslc.tools.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.BlackVideoBarBinding;
import com.gzyslczx.yslc.tools.OnVideoPlayState;

public class BlackVideoBarView extends ConstraintLayout {

    private final String TAG = "VideoBar";
    private BlackVideoBarBinding mViewBinding;
    private AnimationSet out, in;
    private OnVideoPlayState playState;
    private boolean RePlay = false;

    public BlackVideoBarView(@NonNull Context context) {
        super(context);
        InitView();
    }

    public BlackVideoBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitView();
    }

    public BlackVideoBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView();
    }

    public BlackVideoBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        InitView();
    }

    private void InitView() {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.black_video_bar, this);
        mViewBinding = BlackVideoBarBinding.bind(layout);
        out = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.video_bar_out);
        in = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.video_bar_in);
    }

    /*
    * 设置最大进度和时间
    * */
    public void SetMaxProgress(int max) {
        if (mViewBinding.HVideoProgress.getMax()==0) {
            mViewBinding.HVideoProgress.setMax(max);
            mViewBinding.HVideoProgress.setSecondaryProgress(max);
            int min=0, second;
            if (max<60){
                second = max;
            }else {
                min = max/60;
                second = max%60;
            }
            SetTotalPlayTime(String.format("%d:%d", min, second));
        }
    }

    /*
    * 重置视频总长
    * */
    public void ReSetMax(){
        mViewBinding.HVideoProgress.setMax(0);
    }

    /*
    * 设置当前进度和时间
    * */
    public void SetCurrentProgress(int current) {
        mViewBinding.HVideoProgress.setProgress(current);
        int min=0, second=0;
        if (current<60){
            second = current;
        }else {
            min = current / 60;
            second = current % 60;
        }
        SetCurrentPlayTime(String.format("%d:%d", min, second));
    }

    private void SetCurrentPlayTime(String playTime) {
        mViewBinding.HVideoOnTime.setText(playTime);
    }

    private void SetTotalPlayTime(String totalTime) {
        mViewBinding.HVideoSumTime.setText(totalTime);
    }

    public void SetProgressOut() {
        this.startAnimation(out);
    }

    public void SetProgressIn() {
        this.startAnimation(in);
    }

    public void SetPlayState(OnVideoPlayState playState) {
        this.playState = playState;
    }

    /*
    * 播放状态监听
    * */
    public void SetPlayStateListener() {
        mViewBinding.HVideoPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RePlay) {
                    playState.OnReStart();
                    RePlay = false;
                    ChangePlayState(false);
                } else {
                    int state = ChangePlayState(false);
                    if (state == 0) {
                        playState.OnStop();
                    } else {
                        playState.OnStart();
                    }
                }
            }
        });
    }

    public void NoticeChangeState(boolean isPause){
        if (isPause){
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.play)).fitCenter().into(mViewBinding.HVideoPlay);
            mViewBinding.HVideoPlay.setTag("STOP");
            playState.OnStop();
        }else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.stop)).fitCenter().into(mViewBinding.HVideoPlay);
            mViewBinding.HVideoPlay.setTag("START");
            playState.OnStart();
        }
    }

    /*
    * 缩放屏幕点击监听
    * */
    public void SetPlayScaleListener() {
        mViewBinding.HVideoScale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewBinding.HVideoScale.getTag().equals("SMALL")) {
                    Log.d(TAG, "点击切换大屏");
                    mViewBinding.HVideoScale.setTag("FULL");
                    playState.OnFullScreen();
                } else {
                    Log.d(TAG, "点击切换小屏");
                    mViewBinding.HVideoScale.setTag("SMALL");
                    playState.OnSmallScreen();
                }
            }
        });
    }

    /*
    * 进度条监听
    * */
    public void SetProgressListener() {
        mViewBinding.HVideoProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    playState.ChangeProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }




    public void SetNormal(OnVideoPlayState playState) {
        SetPlayState(playState);
        SetPlayScaleListener();
        SetProgressListener();
        SetPlayStateListener();
    }

    public int ChangePlayState(boolean isPlayEnd){
        if (mViewBinding.HVideoPlay.getTag().equals("START")) {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.play)).fitCenter().into(mViewBinding.HVideoPlay);
            mViewBinding.HVideoPlay.setTag("STOP");
            RePlay = isPlayEnd;
            return 0;
        } else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.stop)).fitCenter().into(mViewBinding.HVideoPlay);
            mViewBinding.HVideoPlay.setTag("START");
            return 1;
        }
    }




}
