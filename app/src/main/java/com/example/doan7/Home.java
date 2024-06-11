package com.example.doan7;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

public class Home extends AppCompatActivity {

    private Button btnHome;
    private ImageButton ibIntroduce;
    private ImageButton buttonSelectLanguage;
    private Spinner spinnerLanguages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnHome = (Button) findViewById(R.id.btnHome);
        ibIntroduce = (ImageButton) findViewById(R.id.ibIntroduce);
        buttonSelectLanguage = findViewById(R.id.imgBtSelectLanguage);
        spinnerLanguages = findViewById(R.id.spinnerLanguages);

        String[] languages = {"English", "Vietnamese", "French", "Spanish", "Japanese"};

        // Tạo adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(adapter);

        buttonSelectLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hoặc ẩn Spinner khi nhấn nút
                spinnerLanguages.performClick();
            }
        });

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