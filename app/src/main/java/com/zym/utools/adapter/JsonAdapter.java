package com.zym.utools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zym.utools.R;
import com.zym.utools.entity.JsonData;

import java.util.List;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.adapter
 * 文件名：  JsonAdapter
 * 创建者：  ZYM
 * 时间：   2017/8/8 15:30
 * 描述：   TODO
 */
public class JsonAdapter extends BaseAdapter {

    private Context mContext;
    private List<JsonData> mList;
    private LayoutInflater layoutInflater;
    private JsonData jsonData;

    public JsonAdapter(Context mContext, List<JsonData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.jsonData = new JsonData();

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
            view = layoutInflater.inflate(R.layout.activity_wuliu_style, null);
            viewHolder.one = (TextView) view.findViewById(R.id.wuliu_style_txt_one);
            viewHolder.two = (TextView) view.findViewById(R.id.wuliu_style_txt_two);
            viewHolder.three = (TextView) view.findViewById(R.id.wuliu_style_txt_three);
            //设置缓存
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //设置数据
        jsonData = mList.get(i);
        viewHolder.one.setText(jsonData.getRemark());
        viewHolder.two.setText(jsonData.getZone());
        viewHolder.three.setText(jsonData.getDatetime());

        return view;
    }

    //适配器优化
    class ViewHolder {
        private TextView one;
        private TextView two;
        private TextView three;
    }
}
