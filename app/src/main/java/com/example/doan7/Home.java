package com.example.doan7;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Home extends AppCompatActivity {

    Button btnHome;
    ImageButton ibIntroduce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnHome = (Button) findViewById(R.id.btnHome);
        ibIntroduce = (ImageButton) findViewById(R.id.ibIntroduce);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this,MainActivity.class);
                startActivity(myIntent);
            }
        });

        ibIntroduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Prediction", "văn thành " );
                Intent myIntent = new Intent(Home.this, introduce.class);
                startActivity(myIntent);
            }
        });
    }
}