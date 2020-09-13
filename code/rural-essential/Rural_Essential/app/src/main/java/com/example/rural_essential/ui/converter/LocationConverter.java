package com.example.rural_essential.ui.converter;


import androidx.room.TypeConverter;

import com.example.rural_essential.ui.model.LocationPoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

// Location list Converter for save date data into Room database
public class LocationConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromOptionValuesList(List<LocationPoint> locationPoints) {
        if (locationPoints == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<LocationPoint>>() {
        }.getType();
        String json = gson.toJson(locationPoints, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<LocationPoint> toOptionValuesList(String locationsString) {
        if (locationsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<LocationPoint>>() {
        }.getType();
        List<LocationPoint> locationsList = gson.fromJson(locationsString, type);
        return locationsList;
    }

}
