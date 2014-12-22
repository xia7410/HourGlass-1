package com.muggins.yuntu;

import java.util.List;
/********************
 *
 * @author zhaoqin
 * 功能：JSON数据解析，显示云图数据
 * 更新日期：2014-8-21
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
