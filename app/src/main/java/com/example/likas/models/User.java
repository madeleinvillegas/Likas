package com.example.likas.models;

public class User {
    private String name;
    private String admin;

    public User(String name,String admin){
        this.name = name;
        this.admin = admin;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
