package com.example.libai;

import android.app.Application;
import org.xutils.x;


/**
 * 代表整个软件
 */
public class NewsApplication extends Application {
    /**
     * 所有组件被创建之前执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);// 是否输出debug日志, 开启debug会影响性能.
    }
}
