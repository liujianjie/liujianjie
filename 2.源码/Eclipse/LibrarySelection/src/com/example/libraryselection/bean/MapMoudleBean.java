package com.example.libraryselection.bean;

import com.example.libraryselection.resource.MapConstant;

// 地图模板表
public class MapMoudleBean {
	public int mid;
	public String mname;
	public String mlayermap;
	public int mapdata[][];// int二维数组
	
	public MapMoudleBean(){
		mapdata = new int[MapConstant.i][MapConstant.j]; 
	}
	public MapMoudleBean(String name){
		mapdata = new int[MapConstant.i][MapConstant.j]; 
		mname = name;
	}
	// 根据name 和 mapdata初始化
	public MapMoudleBean(String name, int data[][]){
		mapdata = new int[MapConstant.i][MapConstant.j]; 
		// 设置值
		giveDataVal(data);
		mname = name;
		mlayermap = mapToStr();
	}
	// 深拷贝
	public void giveDataVal(int data[][]){
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				mapdata[i][j] = data[i][j];
			}
		}
	}
	// 二维数组变为string
	public String mapToStr(){
		StringBuilder sb = new StringBuilder();
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				sb.append(mapdata[m][n]);
			}
		}
		mlayermap = sb.toString();
		return sb.toString();
	}
	// 字符变为二维数组,flayermap -> mapdata
	public void StrToMapData(){
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				mapdata[m][n] = (byte) (mlayermap.charAt(MapConstant.j * m + n) - '0');// 很麻烦
			}
		}
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getMlayermap() {
		return mlayermap;
	}
	public void setMlayermap(String mlayermap) {
		this.mlayermap = mlayermap;
	}
	public int[][] getMapdata() {
		return mapdata;
	}
	public void setMapdata(int[][] mapdata) {
		this.mapdata = mapdata;
	}
	
}
