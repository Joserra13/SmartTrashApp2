package dte.masteriot.mdp.smarttrashapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WorkersIntro extends AppCompatActivity implements ItemViewHolder.ItemClickListener{

    ArrayList<String> containerNames_ArrayList = new ArrayList<String>();

    private static final List<Item> listOfItems = new ArrayList<>();

    private RecyclerView myRecycleView;
    private ItemAdapter myAdapter;

    Button bParse;

    TextView parseo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_intro);

        myRecycleView = findViewById(R.id.recyclerView);
        myAdapter = new ItemAdapter(listOfItems, this);
        myRecycleView.setAdapter(myAdapter);
        myRecycleView.setLayoutManager(new LinearLayoutManager(this));

        bParse = findViewById(R.id.button3);
        parseo = findViewById(R.id.textView3);

        parseJSON();
    }

    public void parseJSON() {

        listOfItems.clear();

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
                containerNames_ArrayList.add(containerNames);

                try {
                    // get container's location
                    JSONObject locationNode = eachContainer.getJSONObject("location");
                    location = new LatLng(
                            locationNode.getDouble("latitude"),
                            locationNode.getDouble("longitude"));
                }catch (JSONException ignored){

                }

                listOfItems.add(new Item(containerNames, location, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        myAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(int position, View v) {

        Item container = listOfItems.get(position);

        //Toast.makeText(this, String.valueOf(item.getLocation()), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(WorkersIntro.this,MapsActivity.class);

        i.putExtra("containerName", String.valueOf(container.getDisplayText()));
        i.putExtra("location", String.valueOf(container.getLocation()));
        i.putExtra("seeFullMap", false);

        startActivity(i);
    }

    public void goTo(View view){

        Intent i = new Intent(WorkersIntro.this,MapsActivity.class);
        i.putExtra("seeFullMap", false);
        startActivity(i);
    }

    public void seeMap(View view) {
        Intent i = new Intent(WorkersIntro.this,MapsActivity.class);
        i.putExtra("seeFullMap", true);
        startActivity(i);
    }
}