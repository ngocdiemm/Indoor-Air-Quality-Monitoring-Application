package com.uit.myairquality;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Loading extends AppCompatActivity {

    Activity activity;
    LayoutInflater inflate;
    AlertDialog.Builder alert;
    View view;
    AlertDialog alertDialog;
    public Loading(Activity mainActivity) {
        activity = mainActivity;
    }


    public void startLoadingDialog() {
        alert = new AlertDialog.Builder(activity);


        //Chuyển đổi 1 đối tượng xml thành đối tượng view
        LayoutInflater inflate = activity.getLayoutInflater();
        alert.setView(inflate.inflate(R.layout.activity_loading, null));
        alert.setCancelable(false);


        alertDialog = alert.create();
        alertDialog.show();

    }
    public void cancelLoadingDialog(boolean isSusscess) {
        alertDialog.dismiss();
        alert = new AlertDialog.Builder(activity);
        // Chuyển đổi 1 đối tượng xml thành đối tượng view
        inflate = activity.getLayoutInflater();
        if(isSusscess) {


        }else {


        }
        alert.setView(view);
        alert.setCancelable(false);


        alertDialog = alert.create();
        alertDialog.show();
    }
}
