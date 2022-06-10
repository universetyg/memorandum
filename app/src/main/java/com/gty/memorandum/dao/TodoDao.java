package com.gty.memorandum.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gty.memorandum.bean.MyTodo;

import java.util.List;


@Dao
public interface TodoDao {
    @Query("select * from myTodo order by alert_item ")
    List<MyTodo> getAllMyTodoInfo();

    @Query("select * from myTodo where id = :id")
    List<MyTodo> getMyTodoInfo(int id);

    @Insert()
    void insert(List<MyTodo> myTodoLists);

    @Delete
    void deleteMyTodo(MyTodo... myTodos);

    @Update
    void updateMyTodoInfo(MyTodo... myTodos);

}
