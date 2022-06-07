package com.gty.memorandum.bean;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "myTodo")
public class MyTodo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title",typeAffinity = ColumnInfo.TEXT)
    private String title;
    @ColumnInfo(name = "content",typeAffinity = ColumnInfo.TEXT)
    private String content;
    @ColumnInfo(name = "create_time",typeAffinity = ColumnInfo.TEXT)
    private String createTime;
    @ColumnInfo(name = "update_time",typeAffinity = ColumnInfo.TEXT)
    private String updateTime;
    @ColumnInfo(name = "deadline",typeAffinity = ColumnInfo.TEXT)
    private String deadline;
    @ColumnInfo(name = "finish",typeAffinity = ColumnInfo.INTEGER)
    private Boolean finish;
    @ColumnInfo(name = "delete_item",typeAffinity = ColumnInfo.INTEGER)
    private Boolean deleteItem;


    @Ignore
    public MyTodo(){

    }

    @Ignore
    public MyTodo(int id) {
        this.id = id;
    }

    public MyTodo(int id, String title, String content, String createTime, String updateTime, String deadline, Boolean finish, Boolean deleteItem) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deadline = deadline;
        this.finish = finish;
        this.deleteItem = deleteItem;
    }

    @Override
    public String toString() {
        return "MyTodo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", deadline='" + deadline + '\'' +
                ", finish=" + finish +
                ", deleteItem=" + deleteItem +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }


    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public Boolean getDeleteItem() {
        return deleteItem;
    }

    public void setDeleteItem(Boolean deleteItem) {
        this.deleteItem = deleteItem;
    }
}
