package com.gzyslczx.youruismapp.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class SPTool extends BaseTool{

    public static final String TAG = "SpTool";

    private final String SPName = "YrSmSp.cfg";

    private static volatile SPTool spTool;
    private SharedPreferences sharedPreferences;

    public static final String SpToken = "YrToken";
    public static final String SpTokenTime = "YrTokenTime";

    private SPTool(Context context) {
        PrintLogD(TAG, "初始化SpTool");
        sharedPreferences = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
    }

    public static SPTool init(Context context){
        if (spTool == null) {
            synchronized (SPTool.class) {
                if (spTool == null) {
                    spTool = new SPTool(context);
                }
            }
        }
        return spTool;
    }

    /*
    * 存储SP数据
    * */
    public boolean SaveInfo(String key, String value){
        if (sharedPreferences!=null){
            PrintLogE(TAG, "SPTool存储数据");
            return sharedPreferences.edit().putString(key, value).commit();
        }else {
            PrintLogE(TAG, "SPTool未初始化");
            return false;
        }
    }

    /*
     * 获取SP数据
     * */
    public String GetInfo(String key){
        if (sharedPreferences!=null){
            PrintLogE(TAG, "SPTool获取数据");
            return sharedPreferences.getString(key, TAG);
        }else {
            PrintLogE(TAG, "SPTool未初始化");
            return TAG;
        }
    }

}
