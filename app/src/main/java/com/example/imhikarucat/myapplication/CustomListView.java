package com.example.imhikarucat.myapplication;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomListView extends ArrayAdapter<Clinic> {

    private ArrayList<Clinic> clinics;
    private Activity context;
    public Geocoder geocoder;
    public List<Address> addresses;
    public String resultAddress;

    public CustomListView(@NonNull Activity context, ArrayList<Clinic>clinics){
        super(context, R.layout.custom_cell,clinics);
        this.context = context;
        this.clinics = clinics;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;


        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.custom_cell,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();
        }
        try{
            viewHolder.name.setText(clinics.get(position).getName());

//            try {
//                addresses = geocoder.getFromLocation(clinics.get(position).getLatitude(), clinics.get(position).getLongitude(), 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            resultAddress = addresses.get(0).getAddressLine(0);
//
            viewHolder.address.setText(clinics.get(0).getAddress());
            viewHolder.rating.setText(Integer.toString(clinics.get(position).getRating()));
            viewHolder.impression.setText(clinics.get(position).getImpression());
            viewHolder.lead.setText((clinics.get(position).getLead_phys()));
            viewHolder.specialization.setText(clinics.get(position).getSpecialization());
            viewHolder.average_price.setText(Integer.toString(clinics.get(position).getAvg_price()));
        }catch (Exception e){

        }
        return r;
    }

    class ViewHolder{
        TextView name, address, rating, impression, lead, specialization, average_price;
        ViewHolder(View v){
            name = v.findViewById(R.id.name);
            address = v.findViewById(R.id.address);
            rating = v.findViewById(R.id.rating);
            impression = v.findViewById(R.id.impression);
            lead = v.findViewById(R.id.lead);
            specialization = v.findViewById(R.id.special);
            average_price = v.findViewById(R.id.price);
        }
    }
}
