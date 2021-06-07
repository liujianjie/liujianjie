package com.example.libraryselection.bean;

// 座位信息bean
public class SeatInfoBean {
	public SeatInfoBean(){
		
	}
	public int sid;// 主键
	public int uid;// 用户id
	public int fid;// 楼层id
	public String sday;// 日期
	public int stime;// 时间段
	public String snumber;// 座位号
	public int sx;// j
	public int sy;// i
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getSday() {
		return sday;
	}
	public void setSday(String sday) {
		this.sday = sday;
	}
	public int getStime() {
		return stime;
	}
	public void setStime(int stime) {
		this.stime = stime;
	}
	public String getSnumber() {
		return snumber;
	}
	public void setSnumber(String snumber) {
		this.snumber = snumber;
	}
	public int getSx() {
		return sx;
	}
	public void setSx(int sx) {
		this.sx = sx;
	}
	public int getSy() {
		return sy;
	}
	public void setSy(int sy) {
		this.sy = sy;
	}

	
}
