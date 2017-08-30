package com.zym.utools.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zym.utools.R;
import com.zym.utools.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText forget_editText_one, forget_editText_two, forget_editText_two_ok, forget_edittext_email;
    private Button forget_button_reset, forget_button_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView() {
        //原密码重置
        forget_editText_one = (EditText) findViewById(R.id.forget_editText_one);
        forget_editText_two = (EditText) findViewById(R.id.forget_editText_two);
        forget_editText_two_ok = (EditText) findViewById(R.id.forget_editText_two_ok);
        forget_button_reset = (Button) findViewById(R.id.forget_button_reset);
        forget_button_reset.setOnClickListener(this);
        //邮箱重置
        forget_edittext_email = (EditText) findViewById(R.id.forget_editText_email);
        forget_button_email = (Button) findViewById(R.id.forget_button_email);
        forget_button_email.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //重置密码
            case R.id.forget_button_reset:
                resetPassword();
                break;
            //邮箱重置密码
            case R.id.forget_button_email:
                forgetEmail();
                break;
        }
    }

    //原密码
    private void resetPassword() {
        final String password = forget_editText_one.getText().toString().trim();
        final String newPassword = forget_editText_two.getText().toString().trim();
        final String newPassword_ok = forget_editText_two_ok.getText().toString().trim();
        //判断是否为空
        if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(newPassword) & !TextUtils.isEmpty(newPassword_ok)) {
            //判断密码是否一致
            if (newPassword.equals(newPassword_ok)) {
                //交互数据
                User.updateCurrentUserPassword(password, newPassword_ok, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(ForgetActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgetActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, R.string.toast_message2, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.Toast_login_null, Toast.LENGTH_SHORT).show();
        }
    }

    //邮箱
    private void forgetEmail() {
        final String email = forget_edittext_email.getText().toString().trim();
        if (!TextUtils.isEmpty(email)) {
            //发送邮件
            User.resetPasswordByEmail(email, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(ForgetActivity.this, "邮件已发送至：" + email, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgetActivity.this, "邮件发送至：" + email + "失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.Toast_login_null, Toast.LENGTH_SHORT).show();
        }
    }
}
