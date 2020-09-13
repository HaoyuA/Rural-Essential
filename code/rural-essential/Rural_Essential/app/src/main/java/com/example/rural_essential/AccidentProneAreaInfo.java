package com.example.rural_essential;

import org.json.JSONException;
import org.json.JSONObject;

public class AccidentProneAreaInfo {
    private static final String QUERY_ADDRESS = "https://urgbgfazu1.execute-api.us-east-1.amazonaws.com/prod/is-schoolzone?";
    private Boolean isAccidentProneArea;
    private String isAccidentProneAreaInfo;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    public AccidentProneAreaInfo(){
        this.isAccidentProneArea = false;
    }

    /**
     * Get current point if is in accident prone area information
     * @param lat latitude
     * @param lon longitude
     */
    public void isAccidentProneArea(Double lat, Double lon){
        String urlString = QUERY_ADDRESS + "lat=" + lat + "&lon=" + lon;
        HttpConnection httpConnection = new HttpConnection(urlString);
        this.isAccidentProneAreaInfo = httpConnection.connect();
    }

    /**
     *
     * @return if current location is accident prone area
     */
    public Boolean getAccidentProneArea() {
        JSONObject jsonObject;
        try{
            jsonObject = new JSONObject(this.isAccidentProneAreaInfo);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return isAccidentProneArea;
    }
}
