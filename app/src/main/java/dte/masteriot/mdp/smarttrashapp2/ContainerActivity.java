package dte.masteriot.mdp.smarttrashapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContainerActivity extends AppCompatActivity {

    Intent inputIntent;
    int organic, plastic, paper, glass;
    float temp, hum, xAxis, yAxis, zAxis;
    String name;
    TextView containerName, organicLevel, plasticLevel, paperLevel, glassLevel, temperature, humidity, x_Axis, y_Axis, z_Axis, orientation;
    TextView test;

    Call<JsonObject> containerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        //Getting the Intent
        inputIntent = getIntent();

        //Getting the Values coming from First Activity extracting them from the Intent received
        name = inputIntent.getStringExtra("containerName");

        containerName = findViewById(R.id.textView1);
        organicLevel = findViewById(R.id.textView7);
        plasticLevel = findViewById(R.id.textView3);
        paperLevel = findViewById(R.id.textView5);
        glassLevel = findViewById(R.id.textView9);

        temperature = findViewById(R.id.textView11);
        humidity = findViewById(R.id.textView13);

        x_Axis = findViewById(R.id.textView15);
        y_Axis = findViewById(R.id.textView17);
        z_Axis = findViewById(R.id.textView19);

        containerName.setText(name);

        //Plastic
        getStreetContainersData(0);
        //Paper
        getStreetContainersData(1);
        //Organic
        getStreetContainersData(2);
        //Glass
        getStreetContainersData(3);
    }

    private void writeData() {
        organicLevel.setText(String.valueOf(organic) + "%");
        plasticLevel.setText(String.valueOf(plastic) + "%");
        paperLevel.setText(String.valueOf(paper) + "%");
        glassLevel.setText(String.valueOf(glass) + "%");

        temperature.setText(String.valueOf(temp) + "ºC");
        humidity.setText(String.valueOf(hum) + "%");

        x_Axis.setText(String.valueOf(xAxis));
        y_Axis.setText(String.valueOf(yAxis));
        z_Axis.setText(String.valueOf(zAxis));
    }

    private void getStreetContainersData(int choice) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "defToken");
        token = "Bearer " + token;
        ThingsboardService tbs = ServiceGenerator.createService(ThingsboardService.class);

        //Llamada get the info
        if(choice == 0){
            //Plastic
            containerData = tbs.getPlasticStreetContainersLatestTelemetry(token);
        }else if(choice == 1){
            //Paper
            containerData = tbs.getPaperStreetContainersLatestTelemetry(token);
        }else if(choice == 2){
            //Organic
            containerData = tbs.getOrganicStreetContainersLatestTelemetry(token);
        }else if(choice == 3){
            //Glass
            containerData = tbs.getGlassStreetContainersLatestTelemetry(token);
        }

        //This enqueues of the Callback means we are making an asynchronous request (which won't block the UI-Thread)
        containerData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    //Parse de JSON to obtain the container's telemetry
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response.body().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray capacityArray = null;
                    JSONObject telemetry;

                    if(choice == 0){
                        //Plastic
                        try {
                            capacityArray = obj.getJSONArray("capacity");
                            telemetry = capacityArray.getJSONObject(0);
                            plastic = telemetry.getInt("value");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(choice == 1){
                        //Paper
                        try {
                            capacityArray = obj.getJSONArray("capacity");
                            telemetry = capacityArray.getJSONObject(0);
                            paper = telemetry.getInt("value");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(choice == 2){
                        //Organic
                        try {
                            capacityArray = obj.getJSONArray("capacity");
                            telemetry = capacityArray.getJSONObject(0);
                            organic = telemetry.getInt("value");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(choice == 3){
                        //Glass
                        try {
                            capacityArray = obj.getJSONArray("capacity");
                            telemetry = capacityArray.getJSONObject(0);
                            glass = telemetry.getInt("value");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        capacityArray = obj.getJSONArray("humidity");
                        telemetry = capacityArray.getJSONObject(0);
                        hum = (float) telemetry.getDouble("value");

                        capacityArray = obj.getJSONArray("temperature");
                        telemetry = capacityArray.getJSONObject(0);
                        temp = (float) telemetry.getDouble("value");

                        capacityArray = obj.getJSONArray("X");
                        telemetry = capacityArray.getJSONObject(0);
                        xAxis = (float) telemetry.getDouble("value");

                        capacityArray = obj.getJSONArray("Y");
                        telemetry = capacityArray.getJSONObject(0);
                        yAxis = (float) telemetry.getDouble("value");

                        capacityArray = obj.getJSONArray("Z");
                        telemetry = capacityArray.getJSONObject(0);
                        zAxis = (float) telemetry.getDouble("value");

                        writeData();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else{
                    Log.d("ERROR with code: ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("RESPONSE::ERROR", "It did not work");
            }
        });
    }
}