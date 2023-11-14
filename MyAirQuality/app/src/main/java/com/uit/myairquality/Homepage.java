package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Homepage extends AppCompatActivity {
    Button btnLogin, btnRegister;
    ImageView image;
    int defaultLanguage = 0;
    final String languages[] = {"English", "Vietnamese"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChangeLanguages.loadLocaleChanged(Homepage.this);
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
        image = findViewById(R.id.btnLanguage);
        if (ChangeLanguages.getSelectedLanguage(this).equals("en")) {
            defaultLanguage = 0;
            image.setImageResource(R.drawable.britishflag);
        } else if (ChangeLanguages.getSelectedLanguage(this).equals("vi")) {
            defaultLanguage = 1;
            image.setImageResource(R.drawable.vietnameseflag);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });
    }

    private void showChangeLanguageDialog() {
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
                        if (defaultLanguage == 0) {
                            // Đặt ngôn ngữ cho activity hiện tại
                            ChangeLanguages.setLocale(Homepage.this, "en");
                            image.setImageResource(R.drawable.britishflag);
                            recreate();
                        } else if (defaultLanguage == 1) {
                            // Đặt ngôn ngữ cho activity hiện tại
                            ChangeLanguages.setLocale(Homepage.this, "vi");
                            image.setImageResource(R.drawable.vietnameseflag);
                            recreate();
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
}