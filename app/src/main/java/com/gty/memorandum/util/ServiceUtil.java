package com.gty.memorandum.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gty.memorandum.server.TimerService;

import java.util.List;

public class ServiceUtil {
    private final static String ServiceName="com.example.broadcast.TimerService";
    @SuppressLint("LongLogTag")
    public static boolean isServiceRunning(Context context, String className){
        boolean isRunning=false;
        ActivityManager activityManager =(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo>serviceInfos=activityManager.getRunningServices(50);
        if (null==serviceInfos||serviceInfos.size()<1){
            return false;

        }
        for (int i=0;i<serviceInfos.size();i++){
            if (serviceInfos.get(i).service.getClassName().contains(className)){
                isRunning=true;
                break;
            }
        }
        Log.i("ServiceUtil-AlarmManager", className + " isRunning =  " + isRunning);
        return isRunning;
    };
    @SuppressLint("LongLogTag")
    public static void startAMService(Context context){
        Log.i("ServiceUtil-AlarmManager", "invokeTimerPOIService wac called.." );
        PendingIntent alarmSender =null;
        Intent startIntent=new Intent(context, TimerService.class);
        startIntent.setAction(ServiceName);
        try{
            alarmSender=PendingIntent.getService(context,0,startIntent,0);

        }catch (Exception e){
//            异常捕获
//            闹钟异常启动
            Log.i("ServiceUtil-AlarmManager", "failed to start " + e.toString());
        }
        @SuppressLint("ServiceCast") AlarmManager am =(AlarmManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),5*1000,alarmSender);

    }
    @SuppressLint("LongLogTag")
    public static void  cancleAMServicer(Context context){
//        关闭闹钟服务
        Log.i("ServiceUtil-AlarmManager", "cancleAlarmManager to start ");
        Intent intent =new Intent(context,TimerService.class);
        intent.setAction(ServiceName);
        PendingIntent pendingIntent=PendingIntent.getService(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm=(AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }
    //    服务启动
    public static void startHandlerService(Context cxt){
        Intent intent=new Intent(cxt,TimerService.class);
        cxt.startService(intent);

    }
    //    服务关闭
//    服务启动
    public static void stopHandlerService(Context cxt){
        Intent intent=new Intent(cxt,TimerService.class);
        cxt.stopService(intent);

    }
}


