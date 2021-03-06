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
import com.zym.utools.entity.WeixinData;
import com.zym.utools.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.adapter
 * 文件名：  WeixinAdapter
 * 创建者：  ZYM
 * 时间：   2017/8/16 17:07
 * 描述：   TODO
 */
public class WeixinAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<WeixinData> mList;
    private WeixinData data;
    private int width;
    private WindowManager windowManager;

    public WeixinAdapter(Context mContext, List<WeixinData> mList) {
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
            view = layoutInflater.inflate(R.layout.fragment_winxin_style, null);
            viewHolder.image = (ImageView) view.findViewById(R.id.weixin_style_img);
            viewHolder.title = (TextView) view.findViewById(R.id.weixin_style_title);
            viewHolder.source = (TextView) view.findViewById(R.id.weixin_style_source);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //设置数据
        data = mList.get(i);
        PicassoUtils.loadeImageSize(mContext, data.getImgUrl(), width / 3, 180, viewHolder.image);
        viewHolder.title.setText(data.getTitle());
        viewHolder.source.setText(data.getSource());
        return view;
    }

    //Item优化
    class ViewHolder {
        private ImageView image;
        private TextView title;
        private TextView source;
    }
}
