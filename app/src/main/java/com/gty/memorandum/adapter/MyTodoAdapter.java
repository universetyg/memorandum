package com.gty.memorandum.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gty.memorandum.DetailActivity;
import com.gty.memorandum.R;
import com.gty.memorandum.bean.MyTodo;

import java.util.List;

public class MyTodoAdapter extends RecyclerView.Adapter<MyTodoAdapter.MyHolder> {

    private List<MyTodo> mTodoList;

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDate;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    public MyTodoAdapter(List<MyTodo> mTodoList) {
        this.mTodoList = mTodoList;
    }

    @NonNull
    @Override
    public MyTodoAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todolist,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("data","hahahahahhahahaha");
                view.getContext().startActivity(intent);
            }
        });

        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTodoAdapter.MyHolder holder, int position) {
//        MyTodo myTodo = mTodoList.get(position);
        holder.tvTitle.setText("测试"+position);
        holder.tvDate.setText("6月3日");
    }

    @Override
    public int getItemCount() {
        return 20;
    }


}
