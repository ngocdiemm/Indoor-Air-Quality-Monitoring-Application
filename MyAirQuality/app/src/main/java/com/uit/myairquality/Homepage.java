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
    ImageView image;
    int defaultLanguage = 0;

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

        //Đổi ngôn ngữ
        /*ImageView image = findViewById(R.id.btnLanguage);
        String currentLang = getResources().getConfiguration().locale.getLanguage();
        if (currentLang.equals("en")) {
            image.setImageResource(R.drawable.britishflag);
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
        });*/
        final String selectedLanguage = ChangeLanguages.getSelectedLanguage(this);
        ChangeLanguages.updateResources(this, selectedLanguage);
        String engLang = "English";
        String vnLang = "Vietnamese";
        final String languages[] = {engLang, vnLang};
        image = findViewById(R.id.btnLanguage);
        if(ChangeLanguages.getSelectedLanguage(this).equals("en")) {
            defaultLanguage = 0;
            image.setImageResource(R.drawable.britishflag);
        }else if(ChangeLanguages.getSelectedLanguage(this).equals("vi")) {
            defaultLanguage = 1;
            image.setImageResource(R.drawable.vietnameseflag);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);
                builder.setTitle(getResources().getString(R.string.language))
                        .setSingleChoiceItems(languages, defaultLanguage, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int index) {
                                defaultLanguage = index;
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(defaultLanguage == 0) {
                                    // Đặt ngôn ngữ cho activity hiện tại
                                    ChangeLanguages.setLocale(Homepage.this, "en");
                                } else if (defaultLanguage == 1) {
                                    // Đặt ngôn ngữ cho activity hiện tại
                                    ChangeLanguages.setLocale(Homepage.this, "vi");
                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
            }
        });
}

    //SET LANGUAGE
    /*public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, Homepage.class);
        startActivity(refresh);
        finish();
    }*/

}