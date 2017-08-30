package com.zym.utools.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zym.utools.MainActivity;
import com.zym.utools.R;
import com.zym.utools.entity.User;
import com.zym.utools.utils.SharedUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login, register;
    private EditText login_name, login_pwd;
    private CheckBox kepp_pwd;
    private TextView login_forget_pwd;
    //Dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    //初始化控件
    private void initView() {
        //用户名 密码
        login_name = (EditText) findViewById(R.id.login_et_uname);
        login_pwd = (EditText) findViewById(R.id.login_et_pwd);
        //记住密码
        kepp_pwd = (CheckBox) findViewById(R.id.login_checkBox_keep);
        //设置选中状态
        boolean isChecked = SharedUtils.getBoolean(this, "KeepPassword", false);
        kepp_pwd.setChecked(isChecked);
        if (isChecked) {
            //写入用户名与密码
            login_name.setText(SharedUtils.getString(this, "name", ""));
            login_pwd.setText(SharedUtils.getString(this, "pwd", ""));
        }
        //登陆按钮
        login = (Button) findViewById(R.id.login_bt_login);
        login.setOnClickListener(this);
        //注册按钮
        register = (Button) findViewById(R.id.login_bt_register);
        register.setOnClickListener(this);

        login_forget_pwd = (TextView) findViewById(R.id.login_forget_pwd);
        login_forget_pwd.setOnClickListener(this);
    }

    //按钮监听事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //登陆
            case R.id.login_bt_login:
                bmobLogin();
                break;
            //注册
            case R.id.login_bt_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            //忘记密码
            case R.id.login_forget_pwd:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
        }
    }

    private void bmobLogin() {
        //获取输入框的内容
        String name = login_name.getText().toString().trim();
        String pwd = login_pwd.getText().toString().trim();
        //判断是否为空
        if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(pwd)) {
            //进度条
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("登陆中...");
            progressDialog.show();
            //存放用户名和密码
            User user = new User();
            user.setUsername(name);
            user.setPassword(pwd);
            //与服务器交互数据
            user.login(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        //验证邮箱状态
                        if (user.getEmailVerified()) {
                            progressDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, R.string.toast_login_email_no, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, R.string.toast_login_no + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.Toast_login_null, Toast.LENGTH_SHORT).show();
        }
    }

    //记住密码

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //保存checkbox状态
        SharedUtils.putBoolean(this, "KeepPassword", kepp_pwd.isChecked());
        //记住密码
        if (kepp_pwd.isChecked()) {
            SharedUtils.putString(this, "name", login_name.getText().toString().trim());
            SharedUtils.putString(this, "pwd", login_pwd.getText().toString().trim());
        } else {
            SharedUtils.deleOne(this, "name");
            SharedUtils.deleOne(this, "pwd");
        }
    }
}
