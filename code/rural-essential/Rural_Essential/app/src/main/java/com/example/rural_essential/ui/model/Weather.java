package com.example.rural_essential.ui.model;

public class Weather {
    private String weather;
    private Long sunRiseTime;
    private String weatherDetails;
    private String location;
    private Long sunSetTime;
    public Weather(){

    }
    public Weather(String weather, Long sunRiseTime, Long sunSetTime, String weatherDetails, String location){
        this.weather = weather;
        this.location = location;
        this.sunRiseTime = sunRiseTime;
        this.weatherDetails = weatherDetails;
        this.sunSetTime  = sunSetTime;
    }

    public Long getSunRiseTime() {
        return sunRiseTime;
    }

    public String getLocation() {
        return location;
    }

    public String getWeather() {
        return weather;
    }

    public String getWeatherDetails() {
        return weatherDetails;
    }

    public Long getSunSetTime() {
        return sunSetTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSunRiseTime(Long sunRiseTime) {
        this.sunRiseTime = sunRiseTime;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setWeatherDetails(String weatherDetails) {
        this.weatherDetails = weatherDetails;
    }

    public void setSunSetTime(Long sunSetTime) {
        this.sunSetTime = sunSetTime;
    }
}
