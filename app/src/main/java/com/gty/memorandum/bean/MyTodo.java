package com.gty.memorandum.bean;

public class MyTodo {

    private String tvTitle;
    private String tvDate;

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public String getTvDate() {
        return tvDate;
    }

    public void setTvDate(String tvDate) {
        this.tvDate = tvDate;
    }

    @Override
    public String toString() {
        return "MyTodo{" +
                "tvTitle='" + tvTitle + '\'' +
                ", tvDate='" + tvDate + '\'' +
                '}';
    }
}
