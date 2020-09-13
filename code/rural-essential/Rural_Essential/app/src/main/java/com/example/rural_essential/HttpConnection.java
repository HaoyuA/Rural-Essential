package com.example.rural_essential;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {
    private String API_ADDRESS;
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 25000;
    private static final int CONNECTION_TIMEOUT = 25000;

    public HttpConnection() {
        API_ADDRESS = "";
    }
    public HttpConnection(String API_ADDRESS){
        this.API_ADDRESS = API_ADDRESS;
    }

    /**
     * Perform a Http Connection and get returned information
     * @return information returned from connection
     */
    public String connect(){
        String result = "";
        String inputLine;
        HttpURLConnection connection = null;
        String urlString = API_ADDRESS;
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
                    stringBuilder.append(inputLine.trim());
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            } else {
                stringBuilder.append("{}");
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getAPI_ADDRESS() {
        return API_ADDRESS;
    }

    public void setAPI_ADDRESS(String API_ADDRESS) {
        this.API_ADDRESS = API_ADDRESS;
    }
}
