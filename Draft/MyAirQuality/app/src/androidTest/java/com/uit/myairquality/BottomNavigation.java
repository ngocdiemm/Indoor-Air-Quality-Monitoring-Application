package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import me.ibrahimsn.lib.SmoothBottomBar;

public class BottomNavigation extends AppCompatActivity {

    SmoothBottomBar smoothBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        smoothBottomBar=findViewById(R.id.bottom_nav);
    }
}