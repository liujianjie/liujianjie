package com.example.libraryselection.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;
import com.example.libraryselection.bean.SeatInfoBean;

import android.graphics.Bitmap;
import android.graphics.Rect;

// 地图相关的常量
public class MapConstant {

	// 在activity启动的时候需要加载给予赋值，在application
	// 地图图片bitmap
	public static Bitmap bigwallbitmap, smallwallbitmap;// 墙
	public static Bitmap bigseatbitmap, smallseatbitmap;// 座位
	public static Bitmap bigseatnobitmap, smallseatnobitmap;// 不可用座位
	public static Bitmap bigshujiabitmap, smallshujiabitmap;// 书架
	public static Bitmap bigfloorbitmap, smallfloorbitmap;// 地板
	
	// 图片大小
	public static int MapBigimgWidth = 40;
	public static int MapBigimgHeight = 40;
	public static int MapSmallimgWidth = 15;
	public static int MapSmallimgHeight = 15;
	
	// 图片大小是固定的
	public static Rect BigimgRect = new Rect(0, 0, MapConstant.MapBigimgWidth, MapConstant.MapBigimgHeight);
	public static Rect SmallimgRect = new Rect(0, 0, MapConstant.MapSmallimgWidth, MapConstant.MapSmallimgHeight);
	
	// 我的座位表信息
	public static List<SeatInfoBean> myseatlist = new ArrayList<SeatInfoBean>();
	// 座位表中的信息
	public static List<SeatInfoBean> curallseatlist = new ArrayList<SeatInfoBean>();
	
	// 显示地图和修改地图公用的list 需要初始化
	public static List<MapBean> showandeditmaplist = new ArrayList<MapBean>();;
	// 当前显示地图的list下标
	public static int curmappos;
	
	// 更加curmappos得到mapbean得到获取楼层号
	public static int getLayerNumber(){
		return MapConstant.showandeditmaplist.get(curmappos).getFlayer();
	}
	public static int getLayerNumberByFid(int fid){
		for(MapBean map : showandeditmaplist){
			if(fid == map.getFid()){
				return map.getFlayer();
			}
		}
		return 0;
	}
	// 更据curmappos得到mapbean得到获取主键
	public static int getCurLayerZhujian(){
		return MapConstant.showandeditmaplist.get(curmappos).getFid();
	}
	
	// 显示模板公用的list，需要初始化
	public static List<MapMoudleBean> showeditmapmoudlelist = new ArrayList<MapMoudleBean>();
	// 当前显示模板地图的list下标
	public static int curmapmoudlepos;
	// 更加curmapmoudlepos得到mapmouldebean得到获取名称
	public static String getMoudleName(){
		return MapConstant.showeditmapmoudlelist.get(curmapmoudlepos).getMname();
	}
	
	// 为添加准备的，要添加到第几层，用maplist来得到 
	public static int getAddLayerPos(){
		// 默认是1
		int layer = 1;
		int cnt = showandeditmaplist.size();
		if(cnt > 0){
			// 默认是总数了
			layer = cnt;
			boolean order = true;
			List<Integer> layerlist = new ArrayList<Integer>();
			for(int i = 0; i < cnt; i++){
				layerlist.add(showandeditmaplist.get(i).getFlayer());
			}
			// 层数从1开始，i即是循环数也是楼层号
			// 找list中不存在这层号
			for(int i = 1; i <= cnt; i++){
				// 这一层不存在list，就让要不存在的层号是这个
				if(!layerlist.contains(i)){
					layer = i;
					order = false;
					break;
				}
			}
			// 层数完整，就加1
			if(order){
				layer++;
			}
		}
		return layer;
	}
		
	public static int i = 23;// 行
	public static int j = 34;// 列
	public static int PaintShapeLength = 20;// 绘画图形的长度
	public static int PaintRecycleRadius = 20;// 绘画图形的半径
	
	public static int MapPiexl = 40;
	
	public static int ShowMapPiexl = 15;
	
	// 地图值,0无，1障碍，2座位，3不可用座位，4书架，9预选
	public static int FIELD = 0;
	public static int BAR = 1;
	public static int SEAT = 2;
	public static int SEATNO = 3;
	public static int SHUJIA = 4;
	public static int YU = 9;
	
}
