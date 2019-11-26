package com.example.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

/**
 * An activity that allows a user to enter a barcode number manually or another option to scan a
 * barcode. Displays error messages to users through toasts.
 */
public class EnterBarcodeActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.camera.MESSAGE";
    static final int CHECK_BARCODE_REQUEST = 3;
    static final int SCAN_BARCODE = 2;

    /**
     * When created check if the phone has an internet connection. If false then disable the buttons
     * and display a toast message informing the user.
     *
     * Get intent and EXTRAs that started this activity.
     * If there is a flag then display appropriate toast message or if flag is 5 then a barcode
     * number has been returned and must be passed to CheckBarcode Activity.
     *
     * On click of the submit button will take the barcode entered and start the CheckBarcode
     * Activity for result.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_barcode);

        // Check internet connection
        if (isNetworkAvailable() == false) {

            // Disable buttons & display toast
            Button submit = findViewById(R.id.Submit);
            Button scan_again = findViewById(R.id.scan_again);
            submit.setEnabled(false);
            submit.setBackgroundColor(Color.GRAY);
            scan_again.setEnabled(false);
            scan_again.setBackgroundColor(Color.GRAY);

            CharSequence toastText;
            toastText = "No internet access, connect to the internet or search saved items!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        }

        Intent intent = getIntent();

        if (intent.hasExtra("com.example.camera.FLAG")) {

            String flag = intent.getStringExtra("com.example.camera.FLAG");

            if (flag.equals("5")) {

                // Redirected from scan with message (barcode number) to handle
                String message = intent.getStringExtra("com.example.camera.MESSAGE");

                Intent checkBarcodeIntent = new Intent(getBaseContext(), CheckBarcode.class);
                checkBarcodeIntent.putExtra("com.example.camera.MESSAGE", message);
                startActivityForResult(checkBarcodeIntent, CHECK_BARCODE_REQUEST);
            }

            else {
                // Handle the flag with the appropriate toast message

                CharSequence toastText;
                switch (flag) {
                    case "1": {
                        toastText = "No barcode could be detected, try again or enter manually!";
                        break;
                    }
                    case "2": {
                        toastText = "No product matching barcode found!";
                        break;
                    }
                    case "3": {
                        toastText = "No information found on this product!";
                        break;
                    }
                    default: {
                        toastText = "";
                    }
                }

                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
            }
        }

        // Enter a barcode manually
        final Button button = findViewById(R.id.Submit);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Takes the entered barcode and starts CheckBarcode Activity for result
             * @param v
             */
            public void onClick(View v) {

                Intent checkBarcodeIntent = new Intent(getBaseContext(), CheckBarcode.class);

                EditText editText = findViewById(R.id.enterText);
                String message = editText.getText().toString();

                checkBarcodeIntent.putExtra("com.example.camera.MESSAGE", message);
                startActivityForResult(checkBarcodeIntent, CHECK_BARCODE_REQUEST);
            }
        });

        // Scan a Barcode
        Button btn = findViewById(R.id.scan_again);
        btn.setOnClickListener(new View.OnClickListener() {
            /**
             * Simply starts the scan activity and finishes this current activity
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Scan.class);
                startActivity(intent);
                finish();
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
        mTitle.setText(R.string.lookup_barcode);
        TextViewCompat.setTextAppearance(mTitle, R.style.Toolbar_TitleText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param returnIntent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
        super.onActivityResult(requestCode, resultCode, returnIntent);
        System.out.println("******************HERE****************");
        if (requestCode == CHECK_BARCODE_REQUEST) {
             // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                System.out.println("*****************FURTHER************");
                if (returnIntent.hasExtra("com.example.camera.FLAG")) {
                    String flag = returnIntent.getStringExtra("com.example.camera.FLAG");
                    System.out.println("************** Redirected here, flag is " + flag);

                    CharSequence toastText;
                    switch (flag) {
                        case "1": {
                            toastText = "No barcode could be detected, try again " +
                                    "or enter manually!";
                            System.out.println("******FLAG 1 - No barcode could be detected");
                            break;
                        }
                        case "2": {
                            toastText = "No product matching barcode found!";
                            System.out.println("******FLAG 2 - No product found in DB or API");
                            break;
                        }
                        case "3": {
                            toastText = "No information found on this product!";
                            System.out.println("******FLAG 3 - No appropriate info found on prodcut");
                            break;
                        }
                        default: {
                            toastText = "";
                        }
                    }

                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                }

                else {

                    // No flag so other intent started okay
                    Bundle extras = returnIntent.getExtras();
                    Intent showInfoIntent = new Intent(getApplicationContext(), ShowInfoActivity.class);
                    showInfoIntent.putExtras(extras);
                    startActivity(showInfoIntent);
                }
            }
        }

        else if (requestCode == SCAN_BARCODE) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                System.out.println("*************SCAN RETURNED OKAY**********");
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Back arrow
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When activity is resumed, check if internet connection and if false then disable the buttons
     * and display a toast message.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Check internet connection
        if (isNetworkAvailable() == false) {

            // Disable buttons & display toast
            Button submit = findViewById(R.id.Submit);
            Button scan_again = findViewById(R.id.scan_again);
            submit.setEnabled(false);
            submit.setBackgroundColor(Color.GRAY);
            scan_again.setEnabled(false);
            scan_again.setBackgroundColor(Color.GRAY);

            CharSequence toastText;
            toastText = "No internet access, connect to the internet or search saved items!";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        }
    }

    /**
     * Simple function to check if phone has an internet connection
     * @return True if internet connection
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

}
