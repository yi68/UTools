package com.zym.utools.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zym.utools.R;
import com.zym.utools.adapter.WeixinAdapter;
import com.zym.utools.entity.FuwuData;
import com.zym.utools.entity.WeixinData;
import com.zym.utools.ui.WebviewActivity;
import com.zym.utools.utils.L;
import com.zym.utools.utils.StaticClass;

import org.json.JSONArray;
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
public class WeixinFragment extends Fragment {

    private ListView listView;
    private WeixinData weixinData;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private List<WeixinData> mList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();
    private Object data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weixin, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.weixin_listView);
        //获取数据
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WEIXIN_KEY + "&ps=50";
        Request request = new Request.Builder().get().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.i("请求数据失败!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                ParsingJson(html);
            }
        });
        //listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), WebviewActivity.class);
                intent.putExtra("title", mList.get(i).getTitle());
                intent.putExtra("url", urlList.get(i));
                startActivity(intent);
            }
        });
    }

    //解析数据
    private void ParsingJson(String html) {
        try {
            JSONObject jsonObject = new JSONObject(html);
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                weixinData = new WeixinData();
                weixinData.setImgUrl(object.getString("firstImg"));
                weixinData.setTitle(object.getString("title"));
                weixinData.setSource(object.getString("source"));
                urlList.add(object.getString("url"));
                mList.add(weixinData);
            }
            Message message = new Message();
            message.obj = mList;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<WeixinData> list = (List<WeixinData>) msg.obj;
            WeixinAdapter adapter = new WeixinAdapter(getActivity(), list);
            listView.setAdapter(adapter);
        }
    };

}
