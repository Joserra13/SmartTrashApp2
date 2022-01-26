package dte.masteriot.mdp.smarttrashapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkersList extends AppCompatActivity{

    Button bParse;

    TextView testTV;

    Call<JsonObject> containerAlarm;

    String test = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_list);

        bParse = findViewById(R.id.button3);
        testTV = findViewById(R.id.textView22);

        //Plastic
        getAlarms(0);
        //Paper
        getAlarms(1);
        //Organic
        getAlarms(2);
        //Glass
        getAlarms(3);

    }

    public void goTo(View view){

        Intent i = new Intent(WorkersList.this, WorkersIntro.class);
        startActivity(i);
    }

    public void getAlarms(int choice) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "defToken");
        token = "Bearer " + token;
        ThingsboardService tbs = ServiceGenerator.createService(ThingsboardService.class);

        //Llamada get the alarms
        if(choice == 0){
            //Plastic
            containerAlarm = tbs.getPlasticStreetContainersAlarm(token);
        }else if(choice == 1){
            //Paper
            containerAlarm = tbs.getPaperStreetContainersAlarm(token);
        }else if(choice == 2){
            //Organic
            containerAlarm = tbs.getOrganicStreetContainersAlarm(token);
        }else if(choice == 3){
            //Glass
            containerAlarm = tbs.getGlassStreetContainersAlarm(token);
        }

        //This enqueues of the Callback means we are making an asynchronous request (which won't block the UI-Thread)
        containerAlarm.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    //Parse de JSON to obtain every alarm and display it on a list
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response.body().toString());
                        test = test + obj.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else{
                    Log.d("ERROR with code: ", String.valueOf(response.code()));
                }

                testTV.setText(test);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("RESPONSE::ERROR", "It did not work");
            }
        });
    }


}