package com.gty.memorandum.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gty.memorandum.R;
import com.gty.memorandum.util.Utils;

public class PostActivity extends AppCompatActivity {
    private Button sendNotification;
    private Button updateNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        sendNotification = (Button)findViewById(R.id.bt_send);
        updateNotification = (Button)findViewById(R.id.bt_update);
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendNotification(PostActivity.this);
                //显示更新按钮
                updateNotification.setVisibility(View.VISIBLE);
            }
        });

        updateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.updateNotification();
            }
        });
    }
}
