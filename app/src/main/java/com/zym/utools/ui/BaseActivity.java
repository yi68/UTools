package com.zym.utools.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.ui
 * 文件名：  BaseActivity
 * 创建者：  ZYM
 * 时间：   2017/8/1 13:31
 * 描述：   Activity 基类
 */

/**
 * 1.统一属性
 * 2.统一接口
 * 3.统一方法
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //菜单栏操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
