package com.zym.utools.ui;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zym.utools.R;
import com.zym.utools.adapter.JsonAdapter;
import com.zym.utools.entity.JsonData;
import com.zym.utools.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WuliuActivity extends BaseActivity implements View.OnClickListener {

    private EditText wuliu_et_one, wuliu_et_two;
    private Button wuliu_button;
    private ListView wuliu_listView;
    private List<JsonData> mList = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<JsonData> list = (List<JsonData>) msg.obj;
            JsonAdapter adapter = new JsonAdapter(WuliuActivity.this, list);
            wuliu_listView.setAdapter(adapter);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wuliu);
        initView();
    }

    private void initView() {
        wuliu_et_one = (EditText) findViewById(R.id.wuliu_et_one);
        wuliu_et_two = (EditText) findViewById(R.id.wuliu_et_two);
        wuliu_button = (Button) findViewById(R.id.wuliu_button);
        wuliu_button.setOnClickListener(this);
        wuliu_listView = (ListView) findViewById(R.id.wuliu_listView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wuliu_button:
                Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show();
                //获取输入框内容
                String name = wuliu_et_one.getText().toString().trim();
                String number = wuliu_et_two.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
                    String url = "http://v.juhe.cn/exp/index?key=" + StaticClass.JUHE_KEY + "&com=" + name + "&no=" + number;
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().get().url(url).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //得到网页数据
                            String html = response.body().string();
                            //解析json数据
                            paringJson(html);

                        }
                    });
                } else {
                    Toast.makeText(this, R.string.Toast_login_null, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //解析Json数据
    private void paringJson(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray list = result.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) list.get(i);
                JsonData data = new JsonData();
                data.setRemark(jsonObject1.getString("remark"));
                data.setZone(jsonObject1.getString("zone"));
                data.setDatetime(jsonObject1.getString("datetime"));
                mList.add(data);
            }
            //倒序
            Collections.reverse(mList);
            Message message = new Message();
            message.obj = mList;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
