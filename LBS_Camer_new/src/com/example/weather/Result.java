package com.example.weather;

import java.util.List;
/********************
 * 
 * @author zhaoqin
 * 功能：根据JSON数据格式，获取天气信息
 * 更新日期：2014-8-21
 *
 ********************/

public class Result {
	private String currentCity;
	private String pm25;
	private List<Index> index;
	private List<Weather_data> weather_data;

	
	public String getcurrentCity(){
		return currentCity;
	}
	
	public void setcurrentCity(String currentCity){
		this.currentCity = currentCity;
	}
	
	public String getpm25(){
		return pm25;
	}
	
	public void setpm25(String pm25){
		this.pm25 = pm25;
	}
	
	public List<Index> getindex(){
		return index;
	}
	
	public void setindex(List<Index> index){
		this.index = index;
	}
	
	public List<Weather_data> getweather_data(){
		return weather_data;
	}
	
	public void setweather_data(List<Weather_data> weather_data){
		this.weather_data = weather_data;
	}
	@Override
	public String toString(){
			return  "\n" + currentCity + " " + weather_data.get(0) +
					"\n\nPM2.5:" + pm25 + "\n" + "\n"
					+ index.get(0) + "\n" + index.get(2) + "\n"
					+ index.get(3) + "\n" + index.get(4);
	}
}