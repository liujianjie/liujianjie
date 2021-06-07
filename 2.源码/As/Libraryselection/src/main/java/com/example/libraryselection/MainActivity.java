package com.example.libraryselection;

import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.dao.MapDao;
import com.example.libraryselection.dao.MyHistoryUserDao;
import com.example.libraryselection.dialog.GenerateDialog;
import com.example.libraryselection.mappack.EditMapDataUtil;
import com.example.libraryselection.resource.Constant;
import com.example.libraryselection.resource.MapConstant;
import com.example.libraryselection.resource.PermissionClass;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener{
	// 数据库相关
	MyHistoryUserDao hisdao;
	private SQLiteDatabase db;
	
	// 
	private Button maplayerbtn;
	private Button moudlebtn;
	private Button userbtn;
	
	// fragment
	FragmentManager manager;
	MapLayerFragment mapfrag;
	MapMoudleFragment mapmoudlefrag;
	UserFragment userfrag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
		
		setContentView(R.layout.activity_main);
		findView();
		initFragment();
		setDefaultFragment();
		
		Constant.ScreenWidth = this.getWindowManager().getDefaultDisplay().getWidth(); 
		Constant.ScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		
		// 初始化空间，主需要一次，跟随主程序activity启动一次就行
		EditMapDataUtil.setMapDataSpace();
		
//		Toast.makeText(this, "ScreenWidth："+Constant.ScreenWidth+",ScreenHeight:"+Constant.ScreenHeight, Toast.LENGTH_LONG).show();
		
		// 初始化
		initArgument();
		
		// 权限
		PermissionClass.verifyStoragePermissions(this);
		
		// 检验是否要登录
		checkCurUserGoLog();
		
		
	}
	// 是否重启
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
//		Log.i("library", "MainActivity被重启");
	}
	// 初始化变量
	protected void initArgument(){
		hisdao = new MyHistoryUserDao();
		
		// 以读方式打开
		db = MyApplication.lihelper.getReadableDatabase();
	}
	
	public void findView(){
		maplayerbtn = (Button)findViewById(R.id.main_bt1_layer);
		moudlebtn = (Button)findViewById(R.id.main_bt2_moudle);
		userbtn = (Button)findViewById(R.id.main_bt3_user);
		
		maplayerbtn.setOnClickListener(this);
		moudlebtn.setOnClickListener(this);
		userbtn.setOnClickListener(this);
	}
	public void initFragment(){
		mapfrag = new MapLayerFragment(this);
		mapmoudlefrag = new MapMoudleFragment(this);
		userfrag = new UserFragment(this);
	}
	//一开始设置为mapfragment
	public void setDefaultFragment(){
		manager = this.getSupportFragmentManager();
		FragmentTransaction tran = manager.beginTransaction();
		tran.add(R.id.main_linear_content, mapfrag);
		tran.commit();
	}
	// 点击切换fragment
	@Override
	public void onClick(View ve) {
		FragmentTransaction tran = manager.beginTransaction();
		int id = ve.getId();
		switch (id) {
			// mainlayerfrag
			case R.id.main_bt1_layer:
				maplayerbtn.setEnabled(false);
				moudlebtn.setEnabled(true);
				userbtn.setEnabled(true);
				tran.replace(R.id.main_linear_content, mapfrag);
				tran.commit();
				break;
			// moudlefrag
			case R.id.main_bt2_moudle:
				// 根据全局用户是否可以执行这个操作
				if(checkIsClickMoulde()){
					maplayerbtn.setEnabled(true);
					moudlebtn.setEnabled(false);
					userbtn.setEnabled(true);
					tran.replace(R.id.main_linear_content, mapmoudlefrag);
					tran.commit();
				}
				break;
			// userfrag
			case R.id.main_bt3_user:
				maplayerbtn.setEnabled(true);
				moudlebtn.setEnabled(true);
				userbtn.setEnabled(false);
				tran.replace(R.id.main_linear_content, userfrag);
				tran.commit();
				break;
		default:
			break;
		}
	}
	// 检验当前用户是否为空，默认去登录界面
	public void checkCurUserGoLog(){
		if(MyApplication.user == null){
			Intent in = new Intent(this, LoginActivity.class);
			startActivity(in);
			// 自己结束
			finish();
		}
	}
	public boolean checkIsClickMoulde(){
		// 不为空
		if(MyApplication.user != null){
			int role = MyApplication.user.getRole();
			// 普通用户
			if(role == 0){
				Toast.makeText(this, "对不起，你不是管理员，无法查看", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}
	
	
	// 在退出的时候，保存当前登录用户，为history
	@Override
	protected void onDestroy() {
//		Toast.makeText(this, "main.ondestory", Toast.LENGTH_SHORT).show();
		// 执行 保存当前用户,先删除所有，再添加一行
		hisdao.deleteAllhis(db);
		// 如果当前用户不为空
		if(MyApplication.user != null){
			// 添加一行，只需要一个当前用户id
			int uid = MyApplication.user.uid;
			hisdao.insertHistory(db, uid);
		}
		
		super.onDestroy();
	}
	
	// 退出登录
	public void quitCurUserLogin(){
		// 弹框
		GenerateDialog.geneIsQuitCurUserLogin(this, "确定退出当前用户吗？");
	}
	// 退出，删除历史纪录，当前用户为空
	public void deleteCurUserLogin(){
		hisdao.deleteAllhis(db);
		MyApplication.user = null;
		// 去登录
		checkCurUserGoLog();
	}
	
	long exitTime = 0;
	// 响应按返回键 不退出，应该退出左滑菜单
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// 没有显式 两次返回键退出
		if(System.currentTimeMillis() - exitTime > 2000){// 间隔大于两秒 提示 连按两次
			Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		}else{
			// TODO Auto-generated method stub
			super.onBackPressed();
		}
	}
	
}
