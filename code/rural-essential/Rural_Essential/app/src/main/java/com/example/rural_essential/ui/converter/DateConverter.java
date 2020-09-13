package com.example.rural_essential.ui.converter;

import androidx.room.TypeConverter;

import java.util.Date;

// Date Converter for save date data into Room database
public class DateConverter {

    @TypeConverter
    public static Date toDate(Long dateLong){

        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public static long fromDate(Date date){
            return date == null ? null :date.getTime();
        }

}
