package com.amapv2.apis.poisearch;

import java.util.List;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.Cinema;
import com.amap.api.services.poisearch.Dining;
import com.amap.api.services.poisearch.Hotel;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.amap.api.services.poisearch.Scenic;
import com.example.lbs_camer.R;

/*********************
 * 
 * @author zhaoqin
 * 功能：利用高德地图，实现兴趣点的搜索和显示
 * 更新日期：2014-8-21
 *
 */
public class PoiAroundSearchActivity extends FragmentActivity implements
		OnMarkerClickListener, InfoWindowAdapter, OnItemSelectedListener,
		OnPoiSearchListener, OnMapClickListener, OnInfoWindowClickListener,
		OnClickListener {
	private AMap aMap;
	private ProgressDialog progDialog = null;// 搜索时进度条
	private Spinner selectDeep;//服务选项列表
	private String[] itemDeep = { "餐饮", "景区", "酒店", "影院" };
	private Spinner selectType;//选择团购或者优惠
	private String[] itemTypes = { "所有poi", "有团购", "有优惠", "有团购或者优惠" };
	private String deepType = "";
	private int searchType = 0;// 搜索类型
	private int tsearchType = 0;// 当前选择搜索类型
	private PoiResult poiResult; // poi返回的结果
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch.Query query;// Poi查询条件类
	private LatLonPoint lp;//当前地址
	private Marker locationMarker; //选择的点
	private PoiSearch poiSearch;
	private PoiOverlay poiOverlay;// poi图层
	private List<PoiItem> poiItems;// poi数据
	private Marker detailMarker;// 显示Marker的详情
	private Button nextButton;// 下一页
	private Button listButton;
	private double geoLat, geoLng;
	private String desc,city;
	private int ListResponse;
	private StringBuffer sb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poiaroundsearch_activity);
		//接收数据
		Intent intent = getIntent();
		geoLat = intent.getDoubleExtra("geoLat", 0);
		geoLng = intent.getDoubleExtra("geoLng", 0);
		desc = intent.getStringExtra("desc");
		city = intent.getStringExtra("city");
		lp = new LatLonPoint(geoLat, geoLng);
		
		init();
	}

	// 初始化AMap对象
	private void init() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			setUpMap();
			setSelectType();
			Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/fangzhengkatongjianti.ttf");
			Button searchButton = (Button) findViewById(R.id.searchButton);
			searchButton.setOnClickListener(this);
			nextButton = (Button) findViewById(R.id.nextButton);
			nextButton.setOnClickListener(this);
			listButton = (Button) findViewById(R.id.listButton);
			listButton.setOnClickListener(this);
			// 默认下一页按钮、列表按钮不可点
			nextButton.setClickable(false);
			listButton.setClickable(false);
			searchButton.setTypeface(typeFace);
			nextButton.setTypeface(typeFace);
			listButton.setTypeface(typeFace);
			//当前位置信息
			locationMarker = aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 1)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.pin))
					.position(new LatLng(lp.getLatitude(), lp.getLongitude()))
					.title(desc.substring(0, 12) + "\n" + desc.substring(12, desc.length()) + "为中心点，查其周边"));
			locationMarker.showInfoWindow();

		}
	}
    @Override  
    public void onBackPressed() {  
        super.onBackPressed();  
        overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);     
    }  
    
    //服务类型选择
	private void setUpMap() {
		selectDeep = (Spinner) findViewById(R.id.spinnerdeep);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemDeep);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectDeep.setAdapter(adapter);
		selectDeep.setOnItemSelectedListener(this);// 添加spinner选择框监听事件
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
	}
	
	//团购、优惠选择类型
	private void setSelectType() {
		selectType = (Spinner) findViewById(R.id.searchType);// 搜索类型
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemTypes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectType.setAdapter(adapter);
		selectType.setOnItemSelectedListener(this);// 添加spinner选择框监听事件
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
	}

	//显示进度条
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索中");
		progDialog.show();
	}
	
	//隐藏进度框
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
	
	//poi搜索
	protected void doSearchQuery() {
		showProgressDialog();// 显示进度框
		aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
		currentPage = 0;
		query = new PoiSearch.Query("", deepType, city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页
		
		searchType = tsearchType;
		
		switch (searchType) {
		case 0: {// 所有poi
			query.setLimitDiscount(false);
			query.setLimitGroupbuy(false);
		}
			break;
		case 1: {// 有团购
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(false);
		}
			break;
		case 2: {// 有优惠
			query.setLimitGroupbuy(false);
			query.setLimitDiscount(true);
		}
			break;
		case 3: {// 有团购或者优惠
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(true);
		}
			break;
		}

		if (lp != null) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			// 设置搜索区域为以lp点为圆心，其周围2000米范围
			poiSearch.setBound(new SearchBound(lp, 2000, true));
			poiSearch.searchPOIAsyn();// 异步搜索
		}
	}
	//点击下一页搜索
	public void nextSearch() {
		if (query != null && poiSearch != null && poiResult != null) {
			if (poiResult.getPageCount() - 1 > currentPage) {
				currentPage++;
				
				query.setPageNum(currentPage);// 设置查后一页
				poiSearch.searchPOIAsyn();
			} else {
				ToastUtil
						.show(PoiAroundSearchActivity.this, R.string.no_result);
			}
		}
	}
	
	//查单个POI详情
	public void doSearchPoiDetail(String poiId) {
		System.out.println("deSearchPoiDeatil");
		if (poiSearch != null && poiId != null) {
			poiSearch.searchPOIDetailAsyn(poiId);
		}
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		if (poiOverlay != null && poiItems != null && poiItems.size() > 0) {
			detailMarker = marker;
			doSearchPoiDetail(poiItems.get(poiOverlay.getPoiIndex(marker)).getPoiId());
		}
		detailMarker.setSnippet(sb.toString());
		return false;
	}
	
	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == selectDeep) {
			deepType = itemDeep[position];

		} else if (parent == selectType) {
			tsearchType = position;
		}
		nextButton.setClickable(false);// 改变搜索条件，需重新搜索
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		if (parent == selectDeep) {
			deepType = "餐饮";
		} else if (parent == selectType) {
			tsearchType = 0;
		}
		nextButton.setClickable(false);// 改变搜索条件，需重新搜索
	}

	// POI详情回调
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
		dissmissProgressDialog();// 隐藏对话框
		if (rCode == 0) {
			if (result != null) {// 搜索poi的结果
				if (detailMarker != null) {
					sb = new StringBuffer(result.getSnippet());
					if ((result.getGroupbuys() != null && result.getGroupbuys()
							.size() > 0)
							|| (result.getDiscounts() != null && result
									.getDiscounts().size() > 0)) {

						if (result.getGroupbuys() != null
								&& result.getGroupbuys().size() > 0) {// 取第一条团购信息F
							sb.append("\n团购："
									+ result.getGroupbuys().get(0).getDetail());
						}
						if (result.getDiscounts() != null
								&& result.getDiscounts().size() > 0) {// 取第一条优惠信息
							sb.append("\n优惠："
									+ result.getDiscounts().get(0).getDetail());
						}
					} else {
						sb = new StringBuffer("地址：" + result.getSnippet()
								+ "\n电话：" + result.getTel() + "\n类型："
								+ result.getTypeDes());
					}
					// 判断poi搜索是否有深度信息
					if (result.getDeepType() != null) {
						sb = getDeepInfo(result, sb);
					} else {
					}
				}

			} else {
				ToastUtil
						.show(PoiAroundSearchActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil
					.show(PoiAroundSearchActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(PoiAroundSearchActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(PoiAroundSearchActivity.this,getString(R.string.error_other) + rCode);
		}
	}
	
	//POI深度信息获取
	private StringBuffer getDeepInfo(PoiItemDetail result,
			StringBuffer sbuBuffer) {
		switch (result.getDeepType()) {
		// 餐饮深度信息
		case DINING:
			if (result.getDining() != null) {
				Dining dining = result.getDining();
				sbuBuffer
						.append("\n菜系：" + dining.getTag() + "\n特色："
								+ dining.getRecommend() + "\n来源："
								+ dining.getDeepsrc());
			}
			break;
		// 酒店深度信息
		case HOTEL:
			if (result.getHotel() != null) {
				Hotel hotel = result.getHotel();
				sbuBuffer.append("\n价位：" + hotel.getLowestPrice() + "\n卫生："
						+ hotel.getHealthRating() + "\n来源："
						+ hotel.getDeepsrc());
			}
			break;
		// 景区深度信息
		case SCENIC:
			if (result.getScenic() != null) {
				Scenic scenic = result.getScenic();
				sbuBuffer
						.append("\n价钱：" + scenic.getPrice() + "\n推荐："
								+ scenic.getRecommend() + "\n来源："
								+ scenic.getDeepsrc());
			}
			break;
		// 影院深度信息
		case CINEMA:
			if (result.getCinema() != null) {
				Cinema cinema = result.getCinema();
				sbuBuffer.append("\n停车：" + cinema.getParking() + "\n简介："
						+ cinema.getIntro() + "\n来源：" + cinema.getDeepsrc());
			}
			break;
		default:
			break;
		}
		return sbuBuffer;
	}

	//poi搜索回调方法
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();// 隐藏对话框
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// 清理之前的图标
						poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();//清除标记
						poiOverlay.addToMap();//添加标记
						poiOverlay.zoomToSpan();//调整视图
					
						
						nextButton.setClickable(true);// 设置下一页可点
						listButton.setClickable(true);
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
					} else {
						ToastUtil.show(PoiAroundSearchActivity.this,
								R.string.no_result);
					}
				}	
			} else {
				ToastUtil
						.show(PoiAroundSearchActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil
					.show(PoiAroundSearchActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(PoiAroundSearchActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(PoiAroundSearchActivity.this,getString(R.string.error_other) + rCode);
		}
		locationMarker = aMap.addMarker(new MarkerOptions()
		.anchor(0.5f, 1)
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.pin))
		.position(new LatLng(lp.getLatitude(), lp.getLongitude())));

	}
	@Override
	public void onMapClick(LatLng latng) {
		
	}
	
	@Override
	public void onInfoWindowClick(Marker marker) {
		locationMarker.hideInfoWindow();
		lp = new LatLonPoint(locationMarker.getPosition().latitude,
				locationMarker.getPosition().longitude);
		locationMarker.destroy();
	}

	@Override
	public void onClick(View v) {
		System.out.println("onClick");
		switch (v.getId()) {
		//点击搜索按钮
		case R.id.searchButton:
			doSearchQuery();
			break;
		//点击下一页按钮
		case R.id.nextButton:
			nextSearch();
			break;
		//点击列表按钮	
		case R.id.listButton:
			String[] string = new String[poiItems.size()];
			for(int i = 0; i < poiItems.size(); i++){
				string[i] = poiItems.get(i).toString();
			}
			Intent temp = new Intent();
			temp.putExtra("list", string);
			temp.setClass(this, ListTest.class);
			startActivityForResult(temp, 1000);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		switch (resultCode){
		case RESULT_OK:
			Bundle bundle = data.getExtras();
			ListResponse = bundle.getInt("name");
			doSearchPoiDetail(poiItems.get(ListResponse).getPoiId());
			LatLonPoint temp = poiItems.get(ListResponse).getLatLonPoint();
			aMap.clear();
			poiOverlay.zoomToSpan();
			locationMarker = aMap.addMarker(new MarkerOptions()
			.anchor(0.5f, 1)
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.start1)).position(new LatLng(lp.getLatitude(), lp.getLongitude())));
			locationMarker = aMap.addMarker(new MarkerOptions()
			.anchor(0.5f, 1)
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.end)).position(new LatLng(temp.getLatitude(), temp.getLongitude()))
			.title(poiItems.get(ListResponse).getTitle() + "\n" + sb));
			locationMarker.showInfoWindow();
			break;
			default:
				break;
		}
	}
}
