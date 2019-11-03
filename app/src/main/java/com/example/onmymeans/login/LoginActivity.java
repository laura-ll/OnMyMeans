package com.example.onmymeans.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onmymeans.MainActivity;
import com.example.onmymeans.R;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LitePal.initialize(getApplicationContext());
        userName = findViewById(R.id.et_userName);
        password = findViewById(R.id.et_password);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = userName.getText().toString();
                final String pw = password.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pw)) {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //从数据库拉取所有用户信息
                List<UsersInfo> users = DataSupport.findAll(UsersInfo.class);
                for (UsersInfo userInfo : users) {
                    //如果有一个用户和输入的匹配，则进入主页
                    if (name.equals(userInfo.getUserName()) && pw.equals(userInfo.getPassWord())) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                Toast.makeText(LoginActivity.this, name + "请先注册您的信息，友情提示！", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
