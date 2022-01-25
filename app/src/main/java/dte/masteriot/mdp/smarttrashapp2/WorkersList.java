package dte.masteriot.mdp.smarttrashapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkersList extends AppCompatActivity{

    Button bParse;

    Call<JsonObject> alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_list);

        bParse = findViewById(R.id.button3);

       //getAlarms();

    }

    public void goTo(View view){

        Intent i = new Intent(WorkersList.this, WorkersIntro.class);
        startActivity(i);
    }

    public void getAlarms() {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "defToken");
        token = "Bearer " + token;
        ThingsboardService tbs = ServiceGenerator.createService(ThingsboardService.class);

        //Llamada get the alarms
        //alarmList = tbs.getStreetContainersAlarms(token);

        //This enqueues of the Callback means we are making an asynchronous request (which won't block the UI-Thread)
        alarmList.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    //Parse de JSON to obtain every alarm and display it on a list

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