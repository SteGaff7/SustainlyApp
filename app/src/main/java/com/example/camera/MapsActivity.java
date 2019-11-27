package com.example.camera;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import android.view.Menu;

import android.util.Log;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.Math;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private Polyline line;
    private Toolbar toolbar;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private LatLng latLng;
    private CameraPosition mCameraPosition;
    LatLng currLoc;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (dublin irelND) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(53.3498,   -6.2603);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private Location myLoc;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        String value;
        if (extras != null) {
            value = extras.getString("Location");
            if (value.contains(",")) {
                String[] holder = value.split(",");
                value = holder[holder.length - 1];
                value = value.trim();
            }
            //latLng = new LatLng(42.364506, -71.03888);
            //The key argument here must match that used in the other activity
        }
        else {
            value = "United States";
        }
        super.onCreate(savedInstanceState);
        InputStream is = getResources().openRawResource(R.raw.countries);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        }
        catch (Exception e) {
            Log.e("App", "UnsupportedEncodingException");
        }
        finally {
            try {
                is.close();
            }
            catch (Exception e) {
                Log.e("App", "IO error");
            }
        }
        String jsonString = writer.toString();
        JSONArray jsonArray = new JSONArray();
        String latitude = "";
        String longitude = "";
        try {
            jsonArray = new JSONArray(jsonString);
        }
        catch (Exception e) {
            Log.e("App", "JSON error1");
        }
        boolean found = false;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject country = jsonArray.getJSONObject(i);
                Log.e("VALS", "Country Error");
                String countryName = country.getString("name");
                if (countryName.equals(value)) {
                    latitude = country.getString("latitude");
                    longitude = country.getString("longitude");
                    found = true;
                }

            }
        } catch (JSONException e) {
            Log.e("App", "Json Error2");
        }
        if (!found) {
            latitude = "37.09024";
            longitude = "-95.712891";
        }
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        latLng = new LatLng(lat, lng);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final ExtendedFloatingActionButton infoButton = findViewById(R.id.mapInfo);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MoreInfoActivity.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.distance);
        TextViewCompat.setTextAppearance(mTitle, R.style.Toolbar_TitleText_long);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }


    /**
     * Saves the state of the map when the activity is paused so you can resume the page without
     * weird errors.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Manipulates the map when it's available.
     * This is only triggered when the Map is done loading
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.


    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            buildMap(true, mLastKnownLocation);
                        }
                    }
                });
            }
            else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mDefaultLocation.latitude,
                                mDefaultLocation.longitude), 4));
                currLoc = new LatLng(mDefaultLocation.latitude,
                        mDefaultLocation.longitude);
                Location myLoc = new Location("null");
                myLoc.setLatitude(mDefaultLocation.latitude);
                myLoc.setLongitude(mDefaultLocation.longitude);
                buildMap(false, myLoc);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
        else {
            mLocationPermissionGranted = true;
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                }
                else {
                    mLocationPermissionGranted = false;
                    updateLocationUI();
                }
            }
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getDeviceLocation();
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getDeviceLocation();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Helper method that sets up the polyline that is drawn on the map, and also sets up the adding
     * of the text to the map that discusses the distance the food has travelled etc.
     *
     * @param locAllowed is whether the user has allowed location permissions
     * @param myLoc is the user's location
     */
    private void buildMap(boolean locAllowed, Location myLoc) {
        if (locAllowed) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(myLoc.getLatitude(),
                            myLoc.getLongitude()), 4));
            currLoc = new LatLng(myLoc.getLatitude(),
                    myLoc.getLongitude());
            if (line != null) {
                line.remove();
            }
            line = mMap.addPolyline(new PolylineOptions()
                    .add(latLng, currLoc)
                    .width(10)
                    .color(Color.BLUE));
            Location startLoc = new Location("point A");
            startLoc.setLatitude(latLng.latitude);
            startLoc.setLongitude(latLng.longitude);
            setText(startLoc, myLoc);
        } else {
            if (line != null) {
                line.remove();
            }
            line = mMap.addPolyline(new PolylineOptions()
                    .add(latLng, currLoc)
                    .width(10)
                    .color(Color.BLUE));
            Location startLoc = new Location("point A");
            startLoc.setLatitude(latLng.latitude);
            startLoc.setLongitude(latLng.longitude);
            setText(myLoc, startLoc);
        }
    }

    /**
     * This is a helper method that sets up the text, which includes calculating the
     * sustainability grade based on distance the food has traveled.
     *
     * @param myLoc user location
     * @param startLoc starting location of the food
     */
    private void setText(Location myLoc, Location startLoc) {
        float distance = (startLoc.distanceTo(myLoc)) / 1000;
        float co2;
        if (distance >= 200) {
            co2 = distance * 135;
        } else {
            co2 = distance * 142;
        }
        String grade;
        if (distance <= 500) {
            grade = "A";
        }
        else if (distance > 500 & distance < 2000) {
            grade = "B";
        }
        else if (distance > 2000 && distance < 5000) {
            grade = "C";
        }
        else if (distance > 5000 && distance < 10000) {
            grade = "D";
        }
        else {
            grade = "F";
        }
        co2 = Math.round(co2/1000);
        distance = Math.round(distance);
        TextView tv1 = findViewById(R.id.infoText);
        String setText =
                "Distance Food Traveled: " + distance + " km\n" +
                        "CO2 released: " + co2  + "kg of CO2\n" +
                        "Your sustainability grade based on the origin of your food is a " +
                        "grade of: " + grade;
        tv1.setText(setText);
    }

}

