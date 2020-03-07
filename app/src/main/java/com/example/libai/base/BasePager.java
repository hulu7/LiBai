package com.example.libai.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.libai.R;
import com.example.libai.activity.MainActivity;

public class BasePager {

    /**
     * 上下文
     */
    public final Context context;//MainActivity

    /**
     * 视图，代表各个不同的页面
     */
    public View rootView;


    public TextView tvTitle;
    public ImageButton ibMenu;
    public FrameLayout flContent;


    public BasePager(Context context) {
        this.context = context;
        //构建方法一执行，视图就被初始化
        rootView = initView();
    }

    /**
     * 用于初始化公共部分视图，并且初始化加载子视图的FragmentLayout
     *
     * @return
     */
    private View initView() {
        View view = View.inflate(context, R.layout.base_pager, null);
        tvTitle = view.findViewById(R.id.tv_title);
        ibMenu = view.findViewById(R.id.ib_menu);
        flContent = view.findViewById(R.id.fl_content);
        ibMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//关<->开

            }
        });
        return view;
    }

    /**
     * 初始化数据，当子视图需要初始化数据，或者绑定数据，联网请求数据并且绑定的时候，重写该方法
     */
    public void initData() {

    }

}
