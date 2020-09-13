package com.example.rural_essential.ui.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Model for source in news api response
public class Source {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}