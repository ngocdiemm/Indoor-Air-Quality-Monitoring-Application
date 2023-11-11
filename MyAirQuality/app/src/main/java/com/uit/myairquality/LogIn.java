package com.uit.myairquality;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.uit.myairquality.Interfaces.APIInterface;
import com.uit.myairquality.Model.APIClient;
import com.uit.myairquality.Model.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {

    EditText username, password;
    Button btnLogin, btnBackLogin;

    TextView btnForgotPassword;
    String User,Pass;
    APIInterface apiInterface;
    String client_id = "openremote";
    String grantType = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        btnLogin = findViewById(R.id.btnLogin);
        btnBackLogin = findViewById(R.id.btnBackLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // Hard code for test
        username.setText("user123");
        password.setText("123456789");
        //LoadingAlert loadingAlert = new LoadingAlert(LogIn.this);

        //Quay lại màn hình Homepage
        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingAlert.startAlertDialog();
                Intent intent = new Intent(LogIn.this, Homepage.class);
                startActivity(intent);
              }
       });
        //Quên mật khẩu
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, ResetPassword.class);
                startActivity(intent);
            }
        });
        //Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User = String.valueOf(username.getText());
                Pass = String.valueOf(password.getText());
                getToken(User,Pass);
            }
        });
    }
    public void getToken(String usr, String pwd){
        Call<Token> call = apiInterface.Login(client_id,usr,pwd,grantType);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LogIn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    assert response.body() != null;
                    Token Token = response.body();
                    APIClient.token = com.uit.myairquality.Model.Token.access_token;
                    Intent intent = new Intent(LogIn.this, Settings.class);
                    startActivity(intent);
                } else {
                    Log.d("Login","fail" + response.message());
                    Toast.makeText(LogIn.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(LogIn.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
