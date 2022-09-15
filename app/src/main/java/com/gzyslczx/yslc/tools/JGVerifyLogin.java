package com.gzyslczx.yslc.tools;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzyslczx.yslc.LoginActivity;
import com.gzyslczx.yslc.R;

import java.util.ArrayList;
import java.util.List;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.PreLoginListener;
import cn.jiguang.verifysdk.api.PrivacyBean;

public class JGVerifyLogin {

    private static final String TAG = "JGVerify";

    public static void PreLogin(Context context){
        JVerificationInterface.preLogin(context, 5000, new PreLoginListener() {
            @Override
            public void onResult(int i, String s) {
                if (i==7000){
                    Log.d(TAG, String.format("预取号成功:%s", s));
                }else {
                    Log.d(TAG, String.format("预取号失败%d:%s", i, s));
                }
            }
        });
    }

    public static void InitLoginUI(Context context){
        TextView textView = new TextView(context);
        textView.setText("验证码登录");
        textView.setTextColor(context.getResources().getColor(R.color.black_333));
        textView.setTextSize(14);
        RelativeLayout.LayoutParams navBtnParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        navBtnParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        navBtnParam.setMargins(0, DisplayTool.dp2px(context,270), 0, 0);
        textView.setLayoutParams(navBtnParam);
        List<PrivacyBean> privacyBeans = new ArrayList<PrivacyBean>();
        privacyBeans.add(new PrivacyBean("用户协议", ConnPath.UserXY, "、"));
        privacyBeans.add(new PrivacyBean("隐私政策", ConnPath.PrivateXY, "和"));
        JVerifyUIConfig config = new JVerifyUIConfig.Builder()
                .setPrivacyNameAndUrlBeanList(privacyBeans)
                .setPrivacyCheckboxSize(18)
                .setPrivacyOffsetX(16)
                .setCheckedImgPath("check_img")
                .setUncheckedImgPath("uncheck_img")
                .setAppPrivacyColor(0xff333333, 0xff5BA1FB)
                .setPrivacyTextSize(14)
                .setPrivacyWithBookTitleMark(true)
                .setStatusBarColorWithNav(true)
                .setStatusBarDarkMode(true)
                .setNavColor(0xffffffff)
                .setNavReturnImgPath("close_img")
                .setNavReturnBtnWidth(24)
                .setNavReturnBtnHeight(24)
                .setNavReturnBtnOffsetX(16)
                .setNavText("")
                .setLogoHeight(58)
                .setLogoWidth(58)
                .setLogoImgPath("icon_img")
                .setLogoOffsetY(40)
                .setNumberColor(0xff060708)
                .setNumberSize(16)
                .setNumFieldOffsetY(114)
                .setLogBtnImgPath("login_code_btn_shape")
                .setLogBtnTextSize(14)
                .setLogBtnHeight(44)
                .setLogBtnOffsetY(154)
                .enableHintToast(true, Toast.makeText(context, "请同意相关条款协议", Toast.LENGTH_SHORT))
                .setSloganTextColor(0xff9B9C9C)
                .setSloganTextSize(12)
                .setSloganOffsetY(214)
                .addCustomView(textView, true, new JVerifyUIClickCallback() {
                    @Override
                    public void onClicked(Context context, View view) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                })
                .build();
        JVerificationInterface.setCustomUIWithConfig(config);
    }

}
