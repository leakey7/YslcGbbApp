package com.gzyslczx.yslc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gzyslczx.yslc.databinding.ActivityUserConfigBinding;
import com.gzyslczx.yslc.databinding.SelectPicPopupLayoutBinding;
import com.gzyslczx.yslc.databinding.YesNoSelectorLayoutBinding;
import com.gzyslczx.yslc.events.NoticeUpdateMineInfoEvent;
import com.gzyslczx.yslc.events.UserChangeHeadImgEvent;
import com.gzyslczx.yslc.events.UserChangeNickNameEvent;
import com.gzyslczx.yslc.presenter.UserConfigPresenter;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserConfigActivity extends BaseActivity<ActivityUserConfigBinding> implements View.OnClickListener, TextWatcher {

    private final String TAG = "UserConfigAct";
    public static final String HeadImgKey = "HImgPath";
    private String HeadImg;
    private PopupWindow popupWindow;
    private AlertDialog alertDialog;
    private ActivityResultLauncher<Intent> Version_R_Write, Open_Photo, Crop_Photo, Open_Camera;
    private ActivityResultLauncher<String[]> Version_M_Write;
    private ActivityResultLauncher<String> Camera_Per;
    private String[] Permission_WR = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String Permission_Camera = Manifest.permission.CAMERA;
    private UserConfigPresenter mPresenter;
    private File mCropOutPutFile;
    private Uri mImageUri;
    private boolean isClickedPhotoGroup=false;
    private YesNoSelectorLayoutBinding alterBinding;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityUserConfigBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        EventBus.getDefault().register(this);
        HeadImg = getIntent().getStringExtra(HeadImgKey);
        if (!TextUtils.isEmpty(HeadImg)) {
            Glide.with(this).load(HeadImg)
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                            new RoundedCorners(DisplayTool.dp2px(this, 54)))))
                    .placeholder(ActivityCompat.getDrawable(this, R.drawable.def_headimg))
                    .into(mViewBinding.UserConfigHeadImg);
        }
        //????????????
        mViewBinding.UserConfigToolBarSave.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.UserConfigHeadImg.setOnClickListener(this::onClick);
        mViewBinding.UserConfigHeadImgTag.setOnClickListener(this::onClick);
        //????????????
        SetupBackClicked();
        InitActivityResultLauncherWR();
        setEditTextInputSpace();
        mViewBinding.UserConfigName.addTextChangedListener(this);
        mPresenter = new UserConfigPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }

    /*????????????
     *
     * */
    private void SetupBackClicked() {
        mViewBinding.UserConfigToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
     * ????????????
     * */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UserConfigToolBarSave:
                //????????????
                if (mCropOutPutFile!=null || (!TextUtils.isEmpty(mViewBinding.UserConfigName.getText())
                        && mViewBinding.UserConfigName.getText().toString().trim().length()>=4
                        && mViewBinding.UserConfigName.getText().toString().trim().length()<=12)) {
                    if (alertDialog == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setCancelable(false);
                        alterBinding = YesNoSelectorLayoutBinding.bind(LayoutInflater.from(this).inflate(R.layout.yes_no_selector_layout, null));
                        alterBinding.SelectorMessage.setText("??????????????????????????????");
                        alterBinding.SelectorYes.setText("??????");
                        alterBinding.SelectorNo.setOnClickListener(this::onClick);
                        alterBinding.SelectorYes.setOnClickListener(this::onClick);
                        alertDialog = builder.create();
                    }
                    alertDialog.show();
                    WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                    layoutParams.height = DisplayTool.dp2px(this, 160);
                    layoutParams.gravity = Gravity.CENTER;
                    alertDialog.getWindow().setAttributes(layoutParams);
                    alertDialog.setContentView(alterBinding.getRoot());
                }
                break;
            case R.id.UserConfigHeadImg:
            case R.id.UserConfigHeadImgTag:
                //??????????????????
                if (popupWindow == null) {
                    SelectPicPopupLayoutBinding binding = SelectPicPopupLayoutBinding.bind(LayoutInflater.from(this).inflate(R.layout.select_pic_popup_layout, null));
                    binding.SelectPicCancel.setOnClickListener(this::onClick);
                    binding.SelectPicTakePhoto.setOnClickListener(this::onClick);
                    binding.SelectPicFormGroup.setOnClickListener(this::onClick);
                    binding.SelectParent.setOnClickListener(this::onClick);
                    popupWindow = new PopupWindow(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    popupWindow.setFocusable(false);
                }
                popupWindow.showAtLocation(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.SelectParent:
            case R.id.SelectPicCancel:
                //??????????????????
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                break;
            case R.id.SelectPicFormGroup:
                //?????????????????????
                isClickedPhotoGroup=true;
                CheckPermissionWR();
                break;
            case R.id.SelectPicTakePhoto:
                //??????????????????
                isClickedPhotoGroup=false;
                CheckPermissionWR();
                break;
            case R.id.SelectorNo:
                //????????????
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                break;
            case R.id.SelectorYes:
                //????????????
                alertDialog.dismiss();
                mViewBinding.UserConfigLoadBg.setVisibility(View.VISIBLE);
                mViewBinding.UserConfigLoad.setVisibility(View.VISIBLE);
                mViewBinding.UserConfigLoadText.setVisibility(View.VISIBLE);
                if (mCropOutPutFile!=null){
                    mPresenter.RequestChangeHeadImg(this, this, mCropOutPutFile);
                }else {
                    if (!TextUtils.isEmpty(mViewBinding.UserConfigName.getText())
                            && mViewBinding.UserConfigName.getText().toString().trim().length()>=4
                            && mViewBinding.UserConfigName.getText().toString().trim().length()<=12){
                        mPresenter.RequestChangeNickName(this, this, mViewBinding.UserConfigName.getText().toString());
                    }
                }
                break;
        }
    }

    /*
     * ?????????ActivityResultLauncher
     * */
    private void InitActivityResultLauncherWR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Version_R_Write = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (Environment.isExternalStorageManager()) {
                        Log.d(TAG, "????????????");
                        OpenPhotoGroup();
                    } else {
                        Toast.makeText(UserConfigActivity.this, "??????????????????????????????????????????????????????????????????-???????????????", Toast.LENGTH_SHORT).show();
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
                        Log.d(TAG, "????????????");
                        OpenPhotoGroup();
                    } else {
                        Toast.makeText(UserConfigActivity.this, "??????????????????????????????????????????????????????????????????-???????????????", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        Camera_Per = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    //??????????????????
                    if (isClickedPhotoGroup){
                        OpenPhotoGroup();
                    }else {
                        OpenCamera();
                    }
                }else {
                    Toast.makeText(UserConfigActivity.this, "??????????????????????????????????????????????????????????????????-???????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Open_Photo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null && result.getData().getData() != null) {
                    Uri uri = result.getData().getData();
                    cropPhoto(uri);
                }
            }
        });

        Crop_Photo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (mCropOutPutFile != null) {
                    Glide.with(UserConfigActivity.this).load(mCropOutPutFile)
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                    new RoundedCorners(DisplayTool.dp2px(UserConfigActivity.this, 54)))))
                            .placeholder(ActivityCompat.getDrawable(UserConfigActivity.this, R.drawable.def_headimg))
                            .into(mViewBinding.UserConfigHeadImg);
                    popupWindow.dismiss();
                }
            }
        });
        Open_Camera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    if (mImageUri!=null){
                        cropPhoto(mImageUri);
                    }
                }
            }
        });
    }

    /*
     * ?????????????????????????????????
     * */
    private void CheckPermissionWR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                if (isClickedPhotoGroup) {
                    Log.d(TAG, "????????????");
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
                   Log.d(TAG, "????????????");
                   OpenPhotoGroup();
               }else {
                   Camera_Per.launch(Permission_Camera);
               }
            }else {
                Version_M_Write.launch(Permission_WR);
            }
        }else {
            if (isClickedPhotoGroup) {
                Log.d(TAG, "????????????");
                OpenPhotoGroup();
            }else {
                Camera_Per.launch(Permission_Camera);
            }
        }
    }

    /*
    * ????????????
    * */
    private void OpenPhotoGroup(){
        if (Open_Photo!=null) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Open_Photo.launch(intent);
        }
    }

    /*
    * ????????????
    * */
    private void OpenCamera(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//?????????????????????Intent
        if(takePhotoIntent.resolveActivity(getPackageManager())!=null){
            Log.d(TAG, "????????????");

            File imageFile = createImageFile();//?????????????????????????????????
            if(imageFile!=null){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                    /*7.0???????????????FileProvider???File?????????Uri*/
                    mImageUri = FileProvider.getUriForFile(this,"com.gzyslczx.yslc.fileprovider",imageFile);
                }else {
                    /*7.0?????????????????????Uri???fromFile?????????File?????????Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);//????????????????????????Uri???????????????
                Open_Camera.launch(takePhotoIntent);
            }
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     * @return ?????????????????????
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

    /*
    * ??????????????????
    * */
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //??????URI???selection??????????????????????????????
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /*
    * ????????????
    * */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// ?????????
        intent.putExtra("scaleUpIfNeeded", true);// ?????????
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //?????? android ????????????????????????????????? ????????????????????????????????????????????????  ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createTmpFile(this)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        Crop_Photo.launch(intent);
    }

    public File createTmpFile(Context context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) { // ?????????
//          File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File pic = Environment.getExternalStorageDirectory();
            if (!pic.exists()) {
                pic.mkdirs();
            }
            Log.d(TAG, "?????????: pic dir = " + pic.getAbsolutePath());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = TextUtils.concat("multi_image_", timeStamp, ".jpg").toString();
            mCropOutPutFile = new File(pic, fileName);
            return mCropOutPutFile;
        } else {
            File cacheDir = context.getCacheDir();
            Log.d(TAG, "?????????: pic dir = " + cacheDir.getAbsolutePath());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = TextUtils.concat("multi_image_", timeStamp, ".jpg").toString();
            mCropOutPutFile = new File(cacheDir, fileName);
            return mCropOutPutFile;
        }
    }


    /*
    * ??????????????????
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeHeadImgEvent(UserChangeHeadImgEvent event){
        if (event.isFrag()){
            Log.d(TAG, "?????????????????????");
            mCropOutPutFile=null;
            if (!TextUtils.isEmpty(mViewBinding.UserConfigName.getText())
                    && mViewBinding.UserConfigName.getText().toString().trim().length()>=4
                    && mViewBinding.UserConfigName.getText().toString().trim().length()<=12){
                mPresenter.RequestChangeNickName(this, this, mViewBinding.UserConfigName.getText().toString());
            }else {
                mViewBinding.UserConfigLoadBg.setVisibility(View.GONE);
                mViewBinding.UserConfigLoad.setVisibility(View.GONE);
                mViewBinding.UserConfigLoadText.setVisibility(View.GONE);
                NoticeUpdateMineInfoEvent updateMineInfoEvent = new NoticeUpdateMineInfoEvent(true);
                EventBus.getDefault().post(updateMineInfoEvent);
                Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
    }


    /*
     * ??????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeNickNameEvent(UserChangeNickNameEvent event){
        mViewBinding.UserConfigLoadBg.setVisibility(View.GONE);
        mViewBinding.UserConfigLoad.setVisibility(View.GONE);
        mViewBinding.UserConfigLoadText.setVisibility(View.GONE);
        if (event.isFlag()){
            Log.d(TAG, "?????????????????????");
            NoticeUpdateMineInfoEvent updateMineInfoEvent = new NoticeUpdateMineInfoEvent(true);
            EventBus.getDefault().post(updateMineInfoEvent);
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, event.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length()<4 || charSequence.length()>12){
            mViewBinding.UserConfigTip.setVisibility(View.VISIBLE);
        }else if (charSequence.length()>=4 && charSequence.length()<=12){
            mViewBinding.UserConfigTip.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    /**
     * ??????EditText???????????????????????????????????????????????????&&
     *
     */
    public void setEditTextInputSpace() {
        final Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher emojiMatcher = emoji.matcher(source);
                //??????????????????
//                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~???@#???%??????&*????????????+|{}????????????????????????????????????]";
//                Pattern pattern = Pattern.compile(speChat);
//                Matcher matcher = pattern.matcher(source.toString());
                //??????????????????
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                    //??????????????????
                } else if (emojiMatcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        mViewBinding.UserConfigName.setFilters(new InputFilter[]{filter});
    }

}