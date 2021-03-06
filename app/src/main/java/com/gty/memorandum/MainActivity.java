package com.gty.memorandum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gty.memorandum.activity.DetailActivity;
import com.gty.memorandum.adapter.MyTodoAdapter;
import com.gty.memorandum.bean.MyTodo;
import com.gty.memorandum.database.TodoDatabase;
import com.gty.memorandum.server.TimerService;
import com.gty.memorandum.util.Utils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static List<MyTodo> myTodoList = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private Context mContext = MainActivity.this;
    private View inflate;
    public static MyTodoAdapter myTodoAdapter;
    NumberPicker yearPicker;
    NumberPicker monthPicker;
    NumberPicker datePicker;
    NumberPicker hourPicker;
    NumberPicker minutePicker;
    TextView setDate;
    TextView edit_list;
    private TimerService myservice = null;//?????????service??????
    private ConstraintLayout constraint;
    public static boolean isEdit = false;
    private TextView choose_all;
    private TextView choose_delete;
    private List<MyTodo> myTodo1 = new ArrayList<>();



//    public static final String BROADCAST_ACTION="com.test.TestBroadcast";
//    private TestBroadcastReceiver receiver;
//    private TempReceiver receiver2;
//    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        initView();
        Intent intent = new Intent(MainActivity.this, TimerService.class);
        startService(intent);


//        registerBroadcast();
        //????????????
//        Intent intent = new Intent(MainActivity.this, TimerService.class);
//        bindService(intent,conn,Context.BIND_AUTO_CREATE);
//        postBroadcast();



    }

    public void initView() {
        //recyclerview
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myTodoAdapter = new MyTodoAdapter(myTodoList,MainActivity.this);
        recyclerView.setAdapter(myTodoAdapter);
        edit_list = findViewById(R.id.edit_list);//??????
        constraint = findViewById(R.id.constraint);//??????????????????
        ImageView circle_not_choose = findViewById(R.id.circle_not_choose);
        choose_all = findViewById(R.id.choose_all);
        choose_delete = findViewById(R.id.choose_delete);

        //??????
        choose_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (MyTodo myTodo : myTodoList) {
                    myTodo.setClicItem(true);
                    updateData(myTodo);
                }
                myTodoAdapter.notifyDataSetChanged();
            }
        });

        choose_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<MyTodo> iterator=myTodoList.iterator();
                while(iterator.hasNext()){
                    MyTodo myTodo=iterator.next();
                    if (myTodo.getClickItem()) {
                        deleteData(myTodo);
                        iterator.remove();
//                        myTodoAdapter.notifyDataSetChanged();
                    }
                }
                myTodoAdapter.notifyDataSetChanged();
                selectData();
                Toast.makeText(mContext,"?????????????????????????????????",Toast.LENGTH_SHORT).show();
            }
        });


        //?????????????????????dialog
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
                //????????????????????????
                inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_add, null);
                // setCancelable(iscancelable);//??????????????????dismiss
                //setCanceledOnTouchOutside(isBackCanCelable);
                //???????????????
                EditText etAddTask = (EditText) inflate.findViewById(R.id.et_add_task);
                ImageView putTask = (ImageView) inflate.findViewById(R.id.put_task);
                 setDate = (TextView) inflate.findViewById(R.id.set_date);
                ImageView dateLogo = (ImageView) inflate.findViewById(R.id.date_logo);
                EditText etContent = (EditText) inflate.findViewById(R.id.et_content);
                TextView createTime = (TextView) inflate.findViewById(R.id.createTime);

                //??????????????????Dialog
                dialog.setContentView(inflate);
                //????????????Activity???????????????
                Window dialogWindow = dialog.getWindow();
                //??????Dialog?????????????????????
                dialogWindow.setGravity(Gravity.BOTTOM);
                //?????????????????????
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                //?????????????????????????????????????????????????????????????????????????????????
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.y = 40;//??????Dialog?????????????????????
                //????????????????????????
                dialogWindow.setAttributes(lp);
                dialog.show();//???????????????

                dateLogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPicker();
                    }
                });

                setDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPicker();
                    }
                });

                //????????????
                putTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<MyTodo> myTodoList = new ArrayList<>();
                        MyTodo myTodo = new MyTodo();
                        myTodo.setTitle(etAddTask.getText().toString());
                        myTodo.setContent(etContent.getText().toString());
                        myTodo.setDeadline(setDate.getText().toString());
                        myTodo.setCreateTime(getCurrentTime());//????????????

                        if (myTodo.getTitle().equals("") || myTodo.getContent().equals("")){
                            Toast.makeText(MainActivity.this, "???????????????????????????????????????",Toast.LENGTH_SHORT).show();
                        }else{
                            myTodoList.add(myTodo);
                            addData(myTodoList);
                            dialog.dismiss();//??????
                            selectData();
                            Log.d("insert",myTodo.toString());
                            Toast.makeText(MainActivity.this, "????????????",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //????????????????????????
        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
//        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                selectData();
                refreshlayout.finishRefresh(/*,false*/);//??????false??????????????????
            }
        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                selectData();
//                refreshlayout.finishLoadMore(2000/*,false*/);//??????false??????????????????
//            }
//        });

        //???????????????????????????dialog??????
        myTodoAdapter.setLongClickLisenter(new MyTodoAdapter.LongClickLisenter() {
            @Override
            public void LongClickLisenter(int position) {
                Dialog dialog = new Dialog(mContext,R.style.ActionSheetDialogStyle);
                inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_delete,null);
                //???????????????
                Button negative = (Button) inflate.findViewById(R.id.negative);//??????
                Button positive = (Button) inflate.findViewById(R.id.positive);//??????
                //  ??????????????????Dialog
                dialog.setContentView(inflate);
                //????????????Activity???????????????
                Window dialogWindow = dialog.getWindow();
                //??????Dialog?????????????????????
                dialogWindow.setGravity(Gravity.CENTER);
                //?????????????????????
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                //?????????????????????????????????????????????????????????????????????????????????
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.y = 40;//??????Dialog?????????????????????
                //????????????????????????
                dialogWindow.setAttributes(lp);
                dialog.show();//???????????????

                //????????????
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        deleteData(myTodoList.get(position));
                        dialog.dismiss();
                        selectData();
                        Log.d("myTodoList",myTodoList.toString());

                        Toast.makeText(MainActivity.this, "????????????",Toast.LENGTH_SHORT).show();
                    }
                });

                //??????
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //??????
        myTodoAdapter.setOnItemClickListener(new MyTodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",myTodoList.get(position).getTitle());
                bundle.putString("deadline",myTodoList.get(position).getDeadline());
                bundle.putString("content",myTodoList.get(position).getContent());
                bundle.putString("createTime",myTodoList.get(position).getCreateTime());
//                bundle.putInt("position",position);
                if(null != myTodoList.get(position).getUpdateTime()){
                    bundle.putString("updateTime",myTodoList.get(position).getUpdateTime());

                }
                bundle.putInt("id",myTodoList.get(position).getId());
                intent.putExtras(bundle);
                if (!MainActivity.isEdit) {
                    startActivity(intent);
                } else {
                    if (myTodoList.get(position).getClickItem()) {
                        myTodoList.get(position).setClicItem(false);
                    } else {
                        myTodoList.get(position).setClicItem(true);
                    }
                    updateData(myTodoList.get(position));
                    myTodoAdapter.notifyDataSetChanged();
                }
            }
        });

        //????????????
        edit_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("??????".equals(edit_list.getText().toString())){
                    edit_list.setText("??????");
                    isEdit = true;
                    for (MyTodo myTodo : myTodoList) {
                        myTodo.setClicItem(false);
                        updateData(myTodo);
                    }
                    myTodoAdapter.notifyDataSetChanged();
                    constraint.setVisibility(View.VISIBLE);
                    floatingActionButton.setVisibility(View.INVISIBLE);

//                    myTodoAdapter.setOnItemClickListener(new MyTodoAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            circle_not_choose.setImageResource(R.mipmap.choose);
//                            myTodoList.get(position).set
//                        }
//                    });
//                    myTodoAdapter.setOnItemClickListener(null);
                }else{
                    edit_list.setText("??????");
                    isEdit = false;
                    for (MyTodo myTodo : myTodoList) {
                        myTodo.setClicItem(false);
                        updateData(myTodo);
                    }
                    myTodoAdapter.notifyDataSetChanged();
                    constraint.setVisibility(View.INVISIBLE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    //??????
    private void addData(List<MyTodo> myTodoLists) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TodoDatabase
                        .getInstance(MainActivity.this)
                        .getTodoDao()
                        .insert(myTodoLists);
            }
        }).start();
    }

    //??????
    @SuppressLint("NotifyDataSetChanged")
    private void selectData() {
        myTodoList.clear();
        myTodoAdapter.notifyDataSetChanged();
        new Thread(new Runnable() {
            List<MyTodo> allTodo = new ArrayList<>();
            @Override
            public void run() {
                allTodo = TodoDatabase
                        .getInstance(MainActivity.this)
                        .getTodoDao()
                        .getAllMyTodoInfo();
                myTodoList.addAll(allTodo);
            }
        }).start();
        myTodoAdapter.notifyDataSetChanged();//??????
    }

    //??????
    private void deleteData(MyTodo... myTodo){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TodoDatabase
                        .getInstance(MainActivity.this)
                        .getTodoDao()
                        .deleteMyTodo(myTodo);

//                Log.d("del",myTodoList.toString());
            }
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        selectData();
    }

    //timePicker,dialog
    private void showPicker(){

        Dialog dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        //????????????????????????
        inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_date_time_picker, null);

        Calendar calendar = Calendar.getInstance();
        yearPicker = inflate.findViewById(R.id.number_picker_year);
         monthPicker = inflate.findViewById(R.id.number_picker_month);
         datePicker = inflate.findViewById(R.id.number_picker_date);
         hourPicker = inflate.findViewById(R.id.number_picker_hour);
         minutePicker = inflate.findViewById(R.id.number_picker_minute);
         Button timeSure = inflate.findViewById(R.id.time_sure);
         Button timeCancel = inflate.findViewById(R.id.time_cancel);

//?????????????????????????????????
        int yearNow = calendar.get(Calendar.YEAR);
        yearPicker.setMinValue(yearNow - 5);
        yearPicker.setMaxValue(yearNow + 5);
        yearPicker.setValue(yearNow);
        yearPicker.setWrapSelectorWheel(false);  //?????????????????????

//?????????????????????1~12
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(calendar.get(Calendar.MONTH) + 1);
        monthPicker.setWrapSelectorWheel(false);

//??????????????????????????????????????????????????????????????????
        datePicker.setMinValue(1);
        datePicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        datePicker.setValue(calendar.get(Calendar.DATE));
        datePicker.setWrapSelectorWheel(false);

//24??????????????????????????????0~23
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        hourPicker.setWrapSelectorWheel(false);

//??????????????????0~59
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(calendar.get(Calendar.MINUTE));
        minutePicker.setWrapSelectorWheel(false);

//??????????????????????????????
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String dateStr = String.format(Locale.CHINA, "%d-%d", yearPicker.getValue(), monthPicker.getValue());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(simpleDateFormat.parse(dateStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int dateValue = datePicker.getValue();
                int maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                datePicker.setMaxValue(maxValue);
                //??????????????????????????????????????????????????????
                datePicker.setValue(Math.min(dateValue, maxValue));
            }
        });
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String dateStr = String.format(Locale.CHINA, "%d-%d", yearPicker.getValue(), monthPicker.getValue());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(simpleDateFormat.parse(dateStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int dateValue = datePicker.getValue();
                int maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                datePicker.setMaxValue(maxValue);
                //??????????????????????????????????????????????????????
                datePicker.setValue(Math.min(dateValue, maxValue));
            }
        });



        //??????????????????Dialog
        dialog.setContentView(inflate);
        //????????????Activity???????????????
        Window dialogWindow = dialog.getWindow();
        //??????Dialog?????????????????????
        dialogWindow.setGravity(Gravity.CENTER);
        //?????????????????????
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //?????????????????????????????????????????????????????????????????????????????????
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.y = 40;//??????Dialog?????????????????????
        //????????????????????????
        dialogWindow.setAttributes(lp);
        dialog.show();//???????????????

        //??????
        timeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //???????????????????????????
                String result = String.format(Locale.CHINA, "%d-%02d-%02d %02d:%02d",
                        yearPicker.getValue(), monthPicker.getValue(), datePicker.getValue(),
                        hourPicker.getValue(), minutePicker.getValue());

                setDate.setText(result);
                Log.d("result",result);
                dialog.dismiss();
            }
        });

        //??????
        timeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



    }

    //getCurrentTime
    private String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();//?????????????????????????????? ?????????
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        //???????????????????????????
        String result2 = String.format(Locale.CHINA, "%d-%02d-%02d %02d:%02d",
                year, month, day,hour,minute);
        return "???????????????"+result2;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        localBroadcastManager.unregisterReceiver(receiver);//??????????????????
//        localBroadcastManager.unregisterReceiver(receiver2);
    }


    //????????????
    private void updateData(MyTodo myTodo){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TodoDatabase
                        .getInstance(MainActivity.this)
                        .getTodoDao()
                        .updateMyTodoInfo(myTodo);
                Log.d("update",myTodo.toString());

            }
        }).start();

    }








}