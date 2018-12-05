package com.example.imhikarucat.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClinicListingView extends AppCompatActivity {

    private String jsonString;
    private static final  String TAG = "";
    private ListView listView;
    public ArrayList<Clinic> clinics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_listing_view);

        listView = findViewById(R.id.ListView);
        clinics = new ArrayList<Clinic>();
        new getClinic().execute();
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
                    String address = jsonObject.getString("address");
                    int rating = jsonObject.getInt("rating");
                    Double lat = jsonObject.getDouble("latitute");
                    Double lon = jsonObject.getDouble("longitute");
                    String impresstion = jsonObject.getString("impression");
                    String lead = jsonObject.getString("lead_physician");
                    String specialization = jsonObject.getString("specialization");
                    int averagePrice = jsonObject.getInt("average_price");
                    Log.d(TAG, "onPostExecute: " + jsonArray.toString());

                    Clinic clinic = new Clinic();
                    clinic.id = id;
                    clinic.name = name;
                    clinic.address = address;
                    clinic.rating = rating;
                    clinic.latitude = lat;
                    clinic.longitude = lon;
                    clinic.impression = impresstion;
                    clinic.lead_phys = lead;
                    clinic.specialization = specialization;
                    clinic.avg_price = averagePrice;
                    clinics.add(clinic);

                    CustomListView customListView = new CustomListView(ClinicListingView.this,clinics);
                    listView.setAdapter(customListView);
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            ShowUpMenuActivity showUpMenuActivity = new ShowUpMenuActivity(ClinicListingView.this,position);
                            showUpMenuActivity.showPopup(view);
                            return true;
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public class ShowUpMenuActivity implements PopupMenu.OnMenuItemClickListener {

            private Activity context;
            private Integer excerNum;

            ShowUpMenuActivity(Activity context, Integer excerNum) {
                this.context = context;
                this.excerNum = excerNum;
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
//                        deleteExcercise(excerNum);
                        Toast.makeText(context,"item has been deleted",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.edit:
//                        editExcercise(excerNum);
                        break;
                    case R.id.map:
                        Toast.makeText(context,"Map",Toast.LENGTH_LONG).show();
                        //send lat long data back to MapsActivity
                        //create a func in MapsActivity to move camera to designated location
                        break;

                }
                return false;
            }
        }
    }
}
