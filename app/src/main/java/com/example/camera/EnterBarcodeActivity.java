package com.example.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EnterBarcodeActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.camera.MESSAGE";
    static final int CHECK_BARCODE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_barcode);

        Intent intent = getIntent();

        if (intent.hasExtra("com.example.camera.FLAG")) {
            String flag = intent.getStringExtra("com.example.camera.FLAG");
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
            System.out.println("*********** NO FLAG***************");
        }

        // Enter a barcode manually
        final Button button = findViewById(R.id.Submit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent intent = new Intent(getBaseContext(), ShowInfoActivity.class);
                Intent intent = new Intent(getBaseContext(), CheckBarcode.class);
                EditText editText = (EditText) findViewById(R.id.enterText);
                String message = editText.getText().toString();
                intent.putExtra("com.example.camera.MESSAGE", message);
                //startActivity(intent);
                startActivityForResult(intent, CHECK_BARCODE_REQUEST);
                //finish();
            }
        });

        // Scan a Barcode
        Button btn = findViewById(R.id.scan_again);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Scan.class);
                startActivity(intent);
                //finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
        System.out.println("******************HERE****************");
        if (requestCode == CHECK_BARCODE_REQUEST) {
            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
                System.out.println("*****************FURTHER************");
//                if (returnIntent.hasExtra("com.example.camera.FLAG")) {

            String flag = returnIntent.getStringExtra("com.example.camera.FLAG");
            System.out.println("*****FLAG FLAG " + flag);

            String toastText = "No product matching barcode found!";
            System.out.println("******FLAG 2 - No product found in DB or API");
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
//                }
                //finish();
//            }
        }
    }

    // NEEDED????
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle arrow click here
//        if (item.getItemId() == android.R.id.home) {
//            finish(); // close this activity and return to preview activity (if there is any)
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
