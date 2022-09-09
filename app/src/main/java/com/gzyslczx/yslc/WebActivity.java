package com.gzyslczx.yslc;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gzyslczx.yslc.databinding.ActivityWebBinding;
import com.gzyslczx.yslc.databinding.PhotoPopupLayoutBinding;
import com.gzyslczx.yslc.databinding.SelectPicPopupLayoutBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.GuBbChangeOptionStateEvent;
import com.gzyslczx.yslc.events.NoticeBtmBarHidden;
import com.gzyslczx.yslc.tools.AndroidBug5497Workaround;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.WebTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class WebActivity extends BaseActivity<ActivityWebBinding> implements View.OnClickListener {

    private final String TAG = "NormalWebAct";
    public static final String WebKey = "WebPath";
    public static final String WebVideoKey = "IsWebVideo";
    private String WebPath;
    private SharePopup sharePopup;
    private boolean IsVideoWeb = false;
    private View CustomView;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ActivityResultLauncher<Intent> Version_R_Write, Open_Camera, Open_Photo;
    private ActivityResultLauncher<String[]> Version_M_Write;
    private String[] Permission_WR = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PopupWindow popupWindow;
    private boolean isClickedPhotoGroup=false;
    private ActivityResultLauncher<String> Camera_Per;
    private String Permission_Camera = Manifest.permission.CAMERA;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityWebBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
//        AndroidBug5497Workaround.assistActivity(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        WebPath = getIntent().getStringExtra(WebKey);
        IsVideoWeb = getIntent().getBooleanExtra(WebVideoKey, false);
        WebTool.SetWebRichTextByVIP(mViewBinding.NormalWebView);
        //初始化权限回调
        InitW_Permission();
        mViewBinding.NormalWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress<100){
                    mViewBinding.NormalWebProgress.setVisibility(View.VISIBLE);
                    mViewBinding.NormalWebProgress.setProgress(newProgress);
                }else {
                    mViewBinding.NormalWebProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                Log.d(TAG, "onShowCustomView");
                if (IsVideoWeb) {
                    CustomView = view;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                Log.d(TAG, "onHideCustomView");
                if (IsVideoWeb) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.d(TAG, "Web调起文件");
                mUploadCallbackAboveL = filePathCallback;
                if (popupWindow == null) {
                    SelectPicPopupLayoutBinding binding = SelectPicPopupLayoutBinding.bind(LayoutInflater.from(WebActivity.this).inflate(R.layout.select_pic_popup_layout, null));
                    binding.SelectPicCancel.setOnClickListener(WebActivity.this::onClick);
                    binding.SelectPicTakePhoto.setOnClickListener(WebActivity.this::onClick);
                    binding.SelectPicFormGroup.setOnClickListener(WebActivity.this::onClick);
                    binding.SelectParent.setOnClickListener(WebActivity.this::onClick);
                    popupWindow = new PopupWindow(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    popupWindow.setFocusable(false);
                }
                popupWindow.showAtLocation(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                return true;
            }
        });
        //设置不让其跳转浏览器
        mViewBinding.NormalWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, String.format("Web加载中：%s", url));
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, String.format("Web加载完：%s", url));
            }
        });
        mViewBinding.NormalWebView.addJavascriptInterface(this, "androidObj");
        mViewBinding.NormalWebView.loadUrl(WebPath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sharePopup!=null){
            sharePopup.Clear();
        }
        EventBus.getDefault().unregister(this);
    }

    @JavascriptInterface
    public void CloseWeb(){
        finish();
    }

    @JavascriptInterface
    public String GetPhone(){
        if (SpTool.Instance(this).IsGuBbLogin()){
            Log.d(TAG, "被获取手机号");
            return SpTool.Instance(this).GetInfo(SpTool.UserPhone);
        }
        Log.d(TAG, "无获取手机号");
        return null;
    }

    @JavascriptInterface
    public void ToLogin(){
        NormalActionTool.LoginAction(this, null, this, null, TAG);
    }

    @JavascriptInterface
    public void ToSearch(){
        startActivity(new Intent(this, SearchActivity.class));
    }

    @JavascriptInterface
    public void ShowShareByPath(String UrlPath, String title){
        if (sharePopup==null){
            sharePopup = new SharePopup(this, mViewBinding.getRoot(), true);
        }
        sharePopup.setUrlPath(UrlPath);
        sharePopup.setTitle(title);
        sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
        if (newConfig.orientation == newConfig.ORIENTATION_LANDSCAPE) {
            //满屏播放
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mViewBinding.NormalWebView.setVisibility(View.GONE);
            mViewBinding.NormalWebFrame.setVisibility(View.VISIBLE);
            mViewBinding.NormalWebFrame.addView(CustomView);
        } else if (newConfig.orientation == newConfig.ORIENTATION_PORTRAIT) {
            //小屏播放
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mViewBinding.NormalWebView.setVisibility(View.VISIBLE);
            mViewBinding.NormalWebFrame.setVisibility(View.GONE);
            mViewBinding.NormalWebFrame.removeView(CustomView);
            CustomView=null;
        }
    }

    @JavascriptInterface
    public void ToLoginAfterStock(String stockCode){
        NormalActionTool.LoginAction(this, null, this, stockCode, TAG);
    }


    /*
     * 接收登录状态更新
     * */
    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event){
        //更新登录显示
        mViewBinding.NormalWebView.loadUrl(WebPath);
    }

    /*
     * 接收添删自选
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeOptionStateEvent(GuBbChangeOptionStateEvent event){
        mViewBinding.NormalWebView.loadUrl(WebPath);
    }

    @JavascriptInterface
    public void ShowBtmBar(int hidden){
//        NoticeBtmBarHidden noticeBtmBarHidden = new NoticeBtmBarHidden(hidden);
//        EventBus.getDefault().post(noticeBtmBarHidden);
    }

    @JavascriptInterface
    public void OpenPic(String PicUrl){
        Log.d(TAG, "打开图片");
        PopupWindow popupWindow = new PopupWindow(mViewBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        PhotoPopupLayoutBinding mBinding = PhotoPopupLayoutBinding.bind(LayoutInflater.from(this).inflate(R.layout.photo_popup_layout, null));
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

    /*
     * 初始化读写权限回调
     * */
    private void InitW_Permission(){

        //读写权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Version_R_Write = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (Environment.isExternalStorageManager()) {
                        Log.d(TAG, "打开相册");
                        OpenPhotoGroup();
                    } else {
                        Toast.makeText(WebActivity.this, "未授予相关权限部分功能暂时无法使用，请在设置-应用中授予", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Version_M_Write = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    boolean ReadPer = result.get(Permission_WR[0]);
                    boolean WritePer = result.get(Permission_WR[1]);
                    if (ReadPer && WritePer) {
                        OpenPhotoGroup();
                    } else {
                        Toast.makeText(WebActivity.this, "未授予相关权限部分功能暂时无法使用，请在设置-应用中授予", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        //web调起文件选择
//        fileWebIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                Uri[] results = null;
//                if (result.getResultCode() == Activity.RESULT_OK){
//                    if (result.getData()!=null){
//                        String dataString = result.getData().getDataString();
//                        ClipData clipData = result.getData().getClipData();
//                        if (clipData != null) {
//                            results = new Uri[clipData.getItemCount()];
//                            for (int i = 0; i < clipData.getItemCount(); i++) {
//                                ClipData.Item item = clipData.getItemAt(i);
//                                results[i] = item.getUri();
//                            }
//                        }
//                        if (dataString != null)
//                            results = new Uri[]{Uri.parse(dataString)};
//                    }
//                }else {
//                    Log.d(TAG, "Web调起文件失败");
//                }
//                mUploadCallbackAboveL.onReceiveValue(results);
//                mUploadCallbackAboveL = null;
//                popupWindow.dismiss();
//                return;
//            }
//        });

        //相机权限
        Camera_Per = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    //获取相机权限
                    if (isClickedPhotoGroup){
                        OpenPhotoGroup();
                    }else {
                        OpenCamera();
                    }
                }else {
                    Toast.makeText(WebActivity.this, "未授予相关权限部分功能暂时无法使用，请在设置-应用中授予", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //拍照回调
        Open_Camera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    if (mImageUri!=null){
                        if (mUploadCallbackAboveL != null) {
                            Uri[] resultUri = {mImageUri};
                            mUploadCallbackAboveL.onReceiveValue(resultUri);
                            mUploadCallbackAboveL = null;
                        }
                    }
                }else {
                    if (mUploadCallbackAboveL != null) {
                        mUploadCallbackAboveL.onReceiveValue(null);
                        mUploadCallbackAboveL = null;
                    }
                }
                popupWindow.dismiss();
            }
        });
        //相册回调
        Open_Photo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null && result.getData().getData() != null) {
                    Uri uri = result.getData().getData();
                    if (mUploadCallbackAboveL != null) {
                        Uri[] resultUri = {uri};
                        mUploadCallbackAboveL.onReceiveValue(resultUri);
                        mUploadCallbackAboveL = null;
                    }
                }else {
                    if (mUploadCallbackAboveL != null) {
                        mUploadCallbackAboveL.onReceiveValue(null);
                        mUploadCallbackAboveL = null;
                    }
                }
                popupWindow.dismiss();
            }
        });
    }

    /*
     * 检查读写权限和相机权限
     * */
    private void CheckPermissionWR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                if (isClickedPhotoGroup) {
                    Log.d(TAG, "打开相册");
                    OpenPhotoGroup();
                }else {
                    Camera_Per.launch(Permission_Camera);
                }
            }else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                Version_R_Write.launch(intent);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (isClickedPhotoGroup) {
                    Log.d(TAG, "打开相册");
                    OpenPhotoGroup();
                }else {
                    Camera_Per.launch(Permission_Camera);
                }
            }else {
                Version_M_Write.launch(Permission_WR);
            }
        }else {
            if (isClickedPhotoGroup) {
                Log.d(TAG, "打开相册");
                OpenPhotoGroup();
            }else {
                Camera_Per.launch(Permission_Camera);
            }
        }
    }

    /*
    * 打开相册
    * */
    private void OpenPhotoGroup(){
        if (Open_Photo!=null) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Open_Photo.launch(intent);
        }
    }

    /*
     * 打开相机
     * */
    private Uri mImageUri;
    private void OpenCamera(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//用来打开相机的Intent
        if(takePhotoIntent.resolveActivity(getPackageManager())!=null){
            Log.d(TAG, "调起相机");
            File imageFile = createImageFile();//创建用来保存照片的文件
            if(imageFile!=null){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this,"com.gzyslczx.yslc.fileprovider",imageFile);
                }else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);//将用于输出的文件Uri传递给相机
                Open_Camera.launch(takePhotoIntent);
            }
        }
    }
    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SelectParent:
            case R.id.SelectPicCancel:
                //取消选择头像
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                if (mUploadCallbackAboveL != null) {
                    mUploadCallbackAboveL.onReceiveValue(null);
                    mUploadCallbackAboveL = null;
                }
                break;
            case R.id.SelectPicFormGroup:
                //从相册选择头像
                isClickedPhotoGroup = true;
                CheckPermissionWR();
                break;
            case R.id.SelectPicTakePhoto:
                //选择拍照头像
                isClickedPhotoGroup = false;
                CheckPermissionWR();
                break;
        }
    }
}