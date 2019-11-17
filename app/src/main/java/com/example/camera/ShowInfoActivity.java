package com.example.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ShowInfoActivity extends AppCompatActivity {
    private String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        final TextView productTextView =  findViewById(R.id.productTextView);
        final TextView manuTextView =  findViewById(R.id.manuTextView);
        final TextView originTextView =  findViewById(R.id.originTextView);

        Intent intent = getIntent();

        String productName = intent.getStringExtra("com.example.camera.INFO-NAME");
        String manufacturingPlaces = intent.getStringExtra("com.example.camera.INFO-MAN");
        String origins = intent.getStringExtra("com.example.camera.INFO-ORIGINS");

        if (productName != null) {
            productTextView.append("\n" + productName);
        }
        else {
            productTextView.append("\nNo information found");
        }

        if (manufacturingPlaces != null) {
            manuTextView.append("\n" + manufacturingPlaces);
        }
        else {
            manuTextView.append("\nNo information found");
        }


        if (origins != null) {
            originTextView.append("\n" + origins);
        }
        else {
            originTextView.append("\nNo information found");
        }

        //new map on button click
        final Button mapsButton = findViewById(R.id.more_info_origin);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                intent.putExtra("Location",location);
                startActivity(intent);
            }
        });

        final Button brandButton = findViewById(R.id.more_info_brand);
        brandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), BrandInfo.class);
                startActivity(intent);
            }
        });

        final Button productButton = findViewById(R.id.more_info_manu);
        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ManuInfo.class);
                startActivity(intent);
            }
        });

        final Button searchNearbyButton = findViewById(R.id.search_nearby);
        searchNearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}