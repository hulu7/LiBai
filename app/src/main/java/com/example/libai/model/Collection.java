package com.example.libai.model;

public class Collection {

    private String phone;
    private String uniquekey;
    private String title;
    private String url;

    public Collection(String phone, String uniquekey) {
        this.phone = phone;
        this.uniquekey = uniquekey;
    }

    public Collection(String phone, String uniquekey, String title, String url) {
        this.phone = phone;
        this.uniquekey = uniquekey;
        this.title = title;
        this.url = url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "phone='" + phone + '\'' +
                ", uniquekey='" + uniquekey + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
