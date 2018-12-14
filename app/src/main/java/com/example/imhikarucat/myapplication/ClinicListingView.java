package com.example.imhikarucat.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClinicListingView extends AppCompatActivity {

    private static final  String TAG = "";
    private ListView listView;
    private ArrayList<Clinic> clinics;
    private ArrayList<Clinic> sorting;
    EditText searchbar;
    String filterKey;
    String returnedFiltered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_listing_view);
        filterKey = "";
        returnedFiltered = "";
        listView = findViewById(R.id.ListView);
        clinics = new ArrayList<Clinic>();
        sorting = new ArrayList<>();
//        searchbar = findViewById(R.id.searchSpecialization);

    }

    @Override
    protected void onResume(){
        super.onResume();
//        clinics.clear();
//        listView.invalidateViews();
//        new getClinic().execute();
//        Log.d(TAG, "onResume: " + returnedFiltered);
//        if(filterKey.equals("filting")){
//            sort(clinics,returnedFiltered);
//            listView.invalidateViews();
//        }

        if(filterKey.equals( "filting")){
            sort(clinics,returnedFiltered);
        }else{
            clinics.clear();
            listView.invalidateViews();
            new getClinic().execute();
        }
    }

    public void onSortClickButton(View view) {
//        String result = searchbar.getText().toString();
//        sort(clinics, result);
        Intent intent = new Intent(ClinicListingView.this, SortActivity.class);
        startActivityForResult(intent,1);
    }

    private class getClinic extends AsyncTask<Void,Void,Void> {
        String jsonString="";
        @Override
        protected Void doInBackground(Void... voids) {
            jsonString = HttpHandler.getRequest(MapsActivity.STUDENT_API);
            Log.d(TAG, "doInBackground: " + jsonString);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final ArrayList<String> names = new ArrayList<String>();
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("_id");
                    String name = jsonObject.getString("name");
//                    String address = jsonObject.getString("address");
                    int rating = jsonObject.getInt("rating");
                    final Double lat = jsonObject.getDouble("latitute");
                    final Double lon = jsonObject.getDouble("longitute");
                    String impresstion = jsonObject.getString("impression");
                    String lead = jsonObject.getString("lead_physician");
                    String specialization = jsonObject.getString("specialization");
                    int averagePrice = jsonObject.getInt("average_price");

                    Clinic clinic = new Clinic();
                    clinic.id = id;
                    clinic.name = name;
//                    clinic.address = address;
                    clinic.rating = rating;
                    clinic.latitude = lat;
                    clinic.longitude = lon;
                    clinic.impression = impresstion;
                    clinic.lead_phys = lead;
                    clinic.specialization = specialization;
                    clinic.avg_price = averagePrice;
                    clinics.add(clinic);
                    CustomListView customListView = new CustomListView(ClinicListingView.this,clinics);
                    customListView.notifyDataSetChanged();
                    listView.setAdapter(customListView);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showUpMenuActivity showUpMenuActivity = new showUpMenuActivity(ClinicListingView.this, position);
                            showUpMenuActivity.showPopup(view);
                        }
//                        @Override
//                        public boolean onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            showUpMenuActivity showUpMenuActivity = new showUpMenuActivity(ClinicListingView.this, position);
//                            showUpMenuActivity.showPopup(view);
//                            return true;
//                        }


                    });
                    Log.d(TAG, "onPostExecute: " + clinics.get(0).name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public class showUpMenuActivity implements PopupMenu.OnMenuItemClickListener {

            private Activity context;
            private Integer clinicID;

            showUpMenuActivity(Activity context, Integer clinicID) {
                this.context = context;
                this.clinicID = clinicID;
            }

            void showPopup(View v){
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.inflate(R.menu.popup_layout);
                popupMenu.show();
            }

            @Override public boolean onMenuItemClick(MenuItem item){

                switch (item.getItemId()){
                    case R.id.delete:
                        deleteClinic(clinicID);
                        clinics.clear();
                        listView.invalidateViews();
                        new getClinic().execute();
                        Toast.makeText(context,"item has been deleted",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.edit:
                        editClinic(clinicID);
                        break;
                    case R.id.view:
                        Intent intent = new Intent(ClinicListingView.this, MapsActivity.class);
                        intent.putExtra("intentType", "viewMap");
                        intent.putExtra("viewLat", clinics.get(clinicID).latitude);
                        intent.putExtra("viewLon", clinics.get(clinicID).longitude);
                        startActivity(intent);
                        Toast.makeText(context,"View clinic",Toast.LENGTH_LONG).show();
                        //send lat long data back to MapsActivity
                        //create a func in MapsActivity to move camera to designated location
                        break;
                }
                return false;
            }
        }
    }

    public void editClinic(int id){
        Intent intent = new Intent(this,EditClinicActivity.class);
        intent.putExtra("editName",clinics.get(id).name);
        intent.putExtra("editId",clinics.get(id).id);
        intent.putExtra("editRating",clinics.get(id).rating);
        intent.putExtra("editLat",clinics.get(id).latitude);
        intent.putExtra("editLon",clinics.get(id).longitude);
        intent.putExtra("editImpression",clinics.get(id).impression);
        intent.putExtra("editLead",clinics.get(id).lead_phys);
        intent.putExtra("editSpecial",clinics.get(id).specialization);
        intent.putExtra("editAvg",clinics.get(id).avg_price);
        intent.putExtra("requestType","edit");
        startActivityForResult(intent,1);
    }

    private class deleteClinic extends AsyncTask<Void,Void,Void> {
        String jsonString = "";
        String clinicID = "";

        @Override
        protected Void doInBackground(Void... voids) {
            jsonString = HttpHandler.deleteRequest(MapsActivity.STUDENT_API + "/" + clinicID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final ArrayList<String> names = new ArrayList<String>();
            super.onPostExecute(aVoid);
        }
    }

    public void deleteClinic (int id) {
        deleteClinic deleteClinic = new deleteClinic();
        deleteClinic.clinicID = clinics.get(id).id;
        deleteClinic.execute();
    }

    public void sort(ArrayList<Clinic> clinicArrayList, String keyword){
        sorting = new ArrayList<Clinic>();
        if (clinics.size() < 0) {
            Toast.makeText(this, "Please wait for me to fetch the clinics listing details", Toast.LENGTH_SHORT).show();
        } else {
            for (Clinic clinic : clinics) {
                if (clinic.specialization.matches(keyword) == true) {
                    sorting.add(clinic);
                } else if (clinic.specialization.matches(keyword) != true) {
                    Toast.makeText(this, "Searching complete. There are " + sorting.size() + " clinics that suit the keyword", Toast.LENGTH_SHORT).show();
                } else if (sorting.size() < 0) {
                    Toast.makeText(this, "Please wait for me to fetch the clinics listing details", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "There is no suitable keyword", Toast.LENGTH_SHORT).show();
                }
            }
            CustomListView customListView = new CustomListView(ClinicListingView.this,sorting);
            customListView.notifyDataSetChanged();
            listView.setAdapter(customListView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 101){
            if(requestCode == 1){
                filterKey = data.getStringExtra("intentKey");
                Log.d(TAG, "onActivityResult: "  + filterKey);
                returnedFiltered = data.getStringExtra("filterKey");
//                sort(clinics,returnedFiltered);
            }
        }
    }
}
