package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
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
import com.uit.myairquality.Model.Token;
import com.uit.myairquality.R;
import com.uit.myairquality.Settings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends AppCompatActivity {
    EditText username, password, repassword, email;
    Button signup, btnBackRegister;
    APIInterface apiInterface;

    WebView webView;

    LoadingAlert loadingAlert = new LoadingAlert(Register.this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChangeLanguages.loadLocaleChanged(Register.this);

        setContentView(R.layout.activity_register);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        username = (EditText) findViewById(R.id.Username);
        password = (EditText) findViewById(R.id.Password);
        repassword = (EditText) findViewById(R.id.ReType);
        email = (EditText) findViewById(R.id.Email);

        webView = (WebView) findViewById(R.id.webView);
        signup = (Button) findViewById(R.id.btnRegister);

        // Hard code data for test
        //username.setText("user123");
        email.setText("@gmail.com");
        password.setText("123456789");
        repassword.setText("123456789");

        //Quay lại màn hình Homepage
        btnBackRegister = findViewById(R.id.btnBackRegister);
        btnBackRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Homepage.class);
                startActivity(intent);
            }
        });

        //Đăng ký
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingAlert.startAlertDialog();
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || repassword.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(Register.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    SignUp();
                }
            }
        });


    }

    private void SignUp() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(false);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        cookieManager.removeAllCookies(null);
        cookieManager.flush();
        webView.clearCache(true);

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                Log.d("=====on Page load", url);

                //Kiểm tra email có đúng format hay không
                String usremail = email.getText().toString().trim();
                if (!isValidEmail(usremail)) {
                    Toast.makeText(Register.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    loadingAlert.closeAlertDialog();
                    return;
                }

                // Kiểm tra mật khẩu trùng nhau
                if (!passwordsMatch(password.getText().toString(), repassword.getText().toString())) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    loadingAlert.closeAlertDialog();
                    return;
                }

                // Kiểm tra dữ liệu đăng kí
                //TH1:
                if (url.contains("openid-connect/registrations")) {
                    Log.d("=====on Page registrations", "openid-connect/registrations");

                    submitRegistrationForm(view);
                }
                //TH2:
                else if (url.contains("login-actions/registration?client_id=openremote&tab_id=") || url.contains("manager/#session_state")) {
                    Toast.makeText(Register.this, "Register successful", Toast.LENGTH_SHORT).show();
                    webView.stopLoading();
                    Intent intent = new Intent(Register.this, Settings.class);
                    startActivity(intent);
                }
                //TH3:
                else {

                    if (url.contains("registration?execution")) {
                        view.evaluateJavascript("document.documentElement.outerHTML", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                String finalHtml = s.replace("\\u003C", "<");

                                if (finalHtml.contains("Email already exists") || finalHtml.contains("Username already exists")) {
                                    if (finalHtml.contains("Username already exists")) {
                                        Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Register.this, "Email already exists", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Register.this, "Register successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, Settings.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                }

                loadingAlert.closeAlertDialog();
            }
            public void onReceivedHttpError(WebView view,
                                            WebResourceRequest request,
                                            WebResourceResponse errorResponse) {
                Log.d("This is error", errorResponse.getStatusCode() + "==");
            }


        });

        String url = "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/registrations?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&response_mode=fragment&response_type=code&scope=openid";
        webView.loadUrl(url);
    }

    //Form đăng ký

    private void submitRegistrationForm(WebView view) {
        // Do submit form to register
        String usrScript = "document.getElementById('username').value='" + username.getText().toString() + "';";
        String emailScript = "document.getElementById('email').value='" + email.getText().toString() + "';";
        String pwdScript = "document.getElementById('password').value='" + password.getText().toString() + "';";
        String rePwdScript = "document.getElementById('password-confirm').value='" + repassword.getText().toString() + "';";

        view.evaluateJavascript(usrScript, null);
        view.evaluateJavascript(emailScript, null);
        view.evaluateJavascript(pwdScript, null);
        view.evaluateJavascript(rePwdScript, null);
        view.evaluateJavascript("document.getElementById('kc-register-form').submit();", null);
    }

    //Kiểm tra password
    private boolean passwordsMatch(String password, String repassword) {
        return password.equals(repassword);
    }

    //Kiểm tra format email
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.trim().matches(emailPattern);
    }



}

