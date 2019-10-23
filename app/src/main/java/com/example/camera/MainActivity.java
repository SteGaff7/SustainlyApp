package com.example.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraIntent();
            }
        });

        // NEW -- Kieran
        final Button button2 = findViewById(R.id.lookupBarcode);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EnterBarcodeActivity.class);
                startActivity(intent);
            }
        });

        final Button searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchCategories.class);
                startActivity(intent);
            }
        });
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile;
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            //don't compare the data to null, it will always come as  null because we are providing a file URI,
            // so load with the imageFilePath we obtained before opening the cameraIntent
            mImageView = findViewById(R.id.myImageView);
            //Glide.with(this).load(imageFilePath).into(mImageView);

            Bitmap myBitmap = BitmapFactory.decodeFile(imageFilePath);
            // Set image here if viewing it
            //mImageView.setImageBitmap(myBitmap);

            // Setup Barcode Detector
            BarcodeDetector mDetector = new BarcodeDetector.Builder(getApplicationContext())
                    .setBarcodeFormats(0)
                    .build();

            TextView mTextView = findViewById(R.id.textContent);
            if (!mDetector.isOperational()) {
                mTextView.setText("Could not set up detector");
                return;
            }

            // Frame to pass to detector using bitmap created from image
            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Barcode> barcodes = mDetector.detect(frame);

            System.out.println("OUTSIDE");
            // Usually iterate but we know only 1 image
            for (int i=0; i < barcodes.size(); i++) {
                Barcode thisCode = barcodes.valueAt(i);
                String message = thisCode.rawValue;

                // Create intent to show info
                Intent intent = new Intent(getApplicationContext(), ShowInfoActivity.class);
                // mTextView.setText(message);
                intent.putExtra("com.example.camera.MESSAGE", message);
                startActivity(intent);
                //mTextView.append("In here");
                // System.out.println("***************************************************"+thisCode.rawValue);
                // mTextView.setText(thisCode.rawValue);
            }
        }
    }

    String imageFilePath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }
}
