package com.example.onmymeans.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onmymeans.R;


public class RegisterActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.et_userName);
        password = findViewById(R.id.et_password);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = userName.getText().toString();
                final String pw = password.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pw)) {
                    Toast.makeText(RegisterActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                UsersInfo usersInfo = new UsersInfo(name, pw);
                usersInfo.save();
                Toast.makeText(RegisterActivity.this, "注册成功，请登陆，友情提示！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
