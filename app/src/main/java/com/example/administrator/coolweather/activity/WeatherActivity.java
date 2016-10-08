package com.example.administrator.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.coolweather.R;
import com.example.administrator.coolweather.util.HttpCallbackListener;
import com.example.administrator.coolweather.util.HttpUtil;
import com.example.administrator.coolweather.util.Utility;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WeatherActivity extends Activity implements View.OnClickListener{
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private  TextView publishText;
    private  TextView weatherDsepText;
    private  TextView temp1Text;
    private  TextView temp2Text;
    private  TextView crruentTimeText;
    private Button swith_city;
    private  Button refresh_weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);

        cityNameText = (TextView)findViewById(R.id.city_name);
        publishText= (TextView)findViewById(R.id.publish_text);
        weatherDsepText = (TextView)findViewById(R.id.weather_desp);
        temp1Text = (TextView)findViewById(R.id.temp1);
        temp2Text = (TextView)findViewById(R.id .temp2);
        crruentTimeText = (TextView)findViewById(R.id.crruent_data);

        swith_city = (Button)findViewById(R.id.swith_city);
        refresh_weather = (Button)findViewById(R.id.refresh_weather);
        swith_city.setOnClickListener(this);
        refresh_weather.setOnClickListener(this);

        String countCode = getIntent().getStringExtra("county_code");

        if(!TextUtils.isEmpty(countCode)){
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countCode);
        }else {
            showWeather();
        }
    }
    private void queryWeatherCode(String countyCode){
        String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
        queryFromServer(address,"countyCode");
    }
    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }
    private  void queryFromServer(final String address,final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                if("countyCode".equals(type)){
                   // Log.d("dsf","fs");
                    if(!TextUtils.isEmpty(response)) {
                        String[] arrary = response.split("\\|");
                        if (arrary != null && arrary.length == 2) {
                            String weatherCode = arrary[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                        }else if("weatherCode".equals(type)){
                            Utility.handleWeatherResponse(WeatherActivity.this,response);
                            runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showWeather();
                                    }
                                });
                        }
                    }
            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败。。。");
                    }
                });

            }
        });
    }
    private void  showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name",""));
        temp1Text.setText(prefs.getString("temp1",""));

        temp2Text.setText(prefs.getString("temp2",""));
        weatherDsepText.setText(prefs.getString("weather_desp",""));
        publishText.setText("今天" + prefs.getString("publish_time","") + "发布");
        crruentTimeText.setText(prefs.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.swith_city:
                Intent intent = new Intent(this,ChooseAeraActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中。。。");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("weather_code","");
                Toast.makeText(WeatherActivity.this,"同步成功",Toast.LENGTH_SHORT).show();
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;
        }
    }
}
