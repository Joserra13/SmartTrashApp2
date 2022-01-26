package dte.masteriot.mdp.smarttrashapp2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity{

    String logTag; // to clearly identify logs

    private static List<Item> listOfItems = new ArrayList<>();
    ArrayList<String> containerNames_ArrayList = new ArrayList<String>();

    private FusedLocationProviderClient locationClient;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    LatLng currentLocation;
    String nearestContainer;

    double distance = 1000000000.0; //In m
    double  lat0 = 999.99, lon0 = 999.99;
    double calculatedDistance;

    String distances = "";

    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        logTag = "Harversine";

        note = findViewById(R.id.textView23);

        listOfItems.clear();
        containerNames_ArrayList.clear();

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        // Callback for location permission request result
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        startLocationService();
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        getCurrentLocation();

        parseJSON();

        Intent i = new Intent(LoadingActivity.this, ContainerActivity.class);

        i.putExtra("containerName", nearestContainer);

        startActivity(i);
    }

    public void getCurrentLocation(){

        //Now we get the current location
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            startLocationService();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationService() {
        // Request parameters
        com.google.android.gms.location.LocationRequest locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Callback
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                currentLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                lat0 = currentLocation.latitude;
                lon0 = currentLocation.longitude;

                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lat0", String.valueOf(lat0));
                editor.putString("lon0", String.valueOf(lon0));
                editor.commit();

                //note.setText(String.valueOf(lat0) + "," + String.valueOf(lon0));
                locationClient.removeLocationUpdates(this);
            }
        };

        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("MapaContenedores.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void parseJSON() {

        listOfItems.clear();

        String containerName = "";

        try {
            // get JSONObject from JSON file
            JSONObject json_obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONObject named employee
            JSONArray graph = json_obj.getJSONArray("@graph");

            int nContainers = graph.length();

            for (int i = 0; i < nContainers; i++) {

                JSONObject eachContainer = graph.getJSONObject(i);
                LatLng location = null;
                // get container's name
                containerName = eachContainer.getString("title");
                int punto = containerName.indexOf(".");
                containerName = containerName.substring(punto + 2);
                punto = containerName.indexOf("e");
                containerName = containerName.substring(punto + 2);
                containerName = "Container in " + containerName;
                containerNames_ArrayList.add(containerName);

                try {
                    // get container's location
                    JSONObject locationNode = eachContainer.getJSONObject("location");
                    location = new LatLng(
                            locationNode.getDouble("latitude"),
                            locationNode.getDouble("longitude"));

                    calculatedDistance = getDistance(location);

                    if(calculatedDistance < distance){
                        distance = calculatedDistance;
                        nearestContainer = containerName;
                    }

                } catch (JSONException ignored) {

                }

                listOfItems.add(new Item(containerName, location, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private double getDistance(LatLng location){

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String latitude0 = sharedPref.getString("lat0", "13.13");
        String longitude0 = sharedPref.getString("lon0", "13.13");

        lat0 = Double.parseDouble(latitude0);
        lon0 = Double.parseDouble(longitude0);

        double lat1, lon1, difLat, difLon, a, c;

        lat1 = location.latitude;
        lon1 = location.longitude;

        Log.d(logTag, "lat0:" + lat0 + " lon0:" + lon0 + " lat1:" + lat1 + " lon1:" + lon1);

        try {
            //Harversine's Formula to calculate the distance of 2 terrestrial points
            difLat = lat1 - lat0;
            difLat = Math.toRadians(difLat);
            difLon = lon1 - lon0;
            difLon = Math.toRadians(difLon);

            a = Math.pow(Math.sin(difLat / 2), 2) + Math.cos(lat0) * Math.cos(lat1) * Math.pow(Math.sin(difLon / 2), 2);

            c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            double cd = 6371000 * c; //In metres
            distances += cd + " ";
            //note.setText(String.valueOf(calculatedDistance));
            return cd;
        }catch (NullPointerException e){
            Toast.makeText(this,"Unable to get Location", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
}