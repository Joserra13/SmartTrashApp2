package dte.masteriot.mdp.smarttrashapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {

    TextView alarmInfoTV;

    Intent inputIntent;
    String alarmInfo = "";
    String alarmId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmInfoTV = findViewById(R.id.textView24);

        //Getting the Intent
        inputIntent = getIntent();

        alarmInfo = inputIntent.getStringExtra("alarmInfo");
        alarmId = inputIntent.getStringExtra("alarmId");

        alarmInfoTV.setText(alarmInfo);
    }

    public void sendACK(View view) {
    }

    public void sendClear(View view) {
    }
}