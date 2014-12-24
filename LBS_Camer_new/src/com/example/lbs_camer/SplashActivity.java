package com.example.lbs_camer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/****************************
 * 
 * @author Zhaoqin
 * 功能:启动画面
 * 更新日期：2014-8-21
 * 
 ****************************/
public class SplashActivity extends Activity {  

  private final int SPLASH_DISPLAY_LENGHT = 6000; // 延迟六秒  

  @Override  
  protected void onCreate(Bundle savedInstanceState) {  
      super.onCreate(savedInstanceState);  
      setContentView(R.layout.splash);  

      new Handler().postDelayed(new Runnable() {  
          public void run() {  
              Intent mainIntent = new Intent(SplashActivity.this,  
                      MainActivity.class);  
              SplashActivity.this.startActivity(mainIntent);  
              SplashActivity.this.finish();  
		      overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
          }  

      }, SPLASH_DISPLAY_LENGHT);  

  }  
}  