package com.example.libai.pager;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import com.example.libai.base.BasePager;
import com.example.libai.utils.LogUtil;

public class FindPager extends BasePager {

    public FindPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        LogUtil.e("发现被初始化了。。。");
        //1.设置标题
        tvTitle.setText("发现");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        flContent.addView(textView);
        //4.绑定数据
        textView.setText("发现内容");

    }

}
