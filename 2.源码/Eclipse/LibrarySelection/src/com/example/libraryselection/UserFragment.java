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
	// �ϼ�
	public MainActivity maincontext;

	// �ؼ�
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
	// ����
	private View henview1;

	// ͷ��ͼƬ��BItmap
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
	// ����Ҫ����

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

	// ���ݽ�ɫ������Ϣ
	protected void setText() {
		UserBean user = MyApplication.user;
		// ������Ϣ,���Ϊ�վͲ�Ҫ������
		usernametv.setText(user.getUsername());
		usernametvbelowimg.setText(user.getUsername());
		userpwdtv.setText(user.getUserpwd());
		if(user.getPhone() != null && !user.getPhone().equals("")){
			phonetv.setText(user.getPhone());
		}
		String role = user.getRole() == 0 ? "ѧ��":"����Ա";
		roletv.setText(role);
	}
	// �õ�imagebitmap,���ÿ�δ򿪶���Ҫ,��ʱ
	public void getImageBit(){
		UserBean user = MyApplication.user;
		// ����ͷ��Ƚ��鷳
		// 1.����ģ����2.��ȡ����url
		// ���urlΪ�վ�����Ĭ��ͼƬ��Դ
		
		// ���url��Ϊ�գ���ȡurl���Ϊ��Ҳ��Ĭ��ͼƬ��Դ
		
		// ���url��Ϊ�գ��Ҷ�ȡurl·����Ϊ����Ϊbit
		if(user.getHeadimg() != null && !user.getHeadimg().equals("")){
			// ��ȡurl
			headbitmap = BitmapUtil.getLocalBitmap(user.getHeadimg());
			headbgbitmap = BitmapUtil.getLocalBitmap(user.getHeadimg());
			if(headbitmap != null){
				
			}else{
				// Ĭ����ԴͼƬ���ñ䡣����Ϊ�˱�������Ҫ����Ĭ��ͷ�����Դ��bitmap
				headbitmap = BitmapUtil.readBitMaps(maincontext, R.drawable.headimg1);
				headbgbitmap = BitmapUtil.readBitMaps(maincontext, R.drawable.headimg1);
			}
		}else{
			// Ĭ����ԴͼƬ���ñ䡣����Ϊ�˱�������Ҫ����Ĭ��ͷ�����Դ��bitmap
			headbitmap = BitmapUtil.readBitMaps(maincontext, R.drawable.headimg1);
			headbgbitmap = BitmapUtil.readBitMaps(maincontext, R.drawable.headimg1);
		}
		blurBitmap = BitmapUtil.doBlur(headbgbitmap, 20, false);
	}
	// ����ͼƬֵ
	public void setImageVal(){
		if(headbitmap != null){
			headimg.mybitmap = new BitmapDrawable(getResources(),headbitmap);
		}
		if(blurBitmap != null){
			headbgimg.setImageBitmap(blurBitmap);
		}
		
	}

	// ���ݲ�ͬ��ɫ������ʾ�ؼ�
	protected void setViewVisible() {
		int role = MyApplication.user.getRole();
		// ѧ��
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
			// �ҵ���λ
			Intent ints = new Intent(maincontext, MyseatActivity.class);
			startActivity(ints);
			break;
		case R.id.frag_user_tv_managestudent:
			// ����ѧ��
			Intent intent = new Intent(maincontext, StudentActivity.class);
			startActivity(intent);
			break;
		case R.id.frag_user_bt_quitlogin:
			maincontext.quitCurUserLogin();
			break;
		}
	}

	// ȥ�޸Ľ���
	public void goEditAcitvity(String flagval, int requestcode) {
		Intent intent = new Intent(maincontext, EditUserActivity.class);
		intent.putExtra("flag", flagval);
		startActivityForResult(intent, requestcode);
	}

	// ȥ�޸�ͷ�����
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
