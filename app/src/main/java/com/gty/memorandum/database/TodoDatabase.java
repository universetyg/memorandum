package com.gty.memorandum.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gty.memorandum.bean.MyTodo;
import com.gty.memorandum.dao.TodoDao;

@Database(entities = {MyTodo.class}, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {
    private static final String DB_NAME = "MyTodoDatabase.db";
    private static volatile TodoDatabase instance;

    public static synchronized TodoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static TodoDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                TodoDatabase.class,
                DB_NAME
        ).build();
    }

    public abstract TodoDao getTodoDao();


}
