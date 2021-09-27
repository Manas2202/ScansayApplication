package com.example.scansayapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class loading_dialog {
    Activity activity;
    AlertDialog dialog;
    public loading_dialog(Activity myActivity){
        activity = myActivity;
    }
    public void startLoadingAnimation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
}
