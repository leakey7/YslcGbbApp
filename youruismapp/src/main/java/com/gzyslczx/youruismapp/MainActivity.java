package com.gzyslczx.youruismapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.gzyslczx.youruismapp.databinding.ActivityMainBinding;
import com.gzyslczx.youruismapp.databinding.ConnectYrServiceLayoutBinding;
import com.gzyslczx.youruismapp.events.UpdateYRTokenEvent;
import com.gzyslczx.youruismapp.presenters.MainActPresenter;
import com.gzyslczx.youruismapp.subscribe.MainActSub;
import com.gzyslczx.youruismapp.tools.NetWork.YrNetWorkTool;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainActSub {

    private final String TAG = "MainAct";

    private PopupWindow ConnectYrWindow;

    private MainActPresenter presenter;

    @Override
    protected void onDestroy() {
        if (ConnectYrWindow!=null && ConnectYrWindow.isShowing()){
            ConnectYrWindow.dismiss();
        }
        super.onDestroy();
    }

    @Override
    void onInitLayout(Bundle savedInstanceState) {
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
    }

    @Override
    void onInitViews() {
        presenter = new MainActPresenter();
        ShowConnTipWindow();
        if (!presenter.CheckYRTokenOnActivity(this, TAG)){
            //友睿Token无效
            presenter.PrintLogD(TAG, "申请友睿Token");
            YrNetWorkTool.Wake().ReqYRToken(this, null);
        }
    }

    /*
    * 连接服务浮窗
    * */
    private void ShowConnTipWindow(){
        if (ConnectYrWindow==null) {
            ConnectYrWindow = new PopupWindow();
            ConnectYrWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            ConnectYrWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            ConnectYrServiceLayoutBinding binding = ConnectYrServiceLayoutBinding.bind(LayoutInflater.from(this).inflate(R.layout.connect_yr_service_layout, null));
            ConnectYrWindow.setContentView(binding.getRoot());
            ConnectYrWindow.setOutsideTouchable(false);
        }
        ConnectYrWindow.showAtLocation(mViewBinding.getRoot(), Gravity.CENTER, 0 ,0);
    }


    @Override
    public void onUpdateYRToken(UpdateYRTokenEvent event) {
        if (event.isEff()){
            //友睿Token有效
            ConnectYrWindow.dismiss();
        }
    }
}