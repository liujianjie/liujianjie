package com.example.libraryselection;

import org.json.JSONObject;

import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.UserBean;
import com.example.libraryselection.dao.UserDao;
import com.example.libraryselection.dialog.GenerateDialog;
import com.example.libraryselection.resource.BitmapUtil;
import com.example.libraryselection.resource.MapConstant;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements OnClickListener, TextWatcher{
	// 数据库相关
	private UserDao userdao;
	private SQLiteDatabase db;
	
	// 控件
	private Button loginbt;
	private TextView registertv;
	private EditText loginetname;
	private EditText loginetpwd;
	private RoundView loginimghead;//头像
	private ImageView quitimg;
	private TextView login_tv_forgetpwd;

	// 变量
	private String name;
	private String pwd;
	
	// 默认头像
	Drawable deafultdr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
		setContentView(R.layout.activity_login);
		initDaoAndDb();
		findView();
		setOnclicks();
		
		// 加载默认头像drawable
		deafultdr = this.getResources().getDrawable(R.drawable.headimg1);
		
		// 加载资源
		loadBitmapForMap();
		
		// 当第一次进入的时候
		// 检测全局是否有history，有的话直接登录，没有就停留。
		// 需要一个boolean变量来决定是第一次进入还是主动退出登录，或者直接判断全局user是否为空，退出让全局user为空，。
		if(MyApplication.user != null){
			goMain();
		}
	}
	// 判断是否退出
	
	
	// 寻找控件
	public void findView(){
		quitimg = (ImageView)findViewById(R.id.login_img_quit);
		loginbt=(Button)this.findViewById(R.id.login_bt_log);
		registertv=(TextView)this.findViewById(R.id.login_tv_register);
		loginetname=(EditText)this.findViewById(R.id.login_edit_name);
		loginetpwd=(EditText)this.findViewById(R.id.login_edit_pwd);
		loginimghead = (RoundView) this.findViewById(R.id.login_ig_head);
		login_tv_forgetpwd = (TextView)this.findViewById(R.id.login_tv_forgetpwd);
	}
	public void setOnclicks(){
		loginbt.setOnClickListener(this);
		registertv.setOnClickListener(this);
		quitimg.setOnClickListener(this);
		
		loginetname.addTextChangedListener(this);
		
		login_tv_forgetpwd.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		int viewid=v.getId();
		if(viewid == R.id.login_bt_log){
			//获取到值 去和数据库匹配
			name = loginetname.getText().toString();
			pwd = loginetpwd.getText().toString();
			if(name.trim().equals("")){
				Toast.makeText(this, "请输入账号", 0).show();
				return;
			}
			if(pwd.trim().equals("")){
				Toast.makeText(this, "请输入密码", 0).show();
				return;
			}
			checkIsThisUser();
			
		}else if(viewid == R.id.login_tv_register){
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivityForResult(intent, 1);
		}else if(viewid == R.id.login_img_quit){
			GenerateDialog.genEditMapQuitNormalDialog(this, "确定退出吗？");
		}else if(viewid == R.id.login_tv_forgetpwd){
			GenerateDialog.genNormalDialog(this, "开发时间不够，我也帮不了你找回来了。");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 && resultCode==1){
			name = data.getStringExtra("username");
			pwd = data.getStringExtra("userpwd");
			if(name != null && !name.equals("")){
				loginetname.setText(name);
			}
			if(pwd != null && !pwd.equals("")){
				loginetpwd.setText(pwd);
			}
		}
	}
	
	// 数据库相关初始化
	protected void initDaoAndDb() {
		userdao = new UserDao();
		db = MyApplication.lihelper.getReadableDatabase();
	}
	// 是否存在此用户
	protected void checkIsThisUser(){
		name = loginetname.getText().toString();
		pwd = loginetpwd.getText().toString();
		// 根据name与pwd去查找数据库
		UserBean user = userdao.checkLoginUser(db, name, pwd);
		if(user != null){// 不为空
			// 设为全局，去主页
			MyApplication.setUser(user);
			goMain();
		}else{
			Toast.makeText(this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
		}
	}
	
	// 去主页
	protected void goMain() {
		Intent in = new Intent(this, MainActivity.class);
		startActivity(in);
		finish();
	}
	
	// 加载bitmap给地图资源
	public void loadBitmapForMap(){
		MapConstant.bigwallbitmap = BitmapUtil.readBitMaps(this, R.drawable.bigwall);
		MapConstant.smallwallbitmap = BitmapUtil.readBitMaps(this, R.drawable.smallwall);
		
		MapConstant.bigfloorbitmap = BitmapUtil.readBitMaps(this, R.drawable.bigfloor);
		MapConstant.smallfloorbitmap = BitmapUtil.readBitMaps(this, R.drawable.smallfloor);
		
		MapConstant.bigseatbitmap = BitmapUtil.readBitMaps(this, R.drawable.bigseat);
		MapConstant.smallseatbitmap = BitmapUtil.readBitMaps(this, R.drawable.smallseat);
		
		MapConstant.bigseatnobitmap = BitmapUtil.readBitMaps(this, R.drawable.bigseatnotuse);
		MapConstant.smallseatnobitmap = BitmapUtil.readBitMaps(this, R.drawable.smallseatnotuse);
		
		MapConstant.bigshujiabitmap = BitmapUtil.readBitMaps(this, R.drawable.bigshujia);
		MapConstant.smallshujiabitmap = BitmapUtil.readBitMaps(this, R.drawable.smallshujia);
	}


	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	String lastuname = "";
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		String str = loginetname.getText().toString();
		// 根据用户名获取头像
		if(!str.equals("") && !lastuname.equals(str)){
			setLoginImg(str);
			lastuname = str;
		}
	}
	// 
	public void setLoginImg(String username){
		String headuri = userdao.selUserHeadImgUriByname(db, username);
		if(headuri != null && !headuri.equals("")){
			// 加载
			Bitmap bit = BitmapUtil.getLocalBitmap(headuri);
			// 如果不为空
			if(bit != null){
				// 将
				loginimghead.mybitmap = new BitmapDrawable(getResources(),bit);
				loginimghead.invalidate();
			}
		}else{
			// 设回默认
			loginimghead.mybitmap = deafultdr;
			loginimghead.invalidate();
		}
	}
}
