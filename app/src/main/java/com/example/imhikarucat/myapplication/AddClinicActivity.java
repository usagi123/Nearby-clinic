package com.example.imhikarucat.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddClinicActivity extends AppCompatActivity {

    private Geocoder geocoder;
    private Clinic clinic;
    private List<Address> addresses;
    public String address;
    private EditText editName, editAddress, editRating, editImpression, editLead, editSpecialization, editAveragePrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clinic);

        Intent intent = getIntent();
        clinic = new Clinic();
        clinic.latitude = intent.getDoubleExtra("latitude",0);
        clinic.longitude = intent.getDoubleExtra("longitude",0);

        geocoder = new Geocoder(this,Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(clinic.latitude,clinic.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = addresses.get(0).getAddressLine(0);

        editName = findViewById(R.id.clinicName);
        editAddress = findViewById(R.id.clinicAddress);
        editRating = findViewById(R.id.clinicRating);
        editImpression = findViewById(R.id.clinicImpression);
        editLead = findViewById(R.id.clinicLeadPhysician);
        editSpecialization = findViewById(R.id.clinicSpecialization);
        editAveragePrice = findViewById(R.id.clinicPrice);
        editAddress.setText(address);


    }

    public void onConfirmAddClinic(View view) {
        if (editName.getText().toString().equals("") || editRating.getText().toString().equals("") || editImpression.getText().toString().equals("")
                || editLead.getText().toString().equals("") || editSpecialization.getText().toString().equals("") || editAveragePrice.getText().toString().equals("")) {
            Toast.makeText(AddClinicActivity.this, "There is empty field", Toast.LENGTH_SHORT).show();
        } else {
            clinic.name = editName.getText().toString();
//        clinic.address = address;
            clinic.rating = Integer.parseInt(editRating.getText().toString());
            clinic.impression = editImpression.getText().toString();
            clinic.avg_price = Integer.parseInt(editAveragePrice.getText().toString());
            clinic.lead_phys = editLead.getText().toString();
            clinic.specialization = editSpecialization.getText().toString();
            new PostClinic().execute();

            Intent intent = new Intent(AddClinicActivity.this, MapsActivity.class);
            intent.putExtra("intentType", "newAdd");
            intent.putExtra("newAddLat", clinic.latitude);
            intent.putExtra("newAddLon", clinic.longitude);
            startActivity(intent);
        }
    }


    private class PostClinic extends AsyncTask<Void,Void,Void>{
        private String status = "";
        @Override
        protected Void doInBackground(Void... voids) {
            status = HttpHandler.postClinic(MapsActivity.STUDENT_API,clinic);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(AddClinicActivity.this, status, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddClinicActivity.this,
                    MapsActivity.class);
            setResult(101, intent);
            finish();
        }
    }
}
