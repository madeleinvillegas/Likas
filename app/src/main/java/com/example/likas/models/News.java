package com.example.likas.models;


public class News {

    private String title, author, description;
    private String date;

    public News(){}

    public News(String title,String author,String date, String description){
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
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}