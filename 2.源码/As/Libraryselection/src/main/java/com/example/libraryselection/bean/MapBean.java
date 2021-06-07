package com.example.libraryselection.bean;

import com.example.libraryselection.resource.MapConstant;

// ��ͼ��bean 
public class MapBean {
	public int fid;// ���
	public int flayer;// ¥��
	public String flayermap;// �洢�����ݿ�Ķ�ά���� 
	
	public int mapdata[][];// int��ά����
	
	// ��ʼ����ʱ�򣬳�ʼ��mapdata����Ե����Ϊbar
	public MapBean(){
		mapdata = new int[MapConstant.i][MapConstant.j];
		// ��ԵΪbar
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				if(i == 0 || i == MapConstant.i - 1 || j == 0 || j == MapConstant.j - 1){
					mapdata[i][j] = MapConstant.BAR;
				}
			}
		}
	}
	
	// ��ά�����Ϊstring
	public String mapToStr(){
		StringBuilder sb = new StringBuilder();
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
//				sb.append((char)(mapdata[m][n] + '0'));// Ҫת��Ϊ�ַ�
				sb.append(mapdata[m][n]);
			}
//			sb.append("\n");
		}
		flayermap = sb.toString();
		return sb.toString();
	}
	// �ַ���Ϊ��ά����,flayermap -> mapdata
	public void StrToMapData(){
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				mapdata[m][n] = (byte) (flayermap.charAt(MapConstant.j * m + n) - '0');// ���鷳
			}
		}
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getFlayer() {
		return flayer;
	}

	public void setFlayer(int flayer) {
		this.flayer = flayer;
	}

	public String getFlayermap() {
		return flayermap;
	}

	public void setFlayermap(String flayermap) {
		this.flayermap = flayermap;
	}

	public int[][] getMapdata() {
		return mapdata;
	}

	public void setMapdata(int[][] mapdata) {
		this.mapdata = mapdata;
	}
	
}
