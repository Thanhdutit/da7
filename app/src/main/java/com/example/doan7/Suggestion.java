package com.example.doan7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Suggestion extends AppCompatActivity {

    Button btnBack;
    TextView tvTrieuChung, tvDieuTri, tvTenBenh, tvTitleTrieuChung, tvTitleCachChuaTri;

    String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        btnBack = (Button) findViewById(R.id.btnback);
        tvTrieuChung = findViewById(R.id.tvtrieuchung);
        tvDieuTri = findViewById(R.id.tvdieutri);
        tvTenBenh = findViewById(R.id.tvTenBenh);
        selectedLanguage = MyApplication.getSelectedLanguage();
        tvTitleCachChuaTri = findViewById(R.id.tvTitleCachChuaTri);
        tvTitleTrieuChung = findViewById(R.id.tvTitleTrieuChung);
        updateView();

        Intent myIntent = getIntent();
        Bundle myBundel = myIntent.getBundleExtra("mypackage");
        String a = myBundel.getString("trieuchung");
        String b = myBundel.getString("dieutri");
        String c = myBundel.getString("tenbenh");

        tvTrieuChung.setText(formatTextWithNewline(a));
        tvDieuTri.setText(formatTextWithNewline(b));
        tvTenBenh.setText((c));



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private SpannableString formatTextWithNewline(String text) {
        // Bỏ khoảng trắng đầu và cuối chuỗi nếu có

        if (text != null) {
             text.trim(); // Kiểm tra null trước khi gọi trim()
        }

        // Thêm dấu '-' vào đầu đoạn văn bản
        text = "-" + text;
        // Nếu bắt đầu với dấu gạch đầu dòng, giữ nguyên dấu gạch đầu dòng đầu tiên

        text = text.replace(".", ".\n-");

        if (text.endsWith("\n-")) {
            text = text.substring(0, text.length() - 2);
        }

        return new SpannableString(text);
    }

    private void updateView( ) {
        switch (selectedLanguage) {
            case "English":
                tvTitleTrieuChung.setText(getString(R.string.suggestion_Symptoms_en));
                tvTitleCachChuaTri.setText(getString(R.string.suggestion_Treatment_en));
                btnBack.setText(getString(R.string.suggestion_Back_en));
                break;
            case "Vietnamese":
                tvTrieuChung.setText(getString(R.string.suggestion_Symptoms_vn));
                tvTitleCachChuaTri.setText(getString(R.string.suggestion_Treatment_vn));
                btnBack.setText(getString(R.string.suggestion_Back_vn));
                break;
            case "Japanese":
                tvTitleTrieuChung.setText(getString(R.string.suggestion_Symptoms_jp));
                tvTitleCachChuaTri.setText(getString(R.string.suggestion_Treatment_jp));
                btnBack.setText(getString(R.string.suggestion_Back_jp));
                break;
            default:
                tvTitleTrieuChung.setText(getString(R.string.suggestion_Symptoms_vn));
                tvTitleCachChuaTri.setText(getString(R.string.suggestion_Treatment_vn));
                btnBack.setText(getString(R.string.suggestion_Back_vn));
                break;
        }
    }
}