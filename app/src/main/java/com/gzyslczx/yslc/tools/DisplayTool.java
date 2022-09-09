package com.gzyslczx.yslc.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.gzyslczx.yslc.BaseActivity;

public class DisplayTool {

    /**
     * 单位转换: dp -> px
     *
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        return (int) (getDensity(context) * dp + 0.5);
    }

    /**
     * 单位转换: sp -> px
     *
     * @param sp
     * @return
     */
    public static int sp2px(Context context, int sp) {
        return (int) (getFontDensity(context) * sp + 0.5);
    }

    /**
     * 单位转换:px -> dp
     *
     * @param px
     * @return
     */
    public static int px2dp(Context context, int px) {
        return (int) (px / getDensity(context) + 0.5);
    }

    /**
     * 单位转换:px -> sp
     *
     * @param px
     * @return
     */
    public static int px2sp(Context context, int px) {
        return (int) (px / getFontDensity(context) + 0.5);
    }

    private static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    private static float getFontDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }



    /**
     * 判断是否有状态栏
     *
     * @param context
     * @return
     */
    public static boolean hasStatusBar(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            return (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        return true;
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void hiddenBottomUIMenu(BaseActivity baseActivity, boolean isHidden) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = baseActivity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = baseActivity.getWindow().getDecorView();
            if (isHidden) {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                decorView.setSystemUiVisibility(uiOptions);
            }else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }


}
