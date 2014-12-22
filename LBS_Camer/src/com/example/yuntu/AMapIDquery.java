package com.example.yuntu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amap.api.cloud.model.AMapCloudException;
import com.amap.api.cloud.model.CloudItem;
import com.amap.api.cloud.model.CloudItemDetail;
import com.amap.api.cloud.model.LatLonPoint;
import com.amap.api.cloud.search.CloudResult;
import com.amap.api.cloud.search.CloudSearch;
import com.amap.api.cloud.search.CloudSearch.OnCloudSearchListener;
import com.amap.api.cloud.search.CloudSearch.SearchBound;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.amapv2.apis.poisearch.ToastUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import com.example.lbs_camer.R;
/*************************
 * 
 * @author zhaoqin
 * 功能：地图形式展现云图信息
 * 更新日期：2014-8-21
 *
 *************************/
public class AMapIDquery extends Activity implements OnMarkerClickListener,
		InfoWindowAdapter, OnCloudSearchListener, OnInfoWindowClickListener {
	private MapView mapView;
	private AMap mAMap;
	private CloudSearch mCloudSearch;
	private String mTableID = "53ec76f0e4b01dd7561e0b8a";
	private String mKeyWord = ""; // 搜索关键字
	private CloudSearch.Query mQuery;
	private LatLonPoint mCenterPoint = new LatLonPoint(39.942753, 116.428650); //地图显示中心
	//周边搜索坐标
	private LatLonPoint mPoint1 = new LatLonPoint(39.941711, 116.382248);
	private LatLonPoint mPoint2 = new LatLonPoint(39.884882, 116.359566);
	private LatLonPoint mPoint3 = new LatLonPoint(39.878120, 116.437630);
	private LatLonPoint mPoint4 = new LatLonPoint(39.941711, 116.382248);
	private PoiOverlay mPoiCloudOverlay;
	private List<CloudItem> mCloudItems;
	private Marker mCloudIDMarer;
	private String TAG = "AMapYunTuDemo";
	private int ListResponse;

	private CloudItemDetail item;

	private ArrayList<CloudItem> items = new ArrayList<CloudItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amap_activity);
	
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}
	//列表显示
	public void showlist(){
		String[] string = new String[items.size()];
		for(int i = 0; i < items.size(); i++){
			string[i] = "ID：" + items.get(i).getID().toString() + "\n";
			string[i] += "名称：" + items.get(i).toString() + "\n";
			string[i] += "地址：" + items.get(i).getSnippet() + "\n";
			int num = items.get(i).getCustomfield().toString().length();
			string[i] += "心情：" + items.get(i).getCustomfield().toString().substring(6, num - 1);
		}
		Intent temp = new Intent();
		temp.putExtra("list", string);
		temp.setClass(AMapIDquery.this, Listquery.class);
		startActivityForResult(temp, 1001);
	}
	//周边搜索
	public void searchByBound() {
		items.clear();
		SearchBound bound = new SearchBound(new LatLonPoint(
				mCenterPoint.getLatitude(), mCenterPoint.getLongitude()), 10000);
		try {
			mQuery = new CloudSearch.Query(mTableID, mKeyWord, bound);
		} catch (AMapCloudException e) {
			e.printStackTrace();
		}
		mQuery.setPageSize(22);
		CloudSearch.Sortingrules sorting = new CloudSearch.Sortingrules("_id",
				false);
		mQuery.setSortingrules(sorting);
		mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
	}
	//初始化AMap
	private void init() {
		if (mAMap == null) {
			mAMap = mapView.getMap();
		}
		mCloudSearch = new CloudSearch(this);
		mCloudSearch.setOnCloudSearchListener(this);
		mAMap.setOnMarkerClickListener(this);
		mAMap.setOnInfoWindowClickListener(this);
		mAMap.setInfoWindowAdapter(this);
		mAMap.setOnInfoWindowClickListener(this);
		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
		Button searchbutton = (Button) findViewById(R.id.search_by_bound);
		searchbutton.setTypeface(typeFace);
		searchbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchByBound();
			}
		});
		Button listbutton = (Button) findViewById(R.id.search_list);
		listbutton.setTypeface(typeFace);
		listbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showlist();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
		if (rCode == 0 && item != null) {
			if (mCloudIDMarer != null) {
				mCloudIDMarer.destroy();
			}
			mAMap.clear();
			LatLng position = AMapUtil.convertToLatLng(item.getLatLonPoint());
			mAMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition(position, 18, 0, 30)));
			
			mCloudIDMarer = mAMap.addMarker(new MarkerOptions()
					.position(position)
					.title(item.getTitle()).snippet(item.getSnippet())
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
							
			items.add(item);
			Iterator iter = item.getCustomfield().entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
			}
		}

	}

	@Override
	public void onCloudSearched(CloudResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(mQuery)) {
					mCloudItems = result.getClouds();

					if (mCloudItems != null && mCloudItems.size() > 0) {
						mAMap.clear();
						if (mQuery.getBound().getShape().equals("Bound")) {// 圆形
			
							mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									new LatLng(mCenterPoint.getLatitude(),
											mCenterPoint.getLongitude()), 12));

						} else {
							mAMap.addPolygon(new PolygonOptions()
									.add(AMapUtil.convertToLatLng(mPoint1))
									.add(AMapUtil.convertToLatLng(mPoint2))
									.add(AMapUtil.convertToLatLng(mPoint3))
									.add(AMapUtil.convertToLatLng(mPoint4))
									.fillColor(Color.LTGRAY)
									.strokeColor(Color.RED).strokeWidth(1));
							LatLngBounds bounds = new LatLngBounds.Builder()
									.include(AMapUtil.convertToLatLng(mPoint1))
									.include(AMapUtil.convertToLatLng(mPoint2))
									.include(AMapUtil.convertToLatLng(mPoint3))
									.build();
							mAMap.moveCamera(CameraUpdateFactory
									.newLatLngBounds(bounds, 50));
						}

						mPoiCloudOverlay = new PoiOverlay(mAMap, mCloudItems);
						mPoiCloudOverlay.removeFromMap();
						mPoiCloudOverlay.addToMap();
						for (CloudItem item : mCloudItems) {
							items.add(item);
							Iterator iter = item.getCustomfield().entrySet()
									.iterator();
							while (iter.hasNext()) {
								Map.Entry entry = (Map.Entry) iter.next();
								Object key = entry.getKey();
								Object val = entry.getValue();
							}
						}
					} else {
						ToastUtil.show(this, R.string.no_result);
					}
				}
			} else {
				ToastUtil.show(this, R.string.no_result);
			}
		} else {
			ToastUtil.show(this, R.string.error_network);
		}

	}
    @Override  
    public void onBackPressed() {  
        super.onBackPressed();  
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);     
    }  
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {

	}

	/**
	 * 
	 * 返回键监听
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();

		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		switch (resultCode){
		case RESULT_OK:
			Bundle bundle = data.getExtras();
			ListResponse = bundle.getInt("position");
			System.out.println(ListResponse);
			LatLng position = AMapUtil.convertToLatLng(items.get(ListResponse).getLatLonPoint());
			LatLonPoint temp = items.get(ListResponse).getLatLonPoint();
			mPoiCloudOverlay.zoomToSpan();
			
			mCloudIDMarer = mAMap.addMarker(new MarkerOptions()
					.position(position)
					.title("心情:" + items.get(ListResponse).getCustomfield().toString().substring(6, items.get(ListResponse).getCustomfield().toString().length() - 1))
					.snippet("地址:" + items.get(ListResponse).getSnippet())
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
			mCloudIDMarer.showInfoWindow();
			break;
			default:
				break;
		}		
	}
}
