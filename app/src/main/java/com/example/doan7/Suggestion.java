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

    Button btnback;
    TextView tvTrieuChung, tvDieuTri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        btnback = (Button) findViewById(R.id.btnback);
        tvTrieuChung = findViewById(R.id.tvtrieuchung);
        tvDieuTri = findViewById(R.id.tvdieutri);


        Intent myintent = getIntent();
        Bundle mybundel = myintent.getBundleExtra("mypackage");
        String a = mybundel.getString("trieuchung");
        String b = mybundel.getString("dieutri");

        tvTrieuChung.setText(formatTextWithNewline(a));
        tvDieuTri.setText(formatTextWithNewline(b));


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private SpannableString formatTextWithNewline(String text) {
        // Bỏ khoảng trắng đầu và cuối chuỗi nếu có

        text = text.trim();

        // Thêm dấu '-' vào đầu đoạn văn bản
        text = "-" + text;
        // Nếu bắt đầu với dấu gạch đầu dòng, giữ nguyên dấu gạch đầu dòng đầu tiên

        text = text.replace(".", ".\n-");

        if (text.endsWith("-")) {
            text = text.substring(0, text.length() - 1);
        }

        return new SpannableString(text);
    }
}