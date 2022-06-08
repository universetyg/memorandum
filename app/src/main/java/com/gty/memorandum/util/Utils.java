package com.gty.memorandum.util;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.gty.memorandum.MainActivity;
import com.gty.memorandum.R;
import com.gty.memorandum.receiver.ClockReceiver;

public class Utils {
    private  static NotificationManager mNotificationManager;
    private static NotificationCompat.Builder mBuilder;
    String title;
    String content;

    /**
     * 发送通知
     */
    public static void sendNotification(Context context,String title,String content){
        //设置 channel_id
        final String CHANNAL_ID = "chat";

        //获取 PendingIntent 对象，NotificationActivity是另一个活动
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0 ,intent, 0);

        //获取系统通知服务
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Android 8.0开始要设置通知渠道
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNAL_ID,
                    "chat message",NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        //创建通知
        mBuilder = new NotificationCompat.Builder(context,CHANNAL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon))
                .setContentIntent(pi)
                .setAutoCancel(true);

        //发送通知( id唯一,可用于更新通知时对应旧通知; 通过mBuilder.build()拿到notification对象 )
        mNotificationManager.notify(1,mBuilder.build());
    }

    /**
     * 更新通知
     */
    public static void updateNotification(){
        mBuilder.setContentTitle("你更新了通知标题");
        mBuilder.setContentText("你更新了通知内容");
        mNotificationManager.notify(1,mBuilder.build());
    }

    public static void setAlarm(Context context, long timeOfAlarm) {

        Intent broadcastIntent = new Intent(context, ClockReceiver.class);

        PendingIntent pIntent = PendingIntent.getBroadcast(
                context, 0, broadcastIntent, 0
        );

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (System.currentTimeMillis() < timeOfAlarm) {
            if (alarmMgr != null) {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, timeOfAlarm, pIntent);

                // 非重复闹铃设置
//                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                        SystemClock.elapsedRealtime() + 60 * 1000,
//                        pIntent
//                );
                // 重复性闹铃设置
                alarmMgr.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        timeOfAlarm,
                        AlarmManager.INTERVAL_DAY,
                        pIntent
                );
            }
        }
    }
}