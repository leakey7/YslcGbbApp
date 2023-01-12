package com.gzyslczx.youruismapp;

import android.app.Application;

import com.gzyslczx.youruismapp.tools.NetWork.YrNetWorkTool;
import com.gzyslczx.youruismapp.tools.SPTool;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SPTool.init(this); //SPTool单例初始化
    }
}
