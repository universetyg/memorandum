package com.gty.memorandum.server;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TimerService extends Service {
    private String TAG =TimerService.class.getSimpleName();
    String message;
    int count=1;
    public TimerService() {
    }

    //    创建后台进程
    @Override
    public void onCreate(){
        super.onCreate();
//        super.onDestroy();
//        调用发送函数
        sentMassage();
    }


    //    销毁后台进程
    public void onDestroy(){
        super.onDestroy();
        count=1;
        Log.i(TAG,"进程销毁咯！！");
    }
    //发送创建后机制的发送消息函数
    @SuppressLint("WrongConstant")
    private void sentMassage() {
        Toast.makeText(getApplicationContext(),"创建一个后台任务",0).show();
        stopSelf();
    }
    //    接收消息
    public String getMessage(){
        return message;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
