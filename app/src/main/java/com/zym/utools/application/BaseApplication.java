package com.zym.utools.application;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.zym.utools.utils.StaticClass;

import cn.bmob.v3.Bmob;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.application
 * 文件名：  BaseApplication
 * 创建者：  ZYM
 * 时间：   2017/8/1 13:24
 * 描述：   Application
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_KEY, true);
        //初始化Bmob
        Bmob.initialize(getApplicationContext(), StaticClass.BMOB_KEY);
    }
}
