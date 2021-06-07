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
	
	// 数据库相关
	private UserDao userdao;
	private SQLiteDatabase db;
	private ImageView quitimg;
	// 控件
	private Button regisbt;
	private TextView gologintv;
	private EditText regisname;
	private EditText regispwd;
	private EditText regischeckpwd;
	private String name;
	private String pwd,checkpwd;
	private RadioGroup rolegroup;
	
	// 变量
	private int role;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
		setContentView(R.layout.activity_register);
		initDaoAndDb();
		findView();
		setOnclicks();
	}
	// 寻找控件
	public void findView(){
		regisbt = (Button)this.findViewById(R.id.regis_bt_reg);
		gologintv=(TextView)this.findViewById(R.id.regis_tv_gologin);
		regisname=(EditText)this.findViewById(R.id.regis_ed_name);
		regispwd=(EditText)this.findViewById(R.id.regis_ed_pwd);
		regischeckpwd=(EditText)this.findViewById(R.id.regis_ed_checkpwd);
		quitimg = (ImageView)findViewById(R.id.regis_img_quit);
		quitimg.setOnClickListener(this);
		rolegroup = (RadioGroup)this.findViewById(R.id.regis_radiogroup_role);
		
		//单选
		rolegroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.regis_radio_user){//选择普通用户
					role = 0;
				}else if(checkedId == R.id.regis_radio_admin){//选择管理员
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
				// 获取到name值 去和数据库匹配是否存在
				name = regisname.getText().toString();
				pwd = regispwd.getText().toString();
				checkpwd = regischeckpwd.getText().toString();
				if(name.trim().equals("")){
					Toast.makeText(this, "请输入账号", 0).show();
					return;
				}
				if(pwd.trim().equals("")){
					Toast.makeText(this, "请输入密码", 0).show();
					return;
				}
				if(checkpwd.trim().equals("")){
					Toast.makeText(this, "请确认密码", 0).show();
					return;
				}
				checkRegister();
				break;
			case R.id.regis_tv_gologin:
				finish();// 结束自己
				break;
			case R.id.regis_img_quit:
				finish();// 结束自己
				break;
		default:
			break;
		}
	}
	
	// 数据库相关初始化
	protected void initDaoAndDb() {
		userdao = new UserDao();
		db = MyApplication.lihelper.getReadableDatabase();
	}
	// 注册，判断是否有重名，有提示，没有注册，提示注册成功，去登录界面
	protected void checkRegister() {
		// 
		boolean isok = userdao.checkNameExist(db, name);
		// 不存在
		if(!isok){
			UserBean user = new UserBean();
			user.setUsername(name);
			user.setUserpwd(pwd);
			user.setRole(role);
			int ct = userdao.insertUser(db, user);
			if(ct > 0){
				Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
				// 去登录界面, 是返回，携带参数
				backLoginWithArguments();
			}else{
				Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, "该用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();
		}
	}
	// 返回login
	public void backLoginWithArguments(){
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra("username", name);
		intent.putExtra("userpwd", pwd);
		intent.putExtra("role", role);
		setResult(1,intent);// 结果
		finish();
	}
}
