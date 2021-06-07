package com.example.libraryselection.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.libraryselection.bean.MapBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

// ��ͼ��daoҵ���
public class MapDao {
	
	//
	public MapDao(){
		
	}
	
	// ��ѯ
	public List<MapBean> readMapLayerData(SQLiteDatabase db){
		List<MapBean> mapli = new ArrayList<MapBean>();
		String[] columns = { "fid", "flayer","flayermap"};// ��ȡ����
		Cursor cursor = db.query("floormap", columns, "", null, "", null, "flayer");// flayer��¥���
		if(cursor.getCount() != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				MapBean map = new MapBean();
				map.setFid(cursor.getInt(cursor.getColumnIndex("fid")));
				map.setFlayer(cursor.getInt(cursor.getColumnIndex("flayer")));
				map.setFlayermap(cursor.getString(cursor.getColumnIndex("flayermap")));
				map.StrToMapData();
				mapli.add(map);
			}
		}
		cursor.close();
		return mapli;
	}
	// ��ѯ¥���б�,���ز����ڵĲ�
	public int readLayerCol(SQLiteDatabase db){
		// ���ز����ڵĲ��Ĭ��Ϊ1
		int layer = 1;
		String[] columns = { "flayer"};// ��ȡ����
		Cursor cursor = db.query("floormap", columns, "", null, "", null, "");
		int cnt = cursor.getCount();
		if(cnt != 0){
			// Ĭ����������
			layer = cnt;
			// ����һ������list����ȡ���еĲ���
			List<Integer> layerlist = new ArrayList<Integer>();
			while (!cursor.isLast()) {
				cursor.moveToNext();
				layerlist.add(cursor.getInt(cursor.getColumnIndex("flayer")));
			}
			// 
			boolean order = true;
			// ������1��ʼ��i����ѭ����Ҳ��¥���
			// ��list�в���������
			for(int i = 1; i <= cnt; i++){
				// ��һ�㲻����list������Ҫ�����ڵĲ�������
				if(!layerlist.contains(i)){
					layer = i;
					order = false;
					break;
				}
			}
			// �����������ͼ�1
			if(order){
				layer++;
			}
		}
		cursor.close();
		return layer;
	}
	// ���� nullMapBean
	public void insertDefaultMap(SQLiteDatabase db) {
		// �����ò���
		MapBean map = new MapBean();
		map.setFlayer(readLayerCol(db));// ���ò���
		// ���һ����¥�����ݵ����ݿ⣬ֻ�в���,����Ӧ����Ҫ�ȶ�ȡ���������յ�¥��(��������)
		ContentValues content = new ContentValues();
		content.put("flayer", map.getFlayer());
		content.put("flayermap", map.mapToStr());// string
		db.insert("floormap", null, content);
	}
	// ����  ��MapBean
	public void insertMapBean(SQLiteDatabase db, MapBean map) {
		ContentValues content = new ContentValues();
		content.put("flayer", map.getFlayer());
		content.put("flayermap", map.getFlayermap());// string
		db.insert("floormap", null, content);
	}
	// ���� MapBean ��������
	public int insertMapBeanReturnZhu(SQLiteDatabase db, MapBean map){
		insertMapBean(db, map);
		Cursor cursor = db.rawQuery("select last_insert_rowid() from floormap",null);
		int strid = 0;
		if(cursor.moveToFirst()){
			strid = cursor.getInt(0);
		} 
		Log.i("library", "insertMapBeanReturnZhu��������ݵ�id"+strid);
		return strid;
	}
	// ɾ��
	public int deleteMapBeanByid(SQLiteDatabase db,int fid){
		String whereClause = "fid = ?";
		String[] whereArgs = {fid+""};
		int i = db.delete("floormap", whereClause, whereArgs);
		if(i > 0){
			Log.i("library", "ɾ���ɹ���"+fid);
		}
		return i;
	}
	
	// �޸� ����id
	public int updateMapBeanByid(SQLiteDatabase db, MapBean map){
		ContentValues content = new ContentValues();
		content.put("flayermap", map.getFlayermap());
		
		String whereClause = "fid = ?";
		String []whereArgs = {map.getFid()+""};
		return db.update("floormap", content, whereClause, whereArgs);
	}
	
}
