package com.crescentcatcher.locationtracking;

/**
 * Created by samrezikram on 11/23/17.
 */

public class Location {

    private int id;
    private String routepoints1;
    private String routepoints2;

    public Location() {
    }

    public Location(int id, String _routepoints1, String _routepoints2) {
        this.id = id;
        this.routepoints1 = _routepoints1;
        this.routepoints2 = _routepoints2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPointsForRoute1() {
        return this.routepoints1;
    }

    public void setPointsForRoute1(String _locationPoint1) {
        this.routepoints1 = _locationPoint1;
    }

    public String getPointsForRoute2() {
        return this.routepoints2;
    }

    public void setPointsForRoute2(String _locationPoints2) {
        this.routepoints2 = _locationPoints2;
    }

    @Override
    public String toString() {
        return "LocationData: { Route1: " + this.routepoints1 + "\nRoute2:" + this.routepoints2 ;
    }

}
