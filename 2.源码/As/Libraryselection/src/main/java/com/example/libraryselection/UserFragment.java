package com.example.libraryselection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.UserBean;
import com.example.libraryselection.resource.BitmapUtil;
import com.example.libraryselection.resource.PermissionClass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class UserFragment extends Fragment implements OnClickListener {
	// 上级
	public MainActivity maincontext;

	// 控件
	private TextView backtv;
	private ImageView headbgimg;
	private RoundView headimg;
	private TextView headtv;
	private TextView usernametv, usernametvbelowimg;
	private TextView userpwdtv;
	private TextView phonetv;
	private TextView roletv;
	private TextView myseattv;
	private TextView managestutv;
	private Button quitloginbt;
	// 横线
	private View henview1;

	// 头像图片的BItmap
	Bitmap headbitmap;
	Bitmap headbgbitmap;
	Bitmap blurBitmap;

	public UserFragment(MainActivity con) {
		this.maincontext = con;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getImageBit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user, null);
		findView(view);
		setText();
		setImageVal();
		setViewVisible();
		return view;
	}
	// 重启要更新

	protected void findView(View view) {
//		backtv = (TextView) view.findViewById(R.id.frag_user_tv_back);
		headbgimg = (ImageView) view.findViewById(R.id.frag_user_img_headbgimg);
		headimg = (RoundView) view.findViewById(R.id.frag_user_img_touxiang);
		headtv = (TextView) view.findViewById(R.id.frag_user_tv_head);
		usernametv = (TextView) view.findViewById(R.id.frag_user_tv_frag_username);
		userpwdtv = (TextView) view.findViewById(R.id.frag_user_tv_frag_userpwd);
		phonetv = (TextView) view.findViewById(R.id.frag_user_tv_phone);
		roletv = (TextView) view.findViewById(R.id.frag_user_tv_role);
		myseattv = (TextView) view.findViewById(R.id.frag_user_tv_myseat);
		managestutv = (TextView) view.findViewById(R.id.frag_user_tv_managestudent);
		quitloginbt = (Button) view.findViewById(R.id.frag_user_bt_quitlogin);

		usernametvbelowimg = (TextView) view.findViewById(R.id.frag_user_tv_name_belowimg);
		
//		henview1 = (View) view.findViewById(R.id.frag_user_view1);

//		backtv.setOnClickListener(this);
		headimg.setOnClickListener(this);
		headtv.setOnClickListener(this);
		usernametv.setOnClickListener(this);
		userpwdtv.setOnClickListener(this);
		phonetv.setOnClickListener(this);
		myseattv.setOnClickListener(this);
		managestutv.setOnClickListener(this);
		quitloginbt.setOnClickListener(this);
	}

	// 根据角色设置信息
	protected void setText() {
		UserBean user = MyApplication.user;
		// 设置信息,如果为空就不要设置了
		usernametv.setText(user.getUsername());
		usernametvbelowimg.setText(user.getUsername());
		userpwdtv.setText(user.getUserpwd());
		if(user.getPhone() != null && !user.getPhone().equals("")){
			phonetv.setText(user.getPhone());
		}
		String role = user.getRole() == 0 ? "学生":"管理员";
		roletv.setText(role);
	}
	// 得到imagebitmap,免得每次打开都需要,耗时
	public void getImageBit(){
		UserBean user = MyApplication.user;
		// 设置头像比较麻烦
		// 1.背景模糊。2.读取本地url
		// 如果url为空就设置默认图片资源
		
		// 如果url不为空，读取url如果为空也是默认图片资源
		
		// 如果url不为空，且读取url路径不为空设为bit
		if(user.getHeadimg() != null && !user.getHeadimg().equals("")){
			// 读取url
			headbitmap = BitmapUtil.getLocalBitmap(user.getHeadimg());
			headbgbitmap = BitmapUtil.getLocalBitmap(user.getHeadimg());
			if(headbitmap != null){
				
			}else{
				// 默认资源图片不用变。但是为了背景，需要加载默认头像的资源成bitmap
				headbitmap = BitmapUtil.readBitMaps(maincontext, R.drawable.headimg1);
				headbgbitmap = BitmapUtil.readBitMaps(maincontext, R.drawable.headimg1);
			}
		}else{
			// 默认资源图片不用变。但是为了背景，需要加载默认头像的资源成bitmap
			headbitmap = BitmapUtil.readBitMaps(maincontext, R.drawable.headimg1);
			headbgbitmap = BitmapUtil.readBitMaps(maincontext, R.drawable.headimg1);
		}
		blurBitmap = BitmapUtil.doBlur(headbgbitmap, 20, false);
	}
	// 设置图片值
	public void setImageVal(){
		if(headbitmap != null){
			headimg.mybitmap = new BitmapDrawable(getResources(),headbitmap);
		}
		if(blurBitmap != null){
			headbgimg.setImageBitmap(blurBitmap);
		}
		
	}

	// 根据不同角色设置显示控件
	protected void setViewVisible() {
		int role = MyApplication.user.getRole();
		// 学生
		if (role == 0) {
			myseattv.setVisibility(View.VISIBLE);
			((View) managestutv.getParent()).setVisibility(View.GONE);
//			henview1.setVisibility(View.GONE);
		} else if (role == 1) {
			((View) myseattv.getParent()).setVisibility(View.GONE);
			managestutv.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.frag_user_img_touxiang:
			goHeadImgAcitvity();
			break;
		case R.id.frag_user_tv_head:
			goHeadImgAcitvity();
			break;
		case R.id.frag_user_tv_frag_username:
			goEditAcitvity("username", 1);
			break;
		case R.id.frag_user_tv_frag_userpwd:
			goEditAcitvity("userpwd", 2);
			break;
		case R.id.frag_user_tv_phone:
			goEditAcitvity("phone", 3);
			break;
		case R.id.frag_user_tv_myseat:
			// 我的座位
			Intent ints = new Intent(maincontext, MyseatActivity.class);
			startActivity(ints);
			break;
		case R.id.frag_user_tv_managestudent:
			// 管理学生
			Intent intent = new Intent(maincontext, StudentActivity.class);
			startActivity(intent);
			break;
		case R.id.frag_user_bt_quitlogin:
			maincontext.quitCurUserLogin();
			break;
		}
	}

	// 去修改界面
	public void goEditAcitvity(String flagval, int requestcode) {
		Intent intent = new Intent(maincontext, EditUserActivity.class);
		intent.putExtra("flag", flagval);
		startActivityForResult(intent, requestcode);
	}

	// 去修改头像界面
	public void goHeadImgAcitvity() {
		Intent intent = new Intent(maincontext, HeadImgDetailActivity.class);
		startActivityForResult(intent, 11);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String str = "";
		try {
			str = data.getStringExtra("backval");
		} catch (Exception e) {

		}
		if (str.equals("") || str != null) {
			if (requestCode == 1 && resultCode == 1) {
				usernametv.setText(MyApplication.user.getUsername());
				usernametvbelowimg.setText(MyApplication.user.getUsername());
			} else if (requestCode == 2 && resultCode == 2) {
				userpwdtv.setText(MyApplication.user.getUserpwd());
			} else if (requestCode == 3 && resultCode == 3) {
				phonetv.setText(MyApplication.user.getPhone());
			}
		}
		if(requestCode == 11){
			getImageBit();
			setImageVal();
		}
	}


	
}
