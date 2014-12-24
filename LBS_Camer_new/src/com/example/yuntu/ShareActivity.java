package com.example.yuntu;

import com.amapv2.apis.poisearch.ToastUtil;
import com.example.lbs_camer.R;
import com.renn.sharecomponent.MessageTarget;
import com.renn.sharecomponent.RennShareComponent;
import com.renn.sharecomponent.ShareMessageError;
import com.renn.sharecomponent.RennShareComponent.SendMessageListener;
import com.renn.sharecomponent.message.RennTextMessage;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/****************************
 * 
 * @author zhaoqin
 * 功能：将云图分享到人人网
 * 更新日期：2014-8-21
 *
 ***************************/
public class ShareActivity extends Activity {
	private RennShareComponent shareComponent;
	private TextView title;
	private TextView message;
	private EditText edit_title;
	private EditText edit_message;
	private Button sharebutton;
	private MessageTarget type;
	private static final String APP_ID = "228525";//"237274";
	private static final String API_KEY = "1dd8cba4215d4d4ab96a49d3058c1d7f";
	private static final String SECRET_KEY = "48cea4b12f7442c5bff322312ab2c99e";
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
		title = (TextView) findViewById(R.id.share_title_text);
		message = (TextView) findViewById(R.id.share_message_text);
		edit_title = (EditText) findViewById(R.id.share_title);
		edit_message = (EditText) findViewById(R.id.share_message);
		sharebutton = (Button) findViewById(R.id.share_button);
		title.setTypeface(typeFace);
		message.setTypeface(typeFace);
		edit_title.setTypeface(typeFace);
		edit_message.setTypeface(typeFace);
		sharebutton.setTypeface(typeFace);
		sharebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sharemessage();
			}
		});	
		init();
	}
    @Override  
    public void onBackPressed() {  
        super.onBackPressed();  
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);     
    }  
	public void init(){
		shareComponent = RennShareComponent.getInstance(this);
		shareComponent.init(APP_ID, API_KEY, SECRET_KEY);
		type = MessageTarget.TO_RENREN;
	}
	public void sharemessage(){
		RennTextMessage message = new RennTextMessage();
		String temp = edit_message.getText().toString();
		if(!TextUtils.isEmpty(temp)) {
			message.setText(temp);
		}
		temp = edit_message.getText().toString();
		if(!TextUtils.isEmpty(temp)) {
			message.setUrl(temp);
		}
		temp = edit_title.getText().toString();
		if(!TextUtils.isEmpty(temp)) {
			message.setTitle(temp);
		}
		shareComponent.setSendMessageListener(new SendMessageListener() {			
			@Override
			public void onSendMessageSuccess(String messageKey, Bundle bundle) {
				ToastUtil.show(ShareActivity.this, "发送成功");
			}				
			@Override
			public void onSendMessageFailed(String messageKey, ShareMessageError e) {
				ToastUtil.show(ShareActivity.this, "发送失败");
			}			
			@Override
			public void onSendMessageCanceled(String messageKey) {
				ToastUtil.show(ShareActivity.this, "发送取消:");
			}
		});
		shareComponent.sendMessage(message, type);
	}

}
