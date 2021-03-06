package com.example.libai.pager;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import com.example.libai.base.BasePager;
import com.example.libai.utils.LogUtil;

public class MyPager extends BasePager {

    public MyPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        LogUtil.e("我的被初始化了。。。");
        //1.设置标题
        tvTitle.setText("我的");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        flContent.addView(textView);
        //4.绑定数据
        textView.setText("我的内容");

    }

}
