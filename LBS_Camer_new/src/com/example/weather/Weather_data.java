package com.example.weather;

/********************
 * 
 * @author zhaoqin
 * ���ܣ�����JSON���ݸ�ʽ����ȡ������Ϣ
 * �������ڣ�2014-8-21
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
