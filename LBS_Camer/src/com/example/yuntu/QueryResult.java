package com.example.yuntu;

import java.util.List;
/********************
 * 
 * @author zhaoqin
 * ���ܣ�JSON���ݽ�������ʾ��ͼ����
 * �������ڣ�2014-8-21
 *
 ********************/
public class QueryResult{
	private List<Datas> datas;
		
	public List<Datas> getdatas(){
		return datas;
	}
	
	public void setDatas(List<Datas> datas){
		this.datas = datas;
	}
	@Override
	public String toString(){
		int num = datas.toString().length();
		return datas.toString().substring(1, num-1);
	}
}
