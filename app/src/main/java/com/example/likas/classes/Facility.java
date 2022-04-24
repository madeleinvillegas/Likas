package com.example.likas.classes;

public class Facility {
    public String name;
    public String latitude;
    public String longitude;
    public String type;
    public String slotsTaken;
    public String slotsMax;

    @SuppressWarnings("unused")
    public Facility() {
        // Firebase Required
    }

    public Facility(String name, String latitude, String longitude, String type, String slotsTaken, String slotsMax) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.slotsTaken = slotsTaken;
        this.slotsMax = slotsMax;
    }
}