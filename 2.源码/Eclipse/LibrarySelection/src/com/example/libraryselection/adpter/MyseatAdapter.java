package com.example.libraryselection.adpter;

import java.util.List;

import com.example.libraryselection.MyseatActivity;
import com.example.libraryselection.R;
import com.example.libraryselection.adpter.ShowMapLayerAdapter.ViewHolder;
import com.example.libraryselection.bean.SeatInfoBean;
import com.example.libraryselection.mappack.ShowMapLayerView;
import com.example.libraryselection.resource.Constant;
import com.example.libraryselection.resource.MapConstant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// 我的座位的adpter
public class MyseatAdapter extends RecyclerView.Adapter<MyseatAdapter.ViewHolder>{
	MyseatActivity myseatactivity;
	List<SeatInfoBean> seatlist;
	List<String> mydaystrlist;
	
	public MyseatAdapter(List<SeatInfoBean> my,List<String> mydaystr, MyseatActivity myac){
		seatlist = my;
		myseatactivity = myac;
		this.mydaystrlist = mydaystr;
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		TextView daytv;
		TextView swlayer,xwlayer,wslayer;// 上下晚的层
		TextView swnumber,xwnumber, wsnumber;// 上下晚的座位号
		
		// 横线
		View henview;
		public ViewHolder(View view) {
			super(view);

			daytv = (TextView)view.findViewById(R.id.item_myseat_day);
			
			swlayer = (TextView)view.findViewById(R.id.item_myseat_sw_layernumber);
			swnumber = (TextView)view.findViewById(R.id.item_myseat_sw_seatnumber);
			
			xwlayer = (TextView)view.findViewById(R.id.item_myseat_xw_layernumber);
			xwnumber = (TextView)view.findViewById(R.id.item_myseat_xw_seatnumber);
			
			wslayer = (TextView)view.findViewById(R.id.item_myseat_ws_layernumber);
			wsnumber = (TextView)view.findViewById(R.id.item_myseat_ws_seatnumber);
			
			henview = (View)view.findViewById(R.id.item_recycle_mapseat_henview);
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		// 日期为大小
		return mydaystrlist.size();
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_myseat, parent, false);
		final ViewHolder holder = new ViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		// 匹配，判断了，首先日期。再上午 下午 晚上匹配
		// 得到日期
		String daystr = mydaystrlist.get(position);
		holder.daytv.setText(daystr);
		for(SeatInfoBean seat : seatlist){
			// 首先日期相等
			if(seat.getSday().equals(daystr)){
				// 获取时间段，分别赋值
				int stime = seat.getStime();
				String layer = Constant.maplayerString[MapConstant.getLayerNumberByFid(seat.getFid())];
				String seatnumber = seat.getSnumber()+"";
				switch (stime) {
					case 0:
						holder.swlayer.setText(layer);
						holder.swnumber.setText(seatnumber);
					break;
					case 1:
						holder.xwlayer.setText(layer);
						holder.xwnumber.setText(seatnumber);
					break;
					case 2:
						holder.wslayer.setText(layer);
						holder.wsnumber.setText(seatnumber);
					break;
				}
			}
		}
		if(position == mydaystrlist.size() - 1){
			holder.henview.setVisibility(View.GONE);
		}else{
			holder.henview.setVisibility(View.VISIBLE);
		}
	}

}
