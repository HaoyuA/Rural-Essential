package com.example.rural_essential.ui.converter;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

// Integer list Converter for save date data into Room database
public class IntArrayListConverter {

    @TypeConverter // note this annotation
    public String fromOptionValuesList(List<Integer> speedLimit) {
        if (speedLimit == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.toJson(speedLimit, type);
    }

    @TypeConverter // note this annotation
    public List<Integer> toOptionValuesList(String speedLimitString) {
        if (speedLimitString == null) {
            return (null);
        }
        Gson gson = new Gson();
            Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.fromJson(speedLimitString, type);
    }
}
