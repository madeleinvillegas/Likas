package com.example.likas.classes;

public class Facility {
    public String name;
    public String latitude;
    public String longitude;
    public String type;
    public String slotsAvail;
    public String slotsMax;

    @SuppressWarnings("unused")
    public Facility() {
        // Firebase Required
    }

    public Facility(String name, String latitude, String longitude, String type, String slotsAvail, String slotsMax) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.slotsAvail = slotsAvail;
        this.slotsMax = slotsMax;
    }
}