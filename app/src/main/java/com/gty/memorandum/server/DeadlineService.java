package com.gty.memorandum.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class DeadlineService extends Service {
    private String TAG = DeadlineService.class.getSimpleName();//标志

    public DeadlineService(){}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent("");
        sendBroadcast(intent);
        Log.d("进程销毁",TAG);

    }



    }
}
