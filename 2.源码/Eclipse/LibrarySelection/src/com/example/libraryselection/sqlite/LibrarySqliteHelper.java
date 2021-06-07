package com.example.libraryselection.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

// sqlite���ݿ����
public class LibrarySqliteHelper extends SQLiteOpenHelper{
	// 1.�û��� id���ֻ��ţ����룬�ǳƣ���ɫ(0 ��ͨ�û���1����Ա),ͷ��url���ص�
	String usersql = "create table user(uid integer primary key autoincrement,username text,userpwd text,role integer,headimg text,phone text);";
	// 2.¥���ͼ��id���ڼ��㣬��ͼ����
	String mapsql = "create table floormap(fid integer primary key autoincrement,flayer integer, flayermap text);";
	// 3.��λ��Ϣ,id,�û�id��¥��id�����ڣ�ʱ���intֵ(0���磬1���磬2����,3��)����λ�ţ�sx��sy����
	String seatsql = "create table seatinfo(sid integer primary key autoincrement, uid integer, fid integer, sday text, stime integer, snumber text, sx integer, sy integer);";
	// 4.��ͼģ���,id,ģ��������ͼ����
	String mapmoudlesql = "create table mapmoudle(mid integer primary key autoincrement,mname text, mlayermap text);";
	// 5.�û���¼��ʷ��id��uid
	String historyusersql = "create table myhistoryuser(hid integer primary key autoincrement,uid integer);";
		
	public LibrarySqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase sqlite) {
		// TODO Auto-generated method stub
		sqlite.execSQL(usersql);
		sqlite.execSQL(mapsql);
		sqlite.execSQL(seatsql);
		sqlite.execSQL(mapmoudlesql);
		sqlite.execSQL(historyusersql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlite, int arg1, int arg2) {
		// TODO Auto-generated method stub
		sqlite.execSQL("drop table if exists user");
		sqlite.execSQL("drop table if exists floormap");
		sqlite.execSQL("drop table if exists seatinfo");
		sqlite.execSQL("drop table if exists mapmoudle");
		sqlite.execSQL("drop table if exists myhistoryuser");
		onCreate(sqlite);
	}

}
