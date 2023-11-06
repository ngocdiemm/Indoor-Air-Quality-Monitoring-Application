package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.uit.myairquality.Interfaces.APIInterface;
import com.uit.myairquality.Model.Token;
import com.uit.myairquality.Model.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {

    String grant_type = "password";
    String client_id = "openremote";
    String usr;
    String pwd;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        EditText username = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnBackLogIn = findViewById(R.id.btnBackLogin);
        // Quay lại màn hình Homepage
        btnBackLogIn.setOnClickListener(new View.OnClickListener() {
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
                usr= String.valueOf(username.getText());
                pwd = String.valueOf(password.getText());
                getToken(usr,pwd);
                Intent intent = new Intent(LogIn.this, Settings.class);
                startActivity(intent);
            }
        });
    }
    public void getToken(String username, String password) {
        Call<Token> call = apiInterface.Login(grant_type, usr, pwd, client_id);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    String accessToken = token.getAccess_token();
                    saveTokenToSharedPreferences(accessToken);
                    Toast.makeText(LogIn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogIn.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(LogIn.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTokenToSharedPreferences(String accessToken) {
        // Lưu accessToken vào SharedPreferences hoặc thực hiện các hoạt động khác liên quan đến token
        // Đảm bảo bạn đã tạo phương thức để lưu và quản lý token trong SharedPreferences
    }
}
