package com.example.administrator.coolweather.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.InflaterInputStream;

/**
 * Created by Administrator on 2016/5/6.
 */
public class HttpUtil {


        public  static  void sendHttpRequest(final  String address,final  HttpCallbackListener listener){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(address);
                        connection = (HttpURLConnection)url.openConnection();
                        connection.setRequestMethod("GET");                     //要求方法为get
                        connection.setConnectTimeout(8000);                     //链接超市与读取超时为8s
                        connection.setReadTimeout(8000);
                        InputStream in = connection.getInputStream();           //将读取的数据转化为输入流
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  //bufferedReader封装
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {            //每行读取
                            response.append(line);
                        }
                        if(listener!=null){
                            listener.onFinish(response.toString());
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        if(listener!=null){
                            listener.onError(e);
                        }
                    } finally {                     //最后断开连接
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
                }).start();
        }
}
