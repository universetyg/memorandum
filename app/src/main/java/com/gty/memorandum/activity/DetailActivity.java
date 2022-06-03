package com.gty.memorandum.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gty.memorandum.R;

public class DetailActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        String ss = intent.getStringExtra("data");
        textView.setText(ss);
    }
}