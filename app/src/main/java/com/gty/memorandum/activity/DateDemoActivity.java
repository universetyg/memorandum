package com.gty.memorandum.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.gty.memorandum.R;

import java.util.Calendar;

public class DateDemoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private DatePicker pickDate;//日期选择器
    private Button chooseDate;//选择日期
    private Button sure;//确定


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_demo);

        chooseDate = findViewById(R.id.chooseDate);
        sure = findViewById(R.id.sure);
        pickDate = (DatePicker)findViewById(R.id.pickDate);

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.chooseDate) {
                    //获取实例，包含当前年月日
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(DateDemoActivity.this, DateDemoActivity.this,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MARCH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                }
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("DefaultLocale")
                String desc = String.format("选中日期%d年%d月%d日",pickDate.getYear(),pickDate.getMonth()+1,pickDate.getDayOfMonth());
                Toast.makeText(DateDemoActivity.this,"当前日期为"+desc,Toast.LENGTH_SHORT).show();


            }
        });


    }


    public void onDateSet(DatePicker pickDate,int year,int month,int day){
        String desc = String.format("选中日期%d年%d月%d日",year,month+1,day);
        Toast.makeText(DateDemoActivity.this,"当前日期为"+desc,Toast.LENGTH_SHORT).show();
//            tv_disdate.setText(desc);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


}