package com.gty.memorandum.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gty.memorandum.R;
import com.gty.memorandum.server.TimerService;
import com.gty.memorandum.util.ServiceUtil;

public class DemoActivity extends AppCompatActivity {

    // private TimerService mTimerService;
    private final String MESSAGE = "message";
    private Context mContext;
    private TextView tv, tv2;
    private Button bt1, bt2, bt3, bt4, bt5, bt6,bt7;
    private final int Time = 5 * 1000;
    private boolean isHanderType = false;
    private static final String ACTION_NAME = "android.intent.action.alarm.timer";
    private static final String ACTION_NAME2 = "android.intent.action.handler.timer";
    private int countHandler = 1;// handler广播发送次数计数器
    private int countAlarm = 0;// alarm广播发送次数计数器
    Handler handler = new Handler();


    // 注册控件，注册控件监听
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo);
        mContext = DemoActivity.this;
        tv = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        bt1 = (Button) findViewById(R.id.button1);
        bt2 = (Button) findViewById(R.id.button2);

        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);
        bt5 = (Button) findViewById(R.id.button5);

        bt6 = (Button) findViewById(R.id.button6);

        bt7 = (Button) findViewById(R.id.button7);
        bt1.setOnClickListener(onClickListener);
        bt2.setOnClickListener(onClickListener);
        bt3.setOnClickListener(onClickListener);
        bt4.setOnClickListener(onClickListener);
        bt5.setOnClickListener(onClickListener);
        bt6.setOnClickListener(onClickListener);
        bt7.setOnClickListener(onClickListener);

    }


    Runnable runnable = new Runnable() {
        @Override
//    设置广播发送
        public void run() {
            // TODO Auto-generated method stub
//设置发布延时
            handler.postDelayed(runnable, Time);

            Intent mIntent = new Intent(ACTION_NAME2);

            mIntent.putExtra(MESSAGE, "这是第" + countHandler + "次"
                    + "Handler发送的广播,  接下来是第" + countHandler + "次");

            sendBroadcast(mIntent);

        }
    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Intent intent = new Intent(mContext, TimerService.class);
            startService(intent);
            handler.postDelayed(runnable2, Time);

        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String message = intent.getStringExtra(MESSAGE);

            if (action.equals(ACTION_NAME)) {

                tv.setText("第" + countAlarm + "发送"
                        + "AlarmManager的Alarm广播,  接下来发送第" + countAlarm
                        + "次广播");

                tv2.setText("与handler广播不同，我们的alarm广播是首先执行一次后，就得等待5s后在执行，并且发送的时候只执行一次");
                // Toast.makeText(MainActivity.this, message+countAlarm,
                // 0).show();
                countAlarm++;
            } else if (action.equals(ACTION_NAME2)) {

                if (!TextUtils.isEmpty(message)) {
                    tv2.setText(message);
                }

                countHandler++;

            }

        }

    };

    // ServiceConnection conn = new ServiceConnection() {
    // @Override
    // public void onServiceDisconnected(ComponentName name) {
    //
    // }
    //
    // @Override
    // public void onServiceConnected(ComponentName name, IBinder service) {
    // // TODO Auto-generated method stub
    // mTimerService = ((TimerService.MsgBinder)service).getService();
    // tv.setText(mTimerService.getMessage());
    // }
    // };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//注册广播
        registerBoradcastReceiver();

    }



    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.button1:
                    sendTimerBoaadCastReceiver(true,-1);
                    bt1.setEnabled(false);
                    break;
                case R.id.button2:
                    sendTimerBoaadCastReceiver(false,2);
                    bt2.setEnabled(false);
                    break;
                case R.id.button3:
                    sendTimerService(true);
                    bt3.setEnabled(false);
                    break;
                case R.id.button4:
                    sendTimerService(false);
                    bt4.setEnabled(false);
                    break;
                case R.id.button5:
                    cancelAlLBR();
                    break;
                case R.id.button6:
                    cancelAlLService();
                    break;
                case R.id.button7:
                    sendTimerBoaadCastReceiver(false,1);
                    bt7.setEnabled(false);
                    break;

            }

        }
    };

//注册广播

    private void registerBoradcastReceiver() {

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        myIntentFilter.addAction(ACTION_NAME2);
        //注册广播

        registerReceiver(mBroadcastReceiver, myIntentFilter);

    }


    @SuppressLint("WrongConstant")
    private void sendTimerBoaadCastReceiver(boolean isHandler, int state) {
//    判断是哪一种广播方式
        if (isHandler) {
//       使用Handle方式
//       设定多久执行一次runnable

            handler.postDelayed(runnable, Time);

        } else {

            Intent mIntent = new Intent(ACTION_NAME);
            // 发送广播
            Toast.makeText(DemoActivity.this, "发送广播，开始进攻", 0).show();

            sendBroadcast(mIntent);
//以alarmManager的方式发送广播

            PendingIntent pendIntent = PendingIntent.getBroadcast(mContext, 0,
                    mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//注册闹铃
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);


            switch (state) {
                /**
                 * 5秒后发送广播，只发送一次 elapsedRealtime() and elapsedRealtimeNanos()
                 * 返回系统启动到现在的时间，包含设备深度休眠的时间。该时钟被保证是单调的，
                 * 即使CPU在省电模式下，该时间也会继续计时。该时钟可以被使用在当测量时间间隔可能跨越系统睡眠的时间段。
                 *
                 */
                case 1:

//   AlarmManager提供的方法： void set(int type, long
//  triggerAtTime,PendingIntent operation) 设置一个闹钟
                    long triggerAtTime = SystemClock.elapsedRealtime() + Time;
                    manager.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                            pendIntent);
                    break;
                case 2:
//设置间隔多少秒执行闹钟

                    manager.setInexactRepeating(
                            AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime(), Time, pendIntent);
                    break;
                case 3:
                    break;
                case 4:
                    break;

            }

        }

    }

    //取消handler闹钟
    private void cancelHandlerBR() {

        handler.removeCallbacks(runnable);
        countHandler = 1;
        tv2.setText("敌军还有5s到达，击垮他们");

    }
    //取消AlarmManager闹钟
    private void cancelAlarmManagerBR() {
        Intent mIntent = new Intent(ACTION_NAME);

        PendingIntent pendIntent = PendingIntent.getBroadcast(mContext, 0,
                mIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.cancel(pendIntent);

        countAlarm = 0;
//    改变text控件内容

        tv.setText("敌军还有5s到达，击垮他们");
    }
// 取消广播服务

    private void cancelAlLBR() {

        cancelHandlerBR();
        cancelAlarmManagerBR();
        bt1.setEnabled(true);
        bt2.setEnabled(true);
        bt7.setEnabled(true);

    }

    private void cancelAlLService() {
        handler.removeCallbacks(runnable2);
        ServiceUtil.cancleAMServicer(mContext);
        ServiceUtil.stopHandlerService(mContext);
//让按钮可以点击
        bt3.setEnabled(true);
        bt4.setEnabled(true);
        bt6.setEnabled(true);
        //    改变text控件内容


        tv2.setText("敌军还有5s到达，击垮他们");
        tv.setText("敌军还有5s到达，击垮他们");

    }

    //发送定时服务
    private void sendTimerService(boolean isHandler) {

        if (isHandler) {
            handler.postDelayed(runnable2, Time);

        } else {
            ServiceUtil.startAMService(mContext);

        }

    }
// 销毁进程

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        cancelAlLBR();
        cancelAlLService();
        // unbindService(conn);
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

}
