package com.example.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

public class CheckBarcode extends AppCompatActivity {
    // /** and enter

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra(EnterBarcodeActivity.EXTRA_MESSAGE);

        final ProductOperations productOperations = new ProductOperations(this);
        Barcode generatedBarcode = productOperations.getBarcode(message);

        if (generatedBarcode.getInDB() == true) {

            System.out.println("************ FOUND IN DB WHOOO ************");

            String productName = generatedBarcode.getProduct().getProductName();
            String manufacturingPlaces = generatedBarcode.getProduct().getManufacturingPlaces();
            String origins = generatedBarcode.getProduct().getOrigins();

            Intent showInfoIntent = new Intent(getApplicationContext(), ShowInfoActivity.class);
            showInfoIntent.putExtra("com.example.camera.INFO-NAME", productName);
            showInfoIntent.putExtra("com.example.camera.INFO-MAN", manufacturingPlaces);
            showInfoIntent.putExtra("com.example.camera.INFO-ORIGINS", origins);
            startActivity(showInfoIntent);
            //finish();
        }

        else {

            System.out.println("**************** NOT IN DB SAD ************");
            String URL="https://world.openfoodfacts.org/api/v0/product/"+message+".json";

            RequestQueue requestQueue= Volley.newRequestQueue(this);

            JsonObjectRequest objectRequest= new JsonObjectRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String returnedJSON = response.toString();
                            Gson json = new Gson();
                            Barcode barcode = json.fromJson(returnedJSON, Barcode.class);

                            if (barcode.getStatus() == 0) {
                                System.out.println("******STATUS " + barcode.getStatus());
                                // Redirect to enter barcode page as no product was found

                                Intent redirectIntent = new Intent(getApplicationContext(), EnterBarcodeActivity.class);
                                redirectIntent.putExtra("com.example.camera.FLAG", "2");
                                startActivity(redirectIntent);
                                //finish();
                            } else if (barcode.getStatus() == 1) {
                                System.out.println("******STATUS " + barcode.getStatus());
                                String productName = barcode.getProduct().getProductName();
                                String manufacturingPlaces = barcode.getProduct().getManufacturingPlaces();
                                String origins = barcode.getProduct().getOrigins();


                                if (productName == null & manufacturingPlaces == null & origins == null) {
                                    // Redirect to enter barcode, product found but no appropriate info on it
                                    System.out.println("*******NULL INFO ON OBJECT********");

                                    Intent redirectIntent = new Intent(getApplicationContext(), EnterBarcodeActivity.class);
                                    redirectIntent.putExtra("com.example.camera.FLAG", "3");
                                    startActivity(redirectIntent);
                                    //finish();
                                } else {

                                    // ? SAVE INSERT INTO DB

                                    productOperations.addBarcode(barcode);

                                    // Send info to show info page using intent
                                    System.out.println("*****FOUND SOME INFO********");
                                    Intent intent = new Intent(getApplicationContext(), ShowInfoActivity.class);
                                    intent.putExtra("com.example.camera.INFO-NAME", productName);
                                    intent.putExtra("com.example.camera.INFO-MAN", manufacturingPlaces);
                                    intent.putExtra("com.example.camera.INFO-ORIGINS", origins);
                                    startActivity(intent);
                                    //finish();

                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            System.out.println(error);
                        }
                    }
            );
            requestQueue.add(objectRequest);
            finish();
        }

    }
}
