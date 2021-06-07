package com.example.libraryselection.resource;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.widget.Toast;

// 应用相关的常量
public class Constant {
	public static int ScreenWidth;
	public static int ScreenHeight;
	
	// 第几层 共十五层
	public static String maplayerString[] = {"","第一层","第二层","第三层","第四层","第五层","第六层","第七层","第八层","第九层","第十层","第十一层","第十二层","第十三层","第十四层","第十五层"};
	
	// 添加一层，添加的第几层
	
	// 日期
	
	// 今天，明天
	public static String Today = "今天:";
	public static String Tomorrow = "明天:";
	
	// 上午，下午，晚上
	public static String SW = "上午:9:00~13:30";
	public static String XW = "下午:13:30~18:00";
	public static String WS = "晚上:18:00~21:00";
	public static String NOSEL = "无";
	public static String TIMEDUAN[] = {SW, XW, WS, NOSEL}; 
	// int开始段
	public static int ISWSTART = 900;
	public static int IXWSTART = 1300;
	public static int IWSSTART = 1800;
	
	// 公共弹出
	public static void commonToast(Context con, String msg){
		Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
	}
}
