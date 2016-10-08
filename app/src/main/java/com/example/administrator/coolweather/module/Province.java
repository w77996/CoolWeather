package com.example.administrator.coolweather.module;

/**
 * Created by Administrator on 2016/5/6.
 * 定义一个Province 实体类
 */
public class Province {
    private  int id;
    private  String provinceName;
    private  String provinceCode;
    /*
     *id provincename provincecode
     */

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }
}
