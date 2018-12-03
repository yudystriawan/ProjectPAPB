package com.example.yudystriawan.projectpapb.Data;


import java.io.Serializable;

public class Restoran implements Serializable {
    private String id, name, phone, rating, type, weather, latitude, longitude;


    public Restoran(String id, String name, String phone, String rating, String type, String weather, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.type = type;
        this.weather = weather;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRating() {
        return rating;
    }

    public String getType() {
        return type;
    }

    public String getWeather() {
        return weather;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
