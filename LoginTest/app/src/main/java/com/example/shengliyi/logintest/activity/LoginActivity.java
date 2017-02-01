package com.example.shengliyi.logintest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shengliyi.logintest.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginNumber;
    private EditText loginCode;
    private Button loginButton;

    private String number;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginNumber = (EditText)findViewById(R.id.login_number);
        loginCode = (EditText)findViewById(R.id.login_code);


        loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = loginNumber.getText().toString();
                code = loginCode.getText().toString();
                Intent intent = new Intent(LoginActivity.this, InterfaceActivity.class);
                if (number.equals("admin") && code.equals("123456")){
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"密码或账号错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
