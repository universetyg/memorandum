package com.gty.memorandum.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gty.memorandum.bean.MyTodo;
import com.gty.memorandum.database.TodoDatabase;
import com.gty.memorandum.server.TimerService;
import com.gty.memorandum.util.DateUtil;
import com.gty.memorandum.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ClockReceiver extends BroadcastReceiver {

    public static final String MINUTES = "MINUTES";
    public static final String HOUR = "HOUR";
    public static final String DAY = "DAY";
    List<MyTodo> myTodos = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (Objects.requireNonNull(intent.getAction())) {
            case Intent.ACTION_TIME_TICK:
                //每过一分钟 触发
                Log.d("minute", MINUTES);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<MyTodo> allTodo = TodoDatabase
                                .getInstance(context)
                                .getTodoDao()
                                .getAllMyTodoInfo();
                        myTodos = allTodo;
                        for (MyTodo myTodo : myTodos) {
                            if (myTodo.getDeadline().split(" ")[0].split("-")[2].compareTo("8") >= 0) {
                                Utils.sendNotification(context,"你有一个待完成的任务","点击查看任务");
                            }
                        }
                    }
                }).start();
                break;
            case Intent.ACTION_TIME_CHANGED:
                //设置了系统时间
                break;
            case Intent.ACTION_TIMEZONE_CHANGED:
                //设置了系统时区的action
                break;
        }

    }

}