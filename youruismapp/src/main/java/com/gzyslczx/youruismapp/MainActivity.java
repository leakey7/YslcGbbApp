package com.gzyslczx.youruismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gzyslczx.youruismapp.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private final String TAG = "MainAct";

    @Override
    void onInitLayout(Bundle savedInstanceState) {
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
    }

    @Override
    void onInitViews() {

    }
}