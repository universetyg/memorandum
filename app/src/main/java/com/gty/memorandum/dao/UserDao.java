package com.gty.memorandum.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gty.memorandum.bean.User;

import java.util.List;

/**
 * DAO代表数据访问对象
 * Query  查询
 * Insert  插入
 * Update  更新
 * Delete 删除
 * 传入多种不同的参数
 * 下面的User...users  可变对象，可以多个
 */
@Dao
public interface UserDao {
    @Query("select * from user")
    List<User> getAllUserInfo();
    //根据用户id查询数据,数据表的字段id对应方法的id值，查询语句里可以通过冒号方法变量名方式使用
    @Query("select * from user where id = :id")
    List<User> getUserInfo(int id);
    @Insert()
    void insert(List<User> userLists);
    @Update
    void updateUserInfo(User...users);
    @Delete
    void deleteInfo(User...users);



}