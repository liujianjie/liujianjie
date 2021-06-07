package com.example.libraryselection.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MapMoudleDao {
	public MapMoudleDao(){
		
	}
	// ��ѯ
	public List<MapMoudleBean> readMapMoudleData(SQLiteDatabase db){
		List<MapMoudleBean> mapli = new ArrayList<MapMoudleBean>();
		String[] columns = { "mid", "mname","mlayermap"};// ��ȡ����
		Cursor cursor = db.query("mapmoudle", columns, "", null, "", null, "");
		if(cursor.getCount() != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				MapMoudleBean mapmoudle = new MapMoudleBean();
				mapmoudle.setMid(cursor.getInt(cursor.getColumnIndex("mid")));
				mapmoudle.setMname(cursor.getString(cursor.getColumnIndex("mname")));
				mapmoudle.setMlayermap(cursor.getString(cursor.getColumnIndex("mlayermap")));
				mapmoudle.StrToMapData();// ת����ά
				mapli.add(mapmoudle);
			}
		}
		cursor.close();
		return mapli;
	}
	// ���
	public int insertMapMoudle(SQLiteDatabase db, MapMoudleBean mapmoudlebean){
		ContentValues content = new ContentValues();
		content.put("mname", mapmoudlebean.getMname());
		content.put("mlayermap", mapmoudlebean.getMlayermap());// string
		return (int)db.insert("mapmoudle", null, content);
	}
	// ��ӷ�������
	public int insertMapMoudleBeanReturnZhu(SQLiteDatabase db, MapMoudleBean mapmoudlebean){
		int i = insertMapMoudle(db, mapmoudlebean);
		if(i > 0){
			Cursor cursor = db.rawQuery("select last_insert_rowid() from mapmoudle",null);
			int strid = 0;
			if(cursor.moveToFirst()){
				strid = cursor.getInt(0);
			} 
			Log.i("library", "insertMapMoudleBeanReturnZhu��������ݵ�id"+strid);
			return strid;
		}
		return i;
	}
	// ɾ��
	public int deleteMapMoudleBeanByid(SQLiteDatabase db,int mid){
		String whereClause = "mid = ?";
		String[] whereArgs = {mid+""};
		int i = db.delete("mapmoudle", whereClause, whereArgs);
		if(i > 0){
			Log.i("library", "ɾ���ɹ���"+mid);
		}
		return i;
	}
	
	// �޸� ����id
	public int updateMapMoudleBeanByid(SQLiteDatabase db, MapMoudleBean mapm){
		ContentValues content = new ContentValues();
		content.put("mname", mapm.getMname());
		content.put("mlayermap", mapm.getMlayermap());
		
		String whereClause = "mid = ?";
		String []whereArgs = {mapm.getMid()+""};
		return db.update("mapmoudle", content, whereClause, whereArgs);
	}
}
