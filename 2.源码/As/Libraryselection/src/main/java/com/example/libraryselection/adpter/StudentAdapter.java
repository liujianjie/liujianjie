package com.example.libraryselection.adpter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.example.libraryselection.R;
import com.example.libraryselection.StudentActivity;
import com.example.libraryselection.bean.UserBean;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

// 我的座位的adpter
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder>{
	StudentActivity stuactivity;
	List<UserBean> stulist;
	
	public StudentAdapter(List<UserBean> my, StudentActivity myac){
		stulist = my;
		stuactivity = myac;
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView headimg;
		TextView usertv, pwdtv;
		TextView deltv;
		
		View henview;
		public ViewHolder(View view) {
			super(view);
			
			headimg = (ImageView)view.findViewById(R.id.item_student_headimg);
			usertv = (TextView)view.findViewById(R.id.item_student_username);
			pwdtv = (TextView)view.findViewById(R.id.item_student_userpwd);
			deltv = (TextView)view.findViewById(R.id.item_student_del);
			
			henview = (View) view.findViewById(R.id.item_recycle_student_henview);
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		// 日期为大小
		return stulist.size();
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_student, parent, false);
		final ViewHolder holder = new ViewHolder(view);
		// 添加删除监听
		holder.deltv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int pos = holder.getPosition();
				stuactivity.geneDialogIsDel(pos);
			}
		});
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if(position == stulist.size() - 1){
			holder.henview.setVisibility(View.GONE);
		}else{
			holder.henview.setVisibility(View.VISIBLE);
		}
		
		// TODO Auto-generated method stub
		// 设置值
		UserBean user = stulist.get(position);
		holder.usertv.setText(user.getUsername());
		holder.pwdtv.setText(user.getUserpwd());
		String headstr = user.getHeadimg();
		if(headstr != null && !headstr.equals("")){
			Bitmap bit = getLocalBitmap(user.getHeadimg());
			if(bit != null){// 不为空
				holder.headimg.setMaxHeight(50);
				holder.headimg.setImageBitmap(bit);
			}
		}
		
	}
	// 读取本地图片
	// 根据url读取本地图片返回bitmap
	public static Bitmap getLocalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 2;
			Bitmap btp = BitmapFactory.decodeStream(fis, null, options);
			return btp; /// 把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
