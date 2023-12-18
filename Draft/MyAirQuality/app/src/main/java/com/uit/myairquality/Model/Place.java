package com.uit.myairquality.Model;

import com.mapbox.geojson.Point;

public class Place {
    private String name;
    private Point location;

    public Place(String name, Point location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }
}
