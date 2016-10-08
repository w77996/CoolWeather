package com.example.administrator.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.administrator.coolweather.module.City;
import com.example.administrator.coolweather.module.CoolWeatherDB;
import com.example.administrator.coolweather.module.Count;
import com.example.administrator.coolweather.module.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/7.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvince = response.split(",");
            if (allProvince != null && allProvince.length > 0) {
                for (String p : allProvince) {
                    String[] arrary = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(arrary[0]);
                    province.setProvinceName(arrary[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceid) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCity = response.split(",");
            if (allCity != null && allCity.length > 0) {
                for (String c : allCity) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceid);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCount = response.split(",");
            if (allCount != null && allCount.length > 0) {
                for (String c : allCount) {
                    String[] array = response.split("\\|");
                    Count count = new Count();
                    count.setCountCode(array[0]);
                    count.setCountName(array[1]);
                    count.setCityId(cityId);
                    coolWeatherDB.saveCount(count);
                }
                return true;
            }
        }
        return false;
    }
        /*
        {
        "weatherinfo":
                {
                "city":"鍖椾含","cityid":"101010100","temp1":"-2鈩�","temp2":"16鈩�","weather":"鏅�","img1":"n0.gif","img2":"d0.gif","ptime":"18:00"
                }
          }
         */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context, String cityName,
                                       String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }
}
