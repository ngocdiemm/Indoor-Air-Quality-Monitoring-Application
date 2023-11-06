package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

//    DrawerLayout drawerLayout;
//    ImageView menu;
//    LinearLayout home, settings, share, exit, info;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//
//        drawerLayout = findViewById(R.id.drawerLayout);
//        menu = findViewById(R.id.menu);
//        home = findViewById(R.id.home);
//        share = findViewById(R.id.share);
//        settings = findViewById(R.id.settings);
//        exit = findViewById(R.id.exit);
//        info = findViewById(R.id.info);
//
//
//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDrawer(drawerLayout);
//
//            }
//        });
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(Settings.this, navigation.class);
//
//            }
//        });
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recreate();
//            }
//        });
//
//        /*ishare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(Settings.this, ShareActivity.class);
//            }
//        });
//        info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                redirectActivity(SettingsActivity.this, AboutActivity.class);
//            }
//        });
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(SettingsActivity.this,"Logout", Toast.LENGTH_SHORT).show();
//            }
//        });*/
//    }
//    public static void  openDrawer (DrawerLayout drawerLayout){
//        drawerLayout.openDrawer(GravityCompat.START);
//
//    }
//    public static void closeDrawer (DrawerLayout drawerLayout){
//        drawerLayout.isDrawerOpen(GravityCompat.START);
//        drawerLayout.closeDrawer(GravityCompat.START);
//    }
//
//    public static void  redirectActivity (Activity activity, Class secondActivity){
//        Intent intent = new Intent(activity,secondActivity);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
//        activity.finish();
//    }
//    protected void onPause(){
//        super.onPause();
//        closeDrawer(drawerLayout);
//    }
}