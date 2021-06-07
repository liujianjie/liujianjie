package com.example.libraryselection;

import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.dao.MyHistoryUserDao;
import com.example.libraryselection.dao.UserDao;
import com.example.libraryselection.resource.Constant;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

// �޸ĸ�����Ϣac
public class EditUserActivity extends AppCompatActivity implements OnClickListener{
	// 
	private TextView titletv;
	private EditText inputdt;
	private Button savebt;
	private ImageView quitimg;
	
	// ���ݿ����
	private SQLiteDatabase db;
	private UserDao userdao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//�ޱ���
		
		setContentView(R.layout.activity_edituser);
		findView();
		// ��ȡintent��ֵ
		getIntentVal();
		initArgument();
	}
	public void findView(){
		titletv = (TextView)findViewById(R.id.edituser_tv_title);
		inputdt = (EditText)findViewById(R.id.edituser_ed_val);
		savebt = (Button)findViewById(R.id.edituser_bt_save);
		
		quitimg = (ImageView)findViewById(R.id.edituser_img_quit);
		quitimg.setOnClickListener(this);
		
		savebt.setOnClickListener(this);
	}
	// ��ʼ������
	protected void initArgument(){
		userdao = new UserDao();
		
		// �Զ���ʽ��
		db = MyApplication.lihelper.getReadableDatabase();
	}
	String choose = "";
	public void getIntentVal(){
		Intent intent = this.getIntent();
		choose = intent.getStringExtra("flag");
		if(choose.equals("username")){
			titletv.setText("�޸��û���");
			inputdt.setText(MyApplication.user.getUsername());
		}else if(choose.equals("userpwd")){
			titletv.setText("�޸�����");
			inputdt.setText(MyApplication.user.getUserpwd());
		}else if(choose.equals("phone")){
			titletv.setText("�޸��ֻ�");
			inputdt.setText(MyApplication.user.getPhone());
		}
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.edituser_bt_save:
				String text = inputdt.getText().toString();
				if(!text.trim().equals("")){
					Intent intent  = new Intent(this,MainActivity.class);
					intent.putExtra("backval", text);
					if(choose.equals("username")){
						// ��Ҫ�жϸ��û����Ƿ����
						if(!userdao.checkNameExist(db, text)){
							// ����ȫ�֣������޸����ݿ��
							MyApplication.user.setUsername(text);
							userdao.updateUser(db, MyApplication.user);
							setResult(1, intent);
							finish();
						}else{
							Constant.commonToast(this, "���û����Ѵ��ڣ�����������");
						}
					}else if(choose.equals("userpwd")){
						MyApplication.user.setUserpwd(text);
						userdao.updateUser(db, MyApplication.user);
						setResult(2, intent);
						finish();
					}else if(choose.equals("phone")){
						MyApplication.user.setPhone(text);
						userdao.updateUser(db, MyApplication.user);
						setResult(3, intent);
						finish();
					}
				}else{
					Constant.commonToast(this, "����Ϊ��");
				}
				break;
			case R.id.edituser_img_quit:
				finish();
				break;
		}
	}
}
