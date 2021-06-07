package com.example.libraryselection.dialog;

import com.example.libraryselection.ChooseSeatMapActivity;
import com.example.libraryselection.EditFloorMapActivity;
import com.example.libraryselection.HeadImgDetailActivity;
import com.example.libraryselection.LoginActivity;
import com.example.libraryselection.MainActivity;
import com.example.libraryselection.MapLayerFragment;
import com.example.libraryselection.R;
import com.example.libraryselection.StudentActivity;
import com.example.libraryselection.UserFragment;
import com.example.libraryselection.bean.SeatInfoBean;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GenerateDialog {
	// 产生普通对话框
	public static void genEditMapQuitNormalDialog(final AppCompatActivity con, String msg){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("提示");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	con.finish();
            }
        });
        normalDialog.setNegativeButton("取消", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // 显示
        normalDialog.show();
	}
	// 产生输入框
	public static void genEditMapInputDialog(final EditFloorMapActivity con){
		final EditText editText = new EditText(con);
	    final AlertDialog.Builder inputDialog = new AlertDialog.Builder(con);
	    inputDialog.setTitle("请输入模板名称").setView(editText);
	    inputDialog.setPositiveButton("确定", 
	        new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	String st = editText.getText().toString();
	            if(!st.trim().equals("")){
	            	// 设置模板名
	            	con.setMoudleMname(st);
	            	// 开始往数据库添加数据
	            	con.setMoudleToSqlite();
	            }else{
	            	// 为空不要消失
	            	Toast.makeText( con, "模板名不能为空", Toast.LENGTH_LONG).show();
//	            	inputDialog.show();
	            }
	        }
	    });
	    inputDialog.setNegativeButton("取消", 
		        new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	
		        }
		    });
//	    inputDialog.setPositiveButton("确定",null);
//	    inputDialog.setNegativeButton("取消", 
//			        new DialogInterface.OnClickListener() {
//			        @Override
//			        public void onClick(DialogInterface dialog, int which) {
//			        	
//			        }
//			    });
//	    inputDialog.create();
//	    inputDialog.setOnShowListener
	    inputDialog.show();
	}
	
	// 产生是否生成该楼层普通对话框
	public static void genLayerNormalDialog(final EditFloorMapActivity con, String msg){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("提示");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	con.geneOneLayer();
            }
        });
        normalDialog.setNegativeButton("取消", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // 显示
        normalDialog.show();
	}
	// 说已经座位了
	public static void genHadSeatDialog(final ChooseSeatMapActivity con, String msg,final SeatInfoBean old, final SeatInfoBean newseat){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("提示");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("更换座位", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	con.updateOneSeatToSqliteAndlist(old, newseat);
            }
        });
        normalDialog.setNegativeButton("取消", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // 显示
        normalDialog.show();
	}
	
	// 退出登录
	public static void geneIsQuitCurUserLogin(final MainActivity main, String msg){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(main);
        normalDialog.setTitle("提示");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	main.deleteCurUserLogin();
            }
        });
        normalDialog.setNegativeButton("取消", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // 显示
        normalDialog.show();
	}
	// 生成为更换背景头像的自定义框
	// 自定义对话框
	public static void createChooseImgWAYCustomsDialog(final HeadImgDetailActivity headcon){
		final Dialog dis = new Dialog(headcon);
		dis.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dis.setContentView(R.layout.headimgadd_dialog);
		Button gbt = dis.findViewById(R.id.headimg_add_gallery);
		Button tbt = dis.findViewById(R.id.headimg_add_takephoto);
		gbt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// 图库选择
				headcon.openGalleryphotos();
				
				dis.dismiss();
			}
		});
		tbt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 拍照
				headcon.openCameraForPhoto();
				dis.dismiss();
			}
		});
		dis.show();
	}
	// 产生是否删除这个学生普通对话框
	public static void genDelStuNormalDialog(final StudentActivity stu, String msg, final int pos){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(stu);
        normalDialog.setTitle("提示");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	stu.delStuByUid(pos);
            }
        });
        normalDialog.setNegativeButton("取消", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // 显示
        normalDialog.show();
	}
	// 产生是否删除楼层对话框
	public static void genDelMapNormalDialog(MainActivity con, final MapLayerFragment map, String msg, final int fid){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("提示");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	map.delOneMapFromSqliteAndList(fid);
            }
        });
        normalDialog.setNegativeButton("取消", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // 显示
        normalDialog.show();
	}
	// 产生普通消息框
	public static void genNormalDialog(AppCompatActivity con, String msg){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("提示");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // 显示
        normalDialog.show();
	}
}
