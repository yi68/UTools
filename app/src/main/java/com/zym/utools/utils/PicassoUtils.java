package com.zym.utools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.utils
 * 文件名：  PicassoUtils
 * 创建者：  ZYM
 * 时间：   2017/8/17 10:57
 * 描述：   TODO
 */
public class PicassoUtils {

    //默认加载图片
    public static void loadIamgeDefult(Context mContext, String url, ImageView imageView) {
        Picasso.with(mContext).load(url).into(imageView);
    }

    //指定大小
    public static void loadeImageSize(Context mContext, String url, int width, int height, ImageView imageView) {
        Picasso.with(mContext).load(url).resize(width, height).centerCrop().into(imageView);
    }

    //加载之前显示的图片和加载错误显示的图片
    public static void loadImageError(Context mContext, String url, int defult, int error, ImageView imageView) {
        Picasso.with(mContext).load(url).placeholder(defult).error(error).into(imageView);
    }

    //裁剪图片
    public static void loadImageCrop(Context mContext, String url, ImageView imageView) {
        Picasso.with(mContext).load(url).transform(new CropSquareTransformation()).into(imageView);
    }

    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "square()";
        }
    }
}
