package com.gzyslczx.yslc.tools.jigunagpush;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.gzyslczx.yslc.HomeActivity;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class JiGuangReceiver extends JPushMessageReceiver {

    private final String TAG = "极光推送服务";

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.d(TAG, String.format("自定义消息：%s", jPushMessage.toString()));
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "打开推送");
        String extras = notificationMessage.notificationExtras;
        Log.d(TAG, extras);
        if (TextUtils.isEmpty(extras)){
            super.onNotifyMessageOpened(context, notificationMessage);
        }else {
            ResponsePush push = new Gson().fromJson(extras, ResponsePush.class);
            Intent intent = new Intent(context, HomeActivity.class);
            if (push.getN_extras()!=null && !TextUtils.isEmpty(push.getN_extras().getUrl())) {
                intent.putExtra(HomeActivity.PushKey, push.getN_extras().getUrl());
                intent.putExtra(HomeActivity.PushTitle, push.getN_title());
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "收到推送");
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage notificationMessage) {
        Log.d(TAG, "推送消失");
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        //注册失败+三方厂商注册回调
        Log.e(TAG,"[onCommandResult] "+cmdMessage);
        //cmd为10000时说明为厂商token回调
        if(cmdMessage!=null&&cmdMessage.cmd==10000&&cmdMessage.extra!=null){
            String token = cmdMessage.extra.getString("token");
            int platform = cmdMessage.extra.getInt("platform");
            String deviceName = "unkown";
            switch (platform){
                case 1:
                    deviceName = "小米";
                    break;
                case 2:
                    deviceName = "华为";
                    break;
                case 3:
                    deviceName = "魅族";
                    break;
                case 4:
                    deviceName = "OPPO";
                    break;
                case 5:
                    deviceName = "VIVO";
                    break;
                case 6:
                    deviceName = "ASUS";
                    break;
                case 8:
                    deviceName = "FCM";
                    break;
            }
            Log.e(TAG,"获取到 "+deviceName+" 的token:"+token);
        }
    }
}
