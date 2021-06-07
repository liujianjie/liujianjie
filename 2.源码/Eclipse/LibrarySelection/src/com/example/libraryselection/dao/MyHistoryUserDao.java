package com.example.libraryselection.dao;

import com.example.libraryselection.bean.MapBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

// ��ʷ�û�dao����ʷ��ֻ��1��
public class MyHistoryUserDao {
	public MyHistoryUserDao(){
		
	}
	// ��¼ֻ��һ�� ������uid
	public int selHistory(SQLiteDatabase db){
		String[] columns = { "uid"};// ��ȡ����
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
	
	
	// ���һ����¼
	public void insertHistory(SQLiteDatabase db, int uid) {
		ContentValues content = new ContentValues();
		content.put("uid", uid);
		db.insert("myhistoryuser", null, content);
	}
	// ɾ����¼
	public void deleteAllhis(SQLiteDatabase db){
		db.delete("myhistoryuser", "", null);
	}
}
