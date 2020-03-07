package com.example.libai.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {

    private ArrayList<ImageView> imageViews;

    public MyPagerAdapter(ArrayList<ImageView> imageViews) {
        this.imageViews = imageViews;
    }

    /**
     * 返回数据的总个数
     *
     * @return
     */
    @Override
    public int getCount() {
        return imageViews.size();
    }

    /**
     * 判断
     *
     * @param view 当前创建的视图
     * @param o    下面instantiateItem返回的结果值
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /**
     * 作用，getView
     *
     * @param container viewPager
     * @param position  要创建页面的位置
     * @return 返回和创建当前页面的关系的值
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = imageViews.get(position);
        //添加到容器中
        container.addView(imageView);

        return imageView;
        //return super.instantiateItem(container, position);
    }

    /**
     * 销毁页面
     *
     * @param container viewPager
     * @param position  要销毁页面的位置
     * @param object    要销毁的页面
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
