package com.gty.memorandum.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gty.memorandum.R;
import com.gty.memorandum.bean.User;
import com.gty.memorandum.database.UserDatabase;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();

    }

    private void initView() {
        findViewById(R.id.addData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //不能在主线程中操作数据库，放在后台线程中完成,通过使用AsyncTask，Thread，Handler，RxJava
                        //插入数据
                        List<User> userList=new ArrayList<>();
                        User user=new User();
//            user.setId(getNum(10000));
                        user.setAdress("南宁");
                        user.setImg("图片地址");
                        user.setName("这是名字");
                        user.setPassword("这是密码");
                        user.setPhone("这是手机号码");
                        user.setSex("男");

                        userList.add(user);

                        UserDatabase
                                .getInstance(MainActivity2.this)
                                .getUserDao()
                                .insert(userList);

                    } }).start();
            }
        });
        findViewById(R.id.btn_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //后台线程中完成,通过使用AsyncTask，Thread，Handler，RxJava
                        //查询数据
                        List<User> allUsers = UserDatabase
                                .getInstance(MainActivity2.this)
                                .getUserDao()
                                .getAllUserInfo();
                        for (int i = 0; i < allUsers.size(); i++) {
                            Log.i("sklfjsfksnfsfsf", i+"   "+allUsers.get(i).getName());
                        }

                    } }).start();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //后台线程中完成,通过使用AsyncTask，Thread，Handler，RxJava
                        //user表中字段不传值的，都会被设置为null
                        User user=new User(2,"张三","在南宁这里的地址啊");
                        UserDatabase
                                .getInstance(MainActivity2.this)
                                .getUserDao()
                                .updateUserInfo(user);

                    } }).start();
            }
        });
        findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //后台线程中完成,通过使用AsyncTask，Thread，Handler，RxJava
                        //删除数据，删除id为2的数据
                        User user=new User(2);
                        UserDatabase
                                .getInstance(MainActivity2.this)
                                .getUserDao()
                                .deleteInfo(user);

                    } }).start();

            }
        });
    }


}