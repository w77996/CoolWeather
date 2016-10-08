package com.example.administrator.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLClientInfoException;

/**
 * Created by Administrator on 2016/5/6.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    //建立省表
    public static  final  String CREATE_PROVINCE = "create table Province("
            +"id integer primary key autoincrement, "
            +" province_name text, "
            +" province_code text)";
    //建立市表
    public static   final String CREATE_CITY = "create table City("
            +" id integer primary key autoincrement,"
            + "city_name text,"
            +"city_code text,"
            +"province_id integer)";
    //建立县表
    public static final String CREATE_COUNTY ="create table Count("
            +"id integer primary key autoincrement,"
            +"count_name text,"
            +"count_code text,"
            +"city_id integer)";
    //重写父类构造函数
    public CoolWeatherOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }
    /*
        创建数据库，province,city,count
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }
    //数据库更新函数重写
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
