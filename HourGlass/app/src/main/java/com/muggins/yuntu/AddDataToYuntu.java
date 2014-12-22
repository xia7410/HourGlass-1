package com.muggins.yuntu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.muggins.hourglass.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/*************************
 *
 * @author zhaoqin
 * 功能：根据现在的地理位置，向云图中添加新的数据
 * 更新日期：2014-8-21
 *
 *************************/
public class AddDataToYuntu extends Activity{
    private Handler mHandler;
    private TextView yuntutext;
    private Button save;
    private String address;
    private String url;
    private String data;
    private String key;
    private String tableid;
    private String loctype;
    private String nameEdit;
    private String addressEdit;
    private String moodEdit;
    private EditText name_text;
    private EditText mood_text;
    private EditText address_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuntu_add_data);
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
        TextView name_title = (TextView) findViewById(R.id.name_title);
        name_title.setTypeface(typeFace);
        yuntutext = (TextView) findViewById(R.id.yuntuText);
        yuntutext.setTypeface(typeFace);
        TextView address_title = (TextView) findViewById(R.id.address_title);
        address_title.setTypeface(typeFace);
        name_text = (EditText) findViewById(R.id.name_text);
        name_text.setTypeface(typeFace);
        address_text = (EditText) findViewById(R.id.address_text);
        address_text.setTypeface(typeFace);
        TextView mood_title = (TextView) findViewById(R.id.mood_title);
        mood_title.setTypeface(typeFace);
        mood_text = (EditText) findViewById(R.id.mood_text);
        mood_text.setTypeface(typeFace);


        Intent receive = getIntent();
        address = receive.getStringExtra("address");
        yuntutext.setText("当前默认地址：" + address);

        mHandler = new Handler();

        save = (Button) findViewById(R.id.yun_save_button);
        save.setTypeface(typeFace);
        save.setVisibility(View.VISIBLE);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                CharSequence name_text_value = name_text.getText();
                nameEdit = name_text_value.toString();
                name_text.setKeyListener(null);

                CharSequence address_text_value = address_text.getText();
                addressEdit = address_text_value.toString();
                address_text.setKeyListener(null);

                CharSequence mood_text_value = mood_text.getText();
                moodEdit = mood_text_value.toString();
                mood_text.setKeyListener(null);


                updateUIByrunnable();
                finish();
                overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
    }
    Runnable mRunnable = new Runnable(){
        @Override
        public void run(){
            if(addressEdit.length() != 0){
                address = addressEdit;
            }
            //创建单条数据地址
            url = "http://yuntuapi.amap.com/datamanage/data/create";
            //云图管理密钥
            key = "35164c806192574023e49feed6b12107";
            //云图编号
            tableid = "53ec76f0e4b01dd7561e0b8a";
            //请求参数以地址坐标来计算最终的坐标值
            loctype = "2";//1表示经纬度，2表示地址
            //Json数据格式
            data= "{\"_name\":\"" + nameEdit + "\",\"_address\":\"" + address + "\",\"mood\":\"" + moodEdit + "\"}";

            HttpPost httpRequest = new HttpPost(url);
            //请求体方式上传
            httpRequest.addHeader("Content-Type","application/x-www-form-urlencoded");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("key", key));
            params.add(new BasicNameValuePair("tableid", tableid));
            params.add(new BasicNameValuePair("loctype", loctype));
            params.add(new BasicNameValuePair("data", data));
            try{
                HttpEntity httpEntity = new UrlEncodedFormEntity(params,"utf-8");
                httpRequest.setEntity(httpEntity);
                HttpClient httpClient2 = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient2.execute(httpRequest);
                if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    System.out.println(strResult);
                }
                else{

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
            yuntutext.setText("已存储到云端");
        }
    };
    private void updateUIByrunnable(){
        System.out.println("updateUI");
        new Thread(){
            public void run(){
                mHandler.post(mRunnable);
            }
        }.start();
    }
}
