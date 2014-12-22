package com.example.weather;

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

import com.example.lbs_camer.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
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
	private Handler mHandler;
	private String weatherdata;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_activity);
		weatherView = (TextView) findViewById(R.id.weather_textview);
		weatherView.setMovementMethod(ScrollingMovementMethod.getInstance());
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
		weatherView.setTypeface(typeFace);
		mHandler = new Handler();
		
		Intent receive = getIntent();
		city = receive.getStringExtra("city");
		weatherView.setText("正在获取天气");
		weatherrunnable();
		
	}
    @Override  
    public void onBackPressed() {  
        super.onBackPressed();  
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);     
    }  
    //HTTP获取数据，GET方式访问，采用百度提供的天气信息接口
	Runnable data = new Runnable(){
		@Override
		public void run(){
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
				weatherView.setText(e.getMessage().toString());
			}
			catch(IOException e){
				weatherView.setText(e.getMessage().toString());
			}
			catch(Exception e){
				weatherView.setText(e.getMessage().toString());
			}
			//解析JSON数据
			Gson gson = new Gson();
			TestResult testResult = gson.fromJson(responseData, TestResult.class);
			weatherdata = testResult.toString();
			weatherView.setText(weatherdata);
			}
		};
		//开启子线程，更新界面
		private void weatherrunnable(){
			new Thread(){
				public void run(){
					mHandler.post(data);
				}
			}.start();
		}
}
