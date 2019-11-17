package com.example.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Scan extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    // private static final int REQUEST_IMAGE_CAPTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openCameraIntent();
    }

    String imageFilePath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir;
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
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
                Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                try {
                    startActivityForResult(pictureIntent, REQUEST_TAKE_PHOTO);
                } catch (Exception e) {
                    System.out.println("*********Error:" + e);
                    finish();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("******************" + resultCode);

        // Check which request we're responding to
        if (requestCode == REQUEST_TAKE_PHOTO) {
            //don't compare the data to null, it will always come as  null because we are providing a file URI,
            // so load with the imageFilePath we obtained before opening the cameraIntent
            // mImageView = findViewById(R.id.myImageView);
            //Glide.with(this).load(imageFilePath).into(mImageView);

            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imageFilePath);
                // Set image here if viewing it
                //mImageView.setImageBitmap(myBitmap);

                // Setup Barcode Detector
                BarcodeDetector mDetector = new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(0)
                        .build();

                TextView mTextView = findViewById(R.id.textContent);
                if (!mDetector.isOperational()) {
                    mTextView.setText(getString(R.string.detector_error));
                    return;
                }

                // Frame to pass to detector using bitmap created from image
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<com.google.android.gms.vision.barcode.Barcode> barcodes = mDetector.detect(frame);

                System.out.println("OUTSIDE");
                // Usually iterate but we know only 1 image

                // If barcode result
                if (barcodes.size() != 0) {
                    for (int i = 0; i < barcodes.size(); i++) {
                        Barcode thisCode = barcodes.valueAt(i);
                        String message = thisCode.rawValue;

                        // Create intent to show info
                        Intent intent = new Intent(getApplicationContext(), CheckBarcode.class);
                        // mTextView.setText(message);
                        intent.putExtra("com.example.camera.MESSAGE", message);
                        startActivity(intent);
                        //mTextView.append("In here");
                        // System.out.println("***************************************************"+thisCode.rawValue);
                        // mTextView.setText(thisCode.rawValue);
                    }
                }

                // If no barcode then send to enter manual/ rescan barcode
                else {
                    Intent redirectIntent = new Intent(getApplicationContext(), EnterBarcodeActivity.class);
                    redirectIntent.putExtra("com.example.camera.FLAG", "1");
                    startActivity(redirectIntent);
                }
            }
        }

        // New
        finish();
    }
}
