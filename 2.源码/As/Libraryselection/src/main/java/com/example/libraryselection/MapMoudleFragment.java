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
	// �ϼ�
	private MainActivity maincontext;
	
	// ���ݿ����
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
		// ��ʼ��
		initArgume();
		// �����ݿ��ȡ����
		selMapMoudleDataFromSqlite();
	}
	// ��ʼ������
	protected void initArgume() {
		// db
		db = MyApplication.lihelper.getReadableDatabase();
		mapmoudledao = new MapMoudleDao();
		
		// recycle
		gridManager = new GridLayoutManager(maincontext, 2);// 2��
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mapmoudle, null);
		findView(view);
		// ���ݸ���
		setRecyleDataAndLayout();
		Log.i("library", "MapMoudleFragment��onCreateView");
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		notifyReclyerView();
	}
	// Ѱ�ҿؼ�
	protected void findView(View view){
		recycle = (RecyclerView) view.findViewById(R.id.frg_mapmoudle_recycleview);
	}
	// ��ȡ��ֻ��һ�εĺá�
	protected void selMapMoudleDataFromSqlite() {
		// ��ȡmapmoudle��
		MapConstant.showeditmapmoudlelist = mapmoudledao.readMapMoudleData(db);
		mapmoudleadater = new MapMoudleAdapter(MapConstant.showeditmapmoudlelist,maincontext, this);
	}
	// ����recycleֵ�Ͳ���
	protected void setRecyleDataAndLayout(){
		recycle.setLayoutManager(gridManager);
		recycle.setAdapter(mapmoudleadater);
	}
	// ˢ��reclyerview
	protected void notifyReclyerView(){
		// ȫ��ˢ���򵥿��
		mapmoudleadater.notifyDataSetChanged();
	}
	
	// ɾ��һ��moudle
	public void delOneMapMoudleFromListAndSqlite(int mid) {
		// ɾ�����ݿ�
		int count = mapmoudledao.deleteMapMoudleBeanByid(db, mid);
		if(count > 0){
			// ɾ��list
			for(MapMoudleBean map : MapConstant.showeditmapmoudlelist){
				if(map.getMid() == mid){
					MapConstant.showeditmapmoudlelist.remove(map);
					break;
				}
			}
//			Toast.makeText(maincontext, "ɾ��moudle�ɹ�", Toast.LENGTH_SHORT).show();
			// ����
			notifyReclyerView();
		}
	}
	// ��Ծactivity
	// ȥ�޸ĵ�ͼ��
	public void goEditFloorMap(int pos){
		Intent it = new Intent(maincontext, EditFloorMapActivity.class);
		// ��Ҫ����flag
		it.putExtra("flag", 2);
		// ��Ҫ����postion����list�еڼ���mapmoudle��ֱ������constant
		MapConstant.curmapmoudlepos = pos;
		startActivity(it);
	}
}
