package com.example.libai.pager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import com.example.libai.R;
import com.example.libai.activity.MainActivity;
import com.example.libai.adapter.MyNewsMenuDetailPagerAdapter;
import com.example.libai.base.BasePager;
import com.example.libai.base.MenuDetailBasePager;
import com.example.libai.pager.tabdetailpager.TabDetailPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class HomePager extends BasePager {

    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;

    @ViewInject(R.id.ib_tab_next)
    private ImageButton ibTabNext;

    /**
     * 左侧菜单对应的数据集合
     */
    private List<String> titles;

    /**
     * 详情页面的集合
     */
    private ArrayList<MenuDetailBasePager> detailBasePagers;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    private ArrayList<TabDetailPager> tabDetailPagers;

    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        final View view = View.inflate(context, R.layout.news_menu_detail_pager, null);
        x.view().inject(this, view);
        //设置点击事件
        ibTabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        tvTitle.setText("深度资讯");
        flContent.removeAllViews();
        flContent.addView(view);
//        LogUtil.e("主页被初始化了。。。");
        ibMenu.setVisibility(View.VISIBLE);

        //添加数据
        AddData();

        //准备新闻详情页面的数据
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(context, titles.get(i)));
        }

        //设置ViewPager适配器
        viewPager.setAdapter(new MyNewsMenuDetailPagerAdapter(titles,tabDetailPagers));
        //ViewPager和TabPageIndicator关联
//        tabPageIndicator.setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        //主页以后监听页面的变化，TabPageIndicator监听页面的变化
//        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        //设置滑动或者固定
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    private void AddData() {
        titles = new ArrayList<>();
        titles.add("头条");
        titles.add("社会");
        titles.add("国内");
        titles.add("国际");
        titles.add("娱乐");
        titles.add("体育");
        titles.add("军事");
        titles.add("科技");
        titles.add("财经");
        titles.add("时尚");
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if (i == 0) {
                //SlidingMenu可以全屏滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            } else {
                //SlidingMenu不可以滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    /**
     * 根据传入的参数设置是否让slidingMenu可以滑动
     *
     * @param touchMode
     */
    private void isEnableSlidingMenu(int touchMode) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchMode);
    }


}
