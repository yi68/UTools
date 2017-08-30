package com.zym.utools.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zym.utools.R;
import com.zym.utools.entity.LookData;
import com.zym.utools.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.adapter
 * 文件名：  LookimageAdapter
 * 创建者：  ZYM
 * 时间：   2017/8/23 14:09
 * 描述：   TODO
 */

public class LookimageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<LookData> mlist;
    private LookData data;
    private int width, height;
    private WindowManager windowManager;

    public LookimageAdapter(Context mContext, List<LookData> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
        layoutInflater = LayoutInflater.from(mContext);
        data = new LookData();
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = windowManager.getDefaultDisplay().getWidth();
        height = windowManager.getDefaultDisplay().getHeight();
    }


    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.activity_lookimage_style, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.look_style_imageView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        data = mlist.get(i);
        PicassoUtils.loadeImageSize(mContext, data.getUrl(), width, height, viewHolder.imageView);
        return view;
    }

    class ViewHolder {
        private ImageView imageView;
    }
}
