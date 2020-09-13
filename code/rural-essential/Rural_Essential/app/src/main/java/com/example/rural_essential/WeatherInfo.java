package com.example.rural_essential;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.rural_essential.ui.model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherInfo {
    private static final String WEATHER_KEY = "df4bf6852f353305e06866e0c5dfa4e8";
    private static final String API_ADDRESS = "https://api.openweathermap.org/data/2.5/weather";
    private String weatherInfo;
    private Weather weather;
    public WeatherInfo() {
        this.weatherInfo = "";
        this.weather = new Weather();
    }

    /**
     * Get weather information from open weather api by latitude, and longitude, and assign the result to weatherInfo string.
     * @param lat latitude
     * @param lon longitude
     */
    public void getWeatherInfoString(double lat, double lon) {
        String urlString = API_ADDRESS
                + "?lat=" + lat
                + "&lon=" + lon
                + "&appid=" + WEATHER_KEY;
        HttpConnection httpConnection = new HttpConnection(urlString);
        this.weatherInfo = httpConnection.connect();
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    /**
     * Process the weatherInfo string by Json object
     */
    public void createWeather(){
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(this.weatherInfo);
            this.weather.setWeather(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
            this.weather.setWeatherDetails(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
            this.weather.setLocation(jsonObject.getString("name"));
            this.weather.setSunRiseTime(jsonObject.getJSONObject("sys").getLong("sunrise"));
            this.weather.setSunSetTime(jsonObject.getJSONObject("sys").getLong("sunset"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get warning message about current weather
     * @return a warning string related to current weather
     */
    public String warningMessage(){
        String warningMessage = "";
        switch (this.weather.getWeather()){
            case "Thunderstorm":
            case "Mist":
            case "Dust":
                warningMessage = "The weather is bad";

        }
        return warningMessage;
    }

    private void setWeatherImageView(ImageView imageView, TextView textView, String weatherString) {
        Long tsLong = System.currentTimeMillis() / 1000;

        //set weather icon and welcome text.
        String welcomeText = "Welcome\n";
        if (this.weather.getWeather().length() > 0) {
            switch (this.weather.getWeather()) {
                case "Thunderstorm":
                    imageView.setImageResource(R.drawable.iconfinder_weather_23_1530363);
                    welcomeText += "The weather is bad";
                    break;
                case "Drizzle":
                    imageView.setImageResource(R.drawable.iconfinder_weather_30_1530365);
                    welcomeText += "Please drive carefully";
                    break;
                case "Rain":
                    imageView.setImageResource(R.drawable.iconfinder_weather_31_1530364);
                    welcomeText += "Road is wet, careful";
                    break;
                case "Snow":
                    imageView.setImageResource(R.drawable.iconfinder_weather_32_1530362);
                    welcomeText += "Road is frozen, not safe";
                    break;
                case "Mist":
                    if (tsLong > this.weather.getSunRiseTime() && tsLong < this.weather.getSunSetTime())
                        imageView.setImageResource(R.drawable.iconfinder_weather_06_1530386);
                    else
                        imageView.setImageResource(R.drawable.iconfinder_weather_16_1530377);
                    welcomeText += "The weather is bad";
                    break;
                case "Smoke":
                case "Haze":
                case "Fog":
                    imageView.setImageResource(R.drawable.iconfinder_weather_27_1530368);
                    welcomeText += "Short view range, not safe";
                    break;
                case "Dust":
                    if (tsLong > this.weather.getSunRiseTime() && tsLong < this.weather.getSunSetTime())
                        imageView.setImageResource(R.drawable.iconfinder_weather_19_1530374);
                    else
                        imageView.setImageResource(R.drawable.iconfinder_weather_20_1530372);
                    welcomeText += "The weather is bad";
                    break;
                case "Sand":
                    imageView.setImageResource(R.drawable.iconfinder_weather_28_1530367);
                    welcomeText += "The weather is bad";
                    break;
                case "Clear":
                    if (tsLong > this.weather.getSunRiseTime() && tsLong < this.weather.getSunSetTime())
                        imageView.setImageResource(R.drawable.iconfinder_weather_01_1530392);
                    else
                        imageView.setImageResource(R.drawable.iconfinder_weather_10_1530382);
                    welcomeText += "Today is a good day";
                    break;
                case "Clouds":
                    imageView.setImageResource(R.drawable.iconfinder_weather_22_1530369);
                    welcomeText += "Today is a good day";
                    break;
            }
            textView.setText(welcomeText + "\n" + weather.getLocation());
        }
    }
}
