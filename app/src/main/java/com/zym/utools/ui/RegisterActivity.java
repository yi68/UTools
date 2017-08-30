package com.zym.utools.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zym.utools.R;
import com.zym.utools.entity.User;
import com.zym.utools.utils.L;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText register_name, register_age, register_desc, register_pwd, register_pwd_two, register_email;
    private RadioGroup register_RadioGroup;
    private Button register_button;
    //性别
    private boolean isSex = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        register_name = (EditText) findViewById(R.id.register_name);
        register_age = (EditText) findViewById(R.id.register_age);
        register_desc = (EditText) findViewById(R.id.register_desc);
        register_pwd = (EditText) findViewById(R.id.register_pwd);
        register_pwd_two = (EditText) findViewById(R.id.register_pwd_two);
        register_email = (EditText) findViewById(R.id.register_email);

        register_RadioGroup = (RadioGroup) findViewById(R.id.register_RadioGroup);
        //判断性别
        register_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                isSex = i == R.id.register_sex_2 ? false : true;
            }
        });

        //注册按钮
        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                //获取输入框的内容
                String name = register_name.getText().toString().trim();
                String age = register_age.getText().toString().trim();
                String desc = register_desc.getText().toString().trim();
                String pwd = register_pwd.getText().toString().trim();
                String pwd_two = register_pwd_two.getText().toString().trim();
                String email = register_email.getText().toString().trim();

                //判断内容是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(age) &
                        !TextUtils.isEmpty(pwd) &
                        !TextUtils.isEmpty(pwd_two) &
                        !TextUtils.isEmpty(email)) {
                    //判断密码是否一致
                    if (pwd.equals(pwd_two)) {
                        //判断简介是否为空
                        if (TextUtils.isEmpty(desc)) {
                            desc = "这个家伙很懒，什么都没有写";
                        }
                        //注册 添加参数到User
                        User user = new User();
                        user.setUsername(name);
                        user.setPassword(pwd_two);
                        user.setEmail(email);
                        user.setAge(Integer.parseInt(age));
                        user.setSex(isSex);
                        user.setDesc(desc);
                        //提交
                        user.signUp(new SaveListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(RegisterActivity.this, R.string.toast_regist_ok, Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, R.string.toast_regist_no + e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, R.string.toast_message2, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
