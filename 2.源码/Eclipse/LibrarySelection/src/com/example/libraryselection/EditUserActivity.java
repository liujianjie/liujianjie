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

// 修改个人信息ac
public class EditUserActivity extends AppCompatActivity implements OnClickListener{
	// 
	private TextView titletv;
	private EditText inputdt;
	private Button savebt;
	private ImageView quitimg;
	
	// 数据库相关
	private SQLiteDatabase db;
	private UserDao userdao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
		
		setContentView(R.layout.activity_edituser);
		findView();
		// 获取intent传值
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
	// 初始化变量
	protected void initArgument(){
		userdao = new UserDao();
		
		// 以读方式打开
		db = MyApplication.lihelper.getReadableDatabase();
	}
	String choose = "";
	public void getIntentVal(){
		Intent intent = this.getIntent();
		choose = intent.getStringExtra("flag");
		if(choose.equals("username")){
			titletv.setText("修改用户名");
			inputdt.setText(MyApplication.user.getUsername());
		}else if(choose.equals("userpwd")){
			titletv.setText("修改密码");
			inputdt.setText(MyApplication.user.getUserpwd());
		}else if(choose.equals("phone")){
			titletv.setText("修改手机");
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
						// 需要判断该用户名是否存在
						if(!userdao.checkNameExist(db, text)){
							// 设置全局，并且修改数据库改
							MyApplication.user.setUsername(text);
							userdao.updateUser(db, MyApplication.user);
							setResult(1, intent);
							finish();
						}else{
							Constant.commonToast(this, "改用户名已存在，请重新输入");
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
					Constant.commonToast(this, "不能为空");
				}
				break;
			case R.id.edituser_img_quit:
				finish();
				break;
		}
	}
}
