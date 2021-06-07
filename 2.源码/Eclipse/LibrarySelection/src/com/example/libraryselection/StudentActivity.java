package com.example.libraryselection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.libraryselection.adpter.MyseatAdapter;
import com.example.libraryselection.adpter.ShowMapLayerAdapter;
import com.example.libraryselection.adpter.StudentAdapter;
import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.SeatInfoBean;
import com.example.libraryselection.bean.UserBean;
import com.example.libraryselection.dao.SeatInfoDao;
import com.example.libraryselection.dao.UserDao;
import com.example.libraryselection.dialog.GenerateDialog;
import com.example.libraryselection.resource.Constant;
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

// ����ѧ��
public class StudentActivity extends AppCompatActivity implements OnClickListener {
	
	// ���ݿ����
	UserDao userdao;
	private SQLiteDatabase db;
	SeatInfoDao seatdao;

	// �ؼ�
	private TextView titletv;
	private ImageView quitimg;
	
	//recycle
	private RecyclerView recycle;	
	private StudentAdapter stuadapter;
	private LinearLayoutManager layoutManager;
	
	List<UserBean> stulist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_student);
		findView();
		initArgument();
		// ��ѯ����
		selStuFromSqlite();
		// recycle
		setRecyleDataAndLayout();
	}

	public void initArgument() {
		stulist = new ArrayList<UserBean>();
		userdao = new UserDao();
		seatdao = new SeatInfoDao();
		db = MyApplication.lihelper.getReadableDatabase();
	}

	private void findView() {
		titletv = (TextView)findViewById(R.id.student_tv_title);
		recycle = (RecyclerView) findViewById(R.id.student_recyclerview);
		quitimg = (ImageView)findViewById(R.id.student_img_quit);
		quitimg.setOnClickListener(this);
		titletv.setOnClickListener(this);
	}
	// ��ѯ���ݿ��ֵ���й��ҵ�������λ
	protected void selStuFromSqlite() {
		stulist = userdao.selAllUserByRole(db, 0, stulist);
	}
	
	// ����recycleֵ�Ͳ���
	protected void setRecyleDataAndLayout(){
		layoutManager = new LinearLayoutManager(this);
		
		stuadapter = new StudentAdapter(stulist, this);

		recycle.setLayoutManager(layoutManager);
		recycle.setAdapter(stuadapter);
	}
	// ˢ��reclyerview
	protected void notifyReclyerView(){
		// ȫ��ˢ���򵥿��
		stuadapter.notifyDataSetChanged();
	}
	// ���ɵ����Ƿ�ɾ��
	public void geneDialogIsDel(int pos){
		GenerateDialog.genDelStuNormalDialog(this, "�Ƿ�ɾ����ѧ��?", pos);
	}
	// ����uidɾ��
	public void delStuByUid(int pos){
		//
		int uid = stulist.get(pos).getUid();
		int ct = userdao.deleteUserByid(db, uid);
		if(ct > 0){
			// listҲҪ����
			stulist.remove(pos);
			// ɾ�����˵���λ
			seatdao.deleteSeatByUid(db, uid);
			//  ����
			notifyReclyerView();
			Constant.commonToast(this, "ɾ����ѧ���ɹ�");
		}else{
			Constant.commonToast(this, "ɾ����ѧ��ʧ��");
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
			case R.id.student_img_quit:
				finish();
				break;

		}
	}
}
