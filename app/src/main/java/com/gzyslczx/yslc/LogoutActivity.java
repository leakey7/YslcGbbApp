package com.gzyslczx.yslc;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.gzyslczx.yslc.databinding.ActivityLogoutBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.LogoutCodeEvent;
import com.gzyslczx.yslc.events.LogoutEvent;
import com.gzyslczx.yslc.presenter.LogoutPresenter;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.interfaces.CountDownTimeIfs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.rxjava3.disposables.Disposable;

public class LogoutActivity extends BaseActivity<ActivityLogoutBinding> implements View.OnClickListener, CountDownTimeIfs {

    private LogoutPresenter mPresenter;
    private final String TAG = "LogoutAct";
    private Disposable disposable;
    private boolean isGetCode = false;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityLogoutBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
        EventBus.getDefault().register(this);
    }

    @Override
    void InitView() {
        mViewBinding.logoutPhone.setText(SpTool.Instance(this).GetInfo(SpTool.UserPhone));
        mViewBinding.logoutBtn.setOnClickListener(this::onClick);
        mViewBinding.logoutGetCode.setOnClickListener(this::onClick);
        mViewBinding.logoutClose.setOnClickListener(this::onClick);
        mPresenter = new LogoutPresenter();
    }

    @Override
    protected void onDestroy() {
        if (disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 点击事件
    * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logout_btn:
                //确认注销
                if (TextUtils.isEmpty(mViewBinding.logoutCode.getText().toString().trim())){
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                }else {
                    mPresenter.RequestLogout(this, this, mViewBinding.logoutCode.getText().toString().trim());
                }
                break;
            case R.id.logout_get_code:
                //获取验证码
                if (!isGetCode) {
                    mPresenter.RequestLogoutCode(this, this, mViewBinding.logoutPhone.getText().toString());
                }
                break;
            case R.id.logout_close:
                //退出
                finish();
                break;
        }
    }


    /*
    * 接收验证码
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnLogoutCodeEvent(LogoutCodeEvent event){
        Log.d(TAG, "接收到注销验证码");
        if (event.isFlag()){
            disposable = mPresenter.CountDownTime(0, 90,
                    1, this, this);

            isGetCode = true;
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * 倒计时中
    * */
    @Override
    public void OnCountDown(int time) {
        mViewBinding.logoutGetCode.setClickable(false);
        mViewBinding.logoutGetCode.setTextColor(ActivityCompat.getColor(this, R.color.gray_666));
        mViewBinding.logoutGetCode.setText(time + "s");
        if (mViewBinding.logoutGetCode.isEnabled()) {
            mViewBinding.logoutGetCode.setEnabled(false);
        }
    }

    /*
    * 倒计时结束
    * */
    @Override
    public void OnComplete() {
        mViewBinding.logoutGetCode.setClickable(true);
        mViewBinding.logoutGetCode.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
        isGetCode = false;
        mViewBinding.logoutGetCode.setText("重新获取");
    }

    /*
    * 接收注销账号
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnLogoutUserEvent(LogoutEvent event){
        if (event.isFlag()){
            SpTool.Instance(this).ClearInfo();
            GuBbChangeLoginEvent changeLoginEvent = new GuBbChangeLoginEvent(false);
            EventBus.getDefault().post(changeLoginEvent);
            mViewBinding.logoutBtn.setClickable(false);
            mViewBinding.logoutGetCode.setClickable(false);
        }
        Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
    }

}