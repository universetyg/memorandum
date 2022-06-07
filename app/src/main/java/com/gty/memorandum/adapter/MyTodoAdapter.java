package com.gty.memorandum.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gty.memorandum.activity.DetailActivity;
import com.gty.memorandum.R;
import com.gty.memorandum.bean.MyTodo;

import java.util.List;

public class MyTodoAdapter extends RecyclerView.Adapter<MyTodoAdapter.MyHolder> {

    private List<MyTodo> mTodoList;
    private LongClickLisenter longClickLisenter;

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
        MyHolder myHolder = new MyHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);

            }
        });

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
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

//    //删除item
//    public void removeItem(int position)
//    {
//        //操作数据源
//        mTodoList.remove(position);
//        //刷新
//        notifyDataSetChanged();
//    }

    public interface LongClickLisenter {
        void LongClickLisenter(int position);
    }


    public void setLongClickLisenter(LongClickLisenter longClickLisenter) {
        this.longClickLisenter = longClickLisenter;
    }



}
