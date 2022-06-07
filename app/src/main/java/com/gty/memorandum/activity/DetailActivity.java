package com.gty.memorandum.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.gty.memorandum.R;

public class DetailActivity extends AppCompatActivity {

    private EditText ed_change_title;
    private TextView tv_time;
    private EditText ed_change_content;
    private TextView createTime;
    private TextView changeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();

    }

    public void initView(){
        ed_change_title = findViewById(R.id.ed_change_title);
        tv_time = findViewById(R.id.tv_time);
        ed_change_content = findViewById(R.id.ed_change_content);
        createTime = findViewById(R.id.createTime);
        changeTime = findViewById(R.id.changeTime);

        Intent intent = getIntent();
        ed_change_title.setText(intent.getStringExtra("title"));
        tv_time.setText(intent.getStringExtra("deadline"));
        ed_change_content.setText(intent.getStringExtra("content"));
        createTime.setText(intent.getStringExtra("createTime"));
        changeTime.setText(intent.getStringExtra("changeTime"));
    }
}