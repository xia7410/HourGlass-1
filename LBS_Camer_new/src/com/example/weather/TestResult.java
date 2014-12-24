package com.example.weather;

import java.util.List;
/********************
 * 
 * @author zhaoqin
 * 功能：根据JSON数据格式，获取天气信息
 * 更新日期：2014-8-21
 *
 ********************/
public class TestResult{
	private String date;
	private List<Result> results;
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public List<Result> getResults(){
		return results;
	}
	
	public void setResults(List<Result> results){
		this.results = results;
	}
	@Override
	public String toString(){
		int num = results.toString().length();
		return "" + results.toString().substring(1, num - 1);
	}
}