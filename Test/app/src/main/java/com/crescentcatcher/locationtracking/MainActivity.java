package com.crescentcatcher.locationtracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import com.brouding.blockbutton.BlockButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.location.LocationServices;
import com.kyleduo.switchbutton.SwitchButton;


import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;


public class MainActivity extends Activity implements
        LocationListener,
        OnMapReadyCallback{
    private final int[] MAP_TYPES = {GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private int curMapTypeIndex = 1;


    private static final String TAG = "MainActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minutes
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private static final long INTERVAL_SMALL = 1000 * 1 * 1; //1 sec

    private String mLastUpdateTime;
    private String city = "";
    private String country = "";
    private String area = "";
    private String title;
    private String requiredArea = "";
    private GoogleMap googleMap;
    private LocationRequest mLocationRequest;
    private List<Address> addresses;

    LocationDBHelper locDBhelper;

    public Context context;

    private GoogleLocationService googleLocationService;

    private ArrayList<LatLng> points; //added
    Polyline line; //added

    SwitchButton toggelGPS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        points = new ArrayList<LatLng>(); //added
        if (!isGooglePlayServicesAvailable()) {

            Toast.makeText(MainActivity.this, "Google Play Services is not available", Toast.LENGTH_LONG).show();

            finish();
        }
        createLocationRequest();

        setContentView(R.layout.activity_main);
        BlockButton btnSaveRoute = (BlockButton) findViewById(R.id.btn_save_route);
        btnSaveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btnShowProgressDialog
            }
        });

        this.toggelGPS = (SwitchButton) findViewById(R.id.sb_gps);
        this.toggelGPS.setChecked(true);
        this.toggelGPS.setEnabled(true);
        this.toggelGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(toggelGPS.isChecked()){

                }
            }
        });

        this.context = MainActivity.this;
        try {
            // Loading map
           initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initilizeMap() {
        String ret = readFromFile(context);

        if (googleMap == null) {
            MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map));
            mapFragment.getMapAsync(this);

            this.locDBhelper = new LocationDBHelper(this.context);
            createLocationRequest();
            startService(new Intent(context, LocationService.class));

            googleLocationService = new GoogleLocationService(context, new LocationUpdateListener() {
                @Override
                public void canReceiveLocationUpdates() {
                }

                @Override
                public void cannotReceiveLocationUpdates() {

                }

                //update location to our servers for tracking purpose
                @Override
                public void updateLocation(Location location) {
                    if (location != null ) {
                        Timber.e("updated location %1$s %2$s", location.getLatitude(), location.getLongitude());

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                new LatLng(location.getLatitude(), location.getLongitude())).zoom(20).build();

                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        points.add(latLng);
                        Polyline route = googleMap.addPolyline(new PolylineOptions()
                                .width(10)
                                .color(R.color.colorMapLine)
                                .geodesic(false)
                                .zIndex(200));
                        route.setPoints(points);

                        writeToFile("Lati: " + location.getLatitude() + " , Longi: " + location.getLongitude(), context);

                        Log.d("Location", "Lati: " + location.getLatitude() + " , Longi: " + location.getLongitude());
                        Toast.makeText(MainActivity.this, "Lati: " + location.getLatitude() + " , Longi: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void updateLocationName(String localityName, Location location) {

                    googleLocationService.stopLocationUpdates();
                }
            });
            googleLocationService.startUpdates();


            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }


    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(context, LocationService.class));
        googleLocationService.stopUpdates();
    }

    private void redrawLine(){

        googleMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(10).color(R.color.colorMapLine).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        line = googleMap.addPolyline(options); //add Polyline
    }
    @Override
    public void onLocationChanged(Location location) {
        CameraPosition cameraPosition = new CameraPosition.
                Builder()
                .target(new LatLng(location.getLatitude(),
                        location.getLongitude()))
                        .zoom(6)
                        .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            this.googleMap = googleMap;
            this.googleMap.setMapType(MAP_TYPES[curMapTypeIndex]);
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.setTrafficEnabled(true);
            this.googleMap.setIndoorEnabled(true);
            this.googleMap.setBuildingsEnabled(true);
            this.googleMap.getUiSettings().setZoomControlsEnabled(true);
            this.googleMap.getUiSettings().setRotateGesturesEnabled(true);

        } catch (SecurityException ex) {
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
//    <------------------------>
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            Toast.makeText(getApplicationContext(), "Google Play Services is not Available", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
