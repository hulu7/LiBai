package com.example.libai.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {

    /**
     * 缓存值
     * @param context 上下文
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("news", Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    /**
     * 保存软件的参数
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("news", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 缓存文本数据
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("news", Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    /**
     * 获取缓存文本的信息
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("news", Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static void delString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("news", Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

}
