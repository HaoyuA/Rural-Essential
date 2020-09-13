package com.example.rural_essential;

import org.json.JSONException;
import org.json.JSONObject;

public class SchoolZoneInformation {
    private static final String QUERY_ADDRESS = "https://urgbgfazu1.execute-api.us-east-1.amazonaws.com/prod/is-schoolzone";
    private Boolean isSchoolZone;
    private String isSchoolZoneInfo;
    public SchoolZoneInformation(){
        isSchoolZone = false;
    }

    /**
     * Get if current location is in school zone, and assign result to isSchoolZoneInfo String
     * @param lat latitude
     * @param lon longitude
     */
    public void getIsSchoolZoneInfo(Double lat, Double lon){
        String urlString = QUERY_ADDRESS + "?lat=" + lat + "&lon=" + lon;
        HttpConnection httpConnection = new HttpConnection(urlString);
        this.isSchoolZoneInfo = httpConnection.connect();
    }

    /**
     *
     * @return if in school zone
     */
    public Boolean getIsSchoolZone(){
        JSONObject jsonObject;
        try{
            jsonObject= new JSONObject(this.isSchoolZoneInfo);
            this.isSchoolZone = jsonObject.getBoolean("body");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return this.isSchoolZone;
    }

}
