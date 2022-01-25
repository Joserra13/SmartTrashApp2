package dte.masteriot.mdp.smarttrashapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button b1;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
    }

    public void customerView(View view){

        Intent intent = new Intent(MainActivity.this, CustomerActivity.class);
        startActivity(intent);
    }

    public void garbageView(View view) {

        Intent intent = new Intent(MainActivity.this, WorkersList.class);
        startActivity(intent);
    }
}