package com.example.rural_essential.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.rural_essential.AccidentProneAreaInfo;
import com.example.rural_essential.LoadingDialog;
import com.example.rural_essential.ui.model.LocationPoint;
import com.example.rural_essential.R;
import com.example.rural_essential.RecordViewModel;
import com.example.rural_essential.SchoolZoneInformation;
import com.example.rural_essential.SpeedLimitation;
import com.example.rural_essential.WeatherInfo;
import com.example.rural_essential.ui.api.BaseGPSSpeed;
import com.example.rural_essential.ui.model.Node;
import com.example.rural_essential.ui.model.Record;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LOCATION_SERVICE;

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener, View.OnClickListener, BaseGPSSpeed, TextToSpeech.OnInitListener {
    private static final int MY_APP_PERMISSION = 1;
    private int MY_DATA_CHECK_CODE = 0;
    private GoogleMap googleMap;
    private MapView mapView;
    private LocationManager locationManager;
    private Location myLastLocation;
    private LocationProvider locationProvider;
    private String weatherMain = "";
    private String position = "";
    private static final String DEFAULT_SPEEDLIMIT = "50";
    private static final String DEFAULT_ROAD_INFO = "Unknown Road";
    private static final String SCHOOL_ZONE = "School Nearby, be careful";
    private static final String OVER_SPEEDING = "You are over speeding ";
    private static final String LIMIT_UPDATE = "New speed limit";
    private static final String ACCIDENT_PRONE_AREA = "Accident prone area, be careful";
    private static final List<String> greatGeelong = Arrays.asList("Anakie", "Armstrong Creek", "Avalon", "Balliang",
            "Barwon Heads", "Batesford", "Bell Park", " Bell Post Hill", "Bellarine", "Belmont",
            "Breakwater", "Breamlea", "Ceres", "Charlemont", "City of Greater Geelong",
            "Clifton Springs", "Connewarre", "Corio", "Curlewis", "Drumcondra", "Drysdale",
            "East Geelong", "Fyansford", "Geelong", "Geelong West", "Grovedale", "Hamlyn Heights",
            "Herne Hill", "Highton", "Indented Head", "Lara", "Leopold", "Little River", "Lovely Banks",
            "Manifold Heights", "Mannerim", "Marcus Hill", "Marshall", "Moolap", "Moorabool",
            "Mount Duneed", "Newcomb", "Newtown", "Norlane", "North Geelong", "North Shore",
            "Ocean Grove", "Point Lonsdale", "Point Wilson", "Portarlington", "Queenscliff",
            "Rippleside", "South Geelong", "St Albans Park", "St Leonards", "Staughton Vale",
            "Swan Bay", "Thomson, Wallington", "Wandana Heights", "Waurn Ponds", "Whittington");
    private String currentSpeedLimit = "";
    private String currentRoadName = "";
    private double currentSpeed;
    private Long sunRiseTime;
    private Long sunsetTime;
    private TextView welcomeTextView;
    private TextView roadNameTextView;
    private ImageView weatherIconImageView;
    private TextView serviceableTextView;
    private TextView currentSpeedTextView;
    private TextView speedLimitationTextView;
    private TextView roadSignTextView;
    private ConstraintLayout homeLayout;
    private ScrollView trainingCardView;
    private FrameLayout waringFrameLayout;
    private Button startTrainingButton;
    private Button stopTrainingButton;
    private WeatherInfoAsync wi;
    private SpeedLimitationAsync sl;
    private ArrayList<Double> speedRecords;
    private ArrayList<Integer> speedLimitRecords;
    private Date startTime;
    private Date endTime;
    private ArrayList<LocationPoint> locationRecords;
    private ArrayList<String> voiceMessages;
    private RecordViewModel recordViewModel;
    private Timer timerSpeedLimit;
    private TimerTask timerTaskSpeedLimit;
    private Timer timerAddRecord;
    private TimerTask timerTaskAddRecord;
    private Timer timerSpeech;
    private Timer timerSchoolZone;
    private Timer timerOverSpeeding;
    private Timer timerAccidentProneArea;
    private TimerTask timerTaskSpeech;
    private TimerTask timerTaskSchoolZone;
    private TimerTask timerTaskOverSpeeding;
    private TimerTask timerTaskAccidentProneArea;
    private TextToSpeech voiceNotify;
    private LoadingDialog loadingDialog;
    private ImageView roadStructure;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        // initiate all view items
        loadingDialog = new LoadingDialog(getActivity());
        voiceMessages = new ArrayList<String>();
        welcomeTextView = root.findViewById(R.id.welcome_text);
        serviceableTextView = root.findViewById(R.id.service_availability);
        weatherIconImageView = root.findViewById(R.id.weather_image);
        startTrainingButton = root.findViewById(R.id.start_training);
        stopTrainingButton = root.findViewById(R.id.stop_button);
        homeLayout = root.findViewById(R.id.home_screen_layout);
        trainingCardView = root.findViewById(R.id.training_layout);
        waringFrameLayout = root.findViewById(R.id.warning_layout);
        currentSpeedTextView = root.findViewById(R.id.current_speed_textview);
        speedLimitationTextView = root.findViewById(R.id.speed_limitation_textview);
        roadNameTextView = root.findViewById(R.id.road_name_textView);
        roadSignTextView = root.findViewById(R.id.road_sign_textview);
        roadStructure = root.findViewById(R.id.road_structure);
        wi = new WeatherInfoAsync();
        sl = new SpeedLimitationAsync();

        currentSpeedLimit = DEFAULT_SPEEDLIMIT;
        currentRoadName = DEFAULT_ROAD_INFO;
        recordViewModel = ViewModelProviders.of(requireActivity()).get(RecordViewModel.class);
        //request permission
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Location Permission not granted
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_APP_PERMISSION);
        }
        try {
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        voiceNotify = new TextToSpeech(requireActivity(), this);
        //set up text to speech engine
        try {
            Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            this.startActivityForResult(intent, MY_DATA_CHECK_CODE);
        } catch (ActivityNotFoundException e
        ) {
            Toast.makeText(getContext(), "Your device is not support text to voice!", Toast.LENGTH_SHORT).show();
        }

        this.updateSpeed(null);
        //checking permission
        //Google map access
        mapView = root.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        //show warning
        startTrainingButton.setOnClickListener(this);
        stopTrainingButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.update_notification, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // if gps service not enabled
        TextView notEnabledTextView = getView().findViewById(R.id.service_not_available_textView);
        this.googleMap = googleMap;
        try {
            this.googleMap.setMyLocationEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //if location service is enabled
        if (this.googleMap.isMyLocationEnabled()) {
            try {
                notEnabledTextView.setVisibility(View.INVISIBLE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 8, this);
                myLastLocation = locationManager.getLastKnownLocation(locationProvider.getName());
                LatLng latLng = new LatLng(myLastLocation.getAltitude(), myLastLocation.getLongitude());
                wi = new WeatherInfoAsync();
                wi.execute(myLastLocation.getLatitude(), myLastLocation.getLongitude());
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            } catch (SecurityException | NullPointerException e) {
                e.printStackTrace();
            }
        } else {// if not
            LatLng geelong = new LatLng(-38.149227, 144.355384);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geelong, 12));

            notEnabledTextView.setVisibility(View.VISIBLE);
            serviceableTextView.setText("   You haven't enable gps, the service is not available");
            serviceableTextView.setBackgroundColor(Color.RED);
            startTrainingButton.setEnabled(false);
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setTrafficEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        // after location changed, update information
        if (homeLayout.getVisibility() == View.VISIBLE) {
            try {
                myLastLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (trainingCardView.getVisibility() == View.VISIBLE) {
            DrivingSpeed myLocation = new DrivingSpeed(location);
            updateSpeed(myLocation);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getContext(), "GPS is not enabled. Please turn it on.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //check permission request result
        if (requestCode == 1) {
            MapView mapView = getView().findViewById(R.id.map_view);
            mapView.onResume();
            mapView.getMapAsync(this);
            try {
                locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
                locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
                myLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), Integer.toString(requestCode), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        //do something if a button clicked
        switch (v.getId()) {
            case R.id.start_training:
                homeLayout.setVisibility(View.INVISIBLE);
                waringFrameLayout.setVisibility(View.VISIBLE);
                wi.cancel(true);
                speedRecords = new ArrayList<Double>();
                speedLimitRecords = new ArrayList<Integer>();
                locationRecords = new ArrayList<LocationPoint>();
                startTime = new Date();
                Handler handler = new Handler();
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                waringFrameLayout.setVisibility(View.INVISIBLE);
                                trainingCardView.setVisibility(View.VISIBLE);
                                speedLimitationTextView.setText(currentSpeedLimit);
                                roadSignTextView.setVisibility(View.INVISIBLE);
                                repeatAsynchronousTask();
                            }
                        }, 3000
                );

                break;
            case R.id.stop_button:
                homeLayout.setVisibility(View.VISIBLE);
                trainingCardView.setVisibility(View.INVISIBLE);
                endTime = new Date();
                Record record = new Record(locationRecords, startTime, endTime, speedRecords, speedLimitRecords);
                recordViewModel.insert(record);
                sl.cancel(true);
                timerSpeedLimit.cancel();
                timerTaskSpeedLimit.cancel();
                timerAddRecord.cancel();
                timerTaskAddRecord.cancel();
                timerTaskOverSpeeding.cancel();
                timerOverSpeeding.cancel();
                timerTaskSpeech.cancel();
                timerSpeech.cancel();
                timerTaskSchoolZone.cancel();
                timerSchoolZone.cancel();
                timerAccidentProneArea.cancel();
                timerTaskAccidentProneArea.cancel();
                voiceNotify.shutdown();
                this.onMapReady(googleMap);
                Toast.makeText(getContext(),"Record is stored, you can have a look in Summary", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (voiceNotify.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE) {
                voiceNotify.setLanguage(Locale.ENGLISH);
            }
        }else if (status == TextToSpeech.ERROR) {
                Toast.makeText(getContext(), "Sorry, the voice notification is not working", Toast.LENGTH_LONG).show();
            }
    }

    //Async task for getting weather info
    private class WeatherInfoAsync extends AsyncTask<Double, Void, String> {
        @Override
        protected void onPreExecute() {

            loadingDialog.startLoadingDialog();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Double... params) {
            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.getWeatherInfoString(params[0], params[1]);
            return weatherInfo.getWeatherInfo();
        }

        @Override
        protected void onPostExecute(String result) {
            setWeatherImageView(weatherIconImageView, welcomeTextView, result);
            if (greatGeelong.contains(position)) {
                serviceableTextView.setText("    Service is available in this area");
                serviceableTextView.setBackgroundColor(Color.GREEN);
                startTrainingButton.setEnabled(true);
            } else {

                serviceableTextView.setText("    You are out of service area");
                serviceableTextView.setBackgroundColor(Color.RED);
                startTrainingButton.setEnabled(false);
            }
            loadingDialog.dismissDialog();

        }
    }

    //Async task for checking is school zone
    private class IsSchoolZoneAsync extends AsyncTask<Double, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Double... doubles) {
            SchoolZoneInformation schoolZoneInformation = new SchoolZoneInformation();
            schoolZoneInformation.getIsSchoolZoneInfo(doubles[0], doubles[1]);

            return schoolZoneInformation.getIsSchoolZone();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean && !voiceMessages.contains(SCHOOL_ZONE)) {
                voiceMessages.add(SCHOOL_ZONE);
                roadSignTextView.setText("School Zone\nAhead");
                roadSignTextView.setVisibility(View.VISIBLE);

            }
        }
    }
    //Async task to check if accident prone area.
    private class IsAccidentProneAreaAsync extends AsyncTask<Double, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Double... doubles) {
            AccidentProneAreaInfo accidentProneAreaInfo = new AccidentProneAreaInfo();
            accidentProneAreaInfo.isAccidentProneArea(doubles[0], doubles[1]);
            return accidentProneAreaInfo.getAccidentProneArea();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean && !voiceMessages.contains(ACCIDENT_PRONE_AREA)) {
                voiceMessages.add(ACCIDENT_PRONE_AREA);
                roadSignTextView.setText("Accident\nProne Area");
                roadSignTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    //Async task for getting speed limitation
    private class SpeedLimitationAsync extends AsyncTask<ArrayList<Double>, Void, String> {
        @SafeVarargs
        @Override
        protected final String doInBackground(ArrayList<Double>... params) {

            return SpeedLimitation.getSpeedLimitation(params[0].get(0), params[0].get(1), params[0].get(2), params[0].get(3));
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("Too many requests")) {
                speedLimitationTextView.setText(currentRoadName);
                roadNameTextView.setText(currentRoadName);
            } else {
                ArrayList<Node> nodes;
                try {
                    nodes = parseJsonToNodes(s);
                    if (nodes.size() == 0) {
                        currentSpeedLimit = DEFAULT_SPEEDLIMIT;
                        currentRoadName = DEFAULT_ROAD_INFO;
                        speedLimitationTextView.setText(currentSpeedLimit);
                        roadNameTextView.setText(currentRoadName);
                    } else {
                        // set up all views in training by information from api
                        currentSpeedLimit = Integer.toString(nodes.get(0).getSpeedLimit());
                        currentRoadName = nodes.get(0).getRoadName();
                        speedLimitationTextView.setText(currentSpeedLimit);
                        roadNameTextView.setText(currentRoadName);

                        switch (nodes.get(0).getRoadCategory()){
                            case "motorway":
                                roadStructure.setImageResource(R.drawable.ic_metro);
                                break;
                            case "trunk":
                                roadStructure.setImageResource(R.drawable.ic_trunk);
                                break;
                            case "primary":
                                roadStructure.setImageResource(R.drawable.ic_primary);
                                break;
                            case "secondary":
                                roadStructure.setImageResource(R.drawable.ic_secondary);
                                break;
                            case "unclassified":
                                roadStructure.setImageResource(R.drawable.ic_undifined);
                                break;
                            case "residential":
                                roadStructure.setImageResource(R.drawable.ic_residential);
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setWeatherImageView(ImageView imageView, TextView textView, String weatherString) {
        Long tsLong = System.currentTimeMillis() / 1000;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(weatherString);
            weatherMain = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            String weatherDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            position = jsonObject.getString("name");
            sunRiseTime = jsonObject.getJSONObject("sys").getLong("sunrise");
            sunsetTime = jsonObject.getJSONObject("sys").getLong("sunset");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //set weather icon and welcome text.
        String welcomeText = "Welcome\n";
        if (weatherMain.length() > 0) {
            switch (weatherMain) {
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
                    if (tsLong > sunRiseTime && tsLong < sunsetTime)
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
                    if (tsLong > sunRiseTime && tsLong < sunsetTime)
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
                    if (tsLong > sunRiseTime && tsLong < sunsetTime)
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
            textView.setText(welcomeText + "\n" + position);
        }
    }

    private void updateSpeed(DrivingSpeed location) {
        // TODO Auto-generated method stub
        currentSpeed = 0;
        if (location != null) {
            currentSpeed = location.getSpeed();
        }
        String strCurrentSpeed = Integer.toString((int) currentSpeed);
        String strUnits = "km/h";
        currentSpeedTextView.setText(strCurrentSpeed + " " + strUnits);
    }

    /**
     * Convert location point to a large point with boundary.
     * @param location a Location class with longitude and latitude information
     * @return a large point with four side boundaries.
     */
    private ArrayList<Double> pointToBox(Location location) {
        ArrayList<Double> boxResult = new ArrayList<Double>();
        double changes = 10 / 1.11 * 0.00001 / 2;
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        boxResult.add(lat - changes); //b
        boxResult.add(lon - changes); //l
        boxResult.add(lat + changes); //t
        boxResult.add(lon + changes); //r
        return boxResult;
    }

    /**
     * Convert json string to a Node class Array
     * @param jsonString a string contains Nodes information
     * @return An array of nodes
     * @throws JSONException json exception
     */
    private ArrayList<Node> parseJsonToNodes(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        ArrayList<Node> nodes = new ArrayList<>();
        if (jsonObject.length() > 0) {
            int elementLength = jsonObject.getJSONArray("elements").length();
            if (elementLength == 0) {
                return nodes;
            } else {
                for (int i = 0; i < elementLength; i++) {
                    JSONObject nodeJson = jsonObject.getJSONArray("elements").getJSONObject(i).getJSONObject("tags");
                    Node node = new Node(nodeJson.getString("highway"), nodeJson.getInt("maxspeed"), nodeJson.getString("name"), nodeJson.getString("surface"));
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }

    private void repeatAsynchronousTask() {
        //periodic checking speed limit
        //periodic adding record info
        //periodic if any voice string need to be displayed, if true display then wait 3s
        //periodic if school zone, 20s
        //periodic if over speeding, every second, if true wait 5s
        //periodic if accident prone area, every 3s, if true wait 10s
        final Handler handlerSpeedLimit = new Handler();
        final Handler handlerAddRecord = new Handler();
        final Handler handlerSpeech = new Handler();
        final Handler handleSchoolZone = new Handler();
        final Handler handlerOverSpeeding = new Handler();
        final Handler handlerAccidentProneArea = new Handler();
        int speechPeriod = 2000;
        int overSpeedingPeriod = 1000;
        int accidentProneAreaPeriod = 10000;
        int schoolZonePeriod = 20000;
        timerSpeedLimit = new Timer();
        timerAddRecord = new Timer();
        timerSpeech = new Timer();
        timerOverSpeeding = new Timer();
        timerSchoolZone = new Timer();
        timerAccidentProneArea = new Timer();
        timerTaskSpeedLimit = new TimerTask() {
            @Override
            public void run() {
                handlerSpeedLimit.post(new Runnable() {
                    public void run() {
                        try {
                            Location location = locationManager.getLastKnownLocation(locationProvider.getName());
                            ArrayList<Double> box = pointToBox(location);
                            sl = new SpeedLimitationAsync();
                            // PerformBackgroundTask this class is the class that extends AsyncTask
                            sl.execute(box);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timerSpeedLimit.schedule(timerTaskSpeedLimit, 0, 8000); //execute in every 5000 ms
        timerTaskAddRecord = new TimerTask() {
            @Override
            public void run() {
                handlerAddRecord.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Location location = locationManager.getLastKnownLocation(locationProvider.getName());
                            // PerformBackgroundTask this class is the class that extends AsyncTask
                            speedRecords.add(currentSpeed);
                            speedLimitRecords.add(Integer.parseInt(currentSpeedLimit));
                            locationRecords.add(new LocationPoint(location.getLongitude(), location.getLatitude(), currentRoadName));
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timerAddRecord.schedule(timerTaskAddRecord, 0, 3000);
        timerTaskSchoolZone = new TimerTask() {
            @Override
            public void run() {
                handleSchoolZone.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Location location = locationManager.getLastKnownLocation(locationProvider.getName());
                            IsSchoolZoneAsync isSchoolZoneAsync = new IsSchoolZoneAsync();
                            isSchoolZoneAsync.execute(location.getLatitude(), location.getLongitude());
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timerSchoolZone.schedule(timerTaskSchoolZone, 0, 20000);
        timerTaskSpeech = new TimerTask() {
            @Override
            public void run() {
                handlerSpeech.post(new Runnable() {
                                       @Override
                                       public void run() {
                                           if (voiceMessages.size() > 0) {
                                               voiceNotify.speak(voiceMessages.get(0), TextToSpeech.LANG_COUNTRY_AVAILABLE, null);
                                               voiceMessages.remove(0);
                                               if(roadSignTextView.getVisibility() == View.VISIBLE){
                                                   roadSignTextView.setVisibility(View.INVISIBLE);
                                               }
                                           }
                                       }
                                   }
                );
            }
        };
        timerSpeech.schedule(timerTaskSpeech, 0, 4000);
        timerTaskOverSpeeding = new TimerTask() {
            @Override
            public void run() {
                handlerOverSpeeding.post(new Runnable() {
                    @Override
                    public void run() {
                        String newVoiceMessage = OVER_SPEEDING + "Please drive within " + currentSpeedLimit + " Kilometers Per Hour";
                        if (currentSpeed > Double.parseDouble(currentSpeedLimit)
                                && !voiceMessages.contains(newVoiceMessage)) {
                            voiceMessages.add(newVoiceMessage);
                        }

                    }
                });
            }
        };
        timerOverSpeeding.schedule(timerTaskOverSpeeding, 0, 7000);
        timerTaskAccidentProneArea = new TimerTask() {
            @Override
            public void run() {
                handlerAccidentProneArea.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Location location = locationManager.getLastKnownLocation(locationProvider.getName());
                            IsAccidentProneAreaAsync isAccidentProneAreaAsync = new IsAccidentProneAreaAsync();
                            isAccidentProneAreaAsync.execute(location.getLatitude(), location.getLongitude());
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timerAccidentProneArea.schedule(timerTaskAccidentProneArea, 0, 20000);
    }
}
