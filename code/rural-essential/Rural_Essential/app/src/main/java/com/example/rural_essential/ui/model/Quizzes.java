package com.example.rural_essential.ui.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Quizzes {
    private static final String QUIZZES_ADDRESS = "https://k6v5iw6j6c.execute-api.us-east-1.amazonaws.com/prod/get-value";
    public static String QUIZZES;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    public Quizzes(){}
    public static String getQUIZZES(){
        HttpURLConnection connection = null;
        String inputLine;
        try{
            URL url = new URL(QUIZZES_ADDRESS);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();

            StringBuilder stringBuilder = new StringBuilder();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            //Check if the line we are reading is not null
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            QUIZZES = stringBuilder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return QUIZZES;
    }
}

