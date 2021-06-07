package com.example.libraryselection.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;
import com.example.libraryselection.bean.SeatInfoBean;

import android.graphics.Bitmap;
import android.graphics.Rect;

// ��ͼ��صĳ���
public class MapConstant {

	// ��activity������ʱ����Ҫ���ظ��踳ֵ����application
	// ��ͼͼƬbitmap
	public static Bitmap bigwallbitmap, smallwallbitmap;// ǽ
	public static Bitmap bigseatbitmap, smallseatbitmap;// ��λ
	public static Bitmap bigseatnobitmap, smallseatnobitmap;// ��������λ
	public static Bitmap bigshujiabitmap, smallshujiabitmap;// ���
	public static Bitmap bigfloorbitmap, smallfloorbitmap;// �ذ�
	
	// ͼƬ��С
	public static int MapBigimgWidth = 40;
	public static int MapBigimgHeight = 40;
	public static int MapSmallimgWidth = 15;
	public static int MapSmallimgHeight = 15;
	
	// ͼƬ��С�ǹ̶���
	public static Rect BigimgRect = new Rect(0, 0, MapConstant.MapBigimgWidth, MapConstant.MapBigimgHeight);
	public static Rect SmallimgRect = new Rect(0, 0, MapConstant.MapSmallimgWidth, MapConstant.MapSmallimgHeight);
	
	// �ҵ���λ����Ϣ
	public static List<SeatInfoBean> myseatlist = new ArrayList<SeatInfoBean>();
	// ��λ���е���Ϣ
	public static List<SeatInfoBean> curallseatlist = new ArrayList<SeatInfoBean>();
	
	// ��ʾ��ͼ���޸ĵ�ͼ���õ�list ��Ҫ��ʼ��
	public static List<MapBean> showandeditmaplist = new ArrayList<MapBean>();;
	// ��ǰ��ʾ��ͼ��list�±�
	public static int curmappos;
	
	// ����curmappos�õ�mapbean�õ���ȡ¥���
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
	// ����curmappos�õ�mapbean�õ���ȡ����
	public static int getCurLayerZhujian(){
		return MapConstant.showandeditmaplist.get(curmappos).getFid();
	}
	
	// ��ʾģ�幫�õ�list����Ҫ��ʼ��
	public static List<MapMoudleBean> showeditmapmoudlelist = new ArrayList<MapMoudleBean>();
	// ��ǰ��ʾģ���ͼ��list�±�
	public static int curmapmoudlepos;
	// ����curmapmoudlepos�õ�mapmouldebean�õ���ȡ����
	public static String getMoudleName(){
		return MapConstant.showeditmapmoudlelist.get(curmapmoudlepos).getMname();
	}
	
	// Ϊ���׼���ģ�Ҫ��ӵ��ڼ��㣬��maplist���õ� 
	public static int getAddLayerPos(){
		// Ĭ����1
		int layer = 1;
		int cnt = showandeditmaplist.size();
		if(cnt > 0){
			// Ĭ����������
			layer = cnt;
			boolean order = true;
			List<Integer> layerlist = new ArrayList<Integer>();
			for(int i = 0; i < cnt; i++){
				layerlist.add(showandeditmaplist.get(i).getFlayer());
			}
			// ������1��ʼ��i����ѭ����Ҳ��¥���
			// ��list�в���������
			for(int i = 1; i <= cnt; i++){
				// ��һ�㲻����list������Ҫ�����ڵĲ�������
				if(!layerlist.contains(i)){
					layer = i;
					order = false;
					break;
				}
			}
			// �����������ͼ�1
			if(order){
				layer++;
			}
		}
		return layer;
	}
		
	public static int i = 23;// ��
	public static int j = 34;// ��
	public static int PaintShapeLength = 20;// �滭ͼ�εĳ���
	public static int PaintRecycleRadius = 20;// �滭ͼ�εİ뾶
	
	public static int MapPiexl = 40;
	
	public static int ShowMapPiexl = 15;
	
	// ��ͼֵ,0�ޣ�1�ϰ���2��λ��3��������λ��4��ܣ�9Ԥѡ
	public static int FIELD = 0;
	public static int BAR = 1;
	public static int SEAT = 2;
	public static int SEATNO = 3;
	public static int SHUJIA = 4;
	public static int YU = 9;
	
}
