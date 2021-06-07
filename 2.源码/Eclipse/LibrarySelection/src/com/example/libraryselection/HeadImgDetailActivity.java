package com.example.libraryselection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.dao.UserDao;
import com.example.libraryselection.dialog.GenerateDialog;
import com.example.libraryselection.resource.BitmapUtil;

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
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class HeadImgDetailActivity extends AppCompatActivity implements OnClickListener {
	ImageView headdetail_img;
	Button changeheadbt;

	Bitmap headbitmap = null;

	//
	UserDao userdao;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_headimgdetail);
		findView();
		setImgView();
		initArgument();
	}

	public void initArgument() {
		userdao = new UserDao();
		db = MyApplication.lihelper.getReadableDatabase();
	}

	private void findView() {
		headdetail_img = (ImageView) findViewById(R.id.headdetail_img);
		changeheadbt = (Button) findViewById(R.id.headdetail_bt_change);

		changeheadbt.setOnClickListener(this);
	}

	//
	private void setImgView() {
		// 通过全局即可
		String head = MyApplication.user.getHeadimg();
		if (head != null && !head.equals("")) {
			// 读取url
			headbitmap = BitmapUtil.getLocalBitmap(MyApplication.user.getHeadimg());
			if (headbitmap != null) {
				headdetail_img.setImageBitmap(headbitmap);
			}
		}
	}

	// 读取本地存储路径图片
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.headdetail_bt_change:
			// 弹出款选择
			GenerateDialog.createChooseImgWAYCustomsDialog(this);
			break;

		}
	}

	// 照相
	public void openCameraForPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 打开照相机要用action
																	// 用MediaStore就行
		startActivityForResult(intent, 1);
	}

	// 相册
	public void openGalleryphotos() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 第一个是图库，第二个是图片的uri
		startActivityForResult(intent, 2);
	}

	// 返回
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// data应该是保存了图片的Bitmap
		// 图片用data获取
		if (requestCode == 1) {
			try {
				Bitmap bitmaps = (Bitmap) data.getExtras().get("data");
				if (bitmaps != null) {
					// 通过bit拿到保存图片到应用吧,并且返回路径
					String path = saveImage(bitmaps);

					updateUserHeadImg(bitmaps, path);
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		} else if (requestCode == 2) {// 图库选择一个照片的操作
			try {
				// 这个从图库里拿的话 拿到图片的uri
				Uri photouri = data.getData();
				// 通过uri获取图片位图 可以保存在应用
				Bitmap bitmaps = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photouri);//
				// 图片信息的路径字段名
				// 这个DATA应该是图片路径的字段
				String photopath[] = { MediaStore.Images.Media.DATA };
				// 根据字段名和图片的uri获取图片真正的路径
				Cursor cursor = this.getContentResolver().query(photouri, photopath, "", null, "");
				int index = cursor.getColumnIndexOrThrow(photopath[0]);
				cursor.moveToFirst();
				// 获取路径
				String path = cursor.getString(index);
				// tx=path;只是获取到了绝对路径
				// 获取
				// Toast.makeText(PhotoActivity.this, path,
				// Toast.LENGTH_LONG).show();

				updateUserHeadImg(bitmaps, path);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	public String saveImage(Bitmap bit) {
		// 如果创建成功才返回
		String png = "/" + System.currentTimeMillis() + ".png";
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + png;
		File files = new File(path);// 而这个文件不存在所以我们要创建它
		try {
			if (!files.exists()) {
				files.createNewFile();
			}
			FileOutputStream fo = new FileOutputStream(files);
			bit.compress(CompressFormat.PNG, 100, fo);// 100为这张图片传输的多少 清晰度吧。。。

			fo.flush();
			fo.close();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			path = "";
		}
		return path;
	}

	// 更新当前照片，并且更新数据库表
	public void updateUserHeadImg(Bitmap bit, String path) {
		// 将当前的照片显示最新
		headdetail_img.setImageBitmap(bit);

		// 更新用户保存的路径
		MyApplication.user.setHeadimg(path);

		// 更新user表
		userdao.updateUser(db, MyApplication.user);
	}
	
}
