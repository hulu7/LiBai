package com.example.libai.utils;

public class Constants {

    public static final String NEWS_URL = "url";

    public static String getUrl(String title){
        String url = "http://v.juhe.cn/toutiao/index?type="+ title +"&key=5e4b452d5e7749493dec7a9ced8edc3a";
        return url;
    }

}
