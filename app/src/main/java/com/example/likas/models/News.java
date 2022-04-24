package com.example.likas.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class News {

    private String title, author, description;
    private Date date;

    public News(){}

    public News(String title,String author,Date date, String description){
        this.title = title;
        this.author = author;
        this.date = date;
        this.description = description;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}