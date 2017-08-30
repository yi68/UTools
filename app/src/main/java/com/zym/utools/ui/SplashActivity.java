package com.zym.utools.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zym.utools.MainActivity;
import com.zym.utools.R;
import com.zym.utools.utils.SharedUtils;
import com.zym.utools.utils.StaticClass;
import com.zym.utools.utils.UtilTools;

public class SplashActivity extends AppCompatActivity {


    private TextView tv_splash, tv_splash2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.Handler_splash:
                    //判断
                    if (isFirst()) {
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        tv_splash = (TextView) findViewById(R.id.tv_Splash);
        tv_splash2 = (TextView) findViewById(R.id.tv_Splash2);
        //设置字体样式
        UtilTools.setFont(this, tv_splash);
        UtilTools.setFont(this, tv_splash2);
        //延时2s
        handler.sendEmptyMessageDelayed(StaticClass.Handler_splash, 4000);
    }

    //判断是否为第一次
    private boolean isFirst() {
        boolean first = SharedUtils.getBoolean(this, StaticClass.Shared_First, true);
        if (first) {
            SharedUtils.putBoolean(this, StaticClass.Shared_First, false);
            return true;
        } else {
            return false;
        }
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
