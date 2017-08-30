package com.zym.utools.ui;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zym.utools.MainActivity;
import com.zym.utools.R;
import com.zym.utools.utils.UtilTools;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    //页面
    private List<View> mList = new ArrayList<>();
    private View view1, view2, view3;
    private TextView t1, t2, t3;
    //切换圆点
    private ImageView point1, point2, point3;
    //跳过
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {

        back = (ImageView) findViewById(R.id.guide_img_back);
        back.setOnClickListener(this);
        back.setVisibility(View.VISIBLE);

        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);
        //设置初始圆点状态
        setSelect(true, false, false);

        mViewPager = (ViewPager) findViewById(R.id.guide_ViewPager);

        view1 = View.inflate(this, R.layout.guide_one, null);
        view2 = View.inflate(this, R.layout.guide_two, null);
        view3 = View.inflate(this, R.layout.guide_three, null);
        //按钮事件--进入主页
        view3.findViewById(R.id.guide_button_three).setOnClickListener(this);

        t1 = (TextView) view1.findViewById(R.id.guide_textView_one);
        UtilTools.setFont(this, t1);
        t2 = (TextView) view2.findViewById(R.id.guide_textView_two);
        UtilTools.setFont(this, t2);
        t3 = (TextView) view3.findViewById(R.id.guide_textView_three);
        UtilTools.setFont(this, t3);
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        //为ViewPager设置适配器
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            //添加item
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mList.get(position));
                return mList.get(position);
            }

            //移除item
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mList.get(position));
                //super.destroyItem(container, position, object);
            }
        });

        //ViewPager监听事件
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //item切换
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        back.setVisibility(View.VISIBLE);
                        setSelect(true, false, false);
                        break;
                    case 1:
                        back.setVisibility(View.VISIBLE);
                        setSelect(false, true, false);
                        break;
                    case 2:
                        back.setVisibility(View.GONE);
                        setSelect(false, false, true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //切换图片
    private void setSelect(Boolean select1, Boolean select2, Boolean select3) {

        if (select1) {
            point1.setImageResource(R.drawable.point_on);
        } else {
            point1.setImageResource(R.drawable.point_off);
        }

        if (select2) {
            point2.setImageResource(R.drawable.point_on);
        } else {
            point2.setImageResource(R.drawable.point_off);
        }

        if (select3) {
            point3.setImageResource(R.drawable.point_on);
        } else {
            point3.setImageResource(R.drawable.point_off);
        }
    }


    //禁止返回监听
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guide_button_three:
            case R.id.guide_img_back:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
