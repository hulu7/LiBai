package com.example.libai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.libai.R;
import com.example.libai.StartActivity;
import com.example.libai.adapter.MyPagerAdapter;
import com.example.libai.utils.CacheUtils;
import com.example.libai.utils.DensityUtil;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.btn_start_main)
    Button btnStartMain;
    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @BindView(R.id.iv_red_point)
    ImageView ivRedPoint;

    private ArrayList<ImageView> imageViews;
    //两点的间距
    private int leftmax;

    private int widthpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        //准备数据
        int[] ids = new int[]{
                R.drawable.guild_1,
                R.drawable.guild_2,
                R.drawable.guild_3,
        };

        widthpi = DensityUtil.dip2px(this,10);

        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            //设置背景
            imageView.setBackgroundResource(ids[i]);

            //添加到集合中
            imageViews.add(imageView);

            //创建点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            /**
             * 单位是像素
             * 把单位当成dp转成对应的像素
             */
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthpi, widthpi);
            point.setLayoutParams(params);
            if (i != 0) {
                //包括第0个，所有的点距离左边有10个像素
                params.leftMargin = 10;
            }
            //添加到线性布局里面
            llPointGroup.addView(point);
        }

        //设置viewPager的适配器
        viewpager.setAdapter(new MyPagerAdapter(imageViews));

        //根据View的生命周期，当视图执行到onLayout或者onDraw的时候，视图的高和宽，边距都有了
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //得到屏幕滑动的百分比
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    //设置按钮的点击事件
    @OnClick(R.id.btn_start_main)
    public void onClick() {

        //1.保存曾经进入过主界面
        CacheUtils.putBoolean(GuideActivity.this, StartActivity.START_MAIN,true);

        //2.跳转到主页面
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);

        //3.关闭引导页面
        finish();

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面回调了会调用这个方法
         *
         * @param i  当前滑动页面的位置
         * @param v  页面滑动的百分比
         * @param i1 滑动的像素
         */
        @Override
        public void onPageScrolled(int i, float v, int i1) {

            //两点间移动的距离 = 屏幕滑动百分比 * 间距
//            int leftmargin = (int) (v * leftmax);

            //两点间滑动距离对应的坐标 = 原来的起始位置 + 两点间移动的距离
            int leftmargin = (int) (i * leftmax + v * leftmax);
            //params.leftMargin = 两点间滑动距离对应的坐标

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            params.leftMargin = leftmargin;
            ivRedPoint.setLayoutParams(params);

        }

        /**
         * 当页面被选中的时候，回调这个方法
         *
         * @param i 被选中页面的对应位置
         */
        @Override
        public void onPageSelected(int i) {
            if (i == imageViews.size() - 1) {
                //最后一个页面
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                btnStartMain.setVisibility(View.GONE);
            }
        }

        /**
         * 当viewPager页面滑动状态发生变化的时候
         *
         * @param i
         */
        @Override
        public void onPageScrollStateChanged(int i) {
        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            //执行不只一次
            ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(MyOnGlobalLayoutListener.this);

            //间距 = 第1个点距离左边的距离 - 第0个点距离左边的距离
            leftmax = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
        }
    }
}
