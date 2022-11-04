package com.gzyslczx.yslc;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.gzyslczx.yslc.tools.JGVerifyLogin;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.yourui.RequestApi;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveBaseListener;
import com.tencent.rtmp.TXVodPlayer;
import com.yourui.sdk.message.YRMarket;

import cn.jiguang.api.utils.JCollectionAuth;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.RequestCallback;
import cn.jpush.android.api.JPushInterface;

public class MyApp extends Application {

    private final String TAG = "MyApp";
    private final String WXAppId = "wx80d5b023a0d518c8";
    public static IWXAPI mIwxapi;
    private final String LicenceUrl = "https://license.vod2.myqcloud.com/license/v2/1252545567_1/v_cube.license";
    private final String LicenceKey = "1b28d89c7df5d0c0af0710dc38edcdc6";

    @Override
    public void onCreate() {
        super.onCreate();
        SpTool.Instance(this);
        Log.d(TAG, "onCreate()");
        InitTXLicence();
//        InitJGVerify();
//        InitJGPush();
        //冷启动极光推送
        JCollectionAuth.setAuth(this, false);
        RegToWx();
//        YRMarket.getInstance().init(this);//初始化友睿行情sdk
//        RequestApi.getInstance().initServer(this);//启动行情服务
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        YRMarket.getInstance().destroy(true);
    }

    /*
    * 腾讯视频授权
    * */
    private void InitTXLicence(){
        TXLiveBase.getInstance().setLicence(this, LicenceUrl, LicenceKey);
        TXLiveBase.setListener(new TXLiveBaseListener() {
            @Override
            public void onLicenceLoaded(int result, String reason) {
                super.onLicenceLoaded(result, reason);
                Log.d(TAG, String.format("TX Licence:result=%d；reason=%s", result, reason));
            }
        });
    }

    /*
     * 初始化极光认证
     * */
    private void InitJGVerify(){
        JVerificationInterface.init(this, 10000, new RequestCallback<String>() {
            @Override
            public void onResult(int i, String s) {
                if (i==8000){
                    Log.d(TAG, "极光认证初始化成功");
                    JGVerifyLogin.PreLogin(MyApp.this);
                }else {
                    Log.d(TAG, String.format("极光认证初始化失败%d:%s", i, s));
                }
            }
        });
        JVerificationInterface.setDebugMode(true);
    }

    //注册微信ID
    private void RegToWx(){
        mIwxapi = WXAPIFactory.createWXAPI(this, WXAppId, true);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mIwxapi.registerApp(WXAppId);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

    /*
    * 初始化极光推送
    * */
    private void InitJGPush(){
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

}
