package com.example.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;


public class ShowInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        Intent intent = getIntent();
        String message = intent.getStringExtra(EnterBarcodeActivity.EXTRA_MESSAGE);

        String URL="https://world.openfoodfacts.org/api/v0/product/"+message+".json";

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest= new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse (JSONObject response) {

                        String returnedJSON=response.toString();
                        final TextView brandTextView =  findViewById(R.id.brandTextView);
                        final TextView jsonTextView =  findViewById(R.id.manufTextView);
                        final TextView BarcodeTextView =  findViewById(R.id.originTextView);


                        Gson json= new Gson();
                        Barcode barcode = json.fromJson(returnedJSON, Barcode.class);

                        try {
                            String brandText = "Product: " + barcode.getProduct().getProductName();
                            brandTextView.setText(brandText);

                            String barcodeText = "Manufacturing Location: " + barcode.getProduct().getManufacturingPlaces();
                            BarcodeTextView.setText(barcodeText);

                            String brandName = "Origin of ingredients: " + barcode.getProduct().getOrigins();
                            jsonTextView.setText(brandName);
                        } catch (Exception e) {
                            brandTextView.setText("Error here: " + e);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {

                        final TextView BarcodeTextView =  findViewById(R.id.brandTextView);

                        String barcodeText="Error";
                        BarcodeTextView.setText(barcodeText);



                    }
                }
        );
        requestQueue.add(objectRequest);

        //new map on button click
        final Button mapsButton = findViewById(R.id.more_info_origin);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
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