package com.uit.myairquality;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class LogIn extends AppCompatActivity {


    Button login, register;
    EditText username, password;
    Button btnlogin;
    Database DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnRegister);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });
        username = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        DB = new Database(this,"Final_app.db",null,1);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String user = username.getText().toString();
                String pass = password.getText().toString();


                if(user.equals("")||pass.equals(""))
                    Toast.makeText(LogIn.this, "Vui lòng điền hết tất cả các trường", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if(checkuserpass==true){
                        Toast.makeText(LogIn.this, "Chào mứng đến app của chúng tôi", Toast.LENGTH_SHORT).show();
//                        Intent intent  = new Intent(getApplicationContext(), AboutActivity.class);
//                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LogIn.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    public void openRegister(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}



