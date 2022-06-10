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

    private List<MyTodo> myTodoList = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private Context mContext = MainActivity.this;
    private View inflate;
    MyTodoAdapter myTodoAdapter;
    NumberPicker yearPicker;
    NumberPicker monthPicker;
    NumberPicker datePicker;
    NumberPicker hourPicker;
    NumberPicker minutePicker;
    TextView setDate;
    TextView edit_list;
    private TimerService myservice = null;//绑定的service对象
    private ConstraintLayout constraint;
    public static boolean isEdit = false;
    private TextView choose_all;
    private TextView choose_delete;



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
        //开始绑定
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
        edit_list = findViewById(R.id.edit_list);//编辑
        constraint = findViewById(R.id.constraint);//下面全选删除
        ImageView circle_not_choose = findViewById(R.id.circle_not_choose);
        choose_all = findViewById(R.id.choose_all);
        choose_delete = findViewById(R.id.choose_delete);

        //全选
        choose_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (MyTodo myTodo : myTodoList) {
                    myTodo.setClicItem(true);
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
                        myTodoAdapter.notifyDataSetChanged();
                    }
                }
                selectData();
            }
        });


        //弹出添加对话框dialog
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
                //填充对话框的布局
                inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_add, null);
                // setCancelable(iscancelable);//点击外部不可dismiss
                //setCanceledOnTouchOutside(isBackCanCelable);
                //初始化控件
                EditText etAddTask = (EditText) inflate.findViewById(R.id.et_add_task);
                ImageView putTask = (ImageView) inflate.findViewById(R.id.put_task);
                 setDate = (TextView) inflate.findViewById(R.id.set_date);
                ImageView dateLogo = (ImageView) inflate.findViewById(R.id.date_logo);
                EditText etContent = (EditText) inflate.findViewById(R.id.et_content);
                TextView createTime = (TextView) inflate.findViewById(R.id.createTime);
                //将布局设置给Dialog
                dialog.setContentView(inflate);
                //获取当前Activity所在的窗体
                Window dialogWindow = dialog.getWindow();
                //设置Dialog从窗体底部弹出
                dialogWindow.setGravity(Gravity.BOTTOM);
                //获得窗体的属性
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                //如果没有这行代码，弹框的内容会自适应，而不会充满父控件
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.y = 40;//设置Dialog距离底部的距离
                //将属性设置给窗体
                dialogWindow.setAttributes(lp);
                dialog.show();//显示对话框

                dateLogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPicker();
                    }
                });

                //插入数据
                putTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<MyTodo> myTodoList = new ArrayList<>();
                        MyTodo myTodo = new MyTodo();
                        myTodo.setTitle(etAddTask.getText().toString());
                        myTodo.setContent(etContent.getText().toString());
                        myTodo.setDeadline(setDate.getText().toString());
                        myTodo.setCreateTime(getCurrentTime());//创建时间

                        if (myTodo.getTitle().equals("") || myTodo.getContent().equals("") || myTodo.getDeadline().equals("设置截止日期")){
                            Toast.makeText(MainActivity.this, "请输入标题、内容和截止日期",Toast.LENGTH_SHORT).show();
                        }else{
                            myTodoList.add(myTodo);
                            addData(myTodoList);
                            dialog.dismiss();//消失
                            selectData();
                            Log.d("insert",myTodo.toString());
                            Toast.makeText(MainActivity.this, "添加成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //上拉加载下拉刷新
        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                selectData();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                selectData();
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        //长按弹出删除对话框dialog删除
        myTodoAdapter.setLongClickLisenter(new MyTodoAdapter.LongClickLisenter() {
            @Override
            public void LongClickLisenter(int position) {
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

                //删除数据
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        deleteData(myTodoList.get(position));
                        dialog.dismiss();
                        selectData();
                        Log.d("myTodoList",myTodoList.toString());

                        Toast.makeText(MainActivity.this, "删除成功",Toast.LENGTH_SHORT).show();
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

        //跳转
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
                startActivity(intent);
            }
        });

        //点击编辑
        edit_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("编辑".equals(edit_list.getText().toString())){
                    edit_list.setText("取消");
                    isEdit = true;
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
                    edit_list.setText("编辑");
                    isEdit = false;
                    constraint.setVisibility(View.INVISIBLE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    //添加
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

    //查询
    @SuppressLint("NotifyDataSetChanged")
    private void selectData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MyTodo> allTodo = TodoDatabase
                        .getInstance(MainActivity.this)
                        .getTodoDao()
                        .getAllMyTodoInfo();
                myTodoList.clear();
                myTodoList.addAll(allTodo);
            }
        }).start();

        myTodoAdapter.notifyDataSetChanged();//刷新

    }

    //删除
    private void deleteData(MyTodo... myTodo){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TodoDatabase
                        .getInstance(MainActivity.this)
                        .getTodoDao()
                        .deleteMyTodo(myTodo);

                Log.d("del",myTodoList.toString());
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
        //填充对话框的布局
        inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_date_time_picker, null);

        Calendar calendar = Calendar.getInstance();
        yearPicker = inflate.findViewById(R.id.number_picker_year);
         monthPicker = inflate.findViewById(R.id.number_picker_month);
         datePicker = inflate.findViewById(R.id.number_picker_date);
         hourPicker = inflate.findViewById(R.id.number_picker_hour);
         minutePicker = inflate.findViewById(R.id.number_picker_minute);
         Button timeSure = inflate.findViewById(R.id.time_sure);
         Button timeCancel = inflate.findViewById(R.id.time_cancel);

//限制年份范围为前后五年
        int yearNow = calendar.get(Calendar.YEAR);
        yearPicker.setMinValue(yearNow - 5);
        yearPicker.setMaxValue(yearNow + 5);
        yearPicker.setValue(yearNow);
        yearPicker.setWrapSelectorWheel(false);  //关闭选择器循环

//设置月份范围为1~12
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(calendar.get(Calendar.MONTH) + 1);
        monthPicker.setWrapSelectorWheel(false);

//日期限制存在变化，需要根据当月最大天数来调整
        datePicker.setMinValue(1);
        datePicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        datePicker.setValue(calendar.get(Calendar.DATE));
        datePicker.setWrapSelectorWheel(false);

//24小时制，限制小时数为0~23
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        hourPicker.setWrapSelectorWheel(false);

//限制分钟数为0~59
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(calendar.get(Calendar.MINUTE));
        minutePicker.setWrapSelectorWheel(false);

//为年份和月份设置监听
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
                //重设日期值，防止月份变动时超过最大值
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
                //重设日期值，防止月份变动时超过最大值
                datePicker.setValue(Math.min(dateValue, maxValue));
            }
        });



        //将布局设置给Dialog
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

        //确定
        timeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取的日期时间结果
                String result = String.format(Locale.CHINA, "%d-%02d-%02d %02d:%02d",
                        yearPicker.getValue(), monthPicker.getValue(), datePicker.getValue(),
                        hourPicker.getValue(), minutePicker.getValue());

                setDate.setText(result);
                Log.d("result",result);
                dialog.dismiss();
            }
        });

        //取消
        timeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



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
        //获取的日期时间结果
        String result2 = String.format(Locale.CHINA, "%d-%02d-%02d %02d:%02d",
                year, month, day,hour,minute);
        return "创建时间："+result2;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        localBroadcastManager.unregisterReceiver(receiver);//注销本地广播
//        localBroadcastManager.unregisterReceiver(receiver2);
    }









}