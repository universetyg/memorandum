package com.gty.memorandum.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gty.memorandum.MainActivity;
import com.gty.memorandum.activity.DetailActivity;
import com.gty.memorandum.R;
import com.gty.memorandum.bean.MyTodo;

import java.util.ArrayList;
import java.util.List;

public class MyTodoAdapter extends RecyclerView.Adapter<MyTodoAdapter.MyHolder> {

    private List<MyTodo> mTodoList = new ArrayList<>();
    private LongClickLisenter longClickLisenter;
    private OnItemClickListener mOnItemClickListener;


    public class MyHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDate;
        ImageView circle_not_choose;



        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            circle_not_choose = (ImageView) itemView.findViewById(R.id.circle_not_choose);
        }
    }

    public MyTodoAdapter(List<MyTodo> mTodoList) {
        this.mTodoList = mTodoList;
    }

    @NonNull
    @Override
    public MyTodoAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    @Override
    public void onBindViewHolder(@NonNull MyTodoAdapter.MyHolder holder, int position) {
        MyTodo myTodo = mTodoList.get(position);
        holder.tvTitle.setText(myTodo.getTitle());
        holder.tvDate.setText(myTodo.getDeadline());

        if (myTodo.getClickItem()){
           holder.circle_not_choose.setImageResource(R.mipmap.choose);//明天继续
        }else {
            holder.circle_not_choose.setImageResource(R.mipmap.circle_not_choose);
        }

        holder.circle_not_choose.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if (myTodo.getClickItem()){
                    myTodo.setClicItem(false);
                    notifyDataSetChanged();
                }else {
                    myTodo.setClicItem(true);
                    notifyDataSetChanged();
                }
            }
        });


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




}
