package com.gzyslczx.yslc.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SpTool {

    private final String TAG = "SPTool";

    private SharedPreferences mSp;
    private Context mContext;
    private static volatile SpTool mSpToolInstance;

    //    默认值
    public static final String SpDefault = "SpNull";
    //    SP文件名
    private final String SpName = "GbbUserInfo";
    //    股扒扒Token键
    public static final String GuBbToken = "GbbToken";
    //    UserID键
    public static final String GuBbUserID = "GbbUserName";
    //    UserPhone键
    public static final String UserPhone = "GbbUserPhone";
    //    是否第一次开启
    public static final String isFirst = "GbbIsFirst";
    //    股扒扒Token记录时间
    public static final String GuBbTokenTime = "GbbTokenTime";
    //    基金通用户ID
    public static final String FundTongUID = "FundUId";
    //    基金通Token键
    public static final String FundTongToken = "FundToken";
    //    基金通Token记录时间
    public static final String FundTongTokenTime = "FundTokenTime";
    //    App隐藏直播
    private static final String HiddenLive = "HiddenLive";
    //  友睿Token
    public static final String YRToken = "YRToken";


    private SpTool(Context context) {
        mContext = context;
        mSp = mContext.getSharedPreferences(SpName, Context.MODE_PRIVATE);
    }

    /*
     * 单例
     * */
    public static SpTool Instance(Context context) {
        if (mSpToolInstance == null) {
            synchronized (SpTool.class) {
                if (mSpToolInstance == null) {
                    mSpToolInstance = new SpTool(context);
                }
            }
        }
        return mSpToolInstance;
    }

    /*
     * k-v 键值对
     * */
    public boolean SaveInfo(String k, String v) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(k, v);
        return editor.commit();
    }

    /*
    * 第一次打开保存记录
    * */
    public void SaveIsFirst(boolean flag){
        if (GetFirst()) {
            SharedPreferences.Editor editor = mSp.edit();
            editor.putBoolean(isFirst, flag);
            editor.commit();
        }
    }

    /*
    * 是否第一次打开
    * */
    public boolean GetFirst(){
        return mSp.getBoolean(isFirst, true);
    }

    /*
    *清空存储
    * */
    public void ClearInfo() {
        SaveInfo(UserPhone, SpDefault);
        SaveInfo(GuBbUserID, SpDefault);
        SaveInfo(FundTongUID, SpDefault);
    }

    /*
     * 登录成功后存储用户信息
     * */
    public boolean SaveGuBbUserInfo(String uid, String phone){
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(GuBbUserID, uid);
        editor.putString(UserPhone, phone);
        return editor.commit();
    }

    /*
    * 是否登录股扒扒
    * */
    public boolean IsGuBbLogin(){
        return !(SpDefault.equals(GetInfo(GuBbUserID)));
    }

    /*
    * 是否登录基金通
    * */
    public boolean IsFundTongLogin(){return  !(SpDefault.equals(GetInfo(FundTongUID)));}

    /*
     * 根据k键获取存储值
     * */
    public String GetInfo(String k) {
        return mSp.getString(k, SpDefault);
    }

    /*
    * 是否隐藏直播
    * */
    public boolean SaveHiddenLive(boolean isHidden){
        SharedPreferences.Editor editor = mSp.edit();
        editor.putBoolean(HiddenLive, isHidden);
        return editor.commit();
    }

    /*
    * 获取直播隐藏指示
    * */
    public boolean GetHiddenLiveOrder(){
        return mSp.getBoolean(HiddenLive, false);
    }

}
