package com.example.libai.utils;

public class Constants {
    public static String getUrl(String category, Integer page, Integer limit){
        String url = "https://www.deepinews.com/api/articles/getArticle?page=" +
                     page +
                     "&limit=" +
                     limit +
                     "&catalog=" +
                     category;
        return url;
    }
}
