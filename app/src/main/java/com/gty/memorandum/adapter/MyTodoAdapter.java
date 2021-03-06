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


        //??????????????????
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

//        MyLayoutManager manager = new MyLayoutManager(context);  //????????????????????????
//        manager.setScrollEnabled(false);  //????????????
//        binding.companyRecy.setLayoutManager(manager);



        //?????????????????????item????????????????????????
        if (myTodo.getAlertItem() || MainActivity.isEdit){
            holder.cl_item_todo_list.setBackgroundResource(R.drawable.white_bg);

        }
        //???????????????????????????????????????????????????????????????????????????????????????????????????
        //??????????????????????????????????????????
//        if (!myTodo.getAlertItem()) {
//            if (!MainActivity.isEdit) {
//                if (!myTodo.getFinish()) {//???????????????????????????????????????????????????
//                    holder.cl_item_todo_list.setBackgroundResource(R.drawable.white_bg);
//                    holder.tvTitle.setTextColor(R.color.black);
//                    Log.d("myTodo.getAlertItem()", myTodo.getAlertItem().toString());
//                    holder.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                    holder.circle_not_choose.setImageResource(R.mipmap.choose);
//                } else {//????????????????????????????????????????????????????????????
//                    holder.cl_item_todo_list.setBackgroundResource(R.drawable.teal_bg);
//                    holder.tvTitle.setTextColor(R.color.grey);
//                    holder.tvTitle.getPaint().setFlags(0);
//                    holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
//                }
//            } else {
//                if (myTodo.getClickItem()) {//???????????????????????????????????????????????????
//                    holder.circle_not_choose.setImageResource(R.mipmap.choose);
//                } else {//????????????????????????????????????????????????????????????
//                    holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
//                }
//            }
//        }
        //clickitem????????????1?????????????????????
//        if (myTodo.getFinish()){
//            holder.circle_not_choose.setImageResource(R.mipmap.choose);
//
////            holder.cl_item_todo_list.setBackgroundResource(R.drawable.white_bg);
//            if (!MainActivity.isEdit) {//??????????????????title??????
//                holder.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            } else {
//                holder.tvTitle.getPaint().setFlags(0);
//            }
////            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    // position?????????????????????????????????
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
            if (!myTodo.getAlertItem()) {//??????????????????,???????????????
                if (myTodo.getFinish()) {
                    holder.circle_not_choose.setImageResource(R.mipmap.choose);
                    holder.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.cl_item_todo_list.setBackgroundResource(R.drawable.white_bg);
                } else {
                    holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
                    holder.tvTitle.getPaint().setFlags(0);
                    holder.cl_item_todo_list.setBackgroundResource(R.drawable.teal_bg);
                }
            } else {//???????????????
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



        //item????????????
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //???????????????????????????
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
//     * ??????item?????????
//     *
//     * @param position item??????
//     */
//    public void scrollItemToTop(ViewGroup parent,int position) {
//        LinearTopSmoothScroller smoothScroller = new LinearTopSmoothScroller(parent.getContext(),false);
//        smoothScroller.setTargetPosition(position);
//        layoutManager.startSmoothScroll(smoothScroller);
//    }


    //????????????
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

    //??????
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

        notifyDataSetChanged();//??????

    }



}
