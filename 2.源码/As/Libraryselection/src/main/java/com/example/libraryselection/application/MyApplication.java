package com.example.libraryselection.application;

import com.example.libraryselection.bean.UserBean;
import com.example.libraryselection.dao.MyHistoryUserDao;
import com.example.libraryselection.dao.UserDao;
import com.example.libraryselection.resource.MapConstant;
import com.example.libraryselection.sqlite.LibrarySqliteHelper;
import com.example.libraryselection.sqlite.SqliteVersion;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyApplication extends Application{
	// 数据库
	MyHistoryUserDao hisdao;
	UserDao userdao;
	// 试试全局的myhelper
	public static LibrarySqliteHelper lihelper;
	
	
	// 全局user
	public static UserBean user;
	
	// 
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("application", "我是不是只运行一次");
//		Toast.makeText(this, "我是不是只运行一次", Toast.LENGTH_SHORT).show();
		initSqliteNameAndVersion();
		
		// 查找全局User
		selHistoryAndUser();
		
	}
	// 初始化数据库名和版本
	public void initSqliteNameAndVersion(){
		lihelper = new LibrarySqliteHelper(this, "libraryselection.db", null, SqliteVersion.dataversion);
		hisdao = new MyHistoryUserDao();
		userdao = new UserDao();
	}
	// 需要查询数据库查找历史记录
	// 是否有历史记录，有就查找user，赋予全局user
	public void selHistoryAndUser(){
		SQLiteDatabase db = MyApplication.lihelper.getReadableDatabase();
		int uid = hisdao.selHistory(db);
		if(uid > 0){
			UserBean user = userdao.selUserByUid(db, uid);
			if(user != null){
				// 设置user
				setUser(user);
			}
		}
	}
	
	
	public static void setUser(UserBean user){
		MyApplication.user = user;
	}
}
