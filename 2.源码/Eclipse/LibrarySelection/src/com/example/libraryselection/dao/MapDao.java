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

// 地图的dao业务层
public class MapDao {
	
	//
	public MapDao(){
		
	}
	
	// 查询
	public List<MapBean> readMapLayerData(SQLiteDatabase db){
		List<MapBean> mapli = new ArrayList<MapBean>();
		String[] columns = { "fid", "flayer","flayermap"};// 读取的列
		Cursor cursor = db.query("floormap", columns, "", null, "", null, "flayer");// flayer是楼层号
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
	// 查询楼层列表,返回不存在的层
	public int readLayerCol(SQLiteDatabase db){
		// 返回不存在的层号默认为1
		int layer = 1;
		String[] columns = { "flayer"};// 读取的列
		Cursor cursor = db.query("floormap", columns, "", null, "", null, "");
		int cnt = cursor.getCount();
		if(cnt != 0){
			// 默认是总数了
			layer = cnt;
			// 建立一个层数list，读取所有的层数
			List<Integer> layerlist = new ArrayList<Integer>();
			while (!cursor.isLast()) {
				cursor.moveToNext();
				layerlist.add(cursor.getInt(cursor.getColumnIndex("flayer")));
			}
			// 
			boolean order = true;
			// 层数从1开始，i即是循环数也是楼层号
			// 找list中不存在这层号
			for(int i = 1; i <= cnt; i++){
				// 这一层不存在list，就让要不存在的层号是这个
				if(!layerlist.contains(i)){
					layer = i;
					order = false;
					break;
				}
			}
			// 层数完整，就加1
			if(order){
				layer++;
			}
		}
		cursor.close();
		return layer;
	}
	// 增加 nullMapBean
	public void insertDefaultMap(SQLiteDatabase db) {
		// 让设置层数
		MapBean map = new MapBean();
		map.setFlayer(readLayerCol(db));// 设置层数
		// 添加一个空楼层数据到数据库，只有层数,层数应该是要先读取出来，补空的楼层(上面做了)
		ContentValues content = new ContentValues();
		content.put("flayer", map.getFlayer());
		content.put("flayermap", map.mapToStr());// string
		db.insert("floormap", null, content);
	}
	// 增加  有MapBean
	public void insertMapBean(SQLiteDatabase db, MapBean map) {
		ContentValues content = new ContentValues();
		content.put("flayer", map.getFlayer());
		content.put("flayermap", map.getFlayermap());// string
		db.insert("floormap", null, content);
	}
	// 增加 MapBean 返回主键
	public int insertMapBeanReturnZhu(SQLiteDatabase db, MapBean map){
		insertMapBean(db, map);
		Cursor cursor = db.rawQuery("select last_insert_rowid() from floormap",null);
		int strid = 0;
		if(cursor.moveToFirst()){
			strid = cursor.getInt(0);
		} 
		Log.i("library", "insertMapBeanReturnZhu：添加数据的id"+strid);
		return strid;
	}
	// 删除
	public int deleteMapBeanByid(SQLiteDatabase db,int fid){
		String whereClause = "fid = ?";
		String[] whereArgs = {fid+""};
		int i = db.delete("floormap", whereClause, whereArgs);
		if(i > 0){
			Log.i("library", "删除成功："+fid);
		}
		return i;
	}
	
	// 修改 根据id
	public int updateMapBeanByid(SQLiteDatabase db, MapBean map){
		ContentValues content = new ContentValues();
		content.put("flayermap", map.getFlayermap());
		
		String whereClause = "fid = ?";
		String []whereArgs = {map.getFid()+""};
		return db.update("floormap", content, whereClause, whereArgs);
	}
	
}
