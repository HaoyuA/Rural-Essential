package com.example.rural_essential.ui.model;

import java.io.Serializable;

public class LocationPoint implements Serializable {

    // location point used to store location data retrieved from overpass api when user travel
    private Double longitude;
    private Double lantitude;
    private String roadName;

    public LocationPoint(Double longitude, Double lantitude, String roadName) {
        this.longitude = longitude;
        this.lantitude = lantitude;
        this.roadName = roadName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLantitude() {
        return lantitude;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLantitude(Double lantitude) {
        this.lantitude = lantitude;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
}
