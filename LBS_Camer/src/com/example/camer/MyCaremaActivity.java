package com.example.camer;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.lbs_camer.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/******************************
 * 
 * @author zhaoqin
 * ���ܣ���������ܣ��Ե�ַ��Ϣ��Ϊ��չ�������Ժ��ѯ��Ƭ
 * ��Ҫ��չ������Ƭ�洢���ƶˣ�ͬ��������һ����ʾ����ͼ��
 * �������ڣ�2014-8-21
 *
 */
public class MyCaremaActivity extends Activity {
	private String ceshi;
	private String str;
	private String fileName;
	private ImageView img;
	private TextView cameraview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Intent intent = getIntent();
		ceshi = intent.getStringExtra("message");
		str = null;
		Date date = null;
		//ϵͳʱ�䣬����ͬһ����λ�õĲ�ͬʱ����Ƭ
		SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH mm ss");
		date = new Date();
		str = format.format(date);
		fileName = "/sdcard/myImage/" + ceshi + str + ".jpg";
		
		Button button = (Button) findViewById(R.id.button);
		Animation animation = AnimationUtils.loadAnimation(MyCaremaActivity.this, R.anim.alpha);
		button.startAnimation(animation);
		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new OnClickListener() {
	    
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 2);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		img = (ImageView) findViewById(R.id.imageView);
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
		cameraview = (TextView) findViewById(R.id.cameraview);
		if (resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			// ���sd�Ƿ����
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}

			Bundle bundle = data.getExtras();
			// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
			Bitmap bitmap = (Bitmap) bundle.get("data");
			FileOutputStream b = null;
			File file = new File("/sdcard/myImage/");
		       if (!file.exists()) {
		         file.mkdirs();
		        }
		        else{
		        }
			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//��ͼƬ��ʾ��ImageView��
			img.setImageBitmap(bitmap);
			cameraview.setTypeface(typeFace);
			cameraview.setText(ceshi);
		}
	}
}