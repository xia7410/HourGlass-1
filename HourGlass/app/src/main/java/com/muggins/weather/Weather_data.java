package com.muggins.weather;

/********************
 *
 * @author zhaoqin
 * 功能：根据JSON数据格式，获取天气信息
 * 更新日期：2014-8-21
 *
 ********************/
public class Weather_data {
	private String weather;
	private String temperature;
		
	public String getWeather(){
		return weather;
	}
	
	public void setWeather(String weather){
		this.weather = weather;
	}
	
	public String gettemperature(){
		return temperature;
	}
	
	public void settemperature(String temperature){
		this.temperature = temperature;
	}
	
	
	public String toString(){
		return weather + temperature;
	}
}
