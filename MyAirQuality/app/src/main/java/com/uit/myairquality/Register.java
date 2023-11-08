package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uit.myairquality.Interfaces.APIInterface;
import com.uit.myairquality.Model.APIClient;
import com.uit.myairquality.R;
import com.uit.myairquality.Settings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends AppCompatActivity {
    EditText username, password, repassword, email;
    TextView txtToken, WebView;
    Button signup, btnBackRegister;
    APIInterface apiInterface;

    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        LoadElement();
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                username = (EditText) findViewById(R.id.Username);
                password = (EditText) findViewById(R.id.Password);
                repassword = (EditText) findViewById(R.id.ReType);
                email = (EditText) findViewById(R.id.Email);
                if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || repassword.getText().toString().isEmpty()||email.getText().toString().isEmpty())
                {
                    Toast.makeText(Register.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    SignUp();
                }

            }
        });
        btnBackRegister = findViewById(R.id.btnBackRegister);
        btnBackRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Homepage.class);
                startActivity(intent);
            }
        });

    }


    private void SignUp() {

        webView.getSettings().setJavaScriptEnabled(true);
        username = (EditText) findViewById(R.id.Username);
        password = (EditText) findViewById(R.id.Password);
        repassword = (EditText) findViewById(R.id.ReType);
        email = (EditText) findViewById(R.id.Email);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                if (url.contains("uiot.ixxc.dev/manager/")) {


                    Toast.makeText(Register.this, "Register Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Settings.class);
                    startActivity(intent);
                    Log.i("dang ki", "dang đang kí 1");
                } else if(!url.contains("uiot.ixxc.dev/manager/")) {
                    Toast.makeText(Register.this, "Email or account exists", Toast.LENGTH_SHORT).show();
                    Log.i("dang ki", "dang đang kí 2");
                }

            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("openid-connect/registrations")) {

                    String usrScript = "document.getElementById('username').value ='" + username.getText().toString()+ "';";
                    String emailScript = "document.getElementById('email').value ='" + email.getText().toString() + "';";
                    String pwdScript = "document.getElementById('password').value ='" + password.getText().toString()+ "';";
                    String rePwdScript = "document.getElementById('password-confirm').value ='" + repassword.getText().toString() + "';";
                    String submitFormScript = "document.querySelector('form').submit();";

                    webView.evaluateJavascript(usrScript, null);
                    webView.evaluateJavascript(pwdScript, null);
                    webView.evaluateJavascript(emailScript, null);
                    webView.evaluateJavascript(rePwdScript, null);
                    webView.evaluateJavascript(submitFormScript, null);
                    Log.i("dang ki", username.getText().toString());
                    Log.i("dang ki", email.getText().toString());
                    Log.i("dang ki", password.getText().toString());
                    Log.i("dang ki", repassword.getText().toString());
                    Log.i("dang ki", "dang đang kí");
                }


            }
        });
        String url = "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/registrations?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&response_mode=fragment&response_type=code&scope=openid";
        webView.loadUrl(url);
    }

    private void LoadElement(){
        txtToken = findViewById(R.id.token);
        webView = findViewById(R.id.webView);
        signup = findViewById(R.id.btnRegister);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        cookieManager.removeAllCookies(null);
        cookieManager.flush();
    }
}