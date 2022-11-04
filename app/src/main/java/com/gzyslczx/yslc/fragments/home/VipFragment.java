package com.gzyslczx.yslc.fragments.home;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.MyApp;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.SearchActivity;
import com.gzyslczx.yslc.databinding.PhotoPopupLayoutBinding;
import com.gzyslczx.yslc.databinding.VipFragmentBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbVipPushEvent;
import com.gzyslczx.yslc.events.NoticeBtmBarHidden;
import com.gzyslczx.yslc.events.NoticeHiddenBtmBarEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.tools.AndroidBug5497Workaround;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.WebTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class VipFragment extends BaseFragment<VipFragmentBinding> implements ViewTreeObserver.OnGlobalLayoutListener {

    private final String TAG = "VipFragment";
    private final String VipPath = "http://yszxv.etz927.com/ChatRoom/App/VIPService/VIP1/index.aspx";
    private SharePopup sharePopup;
    private View CustomView;
    private boolean hasPushing = false;
    private StringBuilder PushVipPath;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = VipFragmentBinding.inflate(inflater, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        InitView();
    }

    @Override
    protected void InitView() {
        EventBus.getDefault().register(this);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mViewBinding.VIPWebProgress.getLayoutParams();
        layoutParams.setMargins(0, TransStatusTool.getStatusbarHeight(getContext()), 0, 0);
        mViewBinding.VIPWebProgress.setLayoutParams(layoutParams);
        AndroidBug5497Workaround();
        WebTool.SetWebRichTextByVIP(mViewBinding.VIPWebView);
        mViewBinding.VIPWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress<100){
                    mViewBinding.VIPWebProgress.setVisibility(View.VISIBLE);
                    mViewBinding.VIPWebProgress.setProgress(newProgress);
                }else {
                    mViewBinding.VIPWebProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                CustomView = view;
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
              getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
        //设置不让其跳转浏览器
        mViewBinding.VIPWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, String.format("VIP加载中：%s", url));
                if (url!=null){
                    try{
                        if(!url.startsWith("http://") && !url.startsWith("https://")){
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        }
                    }catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                        return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, String.format("VIP加载完：%s", url));
                if (hasPushing && PushVipPath!=null && PushVipPath.length()>0){
                    hasPushing = false;
                    if (url.equals(PushVipPath.toString())){
                        PushVipPath.delete(0, PushVipPath.length());
                    }else {
                        view.loadUrl(PushVipPath.toString());
                    }
                    TransStatusTool.setStatusBarLightMode(getActivity());
                }
            }
        });
        mViewBinding.VIPWebView.addJavascriptInterface(this, "androidObj");
        Log.d("VIP初始：", VipPath);
        mViewBinding.VIPWebView.loadUrl(VipPath);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (sharePopup!=null){
            sharePopup.Clear();
        }
        EventBus.getDefault().unregister(this);
    }

    @JavascriptInterface
    public void CloseWeb(){
        mViewBinding.VIPWebView.loadUrl(VipPath);
    }

    @JavascriptInterface
    public String GetPhone(){
        if (SpTool.Instance(getContext()).IsGuBbLogin()){
            Log.d(TAG, "被获取手机号码");
            return SpTool.Instance(getContext()).GetInfo(SpTool.UserPhone);
        }
        Log.d(TAG, "无获取手机号码");
        return null;
    }

    @JavascriptInterface
    public void ToLogin(){
        NormalActionTool.LoginAction(getContext(), null, (BaseActivity) getActivity(), null, TAG);
    }

    @JavascriptInterface
    public void ToSearch(){
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    @JavascriptInterface
    public void ShowShareByPath(String UrlPath, String title){
        if (sharePopup==null){
            sharePopup = new SharePopup(getContext(), mViewBinding.getRoot(), true);
        }
        sharePopup.setUrlPath(UrlPath);
        sharePopup.setTitle(title);
        sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
    }

    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        Log.d("VIP登录更新：", VipPath);
        mViewBinding.VIPWebView.loadUrl(VipPath);
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == newConfig.ORIENTATION_LANDSCAPE) {
            //满屏播放
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mViewBinding.VIPWebView.setVisibility(View.GONE);
            mViewBinding.VIPFrame.setVisibility(View.VISIBLE);
            mViewBinding.VIPFrame.addView(CustomView);
            NoticeHiddenBtmBarEvent event = new NoticeHiddenBtmBarEvent(true);
            EventBus.getDefault().post(event);
        } else if (newConfig.orientation == newConfig.ORIENTATION_PORTRAIT) {
            //小屏播放
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mViewBinding.VIPWebView.setVisibility(View.VISIBLE);
            mViewBinding.VIPFrame.setVisibility(View.GONE);
            mViewBinding.VIPFrame.removeView(CustomView);
            CustomView=null;
            NoticeHiddenBtmBarEvent event = new NoticeHiddenBtmBarEvent(false);
            EventBus.getDefault().post(event);
        }
    }

    @JavascriptInterface
    public void ShowBtmBar(int hidden){
        NoticeBtmBarHidden noticeBtmBarHidden = new NoticeBtmBarHidden(hidden);
        EventBus.getDefault().post(noticeBtmBarHidden);

    }

    @JavascriptInterface
    public void ToWXProgram(String uid, String url){
        Log.d(TAG, "跳转微信小程序");
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = uid;
        req.path = url;
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        MyApp.mIwxapi.sendReq(req);
    }

    @JavascriptInterface
    public void OpenPic(String PicUrl){
        Log.d(TAG, "浏览图片");
        PopupWindow popupWindow = new PopupWindow(mViewBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        PhotoPopupLayoutBinding mBinding = PhotoPopupLayoutBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.photo_popup_layout, null));
        popupWindow.setContentView(mBinding.getRoot());
        Glide.with(this).load(PicUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mBinding.PicScale.post(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.PicScale.setImageDrawable(resource);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        mBinding.PicScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow!=null){
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.showAtLocation(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OnVipPushEvent(GuBbVipPushEvent event){
        if (PushVipPath==null){
            PushVipPath = new StringBuilder();
        }
        PushVipPath.replace(0, PushVipPath.length(), event.getVipWebPath());
        hasPushing = true;
        mViewBinding.VIPWebView.post(new Runnable() {
            @Override
            public void run() {
                mViewBinding.VIPWebView.loadUrl(VipPath);
            }
        });
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ConstraintLayout.LayoutParams frameLayoutParams;

    private void AndroidBug5497Workaround() {
        mChildOfContent = mViewBinding.VIPWebView;
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(this::onGlobalLayout);
        frameLayoutParams = (ConstraintLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }
    @Override
    public void onGlobalLayout() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard/4)) {
                // keyboard probably just became visible
                frameLayoutParams.bottomMargin = heightDifference+DisplayTool.dp2px(getContext(), 124);
            } else {
                // keyboard probably just became hidden
                if (usableHeightNow<usableHeightSansKeyboard){
                    frameLayoutParams.height = usableHeightNow;
                }else {
                    frameLayoutParams.height = usableHeightSansKeyboard;
                }
                frameLayoutParams.bottomMargin = 0;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top)-DisplayTool.dp2px(getContext(), 54);
    }

}
