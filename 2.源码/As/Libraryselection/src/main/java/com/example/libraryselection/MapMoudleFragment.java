package com.example.libraryselection;

import com.example.libraryselection.adpter.MapMoudleAdapter;
import com.example.libraryselection.adpter.ShowMapLayerAdapter;
import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;
import com.example.libraryselection.dao.MapMoudleDao;
import com.example.libraryselection.resource.MapConstant;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MapMoudleFragment extends Fragment{
	// 上级
	private MainActivity maincontext;
	
	// 数据库相关
	SQLiteDatabase db;
	MapMoudleDao mapmoudledao;
	
	// recycle
	private RecyclerView recycle;
	private GridLayoutManager gridManager;
	private MapMoudleAdapter mapmoudleadater;
	
	public MapMoudleFragment(MainActivity man){
		this.maincontext = man;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 初始化
		initArgume();
		// 从数据库读取数据
		selMapMoudleDataFromSqlite();
	}
	// 初始化参数
	protected void initArgume() {
		// db
		db = MyApplication.lihelper.getReadableDatabase();
		mapmoudledao = new MapMoudleDao();
		
		// recycle
		gridManager = new GridLayoutManager(maincontext, 2);// 2列
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mapmoudle, null);
		findView(view);
		// 数据赋予
		setRecyleDataAndLayout();
		Log.i("library", "MapMoudleFragment：onCreateView");
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		notifyReclyerView();
	}
	// 寻找控件
	protected void findView(View view){
		recycle = (RecyclerView) view.findViewById(R.id.frg_mapmoudle_recycleview);
	}
	// 读取表，只读一次的好。
	protected void selMapMoudleDataFromSqlite() {
		// 读取mapmoudle表
		MapConstant.showeditmapmoudlelist = mapmoudledao.readMapMoudleData(db);
		mapmoudleadater = new MapMoudleAdapter(MapConstant.showeditmapmoudlelist,maincontext, this);
	}
	// 设置recycle值和布局
	protected void setRecyleDataAndLayout(){
		recycle.setLayoutManager(gridManager);
		recycle.setAdapter(mapmoudleadater);
	}
	// 刷新reclyerview
	protected void notifyReclyerView(){
		// 全部刷，简单快捷
		mapmoudleadater.notifyDataSetChanged();
	}
	
	// 删除一个moudle
	public void delOneMapMoudleFromListAndSqlite(int mid) {
		// 删除数据库
		int count = mapmoudledao.deleteMapMoudleBeanByid(db, mid);
		if(count > 0){
			// 删除list
			for(MapMoudleBean map : MapConstant.showeditmapmoudlelist){
				if(map.getMid() == mid){
					MapConstant.showeditmapmoudlelist.remove(map);
					break;
				}
			}
//			Toast.makeText(maincontext, "删除moudle成功", Toast.LENGTH_SHORT).show();
			// 更新
			notifyReclyerView();
		}
	}
	// 跳跃activity
	// 去修改地图的
	public void goEditFloorMap(int pos){
		Intent it = new Intent(maincontext, EditFloorMapActivity.class);
		// 需要传入flag
		it.putExtra("flag", 2);
		// 需要传入postion，在list中第几个mapmoudle，直接设置constant
		MapConstant.curmapmoudlepos = pos;
		startActivity(it);
	}
}
