package com.gzyslczx.yslc.fragments.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gzyslczx.yslc.AboutUsActivity;
import com.gzyslczx.yslc.BaseActivity;
import com.gzyslczx.yslc.KLineActivity;
import com.gzyslczx.yslc.MsgBoxActivity;
import com.gzyslczx.yslc.R;
import com.gzyslczx.yslc.StockMarketActivity;
import com.gzyslczx.yslc.SuggestActivity;
import com.gzyslczx.yslc.UserConfigActivity;
import com.gzyslczx.yslc.WebActivity;
import com.gzyslczx.yslc.databinding.MineFragmentBinding;
import com.gzyslczx.yslc.databinding.YesNoSelectorLayoutBinding;
import com.gzyslczx.yslc.events.GuBbChangeLoginEvent;
import com.gzyslczx.yslc.events.MineInfoEvent;
import com.gzyslczx.yslc.events.NewVersionEvent;
import com.gzyslczx.yslc.events.NoticeUpdateMineInfoEvent;
import com.gzyslczx.yslc.fragments.BaseFragment;
import com.gzyslczx.yslc.presenter.MinePresenter;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.DisplayTool;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.SpTool;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class MineFragment extends BaseFragment<MineFragmentBinding> implements View.OnClickListener {

    private final String TAG = "MineFragment";
    private MinePresenter mPresenter;
    private String HeadImgPath;
    private boolean isClickUpdate = false;
    private AlertDialog alertDialog;
    private YesNoSelectorLayoutBinding alterBinding;
    private ActivityResultLauncher<Intent> Version_O_DownLoad;
    private ActivityResultLauncher<String> Version_M_DownLoad;
    private final String DownLoadPermission = Manifest.permission.REQUEST_INSTALL_PACKAGES;
    private ActivityResultLauncher<Intent> AppSetting;
    private boolean firstInit = true;

    @Override
    protected void InitParentLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = MineFragmentBinding.inflate(inflater, container, false);
        InitView();
    }

    @Override
    protected void InitView() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mViewBinding.MineHeadImg.getLayoutParams();
        layoutParams.setMargins(DisplayTool.dp2px(getContext(), 14), TransStatusTool.getStatusbarHeight(getContext()) + DisplayTool.dp2px(getContext(), 28), 0, 0);
        mViewBinding.MineHeadImg.setLayoutParams(layoutParams);
        EventBus.getDefault().register(this);
        //??????????????????
        mViewBinding.MineMsgImg.setOnClickListener(this::onClick);
        mViewBinding.MineMsgText.setOnClickListener(this::onClick);
        mViewBinding.MineMsgRightImg.setOnClickListener(this::onClick);
        mViewBinding.MineMsgNum.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.MineAboutImg.setOnClickListener(this::onClick);
        mViewBinding.MineAboutText.setOnClickListener(this::onClick);
        mViewBinding.MineAboutRightImg.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.MineUpdateText.setOnClickListener(this::onClick);
        mViewBinding.MineUpdateImg.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.MineUpdateRightImg.setOnClickListener(this::onClick);
        mViewBinding.MineVersion.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.MineSuggestImg.setOnClickListener(this::onClick);
        mViewBinding.MineSuggestText.setOnClickListener(this::onClick);
        mViewBinding.MineSuggestRightImg.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.MineLockImg.setOnClickListener(this::onClick);
        mViewBinding.MineLockText.setOnClickListener(this::onClick);
        mViewBinding.MineLockRightImg.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.MineRiskImg.setOnClickListener(this::onClick);
        mViewBinding.MineRiskText.setOnClickListener(this::onClick);
        mViewBinding.MineRiskRightImg.setOnClickListener(this::onClick);
        //??????????????????
        mViewBinding.MineCancelLogin.setOnClickListener(this::onClick);
        //????????????
        mViewBinding.MineHeadImg.setOnClickListener(this::onClick);
        //????????????
        mViewBinding.MineName.setOnClickListener(this::onClick);
        AppSetting = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.d(TAG, "?????????????????????");
            }
        });
        mPresenter = new MinePresenter();
        InitDownLoadPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpTool.Instance(getContext()).IsGuBbLogin()) {
            if (firstInit){
                mViewBinding.MineLoad.setVisibility(View.VISIBLE);
                firstInit=false;
            }
            mPresenter.RequestMineInfo(getContext(), this);
            mPresenter.UpdateVersion(getContext(), this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (broadcastReceiver != null) {
            getContext().unregisterReceiver(broadcastReceiver);
        }
    }

    /*
     * ?????????????????????
     * */
    private void InitDownLoadPermission() {
        Version_O_DownLoad = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (getActivity().getPackageManager().canRequestPackageInstalls()) {
                        //????????????
                        DownLoadApp(mViewBinding.MineVersion.getText().toString().substring(3), (String) mViewBinding.MineUpdateRightImg.getTag());
                    } else {
                        Toast.makeText(getContext(), "??????????????????????????????????????????????????????????????????-???????????????", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Version_M_DownLoad = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    //????????????
                    DownLoadApp(mViewBinding.MineVersion.getText().toString().substring(3), (String) mViewBinding.MineUpdateRightImg.getTag());
                } else {
                    Toast.makeText(getContext(), "??????????????????????????????????????????????????????????????????-???????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
     * ????????????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChangeLoginEvent(GuBbChangeLoginEvent event) {
        //??????????????????
        Log.d(TAG, "?????????????????????");
        if (event.isLogin()) {
            mViewBinding.MineLoad.setVisibility(View.VISIBLE);
            mPresenter.RequestMineInfo(getContext(), this);
        } else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.def_headimg)).centerCrop().into(mViewBinding.MineHeadImg);
            mViewBinding.MinePhone.setVisibility(View.GONE);
            mViewBinding.MineCancelLogin.setVisibility(View.GONE);
            mViewBinding.MineMsgNum.setVisibility(View.GONE);
            mViewBinding.MineUpdateRightImg.setVisibility(View.GONE);
            mViewBinding.MineVersion.setVisibility(View.GONE);
            mViewBinding.MineName.setText(getString(R.string.ClickLogin));
        }
    }

    /*
     * ??????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMineInfoEvent(MineInfoEvent event) {
        Log.d(TAG, "?????????????????????");
        if (event.isFlag()) {
            if (!TextUtils.isEmpty(event.getDataObj().getHeadImg())) {
                Glide.with(getContext()).load(event.getDataObj().getHeadImg())
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(),
                                new RoundedCorners(DisplayTool.dp2px(getContext(), 54)))))
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.def_headimg))
                        .into(mViewBinding.MineHeadImg);
                HeadImgPath = event.getDataObj().getHeadImg();
            }
            mViewBinding.MineName.setText(event.getDataObj().getNickName());
            mViewBinding.MinePhone.setText(event.getDataObj().getMobilePhone());
            mViewBinding.MinePhone.setVisibility(View.VISIBLE);
            mViewBinding.MineCancelLogin.setVisibility(View.VISIBLE);
            if (!event.getDataObj().getMsgNum().equals("0")) {
                mViewBinding.MineMsgNum.setText(String.valueOf(event.getDataObj().getMsgNum()));
                mViewBinding.MineMsgNum.setVisibility(View.VISIBLE);
            } else {
                mViewBinding.MineMsgNum.setVisibility(View.GONE);
            }
        } else {
            Glide.with(getContext()).load(ContextCompat.getDrawable(getContext(), R.drawable.def_headimg)).centerCrop().into(mViewBinding.MineHeadImg);
            mViewBinding.MinePhone.setVisibility(View.GONE);
            mViewBinding.MineCancelLogin.setVisibility(View.GONE);
            mViewBinding.MineMsgNum.setVisibility(View.GONE);
            mViewBinding.MineUpdateRightImg.setVisibility(View.GONE);
            mViewBinding.MineVersion.setVisibility(View.GONE);
            mViewBinding.MineName.setText(getString(R.string.ClickLogin));
        }
        mViewBinding.MineLoad.setVisibility(View.GONE);
    }

    /*
     * ????????????
     * */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MineMsgImg:
            case R.id.MineMsgText:
            case R.id.MineMsgRightImg:
            case R.id.MineMsgNum:
                //??????????????????
                startActivity(new Intent(getContext(), MsgBoxActivity.class));
                break;
            case R.id.MineAboutImg:
            case R.id.MineAboutText:
            case R.id.MineAboutRightImg:
                //??????????????????
//                startActivity(new Intent(getContext(), AboutUsActivity.class));
                startActivity(new Intent(getContext(), StockMarketActivity.class));
                break;
            case R.id.MineUpdateImg:
            case R.id.MineUpdateText:
                //??????????????????
                isClickUpdate = true;
                mPresenter.UpdateVersion(getContext(), this);
                break;
            case R.id.MineUpdateRightImg:
            case R.id.MineVersion:
                //????????????
                if (!TextUtils.isEmpty((String) mViewBinding.MineUpdateRightImg.getTag())) {
                    if (alertDialog == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setCancelable(false);
                        alterBinding = YesNoSelectorLayoutBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.yes_no_selector_layout, null));
                        alterBinding.SelectorYes.setText("??????");
                        alterBinding.SelectorNo.setOnClickListener(this::onClick);
                        alterBinding.SelectorYes.setOnClickListener(this::onClick);
                        alertDialog = builder.create();
                    }
                    if (TextUtils.isEmpty((String) mViewBinding.MineVersion.getTag())) {
                        alterBinding.SelectorMessage.setText("?????????????????????");
                    } else {
                        alterBinding.SelectorMessage.setText(String.format("%s\t?????????????????????", (String) mViewBinding.MineVersion.getTag()));
                    }
                    alertDialog.show();
                    WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
                    layoutParams.height = DisplayTool.dp2px(getContext(), 160);
                    layoutParams.gravity = Gravity.CENTER;
                    alertDialog.getWindow().setAttributes(layoutParams);
                    alertDialog.setContentView(alterBinding.getRoot());
                }
                break;
            case R.id.MineSuggestImg:
            case R.id.MineSuggestText:
            case R.id.MineSuggestRightImg:
                //??????????????????
                if (SpTool.Instance(getContext()).IsGuBbLogin()) {
                    Intent suggestIntent = new Intent(getContext(), SuggestActivity.class);
                    startActivity(suggestIntent);
                } else {
                    Toast.makeText(getContext(), "????????????????????????/??????", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.MineLockImg:
            case R.id.MineLockText:
            case R.id.MineLockRightImg:
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                AppSetting.launch(intent);
                break;
            case R.id.MineRiskImg:
            case R.id.MineRiskText:
            case R.id.MineRiskRightImg:
                Intent webIntent = new Intent(getContext(), WebActivity.class);
                webIntent.putExtra(WebActivity.WebKey, ConnPath.RiskTestUrl);
                webIntent.putExtra(WebActivity.WebVideoKey, false);
                startActivity(webIntent);
                break;
            case R.id.MineCancelLogin:
                //??????????????????
                SpTool.Instance(getContext()).ClearInfo();
                GuBbChangeLoginEvent event = new GuBbChangeLoginEvent(false);
                EventBus.getDefault().post(event);
                break;
            case R.id.MineHeadImg:
                if (SpTool.Instance(getContext()).IsGuBbLogin()) {
                    //???????????????????????????????????????????????????
                    Intent ConfigUserIntent = new Intent(getContext(), UserConfigActivity.class);
                    if (!TextUtils.isEmpty(HeadImgPath)) {
                        ConfigUserIntent.putExtra(UserConfigActivity.HeadImgKey, HeadImgPath);
                    }
                    startActivity(ConfigUserIntent);

                } else {
                    NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
                }
                break;
            case R.id.MineName:
                if (!SpTool.Instance(getContext()).IsGuBbLogin()) {
                    NormalActionTool.LoginAction(getContext(), this, (BaseActivity) getActivity(), null, TAG);
                }
                break;
            case R.id.SelectorYes:
                alertDialog.dismiss();
                boolean HasPermission = CheckUpdatePermission();
                if (HasPermission) {
                    DownLoadApp(mViewBinding.MineVersion.getText().toString().substring(3), (String) mViewBinding.MineUpdateRightImg.getTag());
                }
                break;
            case R.id.SelectorNo:
                alertDialog.dismiss();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnUpdateMineInfoEvent(NoticeUpdateMineInfoEvent event) {
        if (event.isNeedUpdate()) {
            mViewBinding.MineLoad.setVisibility(View.VISIBLE);
            mPresenter.RequestMineInfo(getContext(), this);
        }
    }

    /*
     * ??????????????????
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnUpdateVersion(NewVersionEvent event) {
        if (event.isFlag()) {
            String cVersion = null;
            try {
                cVersion = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName.substring(1);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String nVersion = event.getDataObj().getVersionNum().substring(1);
            if (Double.parseDouble(cVersion) < Double.parseDouble(nVersion)) {
                mViewBinding.MineUpdateRightImg.setTag(event.getDataObj().getUrl());
                mViewBinding.MineVersion.setText(getString(R.string.VersionNumber) + event.getDataObj().getVersionNum());
                mViewBinding.MineVersion.setTag(event.getDataObj().getDesc());
                mViewBinding.MineVersion.setVisibility(View.VISIBLE);
                mViewBinding.MineUpdateRightImg.setVisibility(View.VISIBLE);
            } else {
                mViewBinding.MineUpdateRightImg.setTag(null);
                mViewBinding.MineVersion.setTag(null);
                mViewBinding.MineVersion.setText("");
                mViewBinding.MineVersion.setVisibility(View.GONE);
                mViewBinding.MineUpdateRightImg.setVisibility(View.GONE);
                if (isClickUpdate) {
                    Toast.makeText(getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                    isClickUpdate = false;
                }
            }
        }
    }

    /*
     * ????????????????????????
     * */
    public boolean CheckUpdatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("GbbUpdatePerm", "?????????Android8");
            if (!getContext().getPackageManager().canRequestPackageInstalls()) {
                Uri packageURI = Uri.parse("package:" + getContext().getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                Version_O_DownLoad.launch(intent);
                return false;
            }
        } else {
            Log.d("GbbUpdatePerm", "??????Android8");
            if (ContextCompat.checkSelfPermission(getContext(), DownLoadPermission) == PackageManager.PERMISSION_DENIED) {
                Version_M_DownLoad.launch(DownLoadPermission);
                return false;
            }
        }
        return true;
    }

    /*
     * ??????APK??????
     * */
    public void DownLoadApp(String VersionNum, String UpdateUrl) {
        Log.d(TAG, "???????????????" + UpdateUrl);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(UpdateUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        request.setDestinationInExternalFilesDir(getContext(), Environment.DIRECTORY_DOWNLOADS,
                String.format("gubaba_%s.apk", VersionNum));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // ?????? Notification ??????
        request.setTitle("????????????????????????");
        request.setDescription("???????????????????????????");
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setMimeType("application/vnd.android.package-archive");

        // ?????????DownloadManager ??????
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        UpdateListener(downloadManager.enqueue(request));
        Toast.makeText(getContext(), "???????????????????????????...", Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver broadcastReceiver;

    // ??????????????????
    private void UpdateListener(long Id) {
        // ????????????????????????????????????????????????
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                // ????????????????????????????????????????????????id???
                long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                // ??????????????????id?????????????????????id?????????????????????????????????apk?????????id?????????????????????????????????????????????????????????
                if (ID == Id) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(Id);
                    Cursor cursor = manager.query(query);
                    if (cursor.moveToFirst()) {
                        // ????????????????????????
                        @SuppressLint("Range")
                        String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                        // ????????????????????????????????????????????????,?????????????????????apk
                        if (fileName != null) {
                            OpenAPK(fileName);
                        } else {
                            Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                    cursor.close();
                }
            }
        };
        getContext().registerReceiver(broadcastReceiver, intentFilter);
    }

    private void OpenAPK(String fileSavePath) {
        File file = new File(Uri.parse(fileSavePath).getPath());
        String filePath = file.getAbsolutePath();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//????????????????????????7.0
            // ???????????????uri??????
            //  authority: ???apk???????????????.fileprovider???
            data = FileProvider.getUriForFile(getContext(),
                    "com.gzyslczx.yslc.fileprovider", new File(filePath));
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// ?????????????????????????????????
        } else {
            data = Uri.fromFile(file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
