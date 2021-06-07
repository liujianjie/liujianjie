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

// �ҵ���λ��adpter
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
		TextView swlayer,xwlayer,wslayer;// ������Ĳ�
		TextView swnumber,xwnumber, wsnumber;// ���������λ��
		
		// ����
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
		// ����Ϊ��С
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
		// ƥ�䣬�ж��ˣ��������ڡ������� ���� ����ƥ��
		// �õ�����
		String daystr = mydaystrlist.get(position);
		holder.daytv.setText(daystr);
		for(SeatInfoBean seat : seatlist){
			// �����������
			if(seat.getSday().equals(daystr)){
				// ��ȡʱ��Σ��ֱ�ֵ
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
