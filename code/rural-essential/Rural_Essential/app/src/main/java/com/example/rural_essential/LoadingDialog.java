package com.example.rural_essential;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;




public class LoadingDialog {

    Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    //start the load dialog
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    //stop the load dialog
    public void dismissDialog(){
        dialog.dismiss();
    }

}
