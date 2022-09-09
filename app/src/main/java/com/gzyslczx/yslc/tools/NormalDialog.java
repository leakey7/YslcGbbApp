package com.gzyslczx.yslc.tools;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public class NormalDialog {

    AlertDialog alertDialog;


    public NormalDialog(Context context, View view, boolean OutSideCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(OutSideCancel);
        alertDialog = builder.create();
    }



    public void ShowDialog(){
        alertDialog.show();
    }

    public void DismissDialog(){
        alertDialog.cancel();
    }


}
