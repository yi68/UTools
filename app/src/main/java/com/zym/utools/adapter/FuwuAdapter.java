package com.zym.utools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zym.utools.R;
import com.zym.utools.entity.FuwuData;

import java.util.List;

/**
 * 项目名：  UTools
 * 包名：    com.zym.utools.adapter
 * 文件名：  FuwuAdapter
 * 创建者：  ZYM
 * 时间：   2017/8/10 14:56
 * 描述：   服务管家Fragment适配器
 */
public class FuwuAdapter extends BaseAdapter {

    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;

    private Context mContext;
    private LayoutInflater layoutInflater;
    private FuwuData data;
    private List<FuwuData> mList;

    public FuwuAdapter(Context mContext, List<FuwuData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolderLeft viewHolderLeft = null;
        ViewHolderRight viewHolderRight = null;
        //获取当前type
        int viewType = getItemViewType(i);
        if (view == null) {
            //左右layout
            switch (viewType) {
                case TYPE_LEFT:
                    viewHolderLeft = new ViewHolderLeft();
                    view = layoutInflater.inflate(R.layout.fragment_fuwu_style_left, null);
                    viewHolderLeft.textView_left = (TextView) view.findViewById(R.id.fuwu_style_left_text);
                    view.setTag(viewHolderLeft);
                    break;
                case TYPE_RIGHT:
                    viewHolderRight = new ViewHolderRight();
                    view = layoutInflater.inflate(R.layout.fragment_fuwu_style_right, null);
                    viewHolderRight.textView_right = (TextView) view.findViewById(R.id.fuwu_style_right_text);
                    view.setTag(viewHolderRight);
                    break;
            }
        } else {
            switch (viewType) {
                case TYPE_LEFT:
                    viewHolderLeft = (ViewHolderLeft) view.getTag();
                    break;
                case TYPE_RIGHT:
                    viewHolderRight = (ViewHolderRight) view.getTag();
                    break;
            }
        }
        //设置内容
        FuwuData data = mList.get(i);
        switch (viewType) {
            case TYPE_LEFT:
                viewHolderLeft.textView_left.setText(data.getText());
                break;
            case TYPE_RIGHT:
                viewHolderRight.textView_right.setText(data.getText());
                break;
        }
        return view;
    }

    //根据position返回要显示的Item
    @Override
    public int getItemViewType(int position) {
        FuwuData data = mList.get(position);
        int type = data.getType();
        return type;
    }

    //返回所有layout数量
    @Override
    public int getViewTypeCount() {
        return 3; //mList.size() + 1
    }

    //左边的文本优化
    class ViewHolderLeft {
        private TextView textView_left;
    }

    //右边的文本优化
    class ViewHolderRight {
        private TextView textView_right;
    }
}
