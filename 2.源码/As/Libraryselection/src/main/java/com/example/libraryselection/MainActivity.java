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
	// ���ݿ����
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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//�ޱ���
		
		setContentView(R.layout.activity_main);
		findView();
		initFragment();
		setDefaultFragment();
		
		Constant.ScreenWidth = this.getWindowManager().getDefaultDisplay().getWidth(); 
		Constant.ScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		
		// ��ʼ���ռ䣬����Ҫһ�Σ�����������activity����һ�ξ���
		EditMapDataUtil.setMapDataSpace();
		
//		Toast.makeText(this, "ScreenWidth��"+Constant.ScreenWidth+",ScreenHeight:"+Constant.ScreenHeight, Toast.LENGTH_LONG).show();
		
		// ��ʼ��
		initArgument();
		
		// Ȩ��
		PermissionClass.verifyStoragePermissions(this);
		
		// �����Ƿ�Ҫ��¼
		checkCurUserGoLog();
		
		
	}
	// �Ƿ�����
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
//		Log.i("library", "MainActivity������");
	}
	// ��ʼ������
	protected void initArgument(){
		hisdao = new MyHistoryUserDao();
		
		// �Զ���ʽ��
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
	//һ��ʼ����Ϊmapfragment
	public void setDefaultFragment(){
		manager = this.getSupportFragmentManager();
		FragmentTransaction tran = manager.beginTransaction();
		tran.add(R.id.main_linear_content, mapfrag);
		tran.commit();
	}
	// ����л�fragment
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
				// ����ȫ���û��Ƿ����ִ���������
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
	// ���鵱ǰ�û��Ƿ�Ϊ�գ�Ĭ��ȥ��¼����
	public void checkCurUserGoLog(){
		if(MyApplication.user == null){
			Intent in = new Intent(this, LoginActivity.class);
			startActivity(in);
			// �Լ�����
			finish();
		}
	}
	public boolean checkIsClickMoulde(){
		// ��Ϊ��
		if(MyApplication.user != null){
			int role = MyApplication.user.getRole();
			// ��ͨ�û�
			if(role == 0){
				Toast.makeText(this, "�Բ����㲻�ǹ���Ա���޷��鿴", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}
	
	
	// ���˳���ʱ�򣬱��浱ǰ��¼�û���Ϊhistory
	@Override
	protected void onDestroy() {
//		Toast.makeText(this, "main.ondestory", Toast.LENGTH_SHORT).show();
		// ִ�� ���浱ǰ�û�,��ɾ�����У������һ��
		hisdao.deleteAllhis(db);
		// �����ǰ�û���Ϊ��
		if(MyApplication.user != null){
			// ���һ�У�ֻ��Ҫһ����ǰ�û�id
			int uid = MyApplication.user.uid;
			hisdao.insertHistory(db, uid);
		}
		
		super.onDestroy();
	}
	
	// �˳���¼
	public void quitCurUserLogin(){
		// ����
		GenerateDialog.geneIsQuitCurUserLogin(this, "ȷ���˳���ǰ�û���");
	}
	// �˳���ɾ����ʷ��¼����ǰ�û�Ϊ��
	public void deleteCurUserLogin(){
		hisdao.deleteAllhis(db);
		MyApplication.user = null;
		// ȥ��¼
		checkCurUserGoLog();
	}
	
	long exitTime = 0;
	// ��Ӧ�����ؼ� ���˳���Ӧ���˳��󻬲˵�
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// û����ʽ ���η��ؼ��˳�
		if(System.currentTimeMillis() - exitTime > 2000){// ����������� ��ʾ ��������
			Toast.makeText(this,"�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		}else{
			// TODO Auto-generated method stub
			super.onBackPressed();
		}
	}
	
}
