package com.crescentcatcher.locationtracking;

/**
 * Created by samrezikram on 11/23/17.
 */

public class Location {

    private int id;
    private String latitude;
    private String longitude;

    public Location() {
    }

    public Location(int id, String _latitude, String _longitude) {
        this.id = id;
        this.latitude = _latitude;
        this.longitude = _longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String _lati) {
        this.latitude = _lati;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String _longi) {
        this.longitude = _longi;
    }

    @Override
    public String toString() {
        return latitude + ":" + longitude ;
    }

}
