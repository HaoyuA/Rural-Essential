package com.example.rural_essential.ui.model;

import androidx.annotation.NonNull;

public class Node {
    private String roadCategory;
    private int speedLimit;
    private String roadName;
    private String reference;
    private String surface;
    public Node(){
        roadCategory = "Not defined";
        speedLimit = 0;
        roadName = "None";
        reference = "";
        surface = "";
    }
    public Node(String roadCategory, int speedLimit, String roadName, String reference, String surface){
        this.roadCategory = roadCategory;
        this.speedLimit = speedLimit;
        this.roadName = roadName;
        this.reference = reference;
        this.surface = surface;
    }
    public Node(String roadCategory, int speedLimit, String roadName, String surface){
        this.roadCategory = roadCategory;
        this.speedLimit = speedLimit;
        this.roadName = roadName;
        this.reference = "";
        this.surface = surface;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public String getReference() {
        return reference;
    }

    public String getRoadCategory() {
        return roadCategory;
    }

    public String getRoadName() {
        return roadName;
    }

    public String getSurface() {
        return surface;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setRoadCategory(String roadCategory) {
        this.roadCategory = roadCategory;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }
    @NonNull
    @Override
    public String toString() {
        return this.roadName + " " + this.speedLimit;
    }
}
