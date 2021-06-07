package com.example.libraryselection.dao;

import com.example.libraryselection.bean.MapBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

// 历史用户dao，历史表只有1条
public class MyHistoryUserDao {
	public MyHistoryUserDao(){
		
	}
	// 记录只有一条 ，返回uid
	public int selHistory(SQLiteDatabase db){
		String[] columns = { "uid"};// 读取的列
		Cursor cursor = db.query("myhistoryuser", columns, "", null, "", null, "");
		int cnt = cursor.getCount();
		if(cnt != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				return cursor.getInt(cursor.getColumnIndex("uid"));
			}
		}
		return 0;
	}
	
	
	// 添加一条记录
	public void insertHistory(SQLiteDatabase db, int uid) {
		ContentValues content = new ContentValues();
		content.put("uid", uid);
		db.insert("myhistoryuser", null, content);
	}
	// 删除记录
	public void deleteAllhis(SQLiteDatabase db){
		db.delete("myhistoryuser", "", null);
	}
}
