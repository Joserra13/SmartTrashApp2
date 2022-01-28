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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkersList extends AppCompatActivity implements ItemViewHolder.ItemClickListener{

    Button bParse;

    TextView testTV;

    Call<JsonObject> containerAlarm;

    String containerInfo = "";

    long createdTime;
    String type = "";
    String status = "";
    String severity = "";
    String dateString = "";
    String name = "";
    String alarmId = "";

    private static final List<Item> listOfItems = new ArrayList<>();
    ArrayList<String> alarmId_ArrayList = new ArrayList<String>();

    private RecyclerView myRecycleView;
    private ItemAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_list);

        bParse = findViewById(R.id.button3);
        //testTV = findViewById(R.id.textView22);

        myRecycleView = findViewById(R.id.recyclerView2);
        myAdapter = new ItemAdapter(listOfItems, this);
        myRecycleView.setAdapter(myAdapter);
        myRecycleView.setLayoutManager(new LinearLayoutManager(this));

        listOfItems.clear();
        alarmId_ArrayList.clear();

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
            containerAlarm = tbs.getPlasticStreetContainersAlarm2(token);
        }else if(choice == 1){
            //Paper
            containerAlarm = tbs.getPaperStreetContainersAlarm(token);
            containerAlarm = tbs.getPaperStreetContainersAlarm2(token);
        }else if(choice == 2){
            //Organic
            containerAlarm = tbs.getOrganicStreetContainersAlarm(token);
            containerAlarm = tbs.getPaperStreetContainersAlarm2(token);
        }else if(choice == 3){
            //Glass
            containerAlarm = tbs.getGlassStreetContainersAlarm(token);
            containerAlarm = tbs.getPaperStreetContainersAlarm2(token);
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
                        //test = test + obj.toString();

                        JSONArray dataArray = null;
                        JSONObject alarmInfo;
                        dataArray = obj.getJSONArray("data");

                        for(int i=0; i < dataArray.length(); i++){
                            alarmInfo = dataArray.getJSONObject(i);

                            createdTime = alarmInfo.getLong("createdTime");
                            toDateTime(createdTime);

                            status = alarmInfo.getString("status");
                            severity = alarmInfo.getString("severity");
                            type = alarmInfo.getString("type");

                            alarmId = alarmInfo.getJSONObject("id").getString("id");
                            alarmId_ArrayList.add(alarmId);

                            if(choice == 0){

                                name = "Plastic Street Container";
                            }else if(choice == 1){

                                name = "Paper Street Container";
                            }else if(choice == 2){

                                name = "Organic Street Container";
                            }else if(choice == 3){

                                name = "Glass Street Container";
                            }

                            containerInfo = dateString + "\nContainer: " + name + "\nType: " + type + "\nSeverity: " + severity + " Status: " + status;

                            listOfItems.add(new Item(containerInfo, null, dateString));

                            myAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else{
                    Log.d("ERROR with code: ", String.valueOf(response.code()));
                }

                //testTV.setText(containerInfo);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("RESPONSE::ERROR", "It did not work");
            }
        });
    }

    public void toDateTime(long secs) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
        dateString = formatter.format(secs);
    }

    @Override
    public void onItemClick(int position, View v) {

        Item container = listOfItems.get(position);

        Intent i = new Intent(WorkersList.this, AlarmActivity.class);

        i.putExtra("alarmInfo", String.valueOf(container.getDisplayText()));
        i.putExtra("alarmId", String.valueOf(alarmId_ArrayList.get(position)));

        startActivity(i);
    }
}