package com.example.libraryselection;

import org.json.JSONObject;

import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.UserBean;
import com.example.libraryselection.dao.MapDao;
import com.example.libraryselection.dao.MapMoudleDao;
import com.example.libraryselection.dao.UserDao;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends AppCompatActivity implements OnClickListener{
	
	// ���ݿ����
	private UserDao userdao;
	private SQLiteDatabase db;
	private ImageView quitimg;
	// �ؼ�
	private Button regisbt;
	private TextView gologintv;
	private EditText regisname;
	private EditText regispwd;
	private EditText regischeckpwd;
	private String name;
	private String pwd,checkpwd;
	private RadioGroup rolegroup;
	
	// ����
	private int role;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//�ޱ���
		setContentView(R.layout.activity_register);
		initDaoAndDb();
		findView();
		setOnclicks();
	}
	// Ѱ�ҿؼ�
	public void findView(){
		regisbt = (Button)this.findViewById(R.id.regis_bt_reg);
		gologintv=(TextView)this.findViewById(R.id.regis_tv_gologin);
		regisname=(EditText)this.findViewById(R.id.regis_ed_name);
		regispwd=(EditText)this.findViewById(R.id.regis_ed_pwd);
		regischeckpwd=(EditText)this.findViewById(R.id.regis_ed_checkpwd);
		quitimg = (ImageView)findViewById(R.id.regis_img_quit);
		quitimg.setOnClickListener(this);
		rolegroup = (RadioGroup)this.findViewById(R.id.regis_radiogroup_role);
		
		//��ѡ
		rolegroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.regis_radio_user){//ѡ����ͨ�û�
					role = 0;
				}else if(checkedId == R.id.regis_radio_admin){//ѡ�����Ա
					role = 1;
				}
			}
		});
	}
	public void setOnclicks(){
		regisbt.setOnClickListener(this);
		gologintv.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		int viewid=v.getId();
		switch (viewid) {
			case R.id.regis_bt_reg:
				// ��ȡ��nameֵ ȥ�����ݿ�ƥ���Ƿ����
				name = regisname.getText().toString();
				pwd = regispwd.getText().toString();
				checkpwd = regischeckpwd.getText().toString();
				if(name.trim().equals("")){
					Toast.makeText(this, "�������˺�", 0).show();
					return;
				}
				if(pwd.trim().equals("")){
					Toast.makeText(this, "����������", 0).show();
					return;
				}
				if(checkpwd.trim().equals("")){
					Toast.makeText(this, "��ȷ������", 0).show();
					return;
				}
				checkRegister();
				break;
			case R.id.regis_tv_gologin:
				finish();// �����Լ�
				break;
			case R.id.regis_img_quit:
				finish();// �����Լ�
				break;
		default:
			break;
		}
	}
	
	// ���ݿ���س�ʼ��
	protected void initDaoAndDb() {
		userdao = new UserDao();
		db = MyApplication.lihelper.getReadableDatabase();
	}
	// ע�ᣬ�ж��Ƿ�������������ʾ��û��ע�ᣬ��ʾע��ɹ���ȥ��¼����
	protected void checkRegister() {
		// 
		boolean isok = userdao.checkNameExist(db, name);
		// ������
		if(!isok){
			UserBean user = new UserBean();
			user.setUsername(name);
			user.setUserpwd(pwd);
			user.setRole(role);
			int ct = userdao.insertUser(db, user);
			if(ct > 0){
				Toast.makeText(this, "ע��ɹ�", Toast.LENGTH_SHORT).show();
				// ȥ��¼����, �Ƿ��أ�Я������
				backLoginWithArguments();
			}else{
				Toast.makeText(this, "ע��ʧ��", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, "���û����Ѵ��ڣ�����������", Toast.LENGTH_SHORT).show();
		}
	}
	// ����login
	public void backLoginWithArguments(){
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra("username", name);
		intent.putExtra("userpwd", pwd);
		intent.putExtra("role", role);
		setResult(1,intent);// ���
		finish();
	}
}
