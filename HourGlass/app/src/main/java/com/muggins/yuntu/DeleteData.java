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

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.muggins.hourglass.R;
/**************************
 *
 * @author zhaoqin
 * 功能：根据ID，删除云图数据
 * 更新日期：2014-8-21
 *
 **************************/
public class DeleteData extends Activity {
    private Handler mHandler;
    private TextView deletetext;
    private EditText editID;
    private Button deletebutton;
    private String ID;
    private String url;
    private String key;
    private String tableid;
    private String fail;
    private int failint;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yuntu_delete_data);
        Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
        mHandler = new Handler();
        TextView delete_title = (TextView) findViewById(R.id.delete_title);
        deletetext = (TextView) findViewById(R.id.deletedata_text);
        editID = (EditText) findViewById(R.id.delete_edit);
        delete_title.setTypeface(typeFace);
        deletetext.setTypeface(typeFace);
        editID.setTypeface(typeFace);
        deletebutton = (Button) findViewById(R.id.deletedata);
        deletebutton.setTypeface(typeFace);
        deletebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                CharSequence id_text_value = editID.getText();
                ID = id_text_value.toString();
                editID.setEnabled(false);

                deleterunnable();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
    }
    Runnable deletedata = new Runnable(){
        @Override
        public void run(){
            url = "http://yuntuapi.amap.com/datamanage/data/delete";
            key = "35164c806192574023e49feed6b12107";
            tableid = "53ec76f0e4b01dd7561e0b8a";

            HttpPost httpRequest = new HttpPost(url);
            httpRequest.addHeader("Content-Type", "application/x-www-form-urluncoded");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("key", key));
            params.add(new BasicNameValuePair("tableid", tableid));
            params.add(new BasicNameValuePair("ids", ID));
            try{
                HttpEntity httpEntity = new UrlEncodedFormEntity(params, "utf-8");
                httpRequest.setEntity(httpEntity);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(httpRequest);
                if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    int num = strResult.length();
                    fail = strResult.substring(num-2, num-1);
                    System.out.println(strResult);
                    failint = Integer.parseInt(fail);
                    System.out.println(failint);
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
            if(failint == 0){
                deletetext.setText("已删除ID为" + ID + "数据");
            }
            else{
                deletetext.setText("没有ID为" + ID + "数据");
            }
            editID.setEnabled(true);
        }
    };
    private void deleterunnable(){
        new Thread(){
            public void run(){
                mHandler.post(deletedata);
            }
        }.start();
    }
}
