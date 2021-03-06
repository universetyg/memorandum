package com.gty.memorandum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.gty.memorandum.MainActivity;
import com.gty.memorandum.R;
import com.gty.memorandum.activity.DetailActivity;
import com.gty.memorandum.bean.MyTodo;
import com.gty.memorandum.database.TodoDatabase;
import com.gty.memorandum.manager.MyLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MyTodoAdapter extends RecyclerView.Adapter<MyTodoAdapter.MyHolder> {

    private List<MyTodo> mTodoList = new ArrayList<>();
    private LongClickLisenter longClickLisenter;
    private OnItemClickListener mOnItemClickListener;
    private TextView edit_list ;
    private RecyclerView.LayoutManager layoutManager;

    private ViewGroup parent;
    Context context;


    public class MyHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDate;
        ImageView circle_not_choose;
        ConstraintLayout cl_item_todo_list;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            circle_not_choose = (ImageView) itemView.findViewById(R.id.circle_not_choose);
            cl_item_todo_list = (ConstraintLayout) itemView.findViewById(R.id.cl_item_todo_list);
        }
    }

    public MyTodoAdapter(List<MyTodo> mTodoList,Context mContext) {
        this.mTodoList = mTodoList;
        this.context = mContext;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todolist,parent,false);
        MyHolder myHolder = new MyHolder(view);


        //长按监听事件
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                int layoutPosition = myHolder.getLayoutPosition();
                if (longClickLisenter != null) {
                    longClickLisenter.LongClickLisenter(layoutPosition);
                }
                return false;
            }
        });

        return myHolder;
    }

    @SuppressLint({"ResourceAsColor", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MyTodo myTodo = mTodoList.get(position);
        holder.tvTitle.setText(myTodo.getTitle());
        holder.tvDate.setText(myTodo.getDeadline());

//        MyLayoutManager manager = new MyLayoutManager(context);  //自定义布局管理器
//        manager.setScrollEnabled(false);  //禁止滑动
//        binding.companyRecy.setLayoutManager(manager);



        //可以发送通知的item都设置为白色背景
        if (myTodo.getAlertItem() || MainActivity.isEdit){
            holder.cl_item_todo_list.setBackgroundResource(R.drawable.white_bg);

        }
        //不允许发送截止日期通知且不是编辑状态，也就是到了截止日期需要完成的
        //编辑状态下的已经提醒过的内容
//        if (!myTodo.getAlertItem()) {
//            if (!MainActivity.isEdit) {
//                if (!myTodo.getFinish()) {//如果已经点击了，设置背景和字体颜色
//                    holder.cl_item_todo_list.setBackgroundResource(R.drawable.white_bg);
//                    holder.tvTitle.setTextColor(R.color.black);
//                    Log.d("myTodo.getAlertItem()", myTodo.getAlertItem().toString());
//                    holder.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                    holder.circle_not_choose.setImageResource(R.mipmap.choose);
//                } else {//如果取消点击，设置背景颜色，以及字体颜色
//                    holder.cl_item_todo_list.setBackgroundResource(R.drawable.teal_bg);
//                    holder.tvTitle.setTextColor(R.color.grey);
//                    holder.tvTitle.getPaint().setFlags(0);
//                    holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
//                }
//            } else {
//                if (myTodo.getClickItem()) {//如果已经点击了，设置背景和字体颜色
//                    holder.circle_not_choose.setImageResource(R.mipmap.choose);
//                } else {//如果取消点击，设置背景颜色，以及字体颜色
//                    holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
//                }
//            }
//        }
        //clickitem这个值为1时，点击了的话
//        if (myTodo.getFinish()){
//            holder.circle_not_choose.setImageResource(R.mipmap.choose);
//
////            holder.cl_item_todo_list.setBackgroundResource(R.drawable.white_bg);
//            if (!MainActivity.isEdit) {//非编辑状态给title划线
//                holder.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            } else {
//                holder.tvTitle.getPaint().setFlags(0);
//            }
////            // 渐渐上滑定位到评论区域，如果页面是网络请求的数据，则可以等页面展示结束再滑动。
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    // position根据自己的需求传入即可
////                    scrollItemToTop(parent,position);
////                }
////            }, 200);
//
//        }else {
//            holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
//            holder.tvTitle.getPaint().setFlags(0);
//        }
//

        if (!MainActivity.isEdit) {
            if (!myTodo.getAlertItem()) {//可以提醒状态,红色的时候
                if (myTodo.getFinish()) {
                    holder.circle_not_choose.setImageResource(R.mipmap.choose);
                    holder.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.cl_item_todo_list.setBackgroundResource(R.drawable.white_bg);
                } else {
                    holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
                    holder.tvTitle.getPaint().setFlags(0);
                    holder.cl_item_todo_list.setBackgroundResource(R.drawable.teal_bg);
                }
            } else {//一般的时候
                if (myTodo.getFinish()) {
                    holder.circle_not_choose.setImageResource(R.mipmap.choose);
                    holder.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
                    holder.tvTitle.getPaint().setFlags(0);
                }
            }
        } else {
            if (myTodo.getClickItem()) {
                holder.circle_not_choose.setImageResource(R.mipmap.choose);
            } else {
                holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
            }
        }


        holder.circle_not_choose.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if (!MainActivity.isEdit) {
                    if (myTodo.getFinish()) {
                        myTodo.setClicItem(false);
                        myTodo.setFinish(false);
                    } else {
                        myTodo.setClicItem(true);
                        myTodo.setFinish(true);
                    }
                } else {
                    myTodo.setClicItem(!myTodo.getClickItem());
                }
                updateData(myTodo,context);
                notifyDataSetChanged();
                selectData();
            }
        });



        //item点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //通过接口名调用方法
                mOnItemClickListener.onItemClick(view,position);
//                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    public interface LongClickLisenter {
        void LongClickLisenter(int position);
    }


    public void setLongClickLisenter(LongClickLisenter longClickLisenter) {
        this.longClickLisenter = longClickLisenter;
    }




    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

//    /**
//     * 指定item并置顶
//     *
//     * @param position item索引
//     */
//    public void scrollItemToTop(ViewGroup parent,int position) {
//        LinearTopSmoothScroller smoothScroller = new LinearTopSmoothScroller(parent.getContext(),false);
//        smoothScroller.setTargetPosition(position);
//        layoutManager.startSmoothScroll(smoothScroller);
//    }


    //更新数据
    private void updateData(MyTodo myTodo, Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                TodoDatabase
                        .getInstance(context)
                        .getTodoDao()
                        .updateMyTodoInfo(myTodo);
                Log.d("update",myTodo.toString());
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
                        .getInstance(context)
                        .getTodoDao()
                        .getAllMyTodoInfo();
                mTodoList.clear();
                mTodoList.addAll(allTodo);
            }
        }).start();

        notifyDataSetChanged();//刷新

    }



}
