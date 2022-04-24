package com.example.likas.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
    private String uid;
    private String name;
    private String content;
    private Date date;
    private List<String> tags;

    public Post(){}
    public Post(String uid, String name, String content, Date date, List<String> tags){

        this.uid = uid;
        this.name = name;
        this.content = content;
        this.date = date;
        this.tags = tags;
    }


    public String getTags() {
        String strTags = this.tags.toString();

        strTags = strTags.replace("[", "")
                .replace("]", "");
        return strTags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public void setDate(Date date) {
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
