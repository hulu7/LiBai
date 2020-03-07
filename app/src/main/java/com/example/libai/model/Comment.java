package com.example.libai.model;

public class Comment {

    private int id;
    private String phone;
    private String content;
    private String time;
    private String uniquekey;

    public Comment(int id, String phone, String content, String time, String uniquekey) {
        this.id = id;
        this.phone = phone;
        this.content = content;
        this.time = time;
        this.uniquekey = uniquekey;
    }

    public Comment(String phone, String content, String time, String uniquekey) {
        this.phone = phone;
        this.content = content;
        this.time = time;
        this.uniquekey = uniquekey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", uniquekey='" + uniquekey + '\'' +
                '}';
    }
}
