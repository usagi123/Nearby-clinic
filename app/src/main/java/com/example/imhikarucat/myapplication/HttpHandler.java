package com.example.imhikarucat.myapplication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class HttpHandler {

    public static String getRequest(String urlStr){
        //urlStr = address ie: localhost:8000

        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!=null){ //while line is not empty
                builder.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static String postRequest(String urlStr, Restaurant restaurant){
        String status = "";
        try {
            //Connect
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            //Prepare json object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", restaurant.name);
            jsonObject.put("rating", restaurant.rating);
            jsonObject.put("latitute", restaurant.latitute);
            jsonObject.put("longitute", restaurant.longitute);

            //Write data
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(jsonObject.toString());
            outputStream.flush();
            outputStream.close();
            status = conn.getResponseCode() + ": " + conn.getResponseMessage();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static String deleteRequest(String urlStr, String id){
        String status = "";
        try{
            URL url = new URL(urlStr + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("DELETE");
            status = conn.getResponseCode() + ": " + conn.getResponseMessage();
            conn.connect();
        }catch(Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public static String postClinic(String urlStr, Clinic clinic){
        String status = "";

        try {
            //Connect
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            //Prepare json object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", clinic.name);
            jsonObject.put("address", clinic.address);
            jsonObject.put("rating", clinic.rating);
            jsonObject.put("latitute", clinic.latitude);
            jsonObject.put("longitute", clinic.longitude);
            jsonObject.put("impression", clinic.impression);
            jsonObject.put("lead_physician", clinic.lead_phys);
            jsonObject.put("specialization", clinic.specialization);
            jsonObject.put("average_price", clinic.avg_price);

            //Write data
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(jsonObject.toString());
            outputStream.flush();
            outputStream.close();
            status = conn.getResponseCode() + ": " + conn.getResponseMessage();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;

    }
}
