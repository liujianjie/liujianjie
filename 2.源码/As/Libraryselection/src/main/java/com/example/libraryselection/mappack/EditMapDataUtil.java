package com.example.libraryselection.mappack;

import java.util.Map;
import java.util.Stack;

import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;
import com.example.libraryselection.resource.MapConstant;

// �޸ĵ�ͼ��ά����Ĺ����࣬����Ϊǽ��ȡ��ǽ���ܣ���ͼ����,�ָ���һ���ͼ
public class EditMapDataUtil {
	// ��ʷ��ͼջ
	public static Stack<int[][]> hismapsta = new Stack<int[][]>();
	
	// ԭʼ��ͼ
	public static int yuanMapData[][];
	// �滭�ĵ�ͼ
	public static int currMapData[][];
	
	// ��ʼ����ά���飬����ռ�
	public static void setMapDataSpace(){
		yuanMapData = new int[MapConstant.i][MapConstant.j];
		currMapData = new int[MapConstant.i][MapConstant.j];
		
		// ��ʼ��ֵ���߽�
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				if(i == 0 || i == MapConstant.i - 1 || j == 0 || j == MapConstant.j - 1){
					yuanMapData[i][j] = MapConstant.BAR;
					currMapData[i][j] = MapConstant.BAR;
				}
			}
		}
	}
	
	// ��ָ�����ݵ�ij��ΪԤѡ
	public static void setIjBeYu(int i, int j){
//		yuanMapData[i][j] = MapConstant.YU;
		currMapData[i][j] = MapConstant.YU;
	}
	// ��ָ�����ݵ�ij��Ϊ��λ
	public static void setIjBeSeat(int i, int j){
		yuanMapData[i][j] = MapConstant.SEAT;
		currMapData[i][j] = MapConstant.SEAT;
	}
	// ��ָ�����ݵ�ij��Ϊǽ
	public static void setIjBeBar(int i, int j){
		yuanMapData[i][j] = MapConstant.BAR;
		currMapData[i][j] = MapConstant.BAR;
	}
	// ��ָ�����ݵ�ij��Ϊ�յ�
	public static void setIjBeField(int i, int j){
		yuanMapData[i][j] = MapConstant.FIELD;
		currMapData[i][j] = MapConstant.FIELD;
	}
	// ��ָ�����ݵ�ij��Ϊ���
	public static void setIjBeShujia(int i, int j){
		yuanMapData[i][j] = MapConstant.SHUJIA;
		currMapData[i][j] = MapConstant.SHUJIA;
	}
	// ��ָ�����ݵ�ij��Ϊ��������λ
	public static void setIjBeSeatNo(int i, int j){
		yuanMapData[i][j] = MapConstant.SEATNO;
		currMapData[i][j] = MapConstant.SEATNO;
	}
	// ����ͼ���ݸ��ݱ�������Ϊ��λ��ǽ���յ�,�����Ըı�
	public static void setIjBeThing(int i, int j, int flag){
		switch (flag) {
			case 0:// �յ�
				setIjBeField(i, j);
			break;
			case 1:// ��λ
				setIjBeSeat(i, j);
				break;
			case 2:// �ϰ�
				setIjBeBar(i, j);
				break;
			case 3:// ��������λ
				setIjBeSeatNo(i, j);
				break;
			case 4:// ���
				setIjBeShujia(i, j);
				break;
		default:
			break;
		}
	}
	
	// �Ƿ�Խ�磬falseû�У�true��,��Χǽ���Ǳ߽�
	public static boolean isOverLine(int i, int j){
		if(i <= 0 || i >= MapConstant.i - 1 || j <= 0 || j >= MapConstant.j - 1){
			return true;
		}
		return false;
	}
	/**-------------------�༭��ͼ���---------------------*/
	// �ָ���Ԥѡλ��ѡ�еģ�ԭ��������
	public static void recoverMapDataFromData(){// ���� ��Ԥѡ�͸�ԭ
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				// ��Ԥѡ
				if(currMapData[m][n] == MapConstant.YU){
					currMapData[m][n] = yuanMapData[m][n];
				}
			}
		}
	}
	// �����滭��ʱ����Ҫ���������ͬ��������������
	/*
	 flag
	 1.�����û���ָ�ƶ�����Ԥѡ��Ԥѡ����Ҫ���õ�ͼֵΪԤѡ
	 2.�����û�λ�õĽ�����С����һ�εĽ����㣬��Ҫ�ָ���ͼԭ��ֵ
	 3.�����û��������ƶ�̧���ˣ�Ҳ��ͬflag,��ͬ�����ٻ滭��Χ��ʱ�򱣴���һ�ݾɵ�ͼֵ
	 */
	// �����滭��Ԥѡλ��
	public static void setStillDraw(int start[] , int end[], int flag){
		if(flag == 3){
			copyOneMapData();// �ȱ���һ��
		}
		// ���һ��ʼ�ʹ��ڱ߽�Ͳ�Ҫѡ��
		if(isOverLine(start[1],start[0]) || isOverLine(end[1],end[0])){
				
		}else{
			// 1.�߽紦��
			// ֻѡ��һ��λ��
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
				// ��ʼ�������Ϸ��������������·� o
				if(start[0] < end[0] && start[1] < end[1]){
					midstart = start;
					midsend = end;
				}
				if(start[0] < end[0] && start[1] == end[1]){
					midstart = start;
					midsend = end;
				}
				//  ����  ����  
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
				// ���ϣ�����
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
				// ���£����� o
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
	// ��ǰ��ͼ���ݣ�������ԭʼ�����С�
	public static void copyOneMapData(){
		for(int m = 0; m < MapConstant.i; m++){
			yuanMapData[m] = currMapData[m].clone();
		}
	}
	// ����ͼ��ֵ��maplist��ȡ,��Ҫ���
	public static void getMapDataFromMapList(){
		MapBean mapbean = MapConstant.showandeditmaplist.get(MapConstant.curmappos);
		// ���
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				yuanMapData[m][n] = mapbean.mapdata[m][n];
				currMapData[m][n] = mapbean.mapdata[m][n];
			}
		}
	}
	// ����ͼ��ֵ��mapmoudlelist��ȡ����Ҫ���
	public static void getMapDataFromMapMoudleList(){
		MapMoudleBean mapbean = MapConstant.showeditmapmoudlelist.get(MapConstant.curmapmoudlepos);
		// ���
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				yuanMapData[m][n] = mapbean.mapdata[m][n];
				currMapData[m][n] = mapbean.mapdata[m][n];
			}
		}
	}
	// ֱ�Ӵ�һ����ά�����������ͼ��������maplist����ģ���ͼ
	public static void setMapDataFromData(int mdata[][]){
		// ���
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				yuanMapData[m][n] = mdata[m][n];
				currMapData[m][n] = mdata[m][n];
			}
		}
	}
	// ��ȡ��ͼ��ԭʼ��ͼ���������飬���
	public static int[][] getYuanMapDataofDeep(){
		int[][] newmap =  new int[MapConstant.i][MapConstant.j];
		for(int m = 0; m < MapConstant.i; m++){
			for(int n = 0; n < MapConstant.j; n++){
				newmap[m][n] = yuanMapData[m][n];
			}
		}
		return newmap;
	}
	
	
	/**ѡ����λ�ĵ�ͼ����*/
	// ֱ�ӽ�ָ��λ�ñ�Ϊԭ��ͼ
	public static void recoverDataFromYuanByIJ(int m, int n){
		currMapData[m][n] = yuanMapData[m][n];
	}
	
	// �ָ���ͼ����
	// ����������ָ���cur��yuanshi
	public static void recoverMapLayerFromStack(int hismap[][]){
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				yuanMapData[i][j] = hismap[i][j];
				currMapData[i][j] = hismap[i][j];
			}
		}
	}
	// ȡ����������λ
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
