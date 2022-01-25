package dte.masteriot.mdp.smarttrashapp2;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import dte.masteriot.mdp.smarttrashapp2.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    Intent inputIntent;
    String name;
    String lat;
    String lon;
    boolean seeFullMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Getting the Intent
        inputIntent = getIntent();

        seeFullMap = inputIntent.getBooleanExtra("seeFullMap", false);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(seeFullMap){

            parseJSON();

        }else{

            //Getting the Values coming from First Activity extracting them from the Intent received
            name = inputIntent.getStringExtra("containerName");
            String location = inputIntent.getStringExtra("location");

            location = location.substring(10, location.length()-1);

            lat = location.substring(0, location.indexOf(","));
            lon = location.substring(location.indexOf(",")+1);

            // Add a marker in Sydney and move the camera
            //LatLng sydney = new LatLng(-34, 151);
            LatLng container = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
            mMap.addMarker(new MarkerOptions().position(container).title("Container in " + name).snippet("-->Click here to see the details<--"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(container));
            mMap.setMinZoomPreference(13);
        }

        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {

        // Creating Intent For Navigating to VisitActivity (Explicit Intent)
        Intent i = new Intent(MapsActivity.this, ContainerActivity.class);

        // Adding values to the intent to pass them to VisitActivity
        i.putExtra("containerName", name);

        // Once the intent is parametrized, start the VisitActivity:
        startActivity(i);
    }

    public void parseJSON() {

        String containerNames = "";

        try {
            // get JSONObject from JSON file
            JSONObject json_obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONObject named employee
            JSONArray graph = json_obj.getJSONArray("@graph");

            int nContainers = graph.length();

            for(int i = 0; i < nContainers; i++) {

                JSONObject eachContainer = graph.getJSONObject(i);
                LatLng location = null;
                // get container's name
                containerNames = eachContainer.getString("title");
                int punto = containerNames.indexOf(".");
                containerNames = containerNames.substring(punto + 2);

                try {
                    // get container's location
                    JSONObject locationNode = eachContainer.getJSONObject("location");
                    location = new LatLng(
                            locationNode.getDouble("latitude"),
                            locationNode.getDouble("longitude"));

                    mMap.addMarker(new MarkerOptions().position(location).title("Container in " + containerNames).snippet("-->Click here to see the details<--"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                }catch (JSONException ignored){

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}