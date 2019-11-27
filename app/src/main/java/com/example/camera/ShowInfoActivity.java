package com.example.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

/**
 * This activity displays the information provided on the barcode to the user. It can also direct
 * users to another activity that displays how far their product had to travel on a map
 */
public class ShowInfoActivity extends AppCompatActivity {
    private String location = "";

    /**
     * Get intent from the activity that started this activity.
     * Display the appropriate values in their respective text view and check if they are null or
     * empty strings.
     *
     * @param savedInstanceState
     */
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

        //check to see if results are in the database
        if (inDB.equals("0")) {
            title.append(" Local Database");
        } else {
            title.append(" Open Food Facts Org");
        }
        //check to see if the product name couldn't be found
        if (productName != null && !productName.equals("")) {
            productTextView.setText(productName);
        } else {
            productTextView.setText(R.string.error);
        }
        //check to see if the manufacturing name couldn't be found
        if (manufacturingPlaces != null && !manufacturingPlaces.equals("")) {
            manuTextView.setText(getString(R.string.more_info_manu) + " " + manufacturingPlaces + ".");
        } else {
            manuTextView.setText(getString(R.string.manu_error));
        }
        //check to see if origin couldn't be found
        if (origins != null && !origins.equals("")) {
            location = origins;
            originTextView.setText(getString(R.string.more_info_product) + " " + origins + ".");
        } else {
            originTextView.setText(getString(R.string.origin_error));
            final Button mapsButton = findViewById(R.id.more_info_origin);
            mapsButton.setVisibility(View.GONE);
        }

        // Button to redirect to map activity
        final Button mapsButton = findViewById(R.id.more_info_origin);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Starts the Map Activity and sends it the origins of the products if defined.
             * @param v
             */
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Get access to the custom title view
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.brand_info);
        TextViewCompat.setTextAppearance(mTitle, R.style.Toolbar_TitleText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    /**
     * Handles the back arrow in the toolbar
     *
     * @param item item of toolbar
     * @return True/False
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}