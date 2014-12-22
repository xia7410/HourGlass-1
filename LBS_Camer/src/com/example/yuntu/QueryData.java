package com.example.yuntu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import com.example.lbs_camer.R;
/**************************
 * 
 * @author zhaoqin
 * ���ܣ���ͼ��ʾ����ѡ�����б���ͼ����ʽչʾ��ͼ�������Է�����ͼ��������
 * �������ڣ�2014-8-21
 *
 **************************/
public class QueryData extends Activity{
	private Button querybutton;
	private Button IDquerybutton;
	private Button sharebutton;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuntu_list_data);
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
		querybutton = (Button) findViewById(R.id.yun_list_button);
		Animation animation_query = AnimationUtils.loadAnimation(QueryData.this, R.anim.alpha);
		querybutton.startAnimation(animation_query);
		querybutton.setTypeface(typeFace);
		querybutton.setVisibility(View.VISIBLE);
		querybutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//�б���ʽ��ʾ��ͼ��Ϣ
				Intent launchlist = new Intent();
				launchlist.setClass(QueryData.this, Datalist.class);
				startActivity(launchlist);
		        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha); 
			}
		});
		
		IDquerybutton = (Button) findViewById(R.id.yun_track_button);
		Animation animation_id = AnimationUtils.loadAnimation(QueryData.this, R.anim.alpha);
		IDquerybutton.startAnimation(animation_id);
		IDquerybutton.setTypeface(typeFace);
		IDquerybutton.setVisibility(View.VISIBLE);
		IDquerybutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//��ͼ��ʽ��ʾ��ͼ��Ϣ
				Intent launchID = new Intent();
				launchID.setClass(QueryData.this, AMapIDquery.class);
				startActivity(launchID);
				overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
				
			}
		});
		
		sharebutton = (Button) findViewById(R.id.share);
		Animation animation_share = AnimationUtils.loadAnimation(QueryData.this, R.anim.alpha);
		sharebutton.startAnimation(animation_share);
		sharebutton.setTypeface(typeFace);
		sharebutton.setVisibility(View.VISIBLE);
		sharebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//������ͼ��������
				Intent launchshare = new Intent();
				launchshare.setClass(QueryData.this, ShareActivity.class);
				startActivity(launchshare);
				overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
			}
		});
		
	}
	@Override  
	public void onBackPressed() {  
		super.onBackPressed();  
	    overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);    
	    }  
}
