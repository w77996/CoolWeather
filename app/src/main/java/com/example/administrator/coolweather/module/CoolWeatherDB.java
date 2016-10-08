package com.example.administrator.coolweather.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.coolweather.db.CoolWeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/6.
 */
public class CoolWeatherDB {
    public static final String DB_NAME="cool_weather";
    public static  final int VERSION=1;
    private static  CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private  CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
        //创建数据库
    }
    public synchronized static  CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    public void saveProvince(Province province){
        if(province!=null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name",province.getProvinceName());
            contentValues.put("province_code",province.getProvinceCode());
            db.insert("Province",null,contentValues);
        }
    }
    public List<Province>  loadProvince(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while(cursor.moveToNext());
        }
        return list;
    }
    public void saveCity(City city){
        if(city!=null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City",null,values);
        }
    }
    public List<City> loadCity(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while(cursor.moveToNext());
        }
        return list;
    }
    public void saveCount(Count count){
        if(count!=null){
            ContentValues values = new ContentValues();
            values.put("count_name",count.getCountName());
            values.put("count_code",count.getCountCode());
            values.put("city_id",count.getCityId());
            db.insert("Count",null,values);
        }
    }
    public List<Count>  loadCount(int cityId){
        List<Count> list = new ArrayList<Count>();
        Cursor cursor = db.query("Count",null,"city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                Count count = new Count();
                count.setId(cursor.getInt(cursor.getColumnIndex("id")));
                count.setCountName(cursor.getString(cursor.getColumnIndex("count_name")));
                count.setCountCode(cursor.getString(cursor.getColumnIndex("count_code")));
                count.setCityId(cityId);
                list.add(count);
            }while(cursor.moveToNext());
        }
        return list;
    }
}
