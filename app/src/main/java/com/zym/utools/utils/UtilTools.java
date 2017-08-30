package com.zym.utools.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.utils
 * 文件名：  UtilTools
 * 创建者：  ZYM
 * 时间：   2017/8/1 13:45
 * 描述：   工具类
 */
public class UtilTools {

    //设置字体样式
    public static void setFont(Context context, TextView textView) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Fonts/FONT.TTF");
        textView.setTypeface(typeface);
    }
}
