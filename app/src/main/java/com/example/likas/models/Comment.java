package com.example.likas.models;

public class Comment {

    private String body, user;
    private String date;

    public Comment(){

    }

    public Comment(String body, String user, String date) {
        this.body = body;
        this.user = user;
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}