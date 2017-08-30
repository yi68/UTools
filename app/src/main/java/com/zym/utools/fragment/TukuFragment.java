package com.zym.utools.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zym.utools.R;
import com.zym.utools.adapter.TukuAdapter;
import com.zym.utools.entity.TukuData;
import com.zym.utools.ui.LookimageActivity;
import com.zym.utools.ui.WebviewActivity;
import com.zym.utools.utils.L;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TukuFragment extends Fragment {

    private String indexUrl = "https://www.4493.com";
    private GridView gridView;
    private List<TukuData> mList = new ArrayList<>();
    private TukuData data;
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tuku, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        gridView = (GridView) view.findViewById(R.id.fragmnet_tuku_gridView);
        //解析网页图片
        linkWeb(indexUrl);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), LookimageActivity.class);
                intent.putExtra("title", mList.get(i).getTitle());
                intent.putExtra("url", indexUrl + mList.get(i).getHtmlUrl());
                startActivity(intent);
            }
        });
    }

    private void linkWeb(String indexUrl) {
        Request request = new Request.Builder().get().url(indexUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.i("访问出错");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes = response.body().bytes();
                String html = new String(bytes, "GBK");
                parsingHtml(html);
            }
        });
    }

    private void parsingHtml(String html) {
        //定义搜寻字符串
        String reset_img = "<a.*?href=\"(.*?)\".*?<img.*?src=\"http://(.*?).jpg\".*?<span>(.*?)</span>.*?</a>";
        Pattern compile = Pattern.compile(reset_img, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher(html);
        while (matcher.find()) {
            data = new TukuData();
            String htmlUrl = matcher.group(1);
            String src = matcher.group(2);
            String title = matcher.group(3);
            if (src.length() < 100) {
                data.setUrl("http://" + src + ".jpg");
                data.setTitle(title);
                data.setHtmlUrl(htmlUrl);
                mList.add(data);
            }
        }
        Message message = new Message();
        message.obj = mList;
        handler.sendMessage(message);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<TukuData> list = (List<TukuData>) msg.obj;
            TukuAdapter adapter = new TukuAdapter(getActivity(), list);
            gridView.setAdapter(adapter);
        }
    };

}
