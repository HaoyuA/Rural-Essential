package com.example.rural_essential.ui.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.rural_essential.ui.converter.DateConverter;
import com.example.rural_essential.ui.converter.DoubleArrayListConverter;
import com.example.rural_essential.ui.converter.IntArrayListConverter;
import com.example.rural_essential.ui.converter.LocationConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "record")
public class Record {

        @PrimaryKey(autoGenerate = true)
        public int recordid;

        @TypeConverters(LocationConverter.class)
        @ColumnInfo(name = "location")
        public List<LocationPoint> locationPoint;

        @TypeConverters(DateConverter.class)
        @ColumnInfo(name = "time_start")
        public Date timeStart;

        @TypeConverters(DateConverter.class)
        @ColumnInfo(name = "time_end")
        public Date timeEnd;

        @TypeConverters(DoubleArrayListConverter.class)
        @ColumnInfo(name = "current_speed")
        public List<Double> currentSpeed;

        @TypeConverters(IntArrayListConverter.class)
        @ColumnInfo(name = "speed_limit")
        public List<Integer> speedLimit;


    public Record(List<LocationPoint> locationPoint, Date timeStart, Date timeEnd, List<Double> currentSpeed, List<Integer> speedLimit) {
        this.locationPoint = locationPoint;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.currentSpeed = currentSpeed;
        this.speedLimit = speedLimit;
    }

    // Load dummy data for testing
    @Ignore
    public Record(){
        this.locationPoint = new ArrayList<LocationPoint>();
        this.locationPoint.add(new LocationPoint(144.3501774,-38.1610198,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(144.3501181,-38.1612920,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(144.3500613,-38.1616786,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(144.3500500,-38.1617536,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(144.3500233,-38.1619317,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(144.3500033,-38.1622078,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(144.3499839,-38.1624745,"Settlement Road"));


        this.timeStart = new Date();
        this.timeEnd = new Date();

        this.currentSpeed = new ArrayList<Double>();
        this.currentSpeed.add(45.0);
        this.currentSpeed.add(60.0);
        this.currentSpeed.add(63.0);
        this.currentSpeed.add(65.0);
        this.currentSpeed.add(75.0);
        this.currentSpeed.add(80.0);
        this.currentSpeed.add(75.0);

        this.speedLimit = new ArrayList<Integer>();
        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);
    }

//    {
//        "name": "Settlement Road",
//        "speed_limit": "70",
//        "lat": [-38.1610198, -38.1612920, -38.1616786, -38.1617536, -38.1619317, -38.1622078, -38.1624745],
//        "lon": [144.3501774, 144.3501181, 144.3500613, 144.3500500, 144.3500233, 144.3500033, 144.3499839]
//    }
    private void addDummyData() {
        this.locationPoint.add(new LocationPoint(-38.1610198,144.3501774,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(-38.1612920,144.3501181,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(-38.1616786,144.3500613,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(-38.1617536,144.3500500,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(-38.1619317,144.3500233,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(-38.1622078,144.3500033,"Settlement Road"));
        this.locationPoint.add(new LocationPoint(-38.1624745,144.3499839,"Settlement Road"));

        this.timeStart = new Date();
        this.timeEnd = new Date();

        this.currentSpeed.add(45.0);
        this.currentSpeed.add(60.0);
        this.currentSpeed.add(63.0);
        this.currentSpeed.add(65.0);
        this.currentSpeed.add(75.0);
        this.currentSpeed.add(80.0);
        this.currentSpeed.add(75.0);

        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);
        this.speedLimit.add(70);

    }

    public void setRecordid(int recordid) {
        this.recordid = recordid;
    }

    public int getRecordid() {
        return recordid;
    }

    public List<LocationPoint> getLocationPoint() {
        return locationPoint;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public List<Double> getCurrentSpeed() {
        return currentSpeed;
    }

    public List<Integer> getSpeedLimit() {
        return speedLimit;
    }
}
