package com.muggins.yuntu;
/********************
 *
 * @author zhaoqin
 * 功能：JSON数据解析，显示云图数据
 * 更新日期：2014-8-21
 *
 ********************/
public class Datas {
    private String _id;
    private String _name;
    private String _address;
    private String mood;


    public String getid(){
        return _id;
    }

    public void setid(String _id){
        this._id = _id;
    }

    public String getname(){
        return _name;
    }

    public void setname(String _name){
        this._name = _name;
    }

    public String getmood(){
        return mood;
    }

    public void setmood(String mood){
        this.mood = mood;
    }
    public String getaddress(){
        return _address;
    }

    public void setaddress(String _address){
        this._address = _address;
    }
    @Override
    public String toString(){
        return  "\nID:" + _id + "\n名称:" + _name +
                "\n地址:" + _address + "\n心情:" + mood + "\n";
    }
}