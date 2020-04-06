package com.example.libai.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.libai.R;
import com.example.libai.activity.MainActivity;
import com.example.libai.adapter.ContentFragmentAdapter;
import com.example.libai.base.BaseFragment;
import com.example.libai.base.BasePager;
import com.example.libai.pager.FindPager;
import com.example.libai.pager.HomePager;
import com.example.libai.pager.MyPager;
import com.example.libai.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * 正文Fragment
 */
public class ContentFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    Unbinder unbinder;

    /**
     * 装3个页面的集合
     */
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
//        LogUtil.e("正文Fragment视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment, null);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
//        LogUtil.e("正文Fragment数据被初始化了");

        //初始化3个页面，并且放入集合中
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));
        basePagers.add(new FindPager(context));
        basePagers.add(new MyPager(context));

        //设置viewPager的适配器
        viewpager.setAdapter(new ContentFragmentAdapter(basePagers));

        //设置RadioGroup的选中状态改变的监听
        rgMain.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //监听某个页面被选中，初始对应的页面的数据
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置默认选中首页
        rgMain.check(R.id.rb_home);
        //初始化首页数据
        basePagers.get(0).initData();
        //设置模式slidingMenu可以滑动
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);

    }

    public HomePager getHomePager() {
        return (HomePager) basePagers.get(0);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            basePagers.get(i).initData();
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        /**
         *
         * @param group RadioGround
         * @param checkedId 被选中的RadioButton的id
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_home:
                    viewpager.setCurrentItem(0,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_find:
                    viewpager.setCurrentItem(1,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_my:
                    viewpager.setCurrentItem(2,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    /**
     * 根据传入的参数设置是否让slidingMenu可以滑动
     * @param touchMode
     */
    private void isEnableSlidingMenu(int touchMode) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchMode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
