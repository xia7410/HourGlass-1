package com.example.weather;

import java.util.List;
/********************
 * 
 * @author zhaoqin
 * ���ܣ�����JSON���ݸ�ʽ����ȡ������Ϣ
 * �������ڣ�2014-8-21
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