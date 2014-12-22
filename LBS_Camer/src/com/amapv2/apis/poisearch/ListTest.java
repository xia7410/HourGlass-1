package com.amapv2.apis.poisearch;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/*****************************
 * 
 * @author zhaoqin
 * 功能：列表显示兴趣点
 * 更新日期：2014-8-21
 *
 */
public class ListTest extends ListActivity {
	ListView mListView = null;
	@Override
	public void onCreate(Bundle savedInstanceState){
		Intent intent = getIntent();
		String[] data = intent.getStringArrayExtra("list");
		mListView = getListView();
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
		mListView.setOnItemClickListener(new OnItemClickListener() {  
	        @Override  
	        public void onItemClick(AdapterView<?> adapterView, View view, int position,  
	            long id) {  
	        	Intent i = new Intent();
	        	i.putExtra("name", position);
	        	ListTest.this.setResult(RESULT_OK, i);
	        	ListTest.this.finish();
	        }  
	    });  
		super.onCreate(savedInstanceState);
	}
}
