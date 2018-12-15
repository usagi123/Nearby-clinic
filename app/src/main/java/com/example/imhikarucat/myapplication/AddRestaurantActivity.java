package com.example.imhikarucat.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddRestaurantActivity extends AppCompatActivity {

    private String studentName = "";
    private String status = "";
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        restaurant = new Restaurant();
        Intent intent = getIntent();
        restaurant.latitute = intent.getDoubleExtra("latitute", 0);
        restaurant.longitute = intent.getDoubleExtra("longitute", 0);

        EditText resLat = findViewById(R.id.resLat);
        resLat.setText(restaurant.latitute+"");

        EditText resLong = findViewById(R.id.resLong);
        resLong.setText(restaurant.longitute+"");
    }

    public void onConfirmAddRestaurant(View view) {
        EditText resName = findViewById(R.id.resName);
        restaurant.name = resName.getText().toString();

        EditText resRating = findViewById(R.id.resRating);
        restaurant.rating = Integer.parseInt(resRating.getText().toString());


        new PostRestaurant().execute();

    }

    private class PostRestaurant extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            status = HttpHandler.postRequest(MapsActivity.CLINICS_API, restaurant);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(AddRestaurantActivity.this, status, Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
            Intent intent = new Intent(AddRestaurantActivity.this, MapsActivity.class);
            setResult(101, intent);
            finish();
        }
    }
}
