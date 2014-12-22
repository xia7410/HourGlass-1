package com.example.yuntu;

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
import com.example.lbs_camer.R;
/*************************
 * 
 * @author zhaoqin
 * 功能：根据ID，更新云图数据
 * 更新日期：2014-8-21
 *
 **************************/
public class UpdateYuntu extends Activity{
	private Handler mHandler;
	private TextView updatetext;
	private EditText updateID;
	private EditText updateMood;
	private Button updateButton;
	private String strID;
	private String strMood;
	private String url;
	private String key = "35164c806192574023e49feed6b12107";
	private String tableid = "53ec76f0e4b01dd7561e0b8a";
	private String data;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuntu_update_data);
		
		mHandler = new Handler();
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
		TextView id_title = (TextView) findViewById(R.id.id_title);
		TextView mood_title = (TextView) findViewById(R.id.update_mood_title);
		updatetext = (TextView) findViewById(R.id.update_text);
		updateID = (EditText) findViewById(R.id.update_id_edit);
		updateMood = (EditText) findViewById(R.id.update_mood_edit);
		id_title.setTypeface(typeFace);
		mood_title.setTypeface(typeFace);
		updatetext.setTypeface(typeFace);
		updateID.setTypeface(typeFace);
		updateMood.setTypeface(typeFace);
		updateButton = (Button) findViewById(R.id.update_mood_button);
		updateButton.setTypeface(typeFace);
		updateButton.setVisibility(View.VISIBLE);
		updateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CharSequence update_id_value = updateID.getText();
				strID = update_id_value.toString();
				updateID.setEnabled(false);
				
				CharSequence update_mood_value = updateMood.getText();
				strMood = update_mood_value.toString();
				updateMood.setEnabled(false);
				updaterunnable();
				

			}
		});
	}
    @Override  
    public void onBackPressed() {  
        super.onBackPressed();  
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);     
    }  
	Runnable updatedata = new Runnable(){
		@Override
		public void run(){
			//更新数据地址
			url = "http://yuntuapi.amap.com/datamanage/data/update";
			//Json数据
			data="{\"_id\":\"" + strID + "\"," +"\"mood\":\"" + strMood +"\"}";
			
			HttpPost httpRequest = new HttpPost(url);
			//请求体方式上传
			httpRequest.addHeader("Content-Type", "application/x-www-form-urluncoded");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("key", key));
			params.add(new BasicNameValuePair("tableid", tableid));
			params.add(new BasicNameValuePair("data", data));
			try{
				HttpEntity httpEntity = new UrlEncodedFormEntity(params, "utf-8");
				httpRequest.setEntity(httpEntity);
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpRequest);
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
			updateID.setEnabled(true);
			updateMood.setEnabled(true);
			updatetext.setText("更新数据成功");
		}
	};
		private void updaterunnable(){
			new Thread(){
				public void run(){
					mHandler.post(updatedata);
				}
			}.start();
		}
}
