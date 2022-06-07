package com.gty.memorandum.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gty.memorandum.MainActivity;
import com.gty.memorandum.R;
import com.gty.memorandum.bean.MyTodo;
import com.gty.memorandum.database.TodoDatabase;
import com.gty.memorandum.database.UserDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private EditText ed_change_title;
    private TextView tv_time;
    private EditText ed_change_content;
    private TextView createTime;
    private TextView changeTime;
    private ImageView deleteDetail;
    private ImageView saveDetail;
    private Context mContext = DetailActivity.this;
    private List<MyTodo> myTodoList = new ArrayList<>();
    private MyTodo myTodo;
    private View inflate;

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
        deleteDetail = findViewById(R.id.delete_detail);
        saveDetail = findViewById(R.id.save_detail);


        //接收数据
        Intent intent = getIntent();
        ed_change_title.setText(intent.getStringExtra("title"));
        tv_time.setText(intent.getStringExtra("deadline"));
        ed_change_content.setText(intent.getStringExtra("content"));
        createTime.setText(intent.getStringExtra("createTime"));
        if (null!=intent.getStringExtra("updateTime")){
            changeTime.setText(intent.getStringExtra("updateTime"));
        }
        int id = intent.getIntExtra("id",0);
        myTodo = new MyTodo(id);

        //点击保存更新数据
        saveDetail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                //更新数据
                changeTime.setText(getCurrentTime().toString());
                myTodo.setTitle(ed_change_title.getText().toString());
                myTodo.setContent(ed_change_content.getText().toString());
                myTodo.setDeadline(tv_time.getText().toString());
                myTodo.setCreateTime(createTime.getText().toString());
                myTodo.setUpdateTime(changeTime.getText().toString());
//                myTodo.setId(id);
                updateData(myTodo);
                Toast.makeText(DetailActivity.this,"修改成功",Toast.LENGTH_SHORT).show();

            }
        });

        //删除数据
        deleteDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(mContext,R.style.ActionSheetDialogStyle);
                inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete,null);
                //初始化控件
                Button negative = (Button) inflate.findViewById(R.id.negative);//取消
                Button positive = (Button) inflate.findViewById(R.id.positive);//确认
                //  将布局设置给Dialog
                dialog.setContentView(inflate);
                //获取当前Activity所在的窗体
                Window dialogWindow = dialog.getWindow();
                //设置Dialog从窗体底部弹出
                dialogWindow.setGravity(Gravity.CENTER);
                //获得窗体的属性
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                //如果没有这行代码，弹框的内容会自适应，而不会充满父控件
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.y = 40;//设置Dialog距离底部的距离
                //将属性设置给窗体
                dialogWindow.setAttributes(lp);
                dialog.show();//显示对话框

                //数据库和列表删除数据
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        deleteData(myTodo);
                        dialog.dismiss();
                        finish();

                        Toast.makeText(DetailActivity.this, "删除成功",Toast.LENGTH_SHORT).show();
                    }
                });

                //取消
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    //更新数据
    private void updateData(MyTodo myTodo){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TodoDatabase
                        .getInstance(DetailActivity.this)
                        .getTodoDao()
                        .updateMyTodoInfo(myTodo);
                Log.d("update",myTodo.toString());
            }
        }).start();

    }


    //getCurrentTime
    private String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();//取得当前时间的年月日 时分秒
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String createTime = "修改时间："+year + "年" + (month+1) +"月" + day +"日" + hour +":"+minute+":"+second;
        return createTime;
    }

    //删除数据
    private void deleteData(MyTodo myTodo){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TodoDatabase
                        .getInstance(DetailActivity.this)
                        .getTodoDao()
                        .deleteMyTodo(myTodo);
            }
        }).start();
    }


}