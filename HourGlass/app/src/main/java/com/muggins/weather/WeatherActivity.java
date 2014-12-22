package com.muggins.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;

import com.muggins.hourglass.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
/****************************
 *
 * @author zhaoqin
 * 功能：获取天气信息
 * 更新日期：2014-8-21
 *
 ****************************/
public class WeatherActivity extends Activity {
    private String city;
    private TextView weatherView;

    private String weatherdata;
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    weatherView.setText(weatherdata);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        weatherView = (TextView) findViewById(R.id.weather_textview);
        weatherView.setMovementMethod(ScrollingMovementMethod.getInstance());
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
        weatherView.setTypeface(typeFace);

        Intent receive = getIntent();
        city = receive.getStringExtra("city");
        weatherView.setText("正在获取天气");
        Thread  thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String httpurl = "http://api.map.baidu.com/telematics/v3/weather?location=" + city + "&output=json&ak=B122767f9cf32ad2c5a17d97835d053e";
                HttpClient httpClient = new DefaultHttpClient();
                HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
                String responseData = "";
                try{
                    HttpResponse response = httpClient.execute(new HttpGet(httpurl));
                    HttpEntity entity = response.getEntity();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String line = "";
                    while((line = bufferedReader.readLine()) != null){
                        responseData = responseData + line;
                    }
                }
                catch(ClientProtocolException e){
                    Log.e("WeatherActivity", e.getMessage().toString());
                }
                catch(IOException e){
                    Log.e("WeatherActivity", e.getMessage().toString());
                }
                catch(Exception e){
                    Log.e("WeatherActivity", e.getMessage().toString());
                }
                //解析JSON数据
                Gson gson = new Gson();
                TestResult testResult = gson.fromJson(responseData, TestResult.class);
                weatherdata = testResult.toString();
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        });
        thread.start();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
    }
}
