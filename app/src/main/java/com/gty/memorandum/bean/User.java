package com.gty.memorandum.bean;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * 这里就是一张数据库表，如果(tableName = "user")这里不指定表名，则默认使用类名做表名
 */
@Entity(tableName = "user")
public class User {
    //主健id自增长，一个主键@PrimaryKey，autoGenerate = true 自增长
    //可以使用默认，也可以指定属性设置如下的(name = "name",typeAffinity = ColumnInfo.INTEGER)指定列名和数据类型
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name",typeAffinity = ColumnInfo.TEXT)
    private String name;
    @ColumnInfo
    private String img;
    @ColumnInfo
    private String phone;
    @ColumnInfo
    private String adress;
    @ColumnInfo
    private String sex;
    @ColumnInfo
    private String password;

    /**
     * room只使用一个构造方法，如果多个构造方法使用 @Ignore进行注解，是我们业务使用，
     * 另外业务中使用到其他数量变量也可以使用@Ignore进行注解后，room就不会当成列数据使用
     */
    @Ignore
    public User() {

    }
    @Ignore
    public User(int id, String name, String adress) {
        this.id = id;
        this.name = name;
        this.adress = adress;
    }
    @Ignore
    public User(int id) {
        this.id = id;
    }

    public User(int id, String name, String img, String phone, String adress, String sex, String password) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.phone = phone;
        this.adress = adress;
        this.sex = sex;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", phone='" + phone + '\'' +
                ", adress='" + adress + '\'' +
                ", sex='" + sex + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}