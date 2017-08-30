package com.zym.utools.utils;

import android.util.Log;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.utils
 * 文件名：  L
 * 创建者：  ZYM
 * 时间：   2017/8/1 15:48
 * 描述：   TODO
 */
public class L {

    public static final Boolean Debug = true;
    public static final String TAG = "utools";

    //DIWE
    public static void d(String text) {
        if (Debug) {
            Log.d(TAG, text);
        }
    }

    public static void i(String text) {
        if (Debug) {
            Log.i(TAG, text);
        }
    }

    public static void w(String text) {
        if (Debug) {
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if (Debug) {
            Log.e(TAG, text);
        }
    }

}
