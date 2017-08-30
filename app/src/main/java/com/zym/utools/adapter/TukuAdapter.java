package com.zym.utools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zym.utools.R;
import com.zym.utools.entity.TukuData;
import com.zym.utools.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.adapter
 * 文件名：  TukuAdapter
 * 创建者：  ZYM
 * 时间：   2017/8/17 13:36
 * 描述：   图客适配器
 */
public class TukuAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<TukuData> mList;
    private TukuData data;
    private int width;
    private WindowManager windowManager;

    public TukuAdapter(Context mContext, List<TukuData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        layoutInflater = LayoutInflater.from(mContext);
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = windowManager.getDefaultDisplay().getWidth();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
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
            view = layoutInflater.inflate(R.layout.fragment_tuku_style, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.fragmnet_tuku_style_imageView);
            viewHolder.textView = (TextView) view.findViewById(R.id.fragmnet_tuku_style_textView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        data = mList.get(i);
        PicassoUtils.loadeImageSize(mContext, data.getUrl(), width / 2, 400, viewHolder.imageView);
        viewHolder.textView.setText(data.getTitle());
        return view;
    }

    class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }
}
