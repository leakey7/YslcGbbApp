package com.gzyslczx.yslc.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.gzyslczx.yslc.BaseActivity;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransStatusTool {

    private final static String TAG = "TransStatue";
    private final static String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private final static String KEY_FLYME_VERSION_NAME = "ro.build.display.id";
    private final static String FLYME = "flyme";
    private final static String ZTEC2016 = "zte c2016";
    private final static String ZUKZ1 = "zuk z1";
    private final static String ESSENTIAL = "essential";
    private final static String MEIZUBOARD[] = {"m9", "M9", "mx", "MX"};
    private static String sMiuiVersionName;
    private static String sFlymeVersionName;
    private final static int STATUSBAR_TYPE_DEFAULT = 0;
    private final static int STATUSBAR_TYPE_MIUI = 1;
    private final static int STATUSBAR_TYPE_FLYME = 2;
    private final static int STATUSBAR_TYPE_ANDROID6 = 3; // Android 6.0
    private final static int STATUS_BAR_DEFAULT_HEIGHT_DP = 25; // 大部分状态栏都是25dp
    private static @StatusBarType
    int mStatusBarType = STATUSBAR_TYPE_DEFAULT;

    static {
        Properties properties = new Properties();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // android 8.0，读取 /system/uild.prop 会报 permission denied
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
                properties.load(fileInputStream);
            } catch (Exception e) {
                Log.e(TAG, "read file error");
            } finally {
                close(fileInputStream);
            }
        }

        Class<?> clzSystemProperties = null;
        try {
            clzSystemProperties = Class.forName("android.os.SystemProperties");
            Method getMethod = clzSystemProperties.getDeclaredMethod("get", String.class);
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME);
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME);
        } catch (Exception e) {
            Log.e(TAG, "read SystemProperties error");
        }
    }

    private static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    private static String getLowerCaseName(Properties p, Method get, String key) {
        String name = p.getProperty(key);
        if (name == null) {
            try {
                name = (String) get.invoke(null, key);
            } catch (Exception ignored) {
            }
        }
        if (name != null) name = name.toLowerCase();
        return name;
    }


    /*
     * 沉浸式状态栏
     * */
    public static void translucent(BaseActivity activity) {
        translucent(activity.getWindow());
    }


    public static void translucent(Window window) {
        translucent(window, 0x40000000);
    }

    @TargetApi(19)
    public static void translucent(Window window, @ColorInt int colorOn5x) {
        if (!supportTranslucent()) {
            // 版本小于4.4，绝对不考虑沉浸式
            return;
        }

        if (isNotchOfficialSupport()) {
            handleDisplayCutoutMode(window);
        }

        // 小米和魅族4.4 以上版本支持沉浸式
        // 小米 Android 6.0 ，开发版 7.7.13 及以后版本设置黑色字体又需要 clear FLAG_TRANSLUCENT_STATUS, 因此还原为官方模式
        if (isFlymeLowerThan(8) || (isMIUI() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return;
        }

        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTransclentStatusBar6()) {
            // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
            // ZUK Z1是个另类，自家应用可以实现字体颜色变色，但没开放接口
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            // android 5不能修改状态栏字体颜色，因此直接用FLAG_TRANSLUCENT_STATUS，nexus表现为半透明
            // 魅族和小米的表现如何？
            // update: 部分手机运用FLAG_TRANSLUCENT_STATUS时背景不是半透明而是没有背景了。。。。。
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // 采取setStatusBarColor的方式，部分机型不支持，那就纯黑了，保证状态栏图标可见
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorOn5x);
        }
    }

    private static boolean supportTranslucent() {
        // Essential Phone 在 Android 8 之前沉浸式做得不全，系统不从状态栏顶部开始布局却会下发 WindowInsets
        return !(isEssentialPhone() && Build.VERSION.SDK_INT < 26);
    }

    private static final String BRAND = Build.BRAND.toLowerCase();
    private static boolean isEssentialPhone() {
        return BRAND.contains("essential");
    }

    static boolean isNotchOfficialSupport(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    @TargetApi(28)
    private static void handleDisplayCutoutMode(final Window window) {
        View decorView = window.getDecorView();
        if (decorView != null) {
            if (ViewCompat.isAttachedToWindow(decorView)) {
                realHandleDisplayCutoutMode(window, decorView);
            } else {
                decorView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        v.removeOnAttachStateChangeListener(this);
                        realHandleDisplayCutoutMode(window, v);
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {

                    }
                });
            }
        }
    }

    @TargetApi(28)
    private static void realHandleDisplayCutoutMode(Window window, View decorView) {
        if (decorView.getRootWindowInsets() != null &&
                decorView.getRootWindowInsets().getDisplayCutout() != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams
                    .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(params);
        }
    }

    private static boolean isFlymeLowerThan(int majorVersion) {
        return isFlymeLowerThan(majorVersion, 0, 0);
    }

    public static boolean isFlymeLowerThan(int majorVersion, int minorVersion, int patchVersion) {
        boolean isLower = false;
        if (sFlymeVersionName != null && !sFlymeVersionName.equals("")) {
            try {
                Pattern pattern = Pattern.compile("(\\d+\\.){2}\\d");
                Matcher matcher = pattern.matcher(sFlymeVersionName);
                if (matcher.find()) {
                    String versionString = matcher.group();
                    if (versionString.length() > 0) {
                        String[] version = versionString.split("\\.");
                        if (version.length >= 1) {
                            if (Integer.parseInt(version[0]) < majorVersion) {
                                isLower = true;
                            }
                        }

                        if (version.length >= 2 && minorVersion > 0) {
                            if (Integer.parseInt(version[1]) < majorVersion) {
                                isLower = true;
                            }
                        }

                        if (version.length >= 3 && patchVersion > 0) {
                            if (Integer.parseInt(version[2]) < majorVersion) {
                                isLower = true;
                            }
                        }
                    }
                }
            } catch (Throwable ignore) {

            }
        }
        return isMeizu() && isLower;
    }

    private static boolean isMeizu() {
        return isPhone(MEIZUBOARD) || isFlyme();
    }

    private static boolean isPhone(String[] boards) {
        final String board = android.os.Build.BOARD;
        if (board == null) {
            return false;
        }
        for (String board1 : boards) {
            if (board.equals(board1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是flyme系统
     */
    private static boolean isFlyme() {
        return !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName.contains(FLYME);
    }

    /**
     * 判断是否是MIUI系统
     */
    private static boolean isMIUI() {
        return !TextUtils.isEmpty(sMiuiVersionName);
    }

    /**
     * 检测 Android 6.0 是否可以启用 window.setStatusBarColor(Color.TRANSPARENT)。
     */
    private static boolean supportTransclentStatusBar6() {
        return !(isZUKZ1() || isZTKC2016());
    }

    /**
     * 判断是否为 ZUK Z1 和 ZTK C2016。
     * 两台设备的系统虽然为 android 6.0，但不支持状态栏icon颜色改变，因此经常需要对它们进行额外判断。
     */
    private static boolean isZUKZ1() {
        final String board = android.os.Build.MODEL;
        return board != null && board.toLowerCase().contains(ZUKZ1);
    }

    private static boolean isZTKC2016() {
        final String board = android.os.Build.MODEL;
        return board != null && board.toLowerCase().contains(ZTEC2016);
    }

    private static boolean isMIUIV5() {
        return "v5".equals(sMiuiVersionName);
    }

    private static boolean isMIUIV6() {
        return "v6".equals(sMiuiVersionName);
    }

    private static boolean isMIUIV7() {
        return "v7".equals(sMiuiVersionName);
    }

    public static boolean isMIUIV8() {
        return "v8".equals(sMiuiVersionName);
    }

    public static boolean isMIUIV9() {
        return "v9".equals(sMiuiVersionName);
    }

    /**
     * 设置状态栏黑色字体图标，
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     */
    public static boolean setStatusBarLightMode(Activity activity) {
        if (activity == null) return false;
        // 无语系列：ZTK C2016只能时间和电池图标变色。。。。
        if (isZTKC2016()) {
            return false;
        }

        if (mStatusBarType != STATUSBAR_TYPE_DEFAULT) {
            return setStatusBarLightMode(activity, mStatusBarType);
        }
        if (isMIUICustomStatusBarLightModeImpl() && MIUISetStatusBarLightMode(activity.getWindow(), true)) {
            mStatusBarType = STATUSBAR_TYPE_MIUI;
            return true;
        } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
            mStatusBarType = STATUSBAR_TYPE_FLYME;
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Android6SetStatusBarLightMode(activity.getWindow(), true);
            mStatusBarType = STATUSBAR_TYPE_ANDROID6;
            return true;
        }
        return false;
    }

    /**
     * 设置状态栏白色字体图标
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     */
    public static boolean setStatusBarDarkMode(Activity activity) {
        if (activity == null) return false;
        if (mStatusBarType == STATUSBAR_TYPE_DEFAULT) {
            // 默认状态，不需要处理
            return true;
        }

        if (mStatusBarType == STATUSBAR_TYPE_MIUI) {
            return MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (mStatusBarType == STATUSBAR_TYPE_FLYME) {
            return FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (mStatusBarType == STATUSBAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.getWindow(), false);
        }
        return true;
    }


    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     * @param type     StatusBar 类型，对应不同的系统
     */
    private static boolean setStatusBarLightMode(Activity activity, @StatusBarType int type) {
        if (type == STATUSBAR_TYPE_MIUI) {
            return MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == STATUSBAR_TYPE_FLYME) {
            return FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == STATUSBAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.getWindow(), true);
        }
        return false;
    }

    /**
     * 更改状态栏图标、文字颜色的方案是否是MIUI自家的， MIUI9 && Android 6 之后用回Android原生实现
     * 见小米开发文档说明：https://dev.mi.com/console/doc/detail?pId=1159
     */
    private static boolean isMIUICustomStatusBarLightModeImpl() {
        if (isMIUIV9() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return isMIUIV5() || isMIUIV6() || isMIUIV7() || isMIUIV8();
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为 Flyme 用户
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean FlymeSetStatusBarLightMode(Window window, boolean light) {
        boolean result = false;
        if (window != null) {

            Android6SetStatusBarLightMode(window, light);

            // flyme 在 6.2.0.0A 支持了 Android 官方的实现方案，旧的方案失效
            // 高版本调用这个出现不可预期的 Bug,官方文档也没有给出完整的高低版本兼容方案
            if (isFlymeLowerThan(7)) {
                try {
                    WindowManager.LayoutParams lp = window.getAttributes();
                    Field darkFlag = WindowManager.LayoutParams.class
                            .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                    Field meizuFlags = WindowManager.LayoutParams.class
                            .getDeclaredField("meizuFlags");
                    darkFlag.setAccessible(true);
                    meizuFlags.setAccessible(true);
                    int bit = darkFlag.getInt(null);
                    int value = meizuFlags.getInt(lp);
                    if (light) {
                        value |= bit;
                    } else {
                        value &= ~bit;
                    }
                    meizuFlags.setInt(lp, value);
                    window.setAttributes(lp);
                    result = true;
                } catch (Exception ignored) {

                }
            } else if (isFlyme()) {
                result = true;
            }
        }
        return result;
    }


    /**
     * 设置状态栏字体图标为深色，Android 6
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @TargetApi(23)
    private static boolean Android6SetStatusBarLightMode(Window window, boolean light) {
        View decorView = window.getDecorView();
        int systemUi = decorView.getSystemUiVisibility();
        if (light) {
            systemUi |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            systemUi &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decorView.setSystemUiVisibility(systemUi);
        if (isMIUIV9()) {
            // MIUI 9 低于 6.0 版本依旧只能回退到以前的方案
            // https://github.com/Tencent/QMUI_Android/issues/160
            MIUISetStatusBarLightMode(window, light);
        }
        return true;
    }

    /**
     * 设置状态栏字体图标为深色，需要 MIUIV6 以上
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回 true
     */
    @SuppressWarnings("unchecked")
    private static boolean MIUISetStatusBarLightMode(Window window, boolean light) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (light) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    /**
     * 获取状态栏的高度。
     */
    private static int sStatusBarHeight = -1;
    // 在某些机子上存在不同的density值，所以增加两个虚拟值
    public static float sVirtualDensity = -1;
    public static float sVirtualDensityDpi = -1;
    public static int getStatusbarHeight(Context context) {
        if (sStatusBarHeight == -1) {
            initStatusBarHeight(context);
        }
        return sStatusBarHeight;
    }

    private static void initStatusBarHeight(Context context) {
        Class<?> clazz;
        Object obj = null;
        Field field = null;
        try {
            clazz = Class.forName("com.android.internal.R$dimen");
            obj = clazz.newInstance();
            if (isMeizu()) {
                try {
                    field = clazz.getField("status_bar_height_large");
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            if (field == null) {
                field = clazz.getField("status_bar_height");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (field != null && obj != null) {
            try {
                int id = Integer.parseInt(field.get(obj).toString());
                sStatusBarHeight = context.getResources().getDimensionPixelSize(id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (sStatusBarHeight <= 0) {
            if (sVirtualDensity == -1) {
                sStatusBarHeight = DisplayTool.dp2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP);
            } else {
                sStatusBarHeight = (int) (STATUS_BAR_DEFAULT_HEIGHT_DP * sVirtualDensity + 0.5f);
            }
        }
    }



    @IntDef({STATUSBAR_TYPE_DEFAULT, STATUSBAR_TYPE_MIUI, STATUSBAR_TYPE_FLYME, STATUSBAR_TYPE_ANDROID6})
    @Retention(RetentionPolicy.SOURCE)
    private @interface StatusBarType {
    }

}
