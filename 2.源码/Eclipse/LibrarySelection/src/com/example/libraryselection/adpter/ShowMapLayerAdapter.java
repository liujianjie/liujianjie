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
		LinearLayout adminlin;// ����Ա��linearĬ����ʽ
		LinearLayout userlin;// �û���linear
		// ѡ��bt
		Button btchooseseat; 
		// ����
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
		// ���ݵ�ǰ�û��ǹ���Ա����ͨ�û����ؿؼ�
		public void hidView(){
			
			if(MyApplication.user.role == 0){
				// ��ͨ�û�
				adminlin.setVisibility(View.GONE);
				userlin.setVisibility(View.VISIBLE);
			}else if(MyApplication.user.role == 1){
				// ����Ա
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
//				Toast.makeText(maincon, "������޸İ�ť", Toast.LENGTH_SHORT).show();
				// 1.����༭��ͼ,��Ҫ����pos
				int pos = holder.getPosition();
				maplayerfrag.goEditFloorMap(pos);
			}
		});
		holder.deledit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(maincon, "�����ɾ����ť", Toast.LENGTH_SHORT).show();
				// ��Ҫɾ��,1.ɾ�����ݿ�����ݣ�����id��2.ɾ��list�д������id����
				int pos = holder.getPosition();
				// ֱ��ɾ��
//				maplayerfrag.delOneMapFromSqliteAndList(maplist.get(pos).getFid());
				// ����ѯ��
				maplayerfrag.createdelOneMapDialog(maplist.get(pos).getFid());
			}
		});
		// ѡ��
		holder.btchooseseat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// �����µ�ѡ����activity
				int pos = holder.getPosition();
//				Toast.makeText(maincon, "����ѡ����ͼ��"+pos, Toast.LENGTH_SHORT).show();
				maplayerfrag.goChoosFloorMap(pos);
			}
		});
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		MapBean mapbean = maplist.get(position);
		// ���õ�ͼ����
		holder.showmapview.setMapData(mapbean.mapdata);
		holder.showmapview.invalidate();// ���»滭
		// ���ò��
		holder.layertxview.setText(Constant.maplayerString[mapbean.getFlayer()]);
		if(position == maplist.size() - 1){
			holder.henview.setVisibility(View.GONE);
		}else{
			holder.henview.setVisibility(View.VISIBLE);
		}
	}

}
