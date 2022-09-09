package com.gzyslczx.yslc;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.gzyslczx.yslc.databinding.ActivityYsFlashDetailBinding;
import com.gzyslczx.yslc.tools.ConnPath;
import com.gzyslczx.yslc.tools.NormalActionTool;
import com.gzyslczx.yslc.tools.ShareTool;
import com.gzyslczx.yslc.tools.TransStatusTool;
import com.gzyslczx.yslc.tools.customviews.SharePopup;

public class YsFlashDetailActivity extends BaseActivity<ActivityYsFlashDetailBinding> {

    private final String TAG = "FDetailsAct";

    public static final String DesKey = "FlashDetails";
    public static final String FID = "FlashID";
    public static final String FDate = "FlashDate";
    public static final String FTime = "FlashTime";
    public static final String FContent = "FlashContent";
    private SharePopup sharePopup;

    private String FlashID, mDate, mTime, mContent;

    @Override
    void InitParentLayout() {
        mViewBinding = ActivityYsFlashDetailBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    void InitView() {
        Bundle bundle = getIntent().getBundleExtra(DesKey);
        FlashID = bundle.getString(FID);
        mDate = bundle.getString(FDate);
        mTime = bundle.getString(FTime);
        mContent = bundle.getString(FContent);
        mViewBinding.FDetailsDate.setText(mDate);
        mViewBinding.FDetailsTime.setText(mTime);
        mViewBinding.FDetailsContent.setText(mContent);
        //点击回退
        SetupBackClicked();
        //点击分享
        SetupShareClicked();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sharePopup!=null){
            sharePopup.Clear();
        }
    }

    /*
    * 设置分享点击
    * */
    private void SetupShareClicked() {
        mViewBinding.FDetailsToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.share){
                    if (sharePopup==null) {
                        sharePopup = new SharePopup(YsFlashDetailActivity.this, mViewBinding.getRoot(), true,
                                ConnPath.YsFlashShareUrl+FlashID, getString(R.string.YsFlashInfo));
                    }
                    sharePopup.Show(mViewBinding.getRoot(), Gravity.BOTTOM, 0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    /*
    * 设置回退点击
    * */
    private void SetupBackClicked() {
        mViewBinding.FDetailsToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}