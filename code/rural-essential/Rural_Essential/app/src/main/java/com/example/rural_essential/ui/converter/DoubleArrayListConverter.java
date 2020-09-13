package com.example.rural_essential.ui.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

// Double list Converter for save date data into Room database
public class DoubleArrayListConverter {
    @TypeConverter // note this annotation
    public String fromOptionValuesList(List<Double> currentSpeed) {
        if (currentSpeed == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Double>>() {
        }.getType();
        return gson.toJson(currentSpeed, type);
    }

    @TypeConverter // note this annotation
    public List<Double> toOptionValuesList(String currentSpeedString) {
        if (currentSpeedString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Double>>() {
        }.getType();
        return gson.fromJson(currentSpeedString, type);
    }
}
