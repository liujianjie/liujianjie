package com.example.libraryselection.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

// sqlite数据库操作
public class LibrarySqliteHelper extends SQLiteOpenHelper{
	// 1.用户表 id，手机号，密码，昵称，角色(0 普通用户，1管理员),头像url本地的
	String usersql = "create table user(uid integer primary key autoincrement,username text,userpwd text,role integer,headimg text,phone text);";
	// 2.楼层地图，id，第几层，地图数据
	String mapsql = "create table floormap(fid integer primary key autoincrement,flayer integer, flayermap text);";
	// 3.座位信息,id,用户id，楼层id，日期，时间段int值(0上午，1下午，2晚上,3无)，座位号，sx，sy坐标
	String seatsql = "create table seatinfo(sid integer primary key autoincrement, uid integer, fid integer, sday text, stime integer, snumber text, sx integer, sy integer);";
	// 4.地图模板表,id,模板名，地图数据
	String mapmoudlesql = "create table mapmoudle(mid integer primary key autoincrement,mname text, mlayermap text);";
	// 5.用户登录历史表，id，uid
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
