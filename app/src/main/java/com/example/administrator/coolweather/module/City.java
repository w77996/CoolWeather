package com.example.administrator.coolweather.module;

/**
 * Created by Administrator on 2016/5/6.
 */
public class City {
    private int id;
    private  String cityName;
    private String cityCode;
    private int provinceId;

    public void setId(int id) {this.id = id;}
    public int getId() {
        return id;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getProvinceId() {
        return provinceId;
    }
}
