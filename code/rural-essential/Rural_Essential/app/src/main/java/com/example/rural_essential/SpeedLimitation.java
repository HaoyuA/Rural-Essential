package com.example.rural_essential;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpeedLimitation {
    private static final String API_ADDRESS = "https://www.overpass-api.de/api/interpreter?data=[out:json][timeout:15];way[\"maxspeed\"]";
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 25000;
    private static final int CONNECTION_TIMEOUT = 25000;

    public SpeedLimitation() {
    }

    /**
     * Get road information from over-pass Api, includes speed limitation, road name, road structure
     * @param left left bond of point in longitude
     * @param bottom bottom bond of point in latitude
     * @param right right bond of point in longitude
     * @param top top bond of point in latitude
     * @return Road information string
     */
    public static String getSpeedLimitation(double left, double bottom, double right, double top) {
        String result = "";
        String inputLine;
        HttpURLConnection connection = null;
        String urlString = API_ADDRESS + "(" +
                left + ","+ bottom + "," + right + "," + top + ");out;";
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
            int responseCode = connection.getResponseCode();
            StringBuilder stringBuilder = new StringBuilder();
            //Create a new InputStreamReader
            if (responseCode == 200) {
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            }
            else{
                stringBuilder.append("{}");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return result;
    }

}
