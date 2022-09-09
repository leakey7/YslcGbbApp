package com.gzyslczx.yslc;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.gzyslczx.yslc.databinding.ActivitySuggestBinding;
import com.gzyslczx.yslc.events.SuggestCommitEvent;
import com.gzyslczx.yslc.presenter.MineSuggestPresenter;
import com.gzyslczx.yslc.tools.TransStatusTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SuggestActivity extends BaseActivity<ActivitySuggestBinding> implements View.OnClickListener {

    private  MineSuggestPresenter presenter;
    private AlertDialog alertDialog;

    @Override
    void InitParentLayout() {
        EventBus.getDefault().register(this);
        presenter = new MineSuggestPresenter();
        mViewBinding = ActivitySuggestBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        getWindow().setStatusBarColor(ActivityCompat.getColor(this, R.color.white));
        TransStatusTool.setStatusBarLightMode(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    void InitView() {
        SetupBackListener();
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setCancelable(false);
         builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
             }
         });
         alertDialog = builder.create();
        mViewBinding.SuggestCommit.setOnClickListener(this::onClick);
    }

    private void SetupBackListener() {
        mViewBinding.SuggestToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.SuggestCommit){
            if (TextUtils.isEmpty(mViewBinding.SuggestContent.getText().toString().trim())){
                alertDialog.setMessage("提交内容不能为空");
                alertDialog.show();
            }else {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(mViewBinding.SuggestCommit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                presenter.RequestSuggest(this, this,
                        mViewBinding.SuggestContent.getText().toString().trim());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuggestEvent(SuggestCommitEvent event){
        if (event.isFlag()){
            mViewBinding.SuggestContent.setText("");
//            Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
            alertDialog.setMessage("提交成功");
            alertDialog.show();
        }else {
            Toast.makeText(this, event.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

}