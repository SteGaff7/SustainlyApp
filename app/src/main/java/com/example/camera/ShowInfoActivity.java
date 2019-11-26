package com.example.camera;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;


public class ShowInfoActivity extends AppCompatActivity {
    private String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_info);

        final TextView productTextView = findViewById(R.id.productTextView);
        final TextView manuTextView = findViewById(R.id.manuTextView);
        final TextView originTextView = findViewById(R.id.originTextView);
        final TextView title = findViewById(R.id.title);

        Intent intent = getIntent();
        String productName = intent.getStringExtra("com.example.camera.INFO-NAME");
        String manufacturingPlaces = intent.getStringExtra("com.example.camera.INFO-MAN");
        String origins = intent.getStringExtra("com.example.camera.INFO-ORIGINS");
        String inDB = intent.getStringExtra("com.example.camera.IN-DB");

        if (inDB.equals("0")) {
            title.append(" Local Database");
        } else {
            title.append(" Open Food Facts Org");
        }

        if (productName != null && productName!="") {
            productTextView.setText(productName);
        } else {
            productTextView.setText(R.string.error);
        }

        if (manufacturingPlaces != null && manufacturingPlaces!="") {
            manuTextView.setText(getString(R.string.more_info_manu) + " " + manufacturingPlaces + ".");
        } else {
            manuTextView.setText(R.string.manu_error + ".");
        }


        if (origins != null && origins!="") {
           location = origins;
           originTextView.setText(getString(R.string.more_info_product) + " " + origins + ".");
        } else {
            originTextView.setText(R.string.error);
        }


        //new map on button click
        final Button mapsButton = findViewById(R.id.more_info_origin);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                intent.putExtra("Location", location);
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        // Remove default title text
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Get access to the custom title view
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.brand_info);
        TextViewCompat.setTextAppearance(mTitle, R.style.Toolbar_TitleText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;// close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}