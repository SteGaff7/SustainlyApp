package com.example.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class EnterBarcodeActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.camera.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_barcode);

        Intent intent = getIntent();

        if (intent.hasExtra("com.example.camera.FLAG")) {
            System.out.println("*****************Found a flag, redirected here************************");
            // Create a pop up to say barcode could not be scanned (Toast)
            System.out.println("Found Redirect Intent with code");
            System.out.println(intent.getStringExtra("com.example.camera.FLAG"));
        }

        // Enter a barcode manually
        final Button button = findViewById(R.id.Submit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ShowInfoActivity.class);
                EditText editText = (EditText) findViewById(R.id.enterText);
                String message = editText.getText().toString();
                intent.putExtra("com.example.camera.MESSAGE", message);
                startActivity(intent);

            }
        });

        // Scan a Barcode
        Button btn = findViewById(R.id.scan_again);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Scan.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //Intent intent = new Intent(getBaseContext(), EnterBarcodeActivity.class);
                //startActivity(intent);
            //}
        //});

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
