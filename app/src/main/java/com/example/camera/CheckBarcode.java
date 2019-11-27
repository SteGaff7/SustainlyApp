package com.example.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.camera.util.Barcode;
import com.example.camera.database.ProductOperations;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * This is an activity without a layout, it simply checks for information in the local database or
 * through the API for the barcode provided.
 */
public class CheckBarcode extends Activity {

    /**
     * Retrieve intent that started this activity. Retrieve message which refers to the barcode
     * number. Check if this barcode number is stored in local DB. If so then retrieve information
     * and send back to the activity requesting it.
     * <p>
     * If it is not in the DB then call the Open Food Fact API.
     * If there are no results or no appropriate info then redirect to enterBarcode Activity with
     * a flag.
     * If data returned create a product and barcode object from this data.
     * Save this data to DB for future use and send an intent with this info to the showInfo
     * activity to be displayed to the user.
     * Finish this activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra("com.example.camera.MESSAGE");

        final ProductOperations productOperations = new ProductOperations(this);
        Barcode generatedBarcode = productOperations.getBarcode(message);
        //if barcode is in the database
        if (generatedBarcode.getInDB()) {

            String productName = generatedBarcode.getProduct().getProductName();
            String manufacturingPlaces = generatedBarcode.getProduct().getManufacturingPlaces();
            String origins = generatedBarcode.getProduct().getOrigins();

            // Send results back to activity requesting it
            Intent redirectIntent = new Intent();
            redirectIntent.putExtra("com.example.camera.INFO-NAME", productName);
            redirectIntent.putExtra("com.example.camera.INFO-MAN", manufacturingPlaces);
            redirectIntent.putExtra("com.example.camera.INFO-ORIGINS", origins);
            redirectIntent.putExtra("com.example.camera.IN-DB", "0");
            setResult(RESULT_OK, redirectIntent);

            finish();
        }
        //check API for barcode
        else {

            String URL = "https://world.openfoodfacts.org/api/v0/product/" + message + ".json";

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest objectRequest = new JsonObjectRequest(
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

                                // Redirect to enter barcode page as no product was found
                                Intent redirectIntent = new Intent();
                                redirectIntent.putExtra("com.example.camera.FLAG", "2");
                                setResult(RESULT_OK, redirectIntent);
                                finish();

                            } else if (barcode.getStatus() == 1) {

                                String productName = barcode.getProduct().getProductName();
                                String manufacturingPlaces = barcode.getProduct().getManufacturingPlaces();
                                String origins = barcode.getProduct().getOrigins();

                                if ((productName == null || productName.equals("")) & (manufacturingPlaces == null || manufacturingPlaces.equals("")) & (origins == null || origins.equals(""))) {

                                    // Redirect to enter barcode, product found but no appropriate info on it
                                    Intent redirectIntent = new Intent();
                                    redirectIntent.putExtra("com.example.camera.FLAG", "3");
                                    setResult(RESULT_OK, redirectIntent);
                                    finish();

                                } else {

                                    // Save barcode to local DB for future use
                                    productOperations.addBarcode(barcode);

                                    // Send info to show info page for display
                                    Intent intent = new Intent();
                                    intent.putExtra("com.example.camera.INFO-NAME", productName);
                                    intent.putExtra("com.example.camera.INFO-MAN", manufacturingPlaces);
                                    intent.putExtra("com.example.camera.INFO-ORIGINS", origins);
                                    intent.putExtra("com.example.camera.IN-DB", "1");
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                        }
                    }
            );
            requestQueue.add(objectRequest);
        }
    }
}
