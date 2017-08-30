package com.zym.utools.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.zym.utools.R;
import com.zym.utools.adapter.LookimageAdapter;
import com.zym.utools.entity.LookData;
import com.zym.utools.utils.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LookimageActivity extends AppCompatActivity {

    private String indexUrl;
    private ListView listView;
    private List<String> imgList = new ArrayList<>();
    private List<LookData> mList = new ArrayList<>();
    private LookData data;
    private OkHttpClient okHttpClient;
    //图片页数
    private int allNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookimage);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.look_listView);
        Intent intent = getIntent();
        indexUrl = intent.getStringExtra("url");
        //网页链接
        parsingLineAll(indexUrl);
    }


    //网页链接
    private void parsingLineAll(String url) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.i("获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes = response.body().bytes();
                String html = new String(bytes, "GBK");
                //解析图片页数
                ParsingHtmlNumbers(html);
            }
        });
    }

    private void ParsingHtmlNumbers(String html) {
        String reset_text = "<span.*?id=\"allnum\">(.*?)</span>";
        Pattern compile = Pattern.compile(reset_text);
        Matcher matcher = compile.matcher(html);
        if (matcher.find()) {
            String numbers = matcher.group(1);
            allNumbers = Integer.parseInt(numbers);
            //网页地址集合
            for (int i = 1; i <= allNumbers; i++) {
                String endUrl = indexUrl.substring(0, indexUrl.length() - 5) + i + ".htm";
                L.i(endUrl);
                imgList.add(endUrl);
                webSiteConnection(imgList);
            }
        }

    }

    private void webSiteConnection(List<String> imgList) {
        for (int i = 0; i < imgList.size(); i++) {
            wenConnection(imgList.get(i));
        }
        Message message = new Message();
        message.obj = mList;
        handler.sendMessage(message);
    }

    private void wenConnection(String s) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().get().url(s).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.i("连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes = response.body().bytes();
                String html = new String(bytes, "GBK");
                parsingImageUrl(html);
            }
        });
    }

    //解析图片地址
    private void parsingImageUrl(String html) {
        String reset_img = "<img.*?src=\"http://(.*?).jpg\"";
        Pattern compile = Pattern.compile(reset_img);
        Matcher matcher = compile.matcher(html);
        if (matcher.find()) {
            String imageUrl = matcher.group(1);
            data = new LookData();
            data.setUrl("http://" + imageUrl + ".jpg");
            mList.add(data);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<LookData> list = (List<LookData>) msg.obj;
            LookimageAdapter adapter = new LookimageAdapter(LookimageActivity.this, list);
            listView.setAdapter(adapter);
        }
    };
}
