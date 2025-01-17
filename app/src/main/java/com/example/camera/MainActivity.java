package com.example.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

/**
 * This is the main activity that gives the user 3 different methods of searching for information
 * on products.
 * <p>
 * 1. Scan a barcode
 * 2. Manually enter a barcode
 * 3. Search the saved items in the database
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Check if the phone has an internet connection, if not then disable buttons that require
     * internet access (Scan and enter a barcode) and display a toast message to the user.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check internet connection
        if (isNetworkAvailable() == false) {

            // Disable buttons & display toast
            Button scanButton = findViewById(R.id.button);
            Button lookupBarcode = findViewById(R.id.lookupBarcode);
            scanButton.setEnabled(false);
            scanButton.setBackgroundColor(Color.GRAY);
            lookupBarcode.setEnabled(false);
            lookupBarcode.setBackgroundColor(Color.GRAY);

            CharSequence toastText;
            toastText = "No internet access, connect to the internet and restart app or search saved items!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        }

        // Scan a Barcode
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Scan.class);
                startActivity(intent);
            }
        });

        // Enter Barcode Manually
        final Button button2 = findViewById(R.id.lookupBarcode);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EnterBarcodeActivity.class);
                startActivity(intent);
            }
        });

        // Search Database
        final Button searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchCategoriesActivity.class);
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
        mTitle.setText("Home");
        TextViewCompat.setTextAppearance(mTitle, R.style.Toolbar_TitleText);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_home_white_24dp);
    }

    /**
     * On resume check if the phone still has an internet connection. If no then disable buttons
     * and display a toast message to the user.
     */
    @Override
    public void onResume() {
        super.onResume();

        Button scanButton = findViewById(R.id.button);
        Button lookupBarcode = findViewById(R.id.lookupBarcode);

        // Re-enable buttons
        scanButton.setEnabled(true);
        scanButton.setBackgroundColor(Color.WHITE);
        lookupBarcode.setEnabled(true);
        lookupBarcode.setBackgroundColor(Color.WHITE);

        // Check internet connection
        if (isNetworkAvailable()==false) {

            // Disable buttons & display toast
            scanButton.setEnabled(false);
            scanButton.setBackgroundColor(Color.GRAY);
            lookupBarcode.setEnabled(false);
            lookupBarcode.setBackgroundColor(Color.GRAY);

            CharSequence toastText;
            toastText = "No internet access, connect to the internet and restart app or search saved items!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        }
    }

    /**
     * Simple function to check if phone has an internet connection
     *
     * @return True if internet connection
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }
}
