package com.uit.myairquality;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Locale;

public class Homepage extends AppCompatActivity {
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Mở màn hình LogIn
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, LogIn.class);
                startActivity(intent);
            }
        });
        // Mở màn hình Register
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Register.class);
                startActivity(intent);
            }
        });


        ImageView image = findViewById(R.id.btnLanguage);
        String currentLang = getResources().getConfiguration().locale.getLanguage();
        if (currentLang.equals("en")) {
            image.setImageResource(R.drawable.americanflag);
        } else {
            image.setImageResource(R.drawable.vietnameseflag);
        }

        //When click language image view
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentLang = getResources().getConfiguration().locale.getLanguage();
                if (currentLang.equals("en")) {
                    setLocale("vi");
                } else {
                    setLocale("en");
                }
            }
        });

    }

    //SET LANGUAGE
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, Homepage.class);
        startActivity(refresh);
        finish();
    }


}