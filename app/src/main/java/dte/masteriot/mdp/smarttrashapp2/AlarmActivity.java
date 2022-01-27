package dte.masteriot.mdp.smarttrashapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmActivity extends AppCompatActivity {

    TextView alarmInfoTV;
    TextView commandSent;

    Intent inputIntent;
    String alarmInfo = "";
    String alarmId = "";

    Call<JsonObject> alarmCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmInfoTV = findViewById(R.id.textView24);
        commandSent = findViewById(R.id.textView25);

        //Getting the Intent
        inputIntent = getIntent();

        alarmInfo = inputIntent.getStringExtra("alarmInfo");
        alarmId = inputIntent.getStringExtra("alarmId");

        alarmInfoTV.setText(alarmInfo);
    }

    private void sendCommand(int choice) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "defToken");
        token = "Bearer " + token;
        ThingsboardService tbs = ServiceGenerator.createService(ThingsboardService.class);

        //Llamada get the info
        if(choice == 0){
            //Ack
            alarmCommand = tbs.sendACKAlarm(token, alarmId);
        }else if(choice == 1){
            //Clear
            alarmCommand = tbs.sendClearAlarm(token, alarmId);
        }

        //This enqueues of the Callback means we are making an asynchronous request (which won't block the UI-Thread)
        alarmCommand.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){

                    if(choice == 0){
                        commandSent.setText("Alarm acknowledged");
                    }else if(choice == 1){
                        commandSent.setText("Alarm cleared");
                    }

                } else{
                    Log.d("ERROR with code: ", String.valueOf(response.code()));

                    if(choice == 0){
                        commandSent.setText("Alarm was not acknowledge correclty");
                    }else if(choice == 1){
                        commandSent.setText("Alarm was not cleared correclty");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("RESPONSE::ERROR", "It did not work");
            }
        });
    }

    public void sendACK(View view) {

        sendCommand(0);
    }

    public void sendClear(View view) {
        sendCommand(1);
    }
}