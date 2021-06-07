package com.example.libraryselection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.libraryselection.adpter.MyseatAdapter;
import com.example.libraryselection.adpter.ShowMapLayerAdapter;
import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.SeatInfoBean;
import com.example.libraryselection.dao.SeatInfoDao;
import com.example.libraryselection.dao.UserDao;
import com.example.libraryselection.dialog.GenerateDialog;
import com.example.libraryselection.resource.MapConstant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// �ҵ���ʷ��λ
public class MyseatActivity extends AppCompatActivity implements OnClickListener {
	
	// ���ݿ����
	UserDao userdao;
	private SQLiteDatabase db;
	SeatInfoDao seatdao;

	// �ؼ�
	private TextView titletv;
	private ImageView quitimg;
	//recycle
	private RecyclerView recycle;	
	private MyseatAdapter myseatadapter;
	private LinearLayoutManager layoutManager;
	
	List<SeatInfoBean> myseatlist;
	List<String> myseatdaystrlist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myseat);
		findView();
		initArgument();
		// ��ѯ����
		selMySeatFromSqlite();
		// recycle
		setRecyleDataAndLayout();
	}

	public void initArgument() {
		// �ǵó�ʼ��
		myseatlist = new ArrayList<SeatInfoBean>();
		
		userdao = new UserDao();
		seatdao = new SeatInfoDao();
		db = MyApplication.lihelper.getReadableDatabase();
	}

	private void findView() {
		titletv = (TextView)findViewById(R.id.myseat_tv_title);
		recycle = (RecyclerView) findViewById(R.id.myseat_recycleview);
		
		quitimg = (ImageView)findViewById(R.id.myseat_img_quit);
		quitimg.setOnClickListener(this);
		
		titletv.setOnClickListener(this);
	}
	// ����recycleֵ�Ͳ���
	protected void setRecyleDataAndLayout(){
		layoutManager = new LinearLayoutManager(this);
		
		myseatadapter = new MyseatAdapter(myseatlist, myseatdaystrlist, this);

		recycle.setLayoutManager(layoutManager);
		recycle.setAdapter(myseatadapter);
	}
	// ��ѯ���ݿ��ֵ���й��ҵ�������λ
	protected void selMySeatFromSqlite() {
		myseatlist = seatdao.selMySeatByUid(db, MyApplication.user.getUid(), myseatlist);
		// �����ҵ���λ�ı����һ������list
		myseatdaystrlist = new ArrayList<String>();
		for(SeatInfoBean seat : myseatlist){
			String days = seat.getSday();
			if(!myseatdaystrlist.contains(days)){
				myseatdaystrlist.add(days);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
			case R.id.myseat_img_quit:
				finish();
				break;

		}
	}
}
