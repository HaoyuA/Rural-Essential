package com.example.rural_essential.ui.home;

import android.location.Location;

public class DrivingSpeed extends Location {

      public DrivingSpeed(Location location) {
            // TODO Auto-generated constructor stub
            super(location);
      }



      @Override
      public float distanceTo(Location dest) {
            // TODO Auto-generated method stub
          // return distance in metric
          return super.distanceTo(dest);
      }

      @Override
      public float getAccuracy() {
            // TODO Auto-generated method stub
          return super.getAccuracy();
      }

      @Override
      public double getAltitude() {
            // TODO Auto-generated method stub
          return super.getAltitude();
      }

      @Override
      public float getSpeed() {
            // TODO Auto-generated method stub
          return super.getSpeed() * 3.6f;
      }
}
