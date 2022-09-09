package com.gzyslczx.yslc;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

public abstract class BaseActivity<T extends ViewBinding> extends RxAppCompatActivity {

    T mViewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitParentLayout();
        InitView();
    }

    //初始化父布局
    abstract void InitParentLayout();
    //初始化子控件
    abstract void InitView();

    /*
     * 横竖屏切换
     * */
    public int ChangeScreen() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return 0;
        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        return 1;
    }

}
