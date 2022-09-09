package com.gzyslczx.yslc.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.gzyslczx.yslc.MyApp;
import com.gzyslczx.yslc.R;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

public class ShareTool {


    /*
     * 微信分享 type:1->朋友圈  0->会话
     * */
    public static void ShareToWX(Context context, String newsId, String title, int type) {
        if (MyApp.mIwxapi.isWXAppInstalled()) {
            WXWebpageObject object = new WXWebpageObject();
            object.webpageUrl = ConnPath.ShareUrl + newsId;
            WXMediaMessage message = new WXMediaMessage(object);
            message.title = title;
            if (title.equals("扒风险")){
                message.description = context.getString(R.string.SharePaFengXian);
            }else if (title.equals("盘前特供")){
                message.description = context.getString(R.string.ShareSpecialSup);
            }else if (title.equals("扒资金")){
                message.description = context.getString(R.string.SharePaZiJin);
            }else if (title.equals("扒游资")){
                message.description = context.getString(R.string.SharePaYouZi);
            }else if (title.equals("扒机会")){
                message.description = context.getString(R.string.ShareChange);
            }else if (title.equals("扒分析师")){
                message.description = context.getString(R.string.SharePaFenXiShi);
            }else if (title.equals("主题资讯") || title.equals("投顾论市")){
                message.description = context.getString(R.string.ShareDesPart);
            }else if (title.equals("K线秘籍")){
                message.description = context.getString(R.string.ShareKLine);
            }else if (title.equals("越声快讯")){
                message.description = context.getString(R.string.ShareYSFlash);
            }else {
                message.description = context.getString(R.string.ShareDesPart);
            }
            Bitmap thumbBmp = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.img_thumb);
            message.setThumbImage(thumbBmp);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = newsId;
            req.message = message;
            if (type == SendMessageToWX.Req.WXSceneTimeline
                    && MyApp.mIwxapi.getWXAppSupportAPI() >= Build.TIMELINE_SUPPORTED_SDK_INT) {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            } else {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            }
            MyApp.mIwxapi.sendReq(req);
            thumbBmp.recycle();
        } else {
            Toast.makeText(context, "请先安装微信", Toast.LENGTH_SHORT).show();
        }
    }

    
    public static void ShareToWXByPath(Context context, String Path, String title, int type) {
        if (MyApp.mIwxapi.isWXAppInstalled()) {
            WXWebpageObject object = new WXWebpageObject();
            object.webpageUrl = Path;
            WXMediaMessage message = new WXMediaMessage(object);
            message.title = title;
            if (title.equals("扒风险")){
                message.description = context.getString(R.string.SharePaFengXian);
            }else if (title.equals("盘前特供")){
                message.description = context.getString(R.string.ShareSpecialSup);
            }else if (title.equals("扒资金")){
                message.description = context.getString(R.string.SharePaZiJin);
            }else if (title.equals("扒游资")){
                message.description = context.getString(R.string.SharePaYouZi);
            }else if (title.equals("扒机会")){
                message.description = context.getString(R.string.ShareChange);
            }else if (title.equals("扒分析师")){
                message.description = context.getString(R.string.SharePaFenXiShi);
            }else if (title.equals("主题资讯") || title.equals("投顾论市")){
                message.description = context.getString(R.string.ShareDesPart);
            }else if (title.equals("K线秘籍")){
                message.description = context.getString(R.string.ShareKLine);
            }else if (title.equals("越声快讯")){
                message.description = context.getString(R.string.ShareYSFlash);
            }else {
                message.description = context.getString(R.string.ShareDesPart);
            }
            Bitmap thumbBmp = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.img_thumb);
            message.setThumbImage(thumbBmp);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.message = message;
            if (type == SendMessageToWX.Req.WXSceneTimeline
                    && MyApp.mIwxapi.getWXAppSupportAPI() >= Build.TIMELINE_SUPPORTED_SDK_INT) {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            } else {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            }
            MyApp.mIwxapi.sendReq(req);
            thumbBmp.recycle();
        } else {
            Toast.makeText(context, "请先安装微信", Toast.LENGTH_SHORT).show();
        }
    }

}
