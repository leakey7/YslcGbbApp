package com.gzyslczx.yslc;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.gzyslczx.yslc.databinding.ActivityLoginBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.LoginCodeEvent;
import com.gzyslczx.yslc.presenter.LoginPresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.interfaces.CountDownTimeIfs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.rxjava3.disposables.Disposable;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener, CountDownTimeIfs {

    private final String TAG = "LoginAct";
    private LoginPresenter mPresenter;
    private boolean isGetCode = false;
    private Disposable disposable;
    private boolean isCheck = false;
    private String StockCodeInWeb;
    public static final String WebStockCodeKey = "StockCodeKey";

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        StockCodeInWeb = getIntent().getStringExtra(StockCodeInWeb);
        mViewBinding.loginClose.setOnClickListener(this::onClick);
        mViewBinding.loginGetCode.setOnClickListener(this::onClick);
        mViewBinding.loginBtn.setOnClickListener(this::onClick);
        mViewBinding.loginCheck.setOnClickListener(this::onClick);
        mViewBinding.loginCheckUserXY.setOnClickListener(this::onClick);
        mViewBinding.loginCheckPrivateXY.setOnClickListener(this::onClick);
        mPresenter = new LoginPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        if (disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_close:
                //??????????????????
                finish();
                break;
            case R.id.login_get_code:
                //???????????????
                String phone = mViewBinding.loginPhone.getText().toString().trim();
                if (mPresenter.CheckPhone(this, phone)) {
                    //???????????????????????????????????????
                    mPresenter.RequestLoginCode(this,this, phone);
                }
                break;
            case R.id.login_btn:
                //??????????????????
                String phoneNum = mViewBinding.loginPhone.getText().toString().trim();
                if (isCheck) {
                    if (mPresenter.CheckPhone(this, phoneNum)) {
                        String codeNum = mViewBinding.loginCode.getText().toString().trim();
                        if (mPresenter.CheckCode(this, codeNum, isGetCode)) {
                            //??????
                            mPresenter.RequestLogin(this, this, phoneNum, codeNum, StockCodeInWeb);
                        }
                    }
                } else {
                    Toast.makeText(this, "?????????????????????????????????????????????????????????????????????????????????",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_check:
                //????????????
                if (isCheck){
                    isCheck=false;
                    Glide.with(this).load(ActivityCompat.getDrawable(this, R.drawable.unselect)).fitCenter().into(mViewBinding.loginCheck);
                }else {
                    isCheck = true;
                    Glide.with(this).load(ActivityCompat.getDrawable(this, R.drawable.selected)).fitCenter().into(mViewBinding.loginCheck);
                }
                break;
            case R.id.login_checkUserXY:
                //??????????????????
                Intent UserXYIntent = new Intent(this, WebActivity.class);
                UserXYIntent.putExtra(WebActivity.WebKey, ConnPath.UserXY);
                startActivity(UserXYIntent);
                break;
            case R.id.login_checkPrivateXY:
                //??????????????????
                Intent PrivateXYIntent = new Intent(this, WebActivity.class);
                PrivateXYIntent.putExtra(WebActivity.WebKey, ConnPath.PrivateXY);
                startActivity(PrivateXYIntent);
                break;
        }
    }

    @Override
    public void OnCountDown(int time) {
        mViewBinding.loginGetCode.setClickable(false);
        mViewBinding.loginGetCode.setTextColor(ActivityCompat.getColor(this, R.color.gray_666));
        mViewBinding.loginGetCode.setText(time + "s");
        if (mViewBinding.loginPhone.isEnabled()) {
            mViewBinding.loginPhone.setEnabled(false);
        }
    }

    @Override
    public void OnComplete() {
        mViewBinding.loginPhone.setEnabled(true);
        mViewBinding.loginGetCode.setClickable(true);
        mViewBinding.loginGetCode.setTextColor(ActivityCompat.getColor(this, R.color.main_red));
        isGetCode = false;
        mViewBinding.loginGetCode.setText("????????????");
    }


    /*
    * ???????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnLoginCodeEvent(LoginCodeEvent event){
        Log.d(TAG, "????????????????????????");
        if (event.isFlag()){
            disposable = mPresenter.CountDownTime(0, 90,
                    1, LoginActivity.this, LoginActivity.this);

            isGetCode = true;
        }else {
            Toast.makeText(LoginActivity.this, event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * ??????????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnLoginEvent(GuBbChangeLoginEvent event){
        if (event.isLogin()){
            finish();
        }else {
            Toast.makeText(LoginActivity.this, event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

}