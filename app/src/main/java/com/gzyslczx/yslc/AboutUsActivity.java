package com.gzyslczx.yslc;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.gzyslczx.yslc.databinding.ActivityAboutUsBinding;
import com.gzyslczx.yslc.databinding.YesNoSelectorLayoutBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AboutUsActivity extends BaseActivity<ActivityAboutUsBinding> implements View.OnClickListener {

    private final String TAG = "AboutUsAct";

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        mViewBinding.AboutUsUserXY.setOnClickListener(this::onClick);
        mViewBinding.AboutUsPrivateXY.setOnClickListener(this::onClick);
        mViewBinding.AboutUsThird.setOnClickListener(this::onClick);
        mViewBinding.AboutUsLogout.setOnClickListener(this::onClick);
        mViewBinding.AboutUsUserXYRightImg.setOnClickListener(this::onClick);
        mViewBinding.AboutUsPrivateXYRightImg.setOnClickListener(this::onClick);
        mViewBinding.AboutUsThirdRightImg.setOnClickListener(this::onClick);
        mViewBinding.AboutUsLogoutImg.setOnClickListener(this::onClick);
        SetupBackClicked();
        if (SpTool.Instance(this).IsGuBbLogin()){
            mViewBinding.AboutUsLogout.setVisibility(View.VISIBLE);
            mViewBinding.AboutUsLogoutImg.setVisibility(View.VISIBLE);
        }else {
            mViewBinding.AboutUsLogout.setVisibility(View.GONE);
            mViewBinding.AboutUsLogoutImg.setVisibility(View.GONE);
        }
    }

    private void SetupBackClicked() {
        mViewBinding.AboutUsToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AboutUsUserXY:
            case R.id.AboutUsUserXYRightImg:
                Intent UserXYIntent = new Intent(this, WebActivity.class);
                UserXYIntent.putExtra(WebActivity.WebKey, ConnPath.UserXY);
                startActivity(UserXYIntent);
                break;
            case R.id.AboutUsPrivateXY:
            case R.id.AboutUsPrivateXYRightImg:
                Intent PrivateXYIntent = new Intent(this, WebActivity.class);
                PrivateXYIntent.putExtra(WebActivity.WebKey, ConnPath.PrivateXY);
                startActivity(PrivateXYIntent);
                break;
            case R.id.AboutUsThird:
            case R.id.AboutUsThirdRightImg:
                Intent ThirdXYIntent = new Intent(this, WebActivity.class);
                ThirdXYIntent.putExtra(WebActivity.WebKey, ConnPath.ThirdUserUrl);
                startActivity(ThirdXYIntent);
                break;
            case R.id.AboutUsLogout:
            case R.id.AboutUsLogoutImg:
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                YesNoSelectorLayoutBinding yesNoSelectorLayoutBinding = YesNoSelectorLayoutBinding.bind(LayoutInflater.from(this).inflate(R.layout.yes_no_selector_layout, null));
                yesNoSelectorLayoutBinding.SelectorMessage.setText(String.format("亲，%s。请认真考虑是否继续前往注销？", getString(R.string.LogoutPageTip)));
                yesNoSelectorLayoutBinding.SelectorMessage.setGravity(Gravity.LEFT);
                yesNoSelectorLayoutBinding.SelectorNo.setTextColor(ActivityCompat.getColor(this, R.color.black_333));
                yesNoSelectorLayoutBinding.SelectorYes.setTextColor(ActivityCompat.getColor(this, R.color.gray_999));
                yesNoSelectorLayoutBinding.SelectorYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //点击是的
                        startActivity(new Intent(AboutUsActivity.this, LogoutActivity.class));
                        if (alertDialog!=null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
                yesNoSelectorLayoutBinding.SelectorNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //点击取消
                        if (alertDialog!=null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
                WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                layoutParams.height = DisplayTool.dp2px(this, 160);
                layoutParams.gravity = Gravity.CENTER;
                alertDialog.getWindow().setAttributes(layoutParams);
                alertDialog.setContentView(yesNoSelectorLayoutBinding.getRoot());
                break;
        }
    }

    /*
    * 接收登录更新
    * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        Log.d(TAG, "接收到登录更新");
        if (!event.isLogin()){
            mViewBinding.AboutUsLogout.setVisibility(View.GONE);
            mViewBinding.AboutUsLogoutImg.setVisibility(View.GONE);
        }
    }

}