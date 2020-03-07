package com.example.libai.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.example.libai.pager.tabdetailpager.TabDetailPager;

import java.util.ArrayList;
import java.util.List;

public class MyNewsMenuDetailPagerAdapter extends PagerAdapter {

    private List<String> titles;
    private ArrayList<TabDetailPager> tabDetailPagers;

    public MyNewsMenuDetailPagerAdapter(List<String> titles, ArrayList<TabDetailPager> tabDetailPagers) {
        this.titles = titles;
        this.tabDetailPagers = tabDetailPagers;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TabDetailPager tabDetailPager = tabDetailPagers.get(position);
        View rootView = tabDetailPager.rootView;
        tabDetailPager.initData();//初始化数据
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
