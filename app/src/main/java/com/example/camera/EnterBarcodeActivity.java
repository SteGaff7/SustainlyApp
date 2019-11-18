package com.example.camera;

import android.content.Intent;
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

public class EnterBarcodeActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.camera.MESSAGE";
    static final int CHECK_BARCODE_REQUEST = 3;
    static final int SCAN_BARCODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_barcode);

        Intent intent = getIntent();


        if (intent.hasExtra("com.example.camera.FLAG")) {
            System.out.println("*************GOT FLAG*****************");
            String flag = intent.getStringExtra("com.example.camera.FLAG");

            CharSequence toastText;
            switch (flag) {
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
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();



            if (flag.equals("1")) {
                System.out.println(flag);
                toastText = "No barcode could be detected, try again " +
                        "or enter manually!";
                duration = Toast.LENGTH_LONG;
                toast = Toast.makeText(getApplicationContext(), toastText, duration);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
            }

            if (flag.equals("5")) {
                // Redirected from scan with message to handle
                String message = intent.getStringExtra("com.example.camera.MESSAGE");

                Intent checkBarcodeIntent = new Intent(getBaseContext(), CheckBarcode.class);
                checkBarcodeIntent.putExtra("com.example.camera.MESSAGE", message);
                startActivityForResult(checkBarcodeIntent, CHECK_BARCODE_REQUEST);
            }
        }

        // Enter a barcode manually
        final Button button = findViewById(R.id.Submit);
        button.setOnClickListener(new View.OnClickListener() {
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
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Scan.class);
                startActivity(intent);
                //startActivityForResult(intent, SCAN_BARCODE);
                //finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
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

    // NEEDED????
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
