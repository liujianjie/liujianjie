//package com.example.libraryselection.dialog;
//
//
//import android.app.Dialog;
//import android.content.Context;
//import android.view.View;
//import android.widget.Button;
//
//public class HeadDetailImgDialog extends Dialog{
//	private Button tv1;
//	private Button tv2;
//	Context co;
//	PhotoActivity photo;
//	
//	public HeadDetailImgDialog(Context context,int view) {
//		super(context,view);
//		setContentView(R.layout.photoadd_dialog);
//		co = context;
//		findView();
//		photo = (PhotoActivity)context;
//	}
//	public void findView(){
//		tv1 = (Button) this.findViewById(R.id.chooseimage_tv1);
//		tv2 = (Button) this.findViewById(R.id.chooseimage_tv2);
//		tv1.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				photo.camera();
//				HeadDetailImgDialog.this.dismiss();
//			}
//		});
//		tv2.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				photo.photos();
//				HeadDetailImgDialog.this.dismiss();
//			}
//		});
//	}
//}
