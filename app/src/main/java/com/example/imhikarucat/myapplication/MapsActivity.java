package com.example.imhikarucat.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String STUDENT_API = "https://clinicandroidasn2.herokuapp.com/clinics";
    private static final int SET_INTERVAL = 100000;
    private static final int FASTEST_INTERVAL = 2000; //milliseconds

    private String jsonString = "";

    private static final int MY_PERMISSION_REQUEST_LOCATION = 6969;
    private static final  String TAG = "";
    private static final int MY_LOCATION_REQUEST = 99;
    private GoogleMap mMap;
    private FusedLocationProviderClient locationClient;
    private LocationRequest locationRequest;
    private ArrayList<Clinic> clinicArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapsInitializer.initialize(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        requestPermission();

        locationClient = LocationServices.
                getFusedLocationProviderClient(this);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(MapsActivity.this,
                        AddClinicActivity.class);
                intent.putExtra("latitude", latLng.latitude);
                intent.putExtra("longitude", latLng.longitude);
                startActivity(intent);
            }
        });
        startLocationUpdate();
    }

    @SuppressLint({"MissingPermission", "RestrictedApi"})
    private void startLocationUpdate() {
//        locationRequest = new LocationRequest();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(SET_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationClient.requestLocationUpdates(locationRequest,
                new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Location location = locationResult.getLastLocation();
                        if (location != null){
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker()));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                            //  mMap.clear();
//                        mMap.addMarker(new MarkerOptions().position(latLng)
//                                        .icon(BitmapDescriptorFactory.defaultMarker()));
                            Toast.makeText(MapsActivity.this,
                                    "(" + location.getLatitude() + ","+
                                            location.getLongitude() +")",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MapsActivity.this, "Please wait for your location service to start", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                ,null);

    }

    @SuppressLint({"MissingPermission", "RestrictedApi"})
    public void onGetPositionClick(View view) {

        locationClient.getLastLocation().
                addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null){
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker()));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                            Toast.makeText(MapsActivity.this,
                                    "(" + location.getLatitude() + ","+
                                            location.getLongitude() +")",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MapsActivity.this, "Please wait for your location service to start", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSION_REQUEST_LOCATION);
    }


    public void onViewAll(View view) {
        Intent intent = new Intent(MapsActivity.this, ClinicListingView.class);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mMap != null){
            mMap.clear();
            Double lat, lon;
            Intent intent = getIntent();
            lat = intent.getDoubleExtra("viewLat",0);
            lon = intent.getDoubleExtra("viewLon",0);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 16.0f));
        }
        new getClinic().execute();
    }

    public class getClinic extends AsyncTask<Void,Void,Void>{
        String jsonString="";
        @Override
        protected Void doInBackground(Void... voids) {
            jsonString = HttpHandler.getRequest(STUDENT_API);
            Log.d(TAG, "doInBackground: " + jsonString);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayList<Clinic> clinics = new ArrayList<Clinic>();
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    LatLng position = new LatLng(
                            jsonObject.getDouble("latitute"),
                            jsonObject.getDouble("longitute")
                    );
                    mMap.addMarker(new MarkerOptions().position(position)
                            .icon(BitmapDescriptorFactory.fromResource(
                                    R.drawable.ic_clinic
                            ))
                            .title("Clinic")
                            .snippet(jsonObject.getString("name")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
