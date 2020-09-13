package com.example.rural_essential.ui.model;

public class Help {
     private String description;
    private  int imageID;
    public Help(String description, int imageID){
        this.description = description;
        this.imageID = imageID;
    }

    public int getImageID() {
        return imageID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
