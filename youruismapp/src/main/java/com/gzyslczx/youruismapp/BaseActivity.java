package com.gzyslczx.youruismapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity<T extends ViewBinding> extends RxAppCompatActivity {

    public T mViewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        onInitLayout(savedInstanceState);
        onInitViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    初始化布局
    abstract void onInitLayout(Bundle savedInstanceState);

//    初始化控件
    abstract void onInitViews();

}
