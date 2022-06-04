package com.gty.memorandum.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gty.memorandum.bean.User;
import com.gty.memorandum.dao.UserDao;

/**
 * 使用@Database注解该类并添加了表名、数据库版本（每当我们改变数据库中的内容时它都会增加），所以这里使用exportSchema = false
 * 除了添加表映射的类以及和数据库版本外，还要添加exportSchema = false否则会报警告。
 */
@Database(entities = {User.class},version = 1,exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    //数据库名字
    private static final String DB_NAME = "UserDatabase.db";
    private static volatile UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static UserDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                UserDatabase.class,
                DB_NAME).build();
    }

    public abstract UserDao getUserDao();

}