package dte.masteriot.mdp.smarttrashapp2;

import static dte.masteriot.mdp.smarttrashapp2.R.color.blue_001;
import static dte.masteriot.mdp.smarttrashapp2.R.color.brown_001;
import static dte.masteriot.mdp.smarttrashapp2.R.color.green_001;
import static dte.masteriot.mdp.smarttrashapp2.R.color.yellow_001;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends AppCompatActivity {

    Button bPlas;
    Button bPaper;
    Button bOrga;
    Button bGlass;

    ImageView cStorage;

    TextView serviceToday;

    LinearLayout serviceTodayLY;

    Call<JsonObject> jsonResult;

    int binCapacity;

    NotificationManagerCompat notificationManager;

    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        bPlas = findViewById(R.id.bPlastic);
        bPaper = findViewById(R.id.bPaper);
        bOrga = findViewById(R.id.bOrganic);
        bGlass = findViewById(R.id.bGlass);
        cStorage = findViewById(R.id.imageView);
        serviceTodayLY = findViewById(R.id.serviceTodayLY);
        serviceToday = findViewById(R.id.serviceToday);

        notificationManager = NotificationManagerCompat.from(this);

        getTheData(0);
    }

    private void printBinCapacity(int binCapacity, int choice) {
        if(binCapacity == 0){
            cStorage.setImageResource(R.drawable.c0);
        }else if(binCapacity > 0 && binCapacity <= 25){
            cStorage.setImageResource(R.drawable.c25);
        }else if(binCapacity > 25 && binCapacity <= 50){
            cStorage.setImageResource(R.drawable.c50);
        }else if(binCapacity > 50 && binCapacity < 80){
            cStorage.setImageResource(R.drawable.c75);
        }else if(binCapacity >= 80) {
            cStorage.setImageResource(R.drawable.c100);
        }

        if(binCapacity == 100){

            Intent intent = new Intent(this, CustomerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,0);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title + " is full")
                    .setContentText("Please take the trash out")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            notificationManager.notify(choice, builder.build());
        }
    }

    public void showPlastic(View view) {

        int choice = 0;
        serviceToday.setText("Plastic");
        serviceTodayLY.setBackgroundColor(getResources().getColor(yellow_001));
        getTheData(choice);
    }

    public void showPaper(View view) {

        int choice = 1;
        serviceToday.setText("Paper");
        serviceTodayLY.setBackgroundColor(getResources().getColor(blue_001));
        getTheData(choice);
    }

    public void showOrganic(View view) {

        int choice = 2;
        serviceToday.setText("Organic");
        serviceTodayLY.setBackgroundColor(getResources().getColor(brown_001));
        getTheData(choice);
    }

    public void showGlass(View view) {

        int choice = 3;
        serviceToday.setText("Glass");
        serviceTodayLY.setBackgroundColor(getResources().getColor(green_001));
        getTheData(choice);
    }

    public void getTheData(int choice){

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String user = sharedPref.getString("user", "defUser");
        String pass = sharedPref.getString("pass", "defPass");
        String token = sharedPref.getString("token", "defToken");
        token = "Bearer " + token;
        ThingsboardService tbs = ServiceGenerator.createService(ThingsboardService.class);

        if(choice == 0){
            //Plastic
            title = "Plastic bin";
            jsonResult = tbs.getLatestPlasticTel(token);
        }else if(choice == 1){
            //Paper
            title = "Paper bin";
            jsonResult = tbs.getLatestPaperTel(token);
        }else if(choice == 2){
            //Organic
            title = "Organic bin";
            jsonResult = tbs.getLatestOrganicTel(token);
        }else if(choice == 3){
            //Glass
            title = "Glass bin";
            jsonResult = tbs.getLatestGlassTel(token);
        }
        //This enqueues of the Callback means we are making an asynchronous request (which won't block the UI-Thread)
        jsonResult.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200){
                    //Parsear JSON
                    try {
                        JSONObject obj = new JSONObject(response.body().toString());
                        JSONArray capacityArray = obj.getJSONArray("capacity");
                        JSONObject telemetry = capacityArray.getJSONObject(0);

                        binCapacity = telemetry.getInt("value");
                        printBinCapacity(binCapacity, choice);
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

    public void goToNearestContainer(View view) {

        Intent i = new Intent(CustomerActivity.this, LoadingActivity.class);

        startActivity(i);
    }
}