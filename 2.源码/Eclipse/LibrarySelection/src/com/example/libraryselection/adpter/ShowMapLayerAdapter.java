package com.example.libraryselection.adpter;

import java.util.List;

import com.example.libraryselection.EditFloorMapActivity;
import com.example.libraryselection.MapLayerFragment;
import com.example.libraryselection.R;
import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.mappack.ShowMapLayerView;
import com.example.libraryselection.resource.Constant;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowMapLayerAdapter extends RecyclerView.Adapter<ShowMapLayerAdapter.ViewHolder>{
	
	Context maincon;
	
	MapLayerFragment maplayerfrag;
	
	List<MapBean> maplist;
	
	public ShowMapLayerAdapter(List<MapBean> maplist, Context main, MapLayerFragment frag) {
		this.maplist = maplist;
		this.maplayerfrag = frag;
		this.maincon = main;
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		ShowMapLayerView showmapview;
		TextView layertxview;
		Button btedit;
		Button deledit;
		LinearLayout adminlin;// 管理员的linear默认显式
		LinearLayout userlin;// 用户的linear
		// 选座bt
		Button btchooseseat; 
		// 横线
		View henview;
		
		public ViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			
			showmapview = (ShowMapLayerView)view.findViewById(R.id.item_recycle_showmaplayerview);
			layertxview = (TextView)view.findViewById(R.id.item_recycle_tvlayer);
			
			btedit = (Button)view.findViewById(R.id.item_recycle_bt_edit);
			deledit = (Button)view.findViewById(R.id.item_recycle_bt_del);
			
			adminlin = (LinearLayout)view.findViewById(R.id.item_recycle_maplayer_adminlin);
			userlin = (LinearLayout)view.findViewById(R.id.item_recycle_maplayer_userlin);
			btchooseseat = (Button)view.findViewById(R.id.item_recycle_maplayer_bt_chooseseat);
			
			henview = (View) view.findViewById(R.id.item_recycle_maplayer_henview);
			
			hidView();
			
		}
		// 根据当前用户是管理员和普通用户隐藏控件
		public void hidView(){
			
			if(MyApplication.user.role == 0){
				// 普通用户
				adminlin.setVisibility(View.GONE);
				userlin.setVisibility(View.VISIBLE);
			}else if(MyApplication.user.role == 1){
				// 管理员
				adminlin.setVisibility(View.VISIBLE);
				userlin.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return maplist.size();
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_maplayer, parent, false);
		final ViewHolder holder = new ViewHolder(view);
		holder.btedit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(maincon, "点击了修改按钮", Toast.LENGTH_SHORT).show();
				// 1.进入编辑地图,需要传入pos
				int pos = holder.getPosition();
				maplayerfrag.goEditFloorMap(pos);
			}
		});
		holder.deledit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(maincon, "点击了删除按钮", Toast.LENGTH_SHORT).show();
				// 需要删除,1.删除数据库的数据，根据id。2.删除list中存在这个id的项
				int pos = holder.getPosition();
				// 直接删除
//				maplayerfrag.delOneMapFromSqliteAndList(maplist.get(pos).getFid());
				// 弹框询问
				maplayerfrag.createdelOneMapDialog(maplist.get(pos).getFid());
			}
		});
		// 选座
		holder.btchooseseat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 进入新的选座的activity
				int pos = holder.getPosition();
//				Toast.makeText(maincon, "进行选座地图："+pos, Toast.LENGTH_SHORT).show();
				maplayerfrag.goChoosFloorMap(pos);
			}
		});
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		MapBean mapbean = maplist.get(position);
		// 设置地图数组
		holder.showmapview.setMapData(mapbean.mapdata);
		holder.showmapview.invalidate();// 重新绘画
		// 设置层号
		holder.layertxview.setText(Constant.maplayerString[mapbean.getFlayer()]);
		if(position == maplist.size() - 1){
			holder.henview.setVisibility(View.GONE);
		}else{
			holder.henview.setVisibility(View.VISIBLE);
		}
	}

}
