package com.example.doan7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class introduce extends AppCompatActivity {
    String selectedLanguage;
    ImageButton imgBack;
    TextView tvTiTle,tvIntroduce,tvIntroduce1,tvIntroduce2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        selectedLanguage = MyApplication.getSelectedLanguage();

        imgBack = (ImageButton) findViewById(R.id.imgBack);
        tvTiTle = findViewById(R.id.tvTiTle);
        tvIntroduce = findViewById(R.id.tvIntroduce);
        tvIntroduce1 = findViewById(R.id.tvIntroduce1);
        tvIntroduce2 = findViewById(R.id.tvIntroduce2);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateTextView(selectedLanguage);




    }

    private void updateTextView(String language) {
        switch (language) {
            case "English":
                tvTiTle.setText(getString(R.string.app_title_en));
                tvIntroduce.setText(getString(R.string.introduce_en));
                tvIntroduce1.setText(getString(R.string.introduce1_en));
                tvIntroduce2.setText(getString(R.string.introduce2_en));
                break;
            case "Vietnamese":
                tvTiTle.setText(getString(R.string.app_title_vn));
                tvIntroduce.setText(getString(R.string.introduce_vn));
                tvIntroduce1.setText(getString(R.string.introduce1_vn));
                tvIntroduce2.setText(getString(R.string.introduce2_vn));
                break;
            case "Japanese":
                tvTiTle.setText(getString(R.string.app_title_jp));
                tvIntroduce.setText(getString(R.string.introduce_jp));
                tvIntroduce1.setText(getString(R.string.introduce1_jp));
                tvIntroduce2.setText(getString(R.string.introduce2_jp));


                break;
            default:
                tvTiTle.setText(getString(R.string.app_title_vn));
                tvIntroduce.setText(getString(R.string.introduce_vn));
                tvIntroduce1.setText(getString(R.string.introduce1_en));
                tvIntroduce2.setText(getString(R.string.introduce2_vn));
                break;
        }
    }
}