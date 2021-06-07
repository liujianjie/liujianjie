package com.example.libraryselection.adpter;

import java.util.List;

import com.example.libraryselection.MainActivity;
import com.example.libraryselection.MapLayerFragment;
import com.example.libraryselection.MapMoudleFragment;
import com.example.libraryselection.R;
import com.example.libraryselection.adpter.ShowMapLayerAdapter.ViewHolder;
import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;
import com.example.libraryselection.mappack.ShowMapLayerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// mapmoudle模板
public class MapMoudleAdapter extends RecyclerView.Adapter<MapMoudleAdapter.ViewHolder>{
	MapMoudleFragment mapmoudlefrag;
	List<MapMoudleBean> mapmoudlelist;
	
	Context maincon;
	
	public MapMoudleAdapter(List<MapMoudleBean> mapmoudlelist, MainActivity main, MapMoudleFragment frag) {
		this.mapmoudlelist = mapmoudlelist;
		this.mapmoudlefrag = frag;
		this.maincon = main;
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		ShowMapLayerView showmapview;
		TextView mnametxview;
		Button btedit;
		Button deledit;
		
		public ViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			
			showmapview = (ShowMapLayerView)view.findViewById(R.id.item_recycle_mapmoudleview);
			mnametxview = (TextView)view.findViewById(R.id.item_recycle_mapmoudle_tvlayer);
			
			btedit = (Button)view.findViewById(R.id.item_recycle_mapmoudle_bt_edit);
			deledit = (Button)view.findViewById(R.id.item_recycle_mapmoudle_bt_del);
			
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mapmoudlelist.size();
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_mapmoudle, parent, false);
		final ViewHolder holder = new ViewHolder(view);
		// 设置监听
		// 当修改的时候进入模板地图（也是修改地图的activity，需要flag标识）
		holder.btedit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(maincon, "点击了修改按钮", Toast.LENGTH_SHORT).show();
				// 1.进入编辑地图,需要传入pos
				int pos = holder.getPosition();
				mapmoudlefrag.goEditFloorMap(pos);
			}
		});
		holder.deledit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(maincon, "点击了删除按钮", Toast.LENGTH_SHORT).show();
				// 需要删除,1.删除数据库的数据，根据id。2.删除list中存在这个id的项
				int pos = holder.getPosition();
//				Toast.makeText(maincon, "点击了position："+pos, Toast.LENGTH_SHORT).show();
				mapmoudlefrag.delOneMapMoudleFromListAndSqlite(mapmoudlelist.get(pos).getMid());
			}
		});
		return holder;
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		MapMoudleBean mapmoudle = mapmoudlelist.get(position);
		holder.mnametxview.setText(mapmoudle.getMname());
		// 设置地图数组
		holder.showmapview.setMapData(mapmoudle.mapdata);
		// 刷新
		holder.showmapview.invalidate();// 重新绘画
	}

	

	
}
