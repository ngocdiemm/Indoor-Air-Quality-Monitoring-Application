package com.uit.myairquality;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    String username = "";
    String password = "";

    Button btnRegister;
    private static final String API_URL = "https://uiot.ixxc.dev/api/master/console/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Quay lại màn hình Homepage
        Button btnBackRegister = findViewById(R.id.btnBackRegister);
        btnBackRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Homepage.class);
                startActivity(intent);
            }
        });
/*
        // Thiết lập WebView
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); // Cho phép JavaScript
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Trang web đã tải xong, ẩn màn hình "Loading" tại đây.
                if (url.equals(API_URL)) {
                    String js = "javascript:document.getElementById('username').value = '" + username + "';" +
                            "document.getElementById('password').value = '" + password + "';" +
                            "document.getElementById('btnRegister').click();";
                    webView.loadUrl(js);
                }
            }
        });

        // Load trang đăng ký
        webView.loadUrl(API_URL);*/
    }
}
