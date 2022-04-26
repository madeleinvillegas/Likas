package com.example.likas.models;

import java.util.List;

public class Post {
    private String uid;
    private String name;
    private String content;
    private String date;
    private List<String> tags;

    public Post() {
    }

    public Post(String uid, String name, String content, String date, List<String> tags) {

        this.uid = uid;
        this.name = name;
        this.content = content;
        this.date = date;
        this.tags = tags;
    }


    public String getTags() {
        String strTags = "null";
        try {
            strTags = tags.toString();
            strTags = strTags.replace("[", "").replace("]", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strTags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}