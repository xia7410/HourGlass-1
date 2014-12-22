package com.muggins.weather;
/********************
 *
 * @author zhaoqin
 * 功能：根据JSON数据格式，获取天气信息
 * 更新日期：2014-8-21
 *
 ********************/
public class Index {
	private String title;
	private String zs;
	private String des;
		
	public String gettitle(){
		return title;
	}
	
	public void settitle(String title){
		this.title = title;
	}
	
	public String getzs(){
		return zs;
	}
	
	public void setzs(String zs){
		this.zs = zs;
	}
	
	public String getdes(){
		return des;
	}
	
	public void setdes(String des){
		this.des = des;
	}
	
	public String toString(){
		return title + ":" + zs + "\n" + des + "\n";
	}
}
