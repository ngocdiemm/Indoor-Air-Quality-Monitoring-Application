package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ResetPassword extends AppCompatActivity {

    Button btnBackResetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        btnBackResetPassword = findViewById(R.id.btnBackResetPassword);
        //Quay lại màn hình Homepage
        btnBackResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, LogIn.class);
                startActivity(intent);
            }
        });
    }

}