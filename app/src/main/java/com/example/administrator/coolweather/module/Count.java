package com.example.administrator.coolweather.module;

/**
 * Created by Administrator on 2016/5/6.
 */
public class Count {
    private  int id;
    private String countName;
    private  String countCode;
    private int cityId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String getCountName() {
        return countName;
    }

    public void setCountCode(String countCode) {
        this.countCode = countCode;
    }

    public void setCountName(String countName) {
        this.countName = countName;
    }

    public String getCountCode() {
        return countCode;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }
}
