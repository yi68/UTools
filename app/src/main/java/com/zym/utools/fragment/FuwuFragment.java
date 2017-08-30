package com.zym.utools.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zym.utools.R;
import com.zym.utools.adapter.FuwuAdapter;
import com.zym.utools.entity.FuwuData;
import com.zym.utools.utils.L;
import com.zym.utools.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FuwuFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private EditText input;
    private Button send;
    private FuwuAdapter adapter;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private List<FuwuData> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuwu, container, false);
        initVIew(view);
        return view;
    }

    private void initVIew(View view) {
        listView = (ListView) view.findViewById(R.id.fuwu_listView);
        input = (EditText) view.findViewById(R.id.fuwu_editText);
        send = (Button) view.findViewById(R.id.fuwu_Button);
        send.setOnClickListener(this);
        //设置适配器
        adapter = new FuwuAdapter(getActivity(), mList);
        listView.setAdapter(adapter);
        //进入显示内容
        addLeftText("您好，有什么可以帮到您？");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fuwu_Button:
                //获取输入内容
                String text = input.getText().toString();
                //判断是否为空
                if (!TextUtils.isEmpty(text)) {
                    if (text.length() > 30) {
                        Toast.makeText(getActivity(), "输入不能超过30个字符!", Toast.LENGTH_SHORT).show();
                    } else {
                        //清空输入框
                        input.setText("");
                        //发送数据
                        addRightText(text);
                        String url = "http://op.juhe.cn/robot/index?info=" + text + "&key=" + StaticClass.FUWU_KEY;
                        final Request request = new Request.Builder().get().url(url).build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                L.i("请求失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String jsonData = response.body().string();
                                parsingJson(jsonData);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //解析得到的数据
    private void parsingJson(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            String text = jsonObject1.getString("text");
            Message message = new Message();
            message.obj = text;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftText(String string) {
        FuwuData data = new FuwuData();
        data.setType(FuwuAdapter.TYPE_LEFT);
        data.setText(string);
        mList.add(data);
        //刷新adapter数据
        adapter.notifyDataSetChanged();
        //ListView滚动到底部
        listView.setSelection(listView.getBottom());
    }

    //添加右边文本
    private void addRightText(String string) {
        FuwuData data = new FuwuData();
        data.setType(FuwuAdapter.TYPE_RIGHT);
        data.setText(string);
        mList.add(data);
        //刷新adapter数据
        adapter.notifyDataSetChanged();
        //ListView滚动到底部
        listView.setSelection(listView.getBottom());
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String text = (String) msg.obj;
            addLeftText(text);
        }
    };
}
