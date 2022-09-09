package com.gzyslczx.yslc.tools.customviews;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.databinding.ShareLayoutBinding;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.ShareTool;

public class SharePopup implements View.OnClickListener {

    private PopupWindow popupWindow;
    private ShareLayoutBinding mBinding;
    private Context context;
    private String Nid;
    private String Title;
    private String UrlPath;
    private boolean isUrl=false;

    public SharePopup(Context context, View parent, boolean isUrl) {
        this.context = context;
        this.isUrl = isUrl;
        popupWindow = new PopupWindow(parent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mBinding = ShareLayoutBinding.bind(LayoutInflater.from(context).inflate(R.layout.share_layout, null));
        popupWindow.setContentView(mBinding.getRoot());
        mBinding.ShareCancel.setOnClickListener(this::onClick);
        mBinding.ShareWxImg.setOnClickListener(this::onClick);
        mBinding.ShareFriendsImg.setOnClickListener(this::onClick);
        mBinding.ShareCopyLinkImg.setOnClickListener(this::onClick);
    }

    public SharePopup(Context context, View parent, String nid, String title) {
        this.context = context;
        this.Nid = nid;
        this.Title = title;
        popupWindow = new PopupWindow(parent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mBinding = ShareLayoutBinding.bind(LayoutInflater.from(context).inflate(R.layout.share_layout, null));
        popupWindow.setContentView(mBinding.getRoot());
        mBinding.ShareCancel.setOnClickListener(this::onClick);
        mBinding.ShareWxImg.setOnClickListener(this::onClick);
        mBinding.ShareFriendsImg.setOnClickListener(this::onClick);
        mBinding.ShareCopyLinkImg.setOnClickListener(this::onClick);
    }

    public SharePopup(Context context, View parent, boolean isUrl, String UrlPath, String title){
        this.context = context;
        this.UrlPath = UrlPath;
        this.Title = title;
        this.isUrl = isUrl;
        popupWindow = new PopupWindow(parent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mBinding = ShareLayoutBinding.bind(LayoutInflater.from(context).inflate(R.layout.share_layout, null));
        popupWindow.setContentView(mBinding.getRoot());
        mBinding.ShareCancel.setOnClickListener(this::onClick);
        mBinding.ShareWxImg.setOnClickListener(this::onClick);
        mBinding.ShareFriendsImg.setOnClickListener(this::onClick);
        mBinding.ShareCopyLinkImg.setOnClickListener(this::onClick);
    }

    public void Clear(){
        this.context = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ShareCancel:
                popupWindow.dismiss();
                break;
            case R.id.ShareWxImg:
                if (isUrl) {
                    ShareTool.ShareToWXByPath(context, UrlPath, Title, 0);
                }else {
                    ShareTool.ShareToWX(context, Nid, Title, 0);
                }
                popupWindow.dismiss();
                break;
            case R.id.ShareFriendsImg:
                if (isUrl){
                    ShareTool.ShareToWXByPath(context, UrlPath, Title, 1);
                }else {
                    ShareTool.ShareToWX(context, Nid, Title, 1);
                }
                popupWindow.dismiss();
                break;
            case R.id.ShareCopyLinkImg:
                if (isUrl){
                    ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setPrimaryClip(ClipData.newPlainText("越声股扒扒", UrlPath));
                }else {
                    String link = ConnPath.ShareUrl + Nid;
                    ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setPrimaryClip(ClipData.newPlainText("越声股扒扒", link));
                }
                Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
        }
    }

    /*
     * 显示分享
     * */
    public void Show(View view, int gravity, int x, int y){
        if (popupWindow!=null){
            popupWindow.showAtLocation(view, gravity, x, y);
        }
    }

    public void setNid(String nid) {
        Nid = nid;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setUrlPath(String urlPath) {
        UrlPath = urlPath;
    }
}
