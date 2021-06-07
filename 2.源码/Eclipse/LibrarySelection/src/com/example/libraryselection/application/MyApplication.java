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
	// ���ݿ�
	MyHistoryUserDao hisdao;
	UserDao userdao;
	// ����ȫ�ֵ�myhelper
	public static LibrarySqliteHelper lihelper;
	
	
	// ȫ��user
	public static UserBean user;
	
	// 
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("application", "���ǲ���ֻ����һ��");
//		Toast.makeText(this, "���ǲ���ֻ����һ��", Toast.LENGTH_SHORT).show();
		initSqliteNameAndVersion();
		
		// ����ȫ��User
		selHistoryAndUser();
		
	}
	// ��ʼ�����ݿ����Ͱ汾
	public void initSqliteNameAndVersion(){
		lihelper = new LibrarySqliteHelper(this, "libraryselection.db", null, SqliteVersion.dataversion);
		hisdao = new MyHistoryUserDao();
		userdao = new UserDao();
	}
	// ��Ҫ��ѯ���ݿ������ʷ��¼
	// �Ƿ�����ʷ��¼���оͲ���user������ȫ��user
	public void selHistoryAndUser(){
		SQLiteDatabase db = MyApplication.lihelper.getReadableDatabase();
		int uid = hisdao.selHistory(db);
		if(uid > 0){
			UserBean user = userdao.selUserByUid(db, uid);
			if(user != null){
				// ����user
				setUser(user);
			}
		}
	}
	
	
	public static void setUser(UserBean user){
		MyApplication.user = user;
	}
}
