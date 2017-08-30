package com.zym.utools.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.utils
 * 文件名：  SharedUtils
 * 创建者：  ZYM
 * 时间：   2017/8/1 16:18
 * 描述：   TODO
 */
public class SharedUtils {

    public static final String Name = "config";

    //String
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    //Int
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    //Boolean
    public static void putBoolean(Context context, String key, Boolean value) {
        SharedPreferences sp = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    //删除 单个
    public static void deleOne(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    //删除 全部
    public static void deleAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
