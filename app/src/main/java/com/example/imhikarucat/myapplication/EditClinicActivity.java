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

public class EditClinicActivity extends AppCompatActivity {

    private Geocoder geocoder;
    private Clinic clinic;
    private List<Address> addresses;
    public String address;
    private EditText editName, editAddress, editRating, editImpression, editLead, editSpecialization, editAveragePrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clinic);
        Intent intent = getIntent();

        editName = findViewById(R.id.clinicName);
        editAddress = findViewById(R.id.clinicAddress);
        editRating = findViewById(R.id.clinicRating);
        editImpression = findViewById(R.id.clinicImpression);
        editLead = findViewById(R.id.clinicLeadPhysician);
        editSpecialization = findViewById(R.id.clinicSpecialization);
        editAveragePrice = findViewById(R.id.clinicPrice);
//        editAddress.setText(address);
        editAddress.setEnabled(false);

        clinic = new Clinic();
        String name = intent.getExtras().get("editName").toString();
        String id = intent.getExtras().get("editId").toString();
        Integer rating = intent.getIntExtra("editRating",0);
        Double lat = intent.getDoubleExtra("editLat",0);
        Double lon = intent.getDoubleExtra("editLon",0);
        String impression = intent.getExtras().get("editImpression").toString();
        String lead = intent.getExtras().get("editLead").toString();
        String specialzation = intent.getExtras().get("editSpecial").toString();
        Integer avgPrice = intent.getIntExtra("editAvg",0);

        geocoder = new Geocoder(this,Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat,lon,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = addresses.get(0).getAddressLine(0);

        editName.setText(name);
        editAddress.setText(address);
        editRating.setText(rating.toString());
        editImpression.setText(impression);
        editLead.setText(lead);
        editSpecialization.setText(specialzation);
        editAveragePrice.setText(avgPrice.toString());

        clinic.latitude = lat;
        clinic.longitude = lon;
        clinic.id = id;
    }

    public void onConfirmEditClinic(View view) {
        clinic.name = editName.getText().toString();
        clinic.rating = Integer.parseInt(editRating.getText().toString());
        clinic.impression = editImpression.getText().toString();
        clinic.avg_price = Integer.parseInt(editAveragePrice.getText().toString());
        clinic.lead_phys = editLead.getText().toString();
        clinic.specialization = editSpecialization.getText().toString();
        new PutClinic().execute();
    }

    private class PutClinic extends AsyncTask<Void,Void,Void> {
        private String status = "";
        @Override
        protected Void doInBackground(Void... voids) {
            status = HttpHandler.editRequest(MapsActivity.STUDENT_API + "/" + clinic.id, clinic);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(EditClinicActivity.this, status, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditClinicActivity.this,
                    MapsActivity.class);
            intent.putExtra("edit_Id",clinic.id);
            intent.putExtra("editName",clinic.name);
            intent.putExtra("editRating",clinic.rating);
            intent.putExtra("editLat",clinic.latitude);
            intent.putExtra("editLon",clinic.longitude);
            intent.putExtra("editImpression",clinic.impression);
            intent.putExtra("editLead",clinic.lead_phys);
            intent.putExtra("editSpecial",clinic.specialization);
            intent.putExtra("editAvgPrice",clinic.avg_price);
            setResult(101,intent);
            finish();
        }
    }
}
