package com.gzyslczx.yslc.tools;

import android.os.Build;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebTool {

    //  短文格式
    public static StringBuilder SetHtmlData(String body) {
        StringBuilder head = new StringBuilder();
        head.append("<html>");
        head.append("<head>");
        head.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> ");
        head.append("<meta name=\"content-type\" content=\"text/html; charset=utf-8\">");
        head.append("<meta http-equlv=\"Content-Type\" content=\"text/html;charset=utf-8\">");
        head.append("<style>img{max-width: 100%; width:auto; height:auto;}</style>");
        head.append("</head>");
        head.append("<body>");
        head.append(body);
        head.append("</body></html>");
        return head;
    }

    public static void SetWebRichText(WebView webView){
        WebSettings settings = webView.getSettings();
        // 设置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
        //支持自动适配
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持放大缩小
        settings.setSupportZoom(true);
        //显示缩放按钮
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(false);
        settings.setSaveFormData(false);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setSupportMultipleWindows(true);
        //显示图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 把图片加载不放在最后来加载渲染
        settings.setBlockNetworkImage(false);
        //设置不让其跳转浏览器
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("Web加载：", url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    public static void SetWebRichTextByVIP(WebView webView){
        WebSettings settings = webView.getSettings();
        // 设置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
        //支持自动适配
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持放大缩小
        settings.setSupportZoom(true);
        //显示缩放按钮
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(false);
        settings.setSaveFormData(false);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setSupportMultipleWindows(true);
        //不适用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //显示图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 把图片加载不放在最后来加载渲染
        settings.setBlockNetworkImage(false);
    }

}
