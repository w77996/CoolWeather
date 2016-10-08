package com.example.administrator.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.coolweather.R;
import com.example.administrator.coolweather.module.City;
import com.example.administrator.coolweather.module.CoolWeatherDB;
import com.example.administrator.coolweather.module.Count;
import com.example.administrator.coolweather.module.Province;
import com.example.administrator.coolweather.util.HttpCallbackListener;
import com.example.administrator.coolweather.util.HttpUtil;
import com.example.administrator.coolweather.util.Utility;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/7.
 */
public class ChooseAeraActivity extends Activity {
    public static  final  int LEVEL_PROVINCE = 0;
    public  static  final  int LEVEL_CITY =1;
    public  static  final  int LEVEL_COUNT =2;

    private  ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String>  datalist = new ArrayList<String>() ;

    private  List<Province>  provinceList;
    private  List<City>  cityList;
    private  List<Count> countList;

    private  Province selectProvince;
    private  City selectCity;
    private  int crruentLevel;

    private  boolean isFromActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromActivity = getIntent().getBooleanExtra("from_weather_activity",false);

        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        if(pre.getBoolean("city_selected",false)&&!isFromActivity){
            Intent i = new Intent(this,WeatherActivity.class);
            startActivity(i);
            finish();
            return ;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        /*
        初始化各个控件

        将datalist加入到listview
         */
        listView = (ListView)findViewById(R.id.list_view);

        titleText = (TextView)findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);

        /*
        从数据库中初始化数据
         */
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        /*
        判断列表是否有按键，如果没有执行queryProvince()
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>arg0, View view, int position, long id) {

                if(crruentLevel == LEVEL_PROVINCE){
                    selectProvince = provinceList.get(position);
                    queryCities();
                }else if(crruentLevel == LEVEL_CITY){
                    selectCity = cityList.get(position);
                    queryCount();
                }else if(crruentLevel == LEVEL_COUNT){
                    String countCode = countList.get(position).getCountCode();
                    Intent intent = new Intent(ChooseAeraActivity.this,WeatherActivity.class);
                    intent.putExtra("county_code",countCode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        queryProvince();
    }
    /*
    优先从数据库中查找数据
    如果有数据则将数据加载进datalist
    如果没哟则执行queryFromServer
     */
    private  void queryProvince(){
        provinceList = coolWeatherDB.loadProvince();
        if(provinceList.size()>0){
            datalist.clear();
            for(Province province:provinceList){
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            crruentLevel=LEVEL_PROVINCE;
        }else {
            queryFromServer(null,"province");
        }
    }
        /*
     优先从数据库中查找数据
     如果有数据则将数据加载进datalist
     如果没哟则执行queryFromServer
      */
    private  void queryCities(){
        cityList = coolWeatherDB.loadCity(selectProvince.getId());
        if(cityList.size()>0){
            datalist.clear();
            for(City city:cityList){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectProvince.getProvinceName());
            crruentLevel = LEVEL_CITY;
        }else{
            queryFromServer(selectProvince.getProvinceCode(),"city");
        }
    }
        /*
     优先从数据库中查找数据
     如果有数据则将数据加载进datalist
     如果没哟则执行queryFromServer
      */
    private  void queryCount(){
        countList = coolWeatherDB.loadCount(selectCity.getId());
        if(countList.size()>0){
            datalist.clear();
            for(Count count:countList){
                datalist.add(count.getCountName());
            }
            adapter.notifyDataSetChanged();;
            listView.setSelection(0);
            titleText.setText(selectCity.getCityName());
            crruentLevel = LEVEL_COUNT;
        }else{
            queryFromServer(selectCity.getCityCode(),"count");
        }

    }
    /*
        从http上将数据读取出来
     */
    private void queryFromServer(final String code,final String type){
        String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code+ ".xml";
        }else{
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvincesResponse(coolWeatherDB,response);
                }else  if("city".equals(type)){
                    result = Utility.handleCitiesResponse(coolWeatherDB,response,selectProvince.getId());
                }else if("count".equals(type)){
                    result = Utility.handleCountResponse(coolWeatherDB,response,selectCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run(){
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvince();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("count".equals(type)){
                                queryCount();
                            }
                        }
                    });
                }
            }
            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAeraActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private  void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog!= null){
            progressDialog.dismiss();
        }
    }
    public  void onBackPressed(){
        if(crruentLevel == LEVEL_COUNT){
            queryCities();
        }else if (crruentLevel == LEVEL_CITY){
            queryProvince();
        }else if(isFromActivity){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
        }
        else{
            finish();
        }
    }
}
