package com.example.yuntu;

import com.example.lbs_camer.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
/***************************
 * 
 * @author zhaoqin
 * 功能：云图选择界面，功能选择
 * 更新日期：2014-8-21
 *
 **************************/
public class YunActivity extends Activity{
	private Button addbutton;
	private Button updatebutton;
	private Button deletebutton;
	private Button querybutton;
	private String address;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuntu_activity);
		
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
		Intent receive = getIntent();
		address = receive.getStringExtra("address");
		
		addbutton = (Button) findViewById(R.id.yun_addbutton);
		Animation animation_add = AnimationUtils.loadAnimation(YunActivity.this, R.anim.alpha);
		addbutton.startAnimation(animation_add);
		addbutton.setTypeface(typeFace);
		addbutton.setVisibility(View.VISIBLE);
		addbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//添加数据
				Intent launchAdd = new Intent();
				launchAdd.putExtra("address", address);
				launchAdd.setClass(YunActivity.this, AddDataToYuntu.class);
				startActivity(launchAdd);
				overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
			}
		});
		
		updatebutton = (Button) findViewById(R.id.yun_updatebutton);
		Animation animation_update = AnimationUtils.loadAnimation(YunActivity.this, R.anim.alpha);
		updatebutton.startAnimation(animation_update);
		updatebutton.setTypeface(typeFace);
		updatebutton.setVisibility(View.VISIBLE);
		updatebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//更新数据
				Intent launchUpdate = new Intent();
				launchUpdate.setClass(YunActivity.this, UpdateYuntu.class);
				startActivity(launchUpdate);
				overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
			}
		});
		
		deletebutton = (Button) findViewById(R.id.yun_deletebutton);
		Animation animation_delete = AnimationUtils.loadAnimation(YunActivity.this, R.anim.alpha);
		deletebutton.startAnimation(animation_delete);
		deletebutton.setTypeface(typeFace);
		deletebutton.setVisibility(View.VISIBLE);
		deletebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//删除数据
				Intent launchDelete = new Intent();
				launchDelete.setClass(YunActivity.this, DeleteData.class);
				startActivity(launchDelete);
				overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
			}
		});

		querybutton = (Button) findViewById(R.id.yun_querybutton);
		Animation animation_query = AnimationUtils.loadAnimation(YunActivity.this, R.anim.alpha);
		querybutton.startAnimation(animation_query);
		querybutton.setTypeface(typeFace);
		querybutton.setVisibility(View.VISIBLE);
		querybutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//显示数据
				Intent launchQuery = new Intent();
				launchQuery.setClass(YunActivity.this, QueryData.class);
				startActivity(launchQuery);
				overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
			}
		});
		
	}
    @Override  
    public void onBackPressed() {  
        super.onBackPressed();  
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);     
    }  
	@Override
	protected void onRestart(){
		super.onRestart();
		Animation animation_add = AnimationUtils.loadAnimation(YunActivity.this, R.anim.alpha);
		addbutton.startAnimation(animation_add);
		addbutton.setVisibility(View.VISIBLE);
		
		Animation animation_update = AnimationUtils.loadAnimation(YunActivity.this, R.anim.alpha);
		updatebutton.startAnimation(animation_update);
		updatebutton.setVisibility(View.VISIBLE);
		
		Animation animation_delete = AnimationUtils.loadAnimation(YunActivity.this, R.anim.alpha);
		deletebutton.startAnimation(animation_delete);
		deletebutton.setVisibility(View.VISIBLE);
		
		Animation animation_query = AnimationUtils.loadAnimation(YunActivity.this, R.anim.alpha);
		querybutton.startAnimation(animation_query);
		querybutton.setVisibility(View.VISIBLE);
	}
}
