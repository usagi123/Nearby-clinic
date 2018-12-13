package com.example.imhikarucat.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchViewAcitivity extends AppCompatActivity {

    ListView searchClinic;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_acitivity);

        searchClinic = (ListView) findViewById(R.id.searchClinic);

        ArrayList<String> sorting = (ArrayList<String>) getIntent().getSerializableExtra("passing");

        adapter = new ArrayAdapter<String>(SearchViewAcitivity.this, android.R.layout.simple_list_item_1, sorting);

        searchClinic.setAdapter(adapter);

    }
}
