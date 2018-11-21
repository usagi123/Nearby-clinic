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

    //https://my-json-server.typicode.com/cristalngo/demo/students
    public static final String STUDENT_API = "http://10.0.2.2:8000/restaurants";
    public static final String STUDENT_API_INDI = "https://vu-nodejs-backend-webprog-a2.herokuapp.com/student";

    private String jsonString = "";


    private static final int MY_PERMISSION_REQUEST_LOCATION = 6969;
    private static final long UPDATE_INTERVAL = 10*1000;
    private static final long FASTEST_INTERVAL = 2000;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        client = LocationServices.getFusedLocationProviderClient(MapsActivity.this);


        //Change map view
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Draw a bitmap on canvas with paint
        Bitmap bitmap = Bitmap.createBitmap(200,50,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0,200, 50, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        canvas.drawText("RMIT VN", 30, 40, paint);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng rmitVN = new LatLng(10.7294553,106.6918624);
//        mMap.addMarker(new MarkerOptions().position(rmitVN).title("Marker in RMIT Vietnam").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_uni)));
//        mMap.addMarker(new MarkerOptions().position(rmitVN).title("Marker in RMIT Vietnam").icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rmitVN, 14));
//        drawCircle(rmitVN);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(MapsActivity.this, AddRestaurantActivity.class);
                intent.putExtra("latitute", latLng.latitude);
                intent.putExtra("longitute", latLng.longitude);
                startActivity(intent);
            }
        });
        startLocationUpdate();
    }

    private void onLocationChanged(Location location) {
        String msg = "Update location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint({"RestrictedApi", "MissingPermission"})
    private void startLocationUpdate() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        client.requestLocationUpdates(mLocationRequest, new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onLocationChanged(locationResult.getLastLocation());
                Location location = locationResult.getLastLocation();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(latLng));
                Toast.makeText(MapsActivity.this,
                        "(" + location.getLatitude() + ", " + location.getLongitude() + ")",
                        Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    private void drawCircle(LatLng latLng){
        CircleOptions circleOptions = new CircleOptions().center(latLng).radius(1000).fillColor(Color.TRANSPARENT).strokeColor(Color.RED);
        mMap.addCircle(circleOptions);
    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
    }

    @SuppressLint("MissingPermission")
    public void getPosition(View view) {
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));

                Toast.makeText(MapsActivity.this,
                        "(" + location.getLatitude() + ", " + location.getLongitude() + ")",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
//        mMap.clear();
        new getRestaurant().execute();
    }

    private class getRestaurant extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            jsonString = HttpHandler.getRequest(STUDENT_API);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
            super.onPostExecute(aVoid);

            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    LatLng pos = new LatLng(
                            jsonObject.getDouble("latitute"),
                            jsonObject.getDouble("longitute")
                    );
                    mMap.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_restaurant))
                            .title("Restaurant")
                            .snippet(jsonObject.getString("name")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
