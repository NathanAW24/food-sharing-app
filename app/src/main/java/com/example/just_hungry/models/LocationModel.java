package com.example.just_hungry.models;

import java.util.HashMap;

public class LocationModel {

    public double latitude;
    public double longitude;

    public LocationModel() {
        //set to default value, which is SUTD coordinates
        this.latitude = 0; // 1.3402320075948917
        this.longitude = 0; // 103.96296752039913
    }
    public LocationModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public LocationModel(HashMap<String, Double> input){
        this.latitude = Double.parseDouble(String.valueOf(input.get("latitude")));
        this.longitude = Double.parseDouble(String.valueOf(input.get("longitude")));
    }
    
    public HashMap<String, Object> getHashMapForFirestore() {
        HashMap<String, Object> output = new HashMap<>();
        output.put("latitude", latitude);
        output.put("longitude", longitude);
        return output;
    }
    // getter and setter methods
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getStringLocation() {
       return ("Latitude: " + latitude + " Longitude: " + longitude);
    }
}
