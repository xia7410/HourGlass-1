package com.example.lbs_camer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amapv2.apis.poisearch.PoiAroundSearchActivity;
import com.example.camer.MyCaremaActivity;
import com.example.weather.WeatherActivity;
import com.example.yuntu.YunActivity;
/****************************
 * 
 * @author zhaoqin
 * ���ܣ������棬����ѡ��
 * �������ڣ�2014-8-21
 * 
 ***************************/
public class MainActivity extends Activity implements
		AMapLocationListener{
	private LocationManagerProxy aMapLocManager = null;
	private TextView myLocation;
	private AMapLocation aMapLocation;
	private GeocodeSearch geocoderSearch;
	private String city;
	private boolean flag = true;
	private String output;
	private Double geoLat;
	private Double geoLng;
	private String desc = "";
	private Button weatherButton;
	private Button myButton;
	private Button poiButton;
	private Button yunButton;
	private ProgressDialog progDialog = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myLocation = (TextView) findViewById(R.id.main_text);
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
		myLocation.setTypeface(typeFace);

		aMapLocManager = LocationManagerProxy.getInstance(this);
		//�������綨λ
		aMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 2000, 10, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocation();
	}

	@Override
	protected void onRestart(){
		super.onRestart();
		Animation animation_weather = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
		weatherButton.startAnimation(animation_weather);
		weatherButton.setVisibility(View.VISIBLE);

		Animation animation_camer = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
		myButton.startAnimation(animation_camer);
		myButton.setVisibility(View.VISIBLE);

		Animation animation_poi = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
		poiButton.startAnimation(animation_poi);
		poiButton.setVisibility(View.VISIBLE);

		Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
		yunButton.startAnimation(animation);
		yunButton.setVisibility(View.VISIBLE);
	}
	/**
	 * ���ٶ�λ
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
	
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("���ڻ�ȡ��ַ");
		progDialog.show();
	}
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
	
	public void getAddress(final LatLonPoint latLonPoint) {
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// ��һ��������ʾһ��Latlng���ڶ�������ʾ��Χ�����ף�������������ʾ�ǻ�ϵ����ϵ����GPSԭ������ϵ
		geocoderSearch.getFromLocationAsyn(query);// ����ͬ��������������
	}
	//��λ�ص�
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			this.aMapLocation = location;// �жϳ�ʱ����
			geoLat = location.getLatitude();
			geoLng = location.getLongitude();

			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				desc = locBundle.getString("desc");//��ַ����
			}
			String str = "";
			output = desc;
			myLocation.setText(str);
			city = location.getCity();
			showButton();
		}
	}
	public void showButton(){
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
		if(flag){
			weatherButton = (Button) findViewById(R.id.weatherbutton);
			Animation animation_weather = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
			weatherButton.startAnimation(animation_weather);
			weatherButton.setTypeface(typeFace);
			weatherButton.setVisibility(View.VISIBLE);
			weatherButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//���ݳ�����Ϣ��WeatherActivity���õ�������Ϣ
					Intent launchweather = new Intent();
					launchweather.putExtra("city", city);
					launchweather.setClass(MainActivity.this, WeatherActivity.class);
					startActivity(launchweather);
					overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
				}
			});
			
			myButton = (Button) findViewById(R.id.myButton);
			Animation animation_camer = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
			myButton.startAnimation(animation_camer);
			myButton.setTypeface(typeFace);
			myButton.setVisibility(View.VISIBLE);
			myButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					//���ݵ�ַ��ϸ��Ϣ��MyCaremaActivity
					Intent intent = new Intent();
					intent.putExtra("message", output);
					intent.setClass(MainActivity.this, MyCaremaActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
				}
			});
			
			poiButton = (Button) findViewById(R.id.poiButton);
			Animation animation_poi = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
			poiButton.startAnimation(animation_poi);
			poiButton.setTypeface(typeFace);
			poiButton.setVisibility(View.VISIBLE);
			poiButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//������Ϣ���õ��ܱ���Ȥ��
					Intent launchPoi = new Intent();
					launchPoi.putExtra("geoLat", geoLat);
					launchPoi.putExtra("geoLng", geoLng);
					launchPoi.putExtra("desc", desc);
					launchPoi.putExtra("city", city);
					launchPoi.setClass(MainActivity.this, PoiAroundSearchActivity.class);
					startActivity(launchPoi);
					overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
				}
			});

			yunButton = (Button) findViewById(R.id.yunButton);
			Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
			yunButton.startAnimation(animation);
			yunButton.setTypeface(typeFace);
			yunButton.setVisibility(View.VISIBLE);
			yunButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub					
					//��ͼ����
					Intent launchyun = new Intent();
					launchyun.putExtra("address", desc);
					launchyun.putExtra("geoLat", geoLat);
					launchyun.putExtra("geoLng", geoLng);
					launchyun.setClass(MainActivity.this, YunActivity.class);
					startActivity(launchyun);
					overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
				}
			});
		}
		flag = false;
	}
}
