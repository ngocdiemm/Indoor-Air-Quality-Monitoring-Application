package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.uit.myairquality.Interfaces.APIInterface;
import com.uit.myairquality.Model.APIClient;
import com.uit.myairquality.Model.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {

    String grant_type = "password";
    String client_id = "openremote";
    String usr,pwd;
    APIInterface apiInterface;
    Button btnLogin,btnBackLogin;

    EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnBackLogin = findViewById(R.id.btnBackLogin);
        // Quay lại màn hình Homepage
        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, Homepage.class);
                startActivity(intent);
            }
        });
        // Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usr = String.valueOf(username.getText());
                pwd = String.valueOf(password.getText());
                getToken(usr,pwd);
            }
        });
    }

    public void getToken(String usr, String pwd) {
        Call<Token> call = apiInterface.Login(grant_type, usr, pwd,client_id);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Token Token = response.body();
                    Log.d("LoginActivity", "Hi ");
                    APIClient.Usertoken = Token.access_token;
                    Toast.makeText(LogIn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogIn.this, Settings.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LogIn.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(LogIn.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTokenToSharedPreferences(String accessToken) {
        // Lưu accessToken vào SharedPreferences hoặc thực hiện các hoạt động khác liên quan đến token
        // Đảm bảo bạn đã tạo phương thức để lưu và quản lý token trong SharedPreferences
    }
}
