package com.example.libraryselection.resource;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.widget.Toast;

// Ӧ����صĳ���
public class Constant {
	public static int ScreenWidth;
	public static int ScreenHeight;
	
	// �ڼ��� ��ʮ���
	public static String maplayerString[] = {"","��һ��","�ڶ���","������","���Ĳ�","�����","������","���߲�","�ڰ˲�","�ھŲ�","��ʮ��","��ʮһ��","��ʮ����","��ʮ����","��ʮ�Ĳ�","��ʮ���"};
	
	// ���һ�㣬��ӵĵڼ���
	
	// ����
	
	// ���죬����
	public static String Today = "����:";
	public static String Tomorrow = "����:";
	
	// ���磬���磬����
	public static String SW = "����:9:00~13:30";
	public static String XW = "����:13:30~18:00";
	public static String WS = "����:18:00~21:00";
	public static String NOSEL = "��";
	public static String TIMEDUAN[] = {SW, XW, WS, NOSEL}; 
	// int��ʼ��
	public static int ISWSTART = 900;
	public static int IXWSTART = 1300;
	public static int IWSSTART = 1800;
	
	// ��������
	public static void commonToast(Context con, String msg){
		Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
	}
}
