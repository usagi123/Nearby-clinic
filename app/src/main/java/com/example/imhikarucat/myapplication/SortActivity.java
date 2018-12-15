package com.example.imhikarucat.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SortActivity extends AppCompatActivity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        editText = findViewById(R.id.editText);

    }

    public void onConfirmedSearch(View view) {
        Intent intent = new Intent(SortActivity.this,ClinicListingView.class);
        intent.putExtra("filterKey",editText.getText().toString());
        intent.putExtra("intentKey","filting");
        setResult(101,intent);
        finish();
    }
}
