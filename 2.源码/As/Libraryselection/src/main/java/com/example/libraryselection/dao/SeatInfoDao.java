package com.example.libraryselection.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;
import com.example.libraryselection.bean.SeatInfoBean;
import com.example.libraryselection.bean.UserBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SeatInfoDao {
	public SeatInfoDao(){
		
	}
	// ��ѯ��ǰ¥����λ�����ݵ�ǰʱ��κ�����
	public List<SeatInfoBean> readSeatInfoByTime(SQLiteDatabase db, String day, int time, int fid){
		List<SeatInfoBean> seatli = new ArrayList<SeatInfoBean>();
		String[] columns = { "sid", "uid","fid", "sday", "stime", "snumber", "sx", "sy"};// ��ȡ����
		String selections = "sday = ? and stime = ? and fid = ?";
		String[] selectionars = {day, time + "", fid+""};
		Cursor cursor = db.query("seatinfo", columns, selections, selectionars, "", null, "");//
		if(cursor.getCount() != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				SeatInfoBean seat = new SeatInfoBean();
				seat.setSid(cursor.getInt(cursor.getColumnIndex("sid")));
				seat.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
				seat.setFid(cursor.getInt(cursor.getColumnIndex("fid")));
				seat.setSday(cursor.getString(cursor.getColumnIndex("sday")));
				seat.setStime(cursor.getInt(cursor.getColumnIndex("stime")));
				seat.setSnumber(cursor.getString(cursor.getColumnIndex("snumber")));
				seat.setSx(cursor.getInt(cursor.getColumnIndex("sx")));
				seat.setSy(cursor.getInt(cursor.getColumnIndex("sy")));
				seatli.add(seat);
			}
		}
		cursor.close();
		return seatli;
	}
	// ����uid��day��time����ѯ�ҵ���λ
	public List<SeatInfoBean> selMySeatBySpeTime(SQLiteDatabase db, String day, int time, int uid){
		List<SeatInfoBean> seatli = new ArrayList<SeatInfoBean>();
		String[] columns = { "sid", "uid","fid", "sday", "stime", "snumber", "sx", "sy"};// ��ȡ����
		String selections = "sday = ? and stime = ? and uid = ?";
		String[] selectionars = {day, time + "", uid+""};
		Cursor cursor = db.query("seatinfo", columns, selections, selectionars, "", null, "");// flayer��¥���
		if(cursor.getCount() != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				SeatInfoBean seat = new SeatInfoBean();
				seat.setSid(cursor.getInt(cursor.getColumnIndex("sid")));
				seat.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
				seat.setFid(cursor.getInt(cursor.getColumnIndex("fid")));
				seat.setSday(cursor.getString(cursor.getColumnIndex("sday")));
				seat.setStime(cursor.getInt(cursor.getColumnIndex("stime")));
				seat.setSnumber(cursor.getString(cursor.getColumnIndex("snumber")));
				seat.setSx(cursor.getInt(cursor.getColumnIndex("sx")));
				seat.setSy(cursor.getInt(cursor.getColumnIndex("sy")));
				seatli.add(seat);
			}
		}
		cursor.close();
		return seatli;
	}
	// ����uid��ѯ�ҵ���λ
	public List<SeatInfoBean> selMySeatByUid(SQLiteDatabase db, int uid, List<SeatInfoBean> seatli){
//		List<SeatInfoBean> seatli = new ArrayList<SeatInfoBean>();
		String[] columns = { "sid", "uid","fid", "sday", "stime", "snumber", "sx", "sy"};// ��ȡ����
		String selections = "uid = ?";
		String[] selectionars = {uid+""};
		Cursor cursor = db.query("seatinfo", columns, selections, selectionars, "", null, "sday desc,stime");// ����sday����stime˳��
		if(cursor.getCount() != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				SeatInfoBean seat = new SeatInfoBean();
				seat.setSid(cursor.getInt(cursor.getColumnIndex("sid")));
				seat.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
				seat.setFid(cursor.getInt(cursor.getColumnIndex("fid")));
				seat.setSday(cursor.getString(cursor.getColumnIndex("sday")));
				seat.setStime(cursor.getInt(cursor.getColumnIndex("stime")));
				seat.setSnumber(cursor.getString(cursor.getColumnIndex("snumber")));
				seat.setSx(cursor.getInt(cursor.getColumnIndex("sx")));
				seat.setSy(cursor.getInt(cursor.getColumnIndex("sy")));
				seatli.add(seat);
			}
		}
		cursor.close();
		return seatli;
	}

	// ���һ��user
	public int insertSeat(SQLiteDatabase db, SeatInfoBean seat){
		ContentValues content = new ContentValues();
		content.put("uid", seat.getUid());
		content.put("fid", seat.getFid());
		content.put("sday", seat.getSday());
		content.put("stime", seat.getStime());
		content.put("snumber", seat.getSnumber());
		content.put("sx", seat.getSx());
		content.put("sy", seat.getSy());
		return (int) db.insert("seatinfo", null, content);
	}
	// ���һ��user ��������
	public int insertSeatReturnId(SQLiteDatabase db, SeatInfoBean seat){
		int i = insertSeat(db, seat);
		if(i > 0){
			Cursor cursor = db.rawQuery("select last_insert_rowid() from seatinfo",null);
			int sid = 0;
			if(cursor.moveToFirst()){
				sid = cursor.getInt(0);
			} 
			Log.i("library", "insertSeatReturnId��������ݵ�id"+sid);
			return sid;
		}
		return i;
	}
	// �޸�
	public int updateSeatById(SQLiteDatabase db, SeatInfoBean seat, int sid){
		ContentValues content = new ContentValues();
		content.put("fid", seat.getFid());
		content.put("snumber", seat.getSnumber());
		content.put("sx", seat.getSx());
		content.put("sy", seat.getSy());
		
		String whereClause = "sid = ?";
		String []whereArgs = {sid+""};
		return db.update("seatinfo", content, whereClause, whereArgs);
	}
	// ɾ��
	public int deleteSeatById(){
		return 0;
	}
	// ͨ��uidɾ��
	public int deleteSeatByUid(SQLiteDatabase db, int uid){
		String whereClause = "uid = ?";
		String[] whereArgs = {uid+""};
		int i = db.delete("seatinfo", whereClause, whereArgs);
		if(i > 0){
			Log.i("library", "ɾ���ɹ���"+uid);
		}
		return i;
	}
	
	// ͨ��fidɾ��
	public int deleteSeatByFid(SQLiteDatabase db, int fid){
		String whereClause = "fid = ?";
		String[] whereArgs = {fid+""};
		int i = db.delete("seatinfo", whereClause, whereArgs);
		if(i > 0){
			Log.i("library", "ɾ���ɹ���"+fid);
		}
		return i;
	}
	// ͨ��ijɾ��
	public int deleteSeatByIJ(SQLiteDatabase db, int i, int j){
		String whereClause = "sx = ? and sy = ?";
		String[] whereArgs = {j+"", i+""};
		int ct = db.delete("seatinfo", whereClause, whereArgs);
		if(ct > 0){
			Log.i("library", "ɾ���ɹ���");
		}
		return ct;
	}
}
