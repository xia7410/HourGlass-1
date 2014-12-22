package com.muggins.yuntu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
/***************************
 *
 * @author zhaoqin
 * 功能：列表显示云图信息，实现方法与获取天气信息相同
 * 更新日期：2014-8-21
 *
 ***************************/
public class Datalist extends Activity {
    private TextView list;
    private String data;
    private Handler mHandler;
    private String url;
    private String tableid;
    private String city;
    private String keywords;
    private String key;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listdata);
        list = (TextView) findViewById(R.id.listdata);
        list.setMovementMethod(ScrollingMovementMethod.getInstance());
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
        list.setTypeface(typeFace);
        mHandler = new Handler();

        listrunnable();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
    }
    Runnable listdata = new Runnable(){
        @Override
        public void run(){
            tableid = "53ec76f0e4b01dd7561e0b8a";
            key = "35164c806192574023e49feed6b12107";
            city = "全国";
            keywords = "";
            url = "http://yuntuapi.amap.com/datasearch/local?tableid=" + tableid + "&city=" + city + "&keywords" + keywords + "&key=" + key;
            HttpClient httpClient = new DefaultHttpClient();
            HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
            String responseData = "";
            try{
                HttpResponse response = httpClient.execute(new HttpGet(url));
                HttpEntity entity = response.getEntity();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    responseData = responseData + line;
                }
            }
            catch(UnsupportedEncodingException e){
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(ClientProtocolException e){
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(IOException e){
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
            Gson gson = new Gson();
            QueryResult queryResult = gson.fromJson(responseData, QueryResult.class);
            data = queryResult.toString();
            //querytext.setText("" + queryResult);
            list.setText(data);
        }
    };

    private void listrunnable(){
        new Thread(){
            public void run(){
                mHandler.post(listdata);
            }
        }.start();
    }
}
