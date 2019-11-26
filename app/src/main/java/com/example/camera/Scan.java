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
import androidx.core.content.FileProvider;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Scan activity is an activity without a layout, it handles camera logic without the need
 * to duplicate code across multiple activities.
 */
public class Scan extends Activity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int CHECK_BARCODE_REQUEST = 3;

    /**
     * On create will simply call an intent that opens the built in camera application
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openCameraIntent();
    }

    String imageFilePath;

    /**
     * Creates a temp file path that the image from the camera will be stored at
     * @return file path to where the image will be stored
     * @throws IOException
     */
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

    /**
     * Opens camera to capture an image of a barcode.
     * Stores image at the file path specified above in the createImageFile() function by passing this in the intent
     * Starts the capture picture intent for a callback on activity result
     */
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

    /**
     * Callback method when an activity returns with a result.
     *
     * On REQUEST_TAKE_PHOTO request code; a frame is created from the image captured using the camera
     * stored at imageFilePath. Using Google vision the barcode number is detected from this frame
     * and stored in the barcodes array from where it is accessed.
     * The checkBarcode Activity is then started for a result with the barcode number passed as
     * an extra. (This is a nested startActivityForResult call).
     *
     * On CHECK_BARCODE_REQUEST request code; check if any flags were returned from the checkBarcode
     * Activity. If so redirect to the enterBarcodeActivity with this flag as an EXTRA.
     * If no flags then checkBarcode Activity returned okay and forward bundle returned to the
     * showInfo Activity to be displayed for the user.
     *
     * Finish activity
     *
     * @param requestCode Two possible request codes return, REQUEST_TAKE_PHOTO and CHECK_BARCODE_REQUEST
     * @param resultCode RESULT_OK
     * @param data information returned from the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        System.out.println("************REQUEST CODE*****" + Integer.toString(requestCode));

        // Check which request we're responding to
        if (requestCode == REQUEST_TAKE_PHOTO) {
            // Don't compare the data to null, it will always come as  null because we are providing a file URI,
            // so load with the imageFilePath we obtained before opening the cameraIntent

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imageFilePath);

                // Setup Barcode Detector
                BarcodeDetector mDetector = new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(0)
                        .build();

                if (!mDetector.isOperational()) {
                    return;
                }

                // Frame to pass to detector using bitmap created from image
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<com.google.android.gms.vision.barcode.Barcode> barcodes = mDetector.detect(frame);

                // Usually iterate but we know only 1 image

                // If barcode result
                if (barcodes.size() != 0) {
                    for (int i = 0; i < barcodes.size(); i++) {
                        Barcode thisCode = barcodes.valueAt(i);
                        String message = thisCode.rawValue;

                        Intent checkBarcodeIntent = new Intent(getApplicationContext(), CheckBarcode.class);
                        checkBarcodeIntent.putExtra("com.example.camera.MESSAGE", message);
                        startActivityForResult(checkBarcodeIntent, CHECK_BARCODE_REQUEST);
                    }
                }

                // If no barcode then send to enter manual/ rescan barcode
                else {
                    Intent redirectIntent = new Intent(getApplicationContext(), EnterBarcodeActivity.class);
                    redirectIntent.putExtra("com.example.camera.FLAG", "1");
                    startActivity(redirectIntent);
                    finish();
                }
            }

            // Request not ok
            else {
                finish();
            }
        }

        else if (requestCode == CHECK_BARCODE_REQUEST) {
            System.out.println("*************MADE IT HERE***********");
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                if (data.hasExtra("com.example.camera.FLAG")) {
                    String flag = data.getStringExtra("com.example.camera.FLAG");

                    // If flag then redirect appropriately
                    switch (flag) {
                        case "2": {
                            Intent redirectIntent = new Intent(getApplicationContext(), EnterBarcodeActivity.class);
                            redirectIntent.putExtra("com.example.camera.FLAG", "2");
                            startActivity(redirectIntent);
                            finish();
                            break;
                        }
                        case "3": {
                            Intent redirectIntent = new Intent(getApplicationContext(), EnterBarcodeActivity.class);
                            redirectIntent.putExtra("com.example.camera.FLAG", "3");
                            startActivity(redirectIntent);
                            finish();
                            break;
                        }
                    }
                }

                else {

                    // No flag implies checkBarcode intent returned okay
                    Bundle extras = data.getExtras();
                    Intent showInfoIntent = new Intent(getApplicationContext(), ShowInfoActivity.class);
                    showInfoIntent.putExtras(extras);
                    startActivity(showInfoIntent);
                    finish();
                }
            }
        }
    }
}