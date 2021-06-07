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
		// ͨ��ȫ�ּ���
		String head = MyApplication.user.getHeadimg();
		if (head != null && !head.equals("")) {
			// ��ȡurl
			headbitmap = BitmapUtil.getLocalBitmap(MyApplication.user.getHeadimg());
			if (headbitmap != null) {
				headdetail_img.setImageBitmap(headbitmap);
			}
		}
	}

	// ��ȡ���ش洢·��ͼƬ
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.headdetail_bt_change:
			// ������ѡ��
			GenerateDialog.createChooseImgWAYCustomsDialog(this);
			break;

		}
	}

	// ����
	public void openCameraForPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// �������Ҫ��action
																	// ��MediaStore����
		startActivityForResult(intent, 1);
	}

	// ���
	public void openGalleryphotos() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// ��һ����ͼ�⣬�ڶ�����ͼƬ��uri
		startActivityForResult(intent, 2);
	}

	// ����
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// dataӦ���Ǳ�����ͼƬ��Bitmap
		// ͼƬ��data��ȡ
		if (requestCode == 1) {
			try {
				Bitmap bitmaps = (Bitmap) data.getExtras().get("data");
				if (bitmaps != null) {
					// ͨ��bit�õ�����ͼƬ��Ӧ�ð�,���ҷ���·��
					String path = saveImage(bitmaps);

					updateUserHeadImg(bitmaps, path);
				}
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		} else if (requestCode == 2) {// ͼ��ѡ��һ����Ƭ�Ĳ���
			try {
				// �����ͼ�����õĻ� �õ�ͼƬ��uri
				Uri photouri = data.getData();
				// ͨ��uri��ȡͼƬλͼ ���Ա�����Ӧ��
				Bitmap bitmaps = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photouri);//
				// ͼƬ��Ϣ��·���ֶ���
				// ���DATAӦ����ͼƬ·�����ֶ�
				String photopath[] = { MediaStore.Images.Media.DATA };
				// �����ֶ�����ͼƬ��uri��ȡͼƬ������·��
				Cursor cursor = this.getContentResolver().query(photouri, photopath, "", null, "");
				int index = cursor.getColumnIndexOrThrow(photopath[0]);
				cursor.moveToFirst();
				// ��ȡ·��
				String path = cursor.getString(index);
				// tx=path;ֻ�ǻ�ȡ���˾���·��
				// ��ȡ
				// Toast.makeText(PhotoActivity.this, path,
				// Toast.LENGTH_LONG).show();

				updateUserHeadImg(bitmaps, path);
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}

	public String saveImage(Bitmap bit) {
		// ��������ɹ��ŷ���
		String png = "/" + System.currentTimeMillis() + ".png";
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + png;
		File files = new File(path);// ������ļ���������������Ҫ������
		try {
			if (!files.exists()) {
				files.createNewFile();
			}
			FileOutputStream fo = new FileOutputStream(files);
			bit.compress(CompressFormat.PNG, 100, fo);// 100Ϊ����ͼƬ����Ķ��� �����Ȱɡ�����

			fo.flush();
			fo.close();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			path = "";
		}
		return path;
	}

	// ���µ�ǰ��Ƭ�����Ҹ������ݿ��
	public void updateUserHeadImg(Bitmap bit, String path) {
		// ����ǰ����Ƭ��ʾ����
		headdetail_img.setImageBitmap(bit);

		// �����û������·��
		MyApplication.user.setHeadimg(path);

		// ����user��
		userdao.updateUser(db, MyApplication.user);
	}
	
}
