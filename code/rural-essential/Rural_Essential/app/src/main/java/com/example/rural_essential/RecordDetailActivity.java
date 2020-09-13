package com.example.rural_essential;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.rural_essential.ui.model.LocationPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.lucene.util.SloppyMath;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class RecordDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView textView;
    //private LineChart mChart;
    private List<LocationPoint> locations;
    private List<Integer> speedLimit;
    private List<Double> currentSpeed;
    private Date startTime;
    private AnyChartView anyChartView;
    private GoogleMap gMap;
    private Object SupportMapFragment;
    private Polyline polyline = null;
    private List<LatLng> lanLngList = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView = findViewById(R.id.coordinates);
        anyChartView = findViewById(R.id.any_chart_view);
        setTitle("Summary Details");


        Intent intent = getIntent();
        //Bundle bundle = getIntent().getExtras();

        // Load locations speedlimit and currentSpeed passed from intent
        if (intent.hasExtra("locations")) {
            locations = (List<LocationPoint>) intent.getSerializableExtra("locations");
            
        } else {
            locations.add(new LocationPoint(-38.1610198,144.3501774,"Settlement Road"));
        }

        if (intent.hasExtra("speedLimit")) {
            speedLimit = (List<Integer>) intent.getSerializableExtra("speedLimit");

        }else{
            speedLimit.add(50);
        }

        if (intent.hasExtra("currentSpeed")) {
            currentSpeed = (List<Double>) intent.getSerializableExtra("currentSpeed");

        }else{
            currentSpeed.add(50.0);
        }

        if (intent.hasExtra("startTime")) {
            startTime = (Date) intent.getSerializableExtra("startTime");

        } else {
            startTime = new Date();
        }

        //setLineChartData();

        // Set the line chart with values in speed limit list and current speed list
        setLineChartData();

        // Add up the total distance based on GEO locations in locations list
        setTotalDistance();

        // Set Google maps to display user's route for this training
        setGoogleMap();
        //setBound();
        
    }

//    private void setBound() {
//        if(lanLngList.get(0).latitude < lanLngList.get(lanLngList.size()-1).latitude){
//            LatLngBounds latLngBounds = new LatLngBounds(lanLngList.get(0),lanLngList.get(lanLngList.size()-1));
//            gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
//        }
//        else{
//            LatLngBounds latLngBounds = new LatLngBounds(lanLngList.get(lanLngList.size()-1),lanLngList.get(0));
//            gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setGoogleMap() {
        SupportMapFragment supportMapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
         gMap = googleMap;
         gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
         gMap.getUiSettings().setZoomControlsEnabled(true);
         ScrollView mScrollView = findViewById(R.id.home_screen_layout);
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch()
                    {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });

        loadLatLngList();
//        PolylineOptions polylineOptions = new PolylineOptions()
//                .addAll(lanLngList).clickable(true);
//        polyline = gMap.addPolyline(polylineOptions);
        try{

          // Add polyline to map and highlight the overspeed section
        for (int i = 0; i < lanLngList.size()-1; i++) {

            if(speedLimit.get(i).doubleValue() >= currentSpeed.get(i)){
                gMap.addPolyline(new PolylineOptions()
                        .add(lanLngList.get(i),lanLngList.get(i+1))
                        .width(5)
                        .color(Color.BLUE));
            }else {
                gMap.addPolyline(new PolylineOptions()
                        .add(lanLngList.get(i),lanLngList.get(i+1))
                        .width(5)
                        .color(Color.RED));
            }
        }
        } catch (Exception e){
            e.printStackTrace();
            PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(lanLngList).clickable(true);
            polyline = gMap.addPolyline(polylineOptions);
        }

        // Add start and end marker
        if(lanLngList.size() <= 1){
            Marker m1 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(-38.15, 144.35))
                    .anchor(0.5f, 0.5f)
                    .title("Start point")
                    .snippet("This the point you start training")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_start))
            );
        }else{
            LatLng pointStart = lanLngList.get(0);
            LatLng pointEnd = lanLngList.get(lanLngList.size()-1);
            Marker m1 = googleMap.addMarker(new MarkerOptions()
                    .position(pointStart)
                    .anchor(0.5f, 0.5f)
                    .title("Start point")
                    .snippet("This the point you start training")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_start))
            );


            Marker m2 = googleMap.addMarker(new MarkerOptions()
                    .position(pointEnd)
                    .anchor(0.5f, 0.5f)
                    .title("End point")
                    .snippet("This the point you end training")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_end))
            );
        }
        this.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lanLngList.get(0),12));

    }

    // Load location list which would be used to google map
    private void loadLatLngList() {
        for (int i = 0; i < locations.size(); i++) {
            double lan = locations.get(i).getLantitude();
            double lng = locations.get(i).getLongitude();
            lanLngList.add(new LatLng(lan,lng));
        }

    }

    // Set the top line chart with values in speed limit list and current speed list
    private void setLineChartData() {
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);

        cartesian.padding(10d, 5d, 10d, 5d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // set ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("The speed trend");

        cartesian.yAxis(0).title("km/h");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();

        Date date = new Date();
        for (int i = 0; i < speedLimit.size(); i++) {
            date.setTime(startTime.getTime() + TimeUnit.SECONDS.toMillis(3 * i ));
            String newDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
            try {
                //Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldDate);
                newDate = dateFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
                newDate = "";
            }

            seriesData.add(new CustomDataEntry(newDate,speedLimit.get(i),currentSpeed.get(i)));
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        //Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Speed Limit");
        series1.stroke("#eb3434");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Current Speed");
        series2.stroke("#58D68D");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
        anyChartView.setChart(cartesian);

    }



    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value,  Number value2) {
            super(x, value);
            setValue("value2", value2);
            //setValue("value3", value3);
        }

    }
    //Calculate total distance based on two
    private void setTotalDistance() {
        double totalDistance = 0.0;
        String str = "";
        if(locations.size() <= 1){
            textView.setText("0");
        }
        try {
            for (int i = 0; i < locations.size()-1; i++) {
                double lat1 = locations.get(i).getLantitude();
                double lon1 = locations.get(i).getLongitude();
                double lat2 = locations.get(i+1).getLantitude();
                double lon2 = locations.get(i+1).getLongitude();
                double distance = calculateDistanceInMeters(lat1,lon1,lat2,lon2);
                totalDistance += distance;
            }
            DecimalFormat df = new DecimalFormat("#.00");
            if(totalDistance == 0.0){
                str = "0";
            }
            else{ str = df.format(totalDistance); }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        textView.setText("Total distance: " + str + "m");
    }

//    private void setLineChartData() {
//        List<Entry> yValuesSpeedLimit = new ArrayList<>();
//        List<Entry> yValuesCurrentSpeed = new ArrayList<>();
//
//
//        for (int i = 0; i < speedLimit.size(); i++) {
//            yValuesSpeedLimit.add(new Entry(i,speedLimit.get(i)));
//        }
//        for (int i = 0; i < currentSpeed.size(); i++) {
//            yValuesCurrentSpeed.add(new Entry(i,currentSpeed.get(i).floatValue()));
//        }
//
//        LineDataSet speedLimitSet = new LineDataSet(yValuesSpeedLimit,"Speed Limit");
//        LineDataSet currentSpeedSet = new LineDataSet(yValuesCurrentSpeed,"Current Speed");
//
//        speedLimitSet.setFillAlpha(110);
//        speedLimitSet.setCircleColor(Color.RED);
//        currentSpeedSet.setFillAlpha(50);
//        currentSpeedSet.setCircleColor(Color.parseColor("#a8a8a8"));
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(speedLimitSet);
//        dataSets.add(currentSpeedSet);
//
//        LineData data = new LineData(dataSets);
//        mChart.setData(data);
//    }
//
    public double calculateDistanceInMeters(double lat1, double long1, double lat2,
                                            double long2) {


        double dist = SloppyMath.haversinMeters(lat1, long1, lat2, long2);
        return dist;
    }
}
