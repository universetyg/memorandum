package com.gty.memorandum.server;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.gty.memorandum.bean.MyTodo;
import com.gty.memorandum.database.TodoDatabase;
import com.gty.memorandum.receiver.ClockReceiver;

import java.util.ArrayList;
import java.util.List;

public class TimerService extends Service {
    private String TAG =TimerService.class.getSimpleName();
    String message;
    int count=1;
    List<MyTodo> myTodos = new ArrayList<>();

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

        IntentFilter intentFilter;
        ClockReceiver timeChangeReceiver;
        // 时间监听

        intentFilter = new IntentFilter();

        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化

        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);//设置了系统时区

        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);//设置了系统时间

        timeChangeReceiver = new ClockReceiver();

        getApplication().registerReceiver(timeChangeReceiver, intentFilter);


//        Toast.makeText(getApplicationContext(),"创建一个后台任务",0).show();
        selectData();
//        stopSelf();
    }
    //    接收消息
    public String getMessage(){
        return message;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //查询
    private void selectData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MyTodo> allTodo = TodoDatabase
                        .getInstance(TimerService.this)
                        .getTodoDao()
                        .getAllMyTodoInfo();
                myTodos = allTodo;
                for (MyTodo myTodo : myTodos) {
                    if (myTodo.getDeadline().equals("2022-6-8 15:28")) {
//                        Utils.sendNotification(TimerService.this);
                    }
                }
            }
        }).start();

    }
}






