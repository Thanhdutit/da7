package com.example.doan7;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    Button btnHome;
    ImageButton ibIntroduce, buttonSelectLanguage;
    Spinner spinnerLanguages;
    TextView tvNameApp;
    String selectedLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        selectedLanguage = MyApplication.getSelectedLanguage();


        btnHome = (Button) findViewById(R.id.btnHome);
        ibIntroduce = (ImageButton) findViewById(R.id.ibIntroduce);
        tvNameApp = findViewById(R.id.tvNameApp);

        buttonSelectLanguage = findViewById(R.id.imgBtSelectLanguage);
        spinnerLanguages = findViewById(R.id.spinnerLanguages);

        String[] languages = {"Vietnamese", "English", "Japanese"};

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

        // Thiết lập sự kiện lắng nghe cho Spinner
        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy ngôn ngữ được chọn từ Spinner
                String selectedLanguage = languages[position];
                // Cập nhật ngôn ngữ được chọn trong MyApplication
                MyApplication.setSelectedLanguage(selectedLanguage);
                // Cập nhật giao diện dựa trên ngôn ngữ được chọn
                updateTextView(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không có mục nào được chọn
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this,MainActivity2.class);
                startActivity(myIntent);
            }
        });

        ibIntroduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this, introduce.class);
                startActivity(myIntent);
            }
        });
    }
    private void updateTextView(String language) {
        switch (language) {
            case "English":
                tvNameApp.setText(getString(R.string.app_title_en));
                btnHome.setText(getString(R.string.home_en));
                break;
            case "Vietnamese":
                tvNameApp.setText(getString(R.string.app_title_vn));
                btnHome.setText(getString(R.string.home_vn));
                break;
            case "Japanese":
                tvNameApp.setText(getString(R.string.app_title_jp));
                btnHome.setText(getString(R.string.home_jp));
                break;
            default:
                tvNameApp.setText(getString(R.string.app_title_vn));
                btnHome.setText(getString(R.string.home_vn));
                break;
        }
    }





}