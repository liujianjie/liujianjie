package com.example.libraryselection.mappack;

import java.util.Map;
import java.util.Stack;

import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;
import com.example.libraryselection.resource.MapConstant;

// 修改地图二维数组的公共类，有设为墙，取消墙功能，地图数组,恢复上一层地图
public class EditMapDataUtil {
	// 历史地图栈
	public static Stack<int[][]> hismapsta = new Stack<int[][]>();
	
	// 原始地图
	public static int yuanMapData[][];
	// 绘画的地图
	public static int currMapData[][];
	
	// 初始化二维数组，分配空间
	public static void setMapDataSpace(){
		yuanMapData = new int[MapConstant.i][MapConstant.j];
		currMapData = new int[MapConstant.i][MapConstant.j];
		
		// 初始化值，边界
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				if(i == 0 || i == MapConstant.i - 1 || j == 0 || j == MapConstant.j - 1){
					yuanMapData[i][j] = MapConstant.BAR;
					currMapData[i][j] = MapConstant.BAR;
				}
			}
		}
	}
	
	// 将指定数据的ij变为预选
	public static void setIjBeYu(int i, int j){
//		yuanMapData[i][j] = MapConstant.YU;
		currMapData[i][j] = MapConstant.YU;
	}
	// 将指定数据的ij变为座位
	public static void setIjBeSeat(int i, int j){
		yuanMapData[i][j] = MapConstant.SEAT;
		currMapData[i][j] = MapConstant.SEAT;
	}
	// 将指定数据的ij变为墙
	public static void setIjBeBar(int i, int j){
		yuanMapData[i][j] = MapConstant.BAR;
		currMapData[i][j] = MapConstant.BAR;
	}
	// 将指定数据的ij变为空地
	public static void setIjBeField(int i, int j){
		yuanMapData[i][j] = MapConstant.FIELD;
		currMapData[i][j] = MapConstant.FIELD;
	}
	// 将指定数据的ij变为书架
	public static void setIjBeShujia(int i, int j){
		yuanMapData[i][j] = MapConstant.SHUJIA;
		currMapData[i][j] = MapConstant.SHUJIA;
	}
	// 将指定数据的ij变为不可用座位
	public static void setIjBeSeatNo(int i, int j){
		yuanMapData[i][j] = MapConstant.SEATNO;
		currMapData[i][j] = MapConstant.SEATNO;
	}
	// 将地图数据根据变量，变为座位，墙，空地,永久性改变
	public static void setIjBeThing(int i, int j, int flag){
		switch (flag) {
			case 0:// 空地
				setIjBeField(i, j);
			break;
			case 1:// 座位
				setIjBeSeat(i, j);
				break;
			case 2:// 障碍
				setIjBeBar(i, j);
				break;
			case 3:// 不可用座位
				setIjBeSeatNo(i, j);
				break;
			case 4:// 书架
				setIjBeShujia(i, j);
				break;
		default:
			break;
		}
	}
	
	// 是否越界，false没有，true有,周围墙就是边界
	public static boolean isOverLine(int i, int j){
		if(i <= 0 || i >= MapConstant.i - 1 || j <= 0 || j >= MapConstant.j - 1){
			return true;
		}
		return false;
	}
	/**-------------------编辑地图相关---------------------*/
	// 恢复从预选位置选中的，原来的物体
	public static void recoverMapDataFromData(){// 遍历 有预选就复原
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				// 是预选
				if(currMapData[m][n] == MapConstant.YU){
					currMapData[m][n] = yuanMapData[m][n];
				}
			}
		}
	}
	// 持续绘画的时候，需要处理各个不同的情况，精简代码
	/*
	 flag
	 1.代表用户手指移动，在预选，预选，需要设置地图值为预选
	 2.代表用户位置的结束点小于上一次的结束点，需要恢复地图原本值
	 3.代表用户结束了移动抬起了，也是同flag,不同的是再绘画范围的时候保存了一份旧地图值
	 */
	// 持续绘画的预选位置
	public static void setStillDraw(int start[] , int end[], int flag){
		if(flag == 3){
			copyOneMapData();// 先保存一份
		}
		// 如果一开始就大于边界就不要选了
		if(isOverLine(start[1],start[0]) || isOverLine(end[1],end[0])){
				
		}else{
			// 1.边界处理
			// 只选了一个位置
			if(start[1] == end[1] && start[0] == end[0]){
				int insertval = MapConstant.YU;
				switch (flag) {
				case 1:case 3:
					insertval = MapConstant.YU;
					break;
				case 2:
					insertval = yuanMapData[start[1]][start[0]];
					break;
				}
				currMapData[start[1]][start[0]] = insertval;
			}else{
				int midstart[] = new int[2];
				int midsend[] = new int[2];
				// 开始点在左上方，结束点在右下方 o
				if(start[0] < end[0] && start[1] < end[1]){
					midstart = start;
					midsend = end;
				}
				if(start[0] < end[0] && start[1] == end[1]){
					midstart = start;
					midsend = end;
				}
				//  左下  右上  
				if(start[0] < end[0] && start[1] > end[1]){
					midstart[0] = start[0];
					midstart[1] = end[1];
					midsend[0] = end[0];
					midsend[1] = start[1];
				}
				if(start[0] > end[0] && start[1] == end[1]){
					midstart = end;
					midsend = start;
				}
				// 右上，左下
				if(start[0] > end[0] && start[1] < end[1]){
					midstart[0] = end[0];
					midstart[1] = start[1];
					
					midsend[0] = start[0];
					midsend[1] = end[1];
				}
				if(start[0] == end[0] && start[1] < end[1]){
					midstart = start;
					midsend = end;
				}
				// 右下，坐上 o
				if(start[0] > end[0] && start[1] > end[1]){
					midstart = end;
					
					midsend = start;
				}
				if(start[0] == end[0] && start[1] > end[1]){
					midstart = end;
					midsend = start;
				}
				for(int m = midstart[1]; m <= midsend[1]; m++){// y
					for(int n = midstart[0]; n <= midsend[0]; n++){// x
						int insertval = MapConstant.YU;
						switch (flag) {
						case 1:case 3:
							insertval = MapConstant.YU;
							break;
						case 2:
							insertval = yuanMapData[m][n];
							break;
						}
						currMapData[m][n] = insertval;
					}
				}
			}
		}
	}
	// 当前绘图数据，拷贝到原始数据中。
	public static void copyOneMapData(){
		for(int m = 0; m < MapConstant.i; m++){
			yuanMapData[m] = currMapData[m].clone();
		}
	}
	// 给地图赋值从maplist获取,需要深拷贝
	public static void getMapDataFromMapList(){
		MapBean mapbean = MapConstant.showandeditmaplist.get(MapConstant.curmappos);
		// 深拷贝
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				yuanMapData[m][n] = mapbean.mapdata[m][n];
				currMapData[m][n] = mapbean.mapdata[m][n];
			}
		}
	}
	// 给地图赋值从mapmoudlelist获取，需要深拷贝
	public static void getMapDataFromMapMoudleList(){
		MapMoudleBean mapbean = MapConstant.showeditmapmoudlelist.get(MapConstant.curmapmoudlepos);
		// 深拷贝
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				yuanMapData[m][n] = mapbean.mapdata[m][n];
				currMapData[m][n] = mapbean.mapdata[m][n];
			}
		}
	}
	// 直接从一个二维数组深拷贝给地图，不论是maplist还是模板地图
	public static void setMapDataFromData(int mdata[][]){
		// 深拷贝
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				yuanMapData[m][n] = mdata[m][n];
				currMapData[m][n] = mdata[m][n];
			}
		}
	}
	// 获取地图，原始地图，返回数组，深拷贝
	public static int[][] getYuanMapDataofDeep(){
		int[][] newmap =  new int[MapConstant.i][MapConstant.j];
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				newmap[m][n] = yuanMapData[m][n];
			}
		}
		return newmap;
	}
	
	
	/**选择座位的地图操作*/
	// 直接将指定位置变为原地图
	public static void recoverDataFromYuanByIJ(int m, int n){
		currMapData[m][n] = yuanMapData[m][n];
	}
	
	// 恢复地图操作
	// 从已有数组恢复给cur，yuanshi
	public static void recoverMapLayerFromStack(int hismap[][]){
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				yuanMapData[i][j] = hismap[i][j];
				currMapData[i][j] = hismap[i][j];
			}
		}
	}
	// 取消不可用座位
	public static boolean cancleYQseat(){
		boolean ischanage = false;
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				if(yuanMapData[i][j] == MapConstant.SEATNO){
					yuanMapData[i][j] = MapConstant.SEAT;
					currMapData[i][j] = MapConstant.SEAT;
					ischanage = true;
				}
			}
		}
		return ischanage;
	}
}
