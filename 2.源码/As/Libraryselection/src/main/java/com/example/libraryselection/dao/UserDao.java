package com.example.libraryselection.dao;

import java.util.List;

import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;
import com.example.libraryselection.bean.UserBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDao {
	public UserDao(){
		
	}
	// 根据用户名返回headimguri
	public String selUserHeadImgUriByname(SQLiteDatabase db, String username){
		String[] columns = { "uid", "username", "userpwd", "role", "headimg", "phone"};// 读取的列
		String selection = "username = ?";
		String selectionArgs[] = new String[]{username+""};
		Cursor cursor = db.query("user", columns, selection, selectionArgs, "", null, "");
		int cnt = cursor.getCount();
		if(cnt != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				UserBean user = new UserBean();
				user.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
				user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
				user.setUserpwd(cursor.getString(cursor.getColumnIndex("userpwd")));
				user.setRole(cursor.getInt(cursor.getColumnIndex("role")));
				user.setHeadimg(cursor.getString(cursor.getColumnIndex("headimg")));
				user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
				return user.getHeadimg();
			}
		}
		return null;
	}
	// 根据role是0的用户，返回用户list
	public List<UserBean> selAllUserByRole(SQLiteDatabase db, int role, List<UserBean> list){
		String[] columns = { "uid", "username", "userpwd", "role", "headimg", "phone"};// 读取的列
		String selection = "role = ?";
		String selectionArgs[] = new String[]{role+""};
		Cursor cursor = db.query("user", columns, selection, selectionArgs, "", null, "");
		int cnt = cursor.getCount();
		if(cnt != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				UserBean user = new UserBean();
				user.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
				user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
				user.setUserpwd(cursor.getString(cursor.getColumnIndex("userpwd")));
				user.setRole(cursor.getInt(cursor.getColumnIndex("role")));
				user.setHeadimg(cursor.getString(cursor.getColumnIndex("headimg")));
				user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
				list.add(user);
			}
		}
		return list;
	}
	// 根据uid查询用户，返回用户
	public UserBean selUserByUid(SQLiteDatabase db, int uid){
		UserBean user = new UserBean();
		String[] columns = { "uid", "username", "userpwd", "role", "headimg", "phone"};// 读取的列
		String selection = "uid = ?";
		String selectionArgs[] = new String[]{uid+""};
		Cursor cursor = db.query("user", columns, selection, selectionArgs, "", null, "");
		int cnt = cursor.getCount();
		if(cnt != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				user.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
				user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
				user.setUserpwd(cursor.getString(cursor.getColumnIndex("userpwd")));
				user.setRole(cursor.getInt(cursor.getColumnIndex("role")));
				user.setHeadimg(cursor.getString(cursor.getColumnIndex("headimg")));
				user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
			}
			return user;
		}
		return null;
	}
	// 为了注册 根据name，判断是否存在
	public boolean checkNameExist(SQLiteDatabase db, String name){
		String[] columns = { "uid"};// 读取的列
		String selection = "username = ?";
		String selectionArgs[] = new String[]{name};
		Cursor cursor = db.query("user", columns, selection, selectionArgs, "", null, "");
		int cnt = cursor.getCount();
		if(cnt != 0){
			return true;
		}
		return false;
	}
	// 为了登录
	public UserBean checkLoginUser(SQLiteDatabase db, String name, String pwd){
		UserBean user = new UserBean();
		String[] columns = { "uid", "username", "userpwd", "role", "headimg", "phone"};// 读取的列
		String selection = "username = ? and userpwd = ?";
		String selectionArgs[] = new String[]{name, pwd};
		Cursor cursor = db.query("user", columns, selection, selectionArgs, "", null, "");
		int cnt = cursor.getCount();
		if(cnt != 0){
			while (!cursor.isLast()) {
				cursor.moveToNext();
				user.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
				user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
				user.setUserpwd(cursor.getString(cursor.getColumnIndex("userpwd")));
				user.setRole(cursor.getInt(cursor.getColumnIndex("role")));
				user.setHeadimg(cursor.getString(cursor.getColumnIndex("headimg")));
				user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
			}
			return user;
		}
		return null;
	}
	
	// 添加一个user
	public int insertUser(SQLiteDatabase db, UserBean userbean){
		ContentValues content = new ContentValues();
		content.put("username", userbean.getUsername());// 名称
		content.put("userpwd", userbean.getUserpwd());// 密码
		content.put("role", userbean.getRole());// 角色
		return (int)db.insert("user", null, content);
	}
	// 修改用户
	public int updateUser(SQLiteDatabase db, UserBean userbean){	
		ContentValues content = new ContentValues();
		content.put("username", userbean.getUsername());// 名称
		content.put("userpwd", userbean.getUserpwd());// 密码
		content.put("headimg", userbean.getHeadimg());// 
		content.put("phone", userbean.getPhone());
		
		String whereClause = "uid = ?";
		String []whereArgs = {userbean.getUid()+""};
		return db.update("user", content, whereClause, whereArgs);
	}
	// 删除用户
	public int deleteUserByid(SQLiteDatabase db,int uid){
		String whereClause = "uid = ?";
		String[] whereArgs = {uid+""};
		int i = db.delete("user", whereClause, whereArgs);
		if(i > 0){
			Log.i("library", "删除成功："+uid);
		}
		return i;
	}
}
