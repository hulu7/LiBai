package com.example.libai.base;

import android.content.Context;
import android.view.View;

public abstract class MenuDetailBasePager {

    public final Context context;

    //代表各个详情页面的视图
    public View rootView;

    public MenuDetailBasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    public abstract View initView();

    public void initData(){

    }

}
