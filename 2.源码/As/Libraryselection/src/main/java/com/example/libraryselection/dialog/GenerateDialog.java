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
	// ������ͨ�Ի���
	public static void genEditMapQuitNormalDialog(final AppCompatActivity con, String msg){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("��ʾ");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("ȷ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	con.finish();
            }
        });
        normalDialog.setNegativeButton("ȡ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // ��ʾ
        normalDialog.show();
	}
	// ���������
	public static void genEditMapInputDialog(final EditFloorMapActivity con){
		final EditText editText = new EditText(con);
	    final AlertDialog.Builder inputDialog = new AlertDialog.Builder(con);
	    inputDialog.setTitle("������ģ������").setView(editText);
	    inputDialog.setPositiveButton("ȷ��", 
	        new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	String st = editText.getText().toString();
	            if(!st.trim().equals("")){
	            	// ����ģ����
	            	con.setMoudleMname(st);
	            	// ��ʼ�����ݿ��������
	            	con.setMoudleToSqlite();
	            }else{
	            	// Ϊ�ղ�Ҫ��ʧ
	            	Toast.makeText( con, "ģ��������Ϊ��", Toast.LENGTH_LONG).show();
//	            	inputDialog.show();
	            }
	        }
	    });
	    inputDialog.setNegativeButton("ȡ��", 
		        new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	
		        }
		    });
//	    inputDialog.setPositiveButton("ȷ��",null);
//	    inputDialog.setNegativeButton("ȡ��", 
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
	
	// �����Ƿ����ɸ�¥����ͨ�Ի���
	public static void genLayerNormalDialog(final EditFloorMapActivity con, String msg){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("��ʾ");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("ȷ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	con.geneOneLayer();
            }
        });
        normalDialog.setNegativeButton("ȡ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // ��ʾ
        normalDialog.show();
	}
	// ˵�Ѿ���λ��
	public static void genHadSeatDialog(final ChooseSeatMapActivity con, String msg,final SeatInfoBean old, final SeatInfoBean newseat){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("��ʾ");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("������λ", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	con.updateOneSeatToSqliteAndlist(old, newseat);
            }
        });
        normalDialog.setNegativeButton("ȡ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // ��ʾ
        normalDialog.show();
	}
	
	// �˳���¼
	public static void geneIsQuitCurUserLogin(final MainActivity main, String msg){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(main);
        normalDialog.setTitle("��ʾ");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("ȷ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	main.deleteCurUserLogin();
            }
        });
        normalDialog.setNegativeButton("ȡ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // ��ʾ
        normalDialog.show();
	}
	// ����Ϊ��������ͷ����Զ����
	// �Զ���Ի���
	public static void createChooseImgWAYCustomsDialog(final HeadImgDetailActivity headcon){
		final Dialog dis = new Dialog(headcon);
		dis.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dis.setContentView(R.layout.headimgadd_dialog);
		Button gbt = dis.findViewById(R.id.headimg_add_gallery);
		Button tbt = dis.findViewById(R.id.headimg_add_takephoto);
		gbt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// ͼ��ѡ��
				headcon.openGalleryphotos();
				
				dis.dismiss();
			}
		});
		tbt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// ����
				headcon.openCameraForPhoto();
				dis.dismiss();
			}
		});
		dis.show();
	}
	// �����Ƿ�ɾ�����ѧ����ͨ�Ի���
	public static void genDelStuNormalDialog(final StudentActivity stu, String msg, final int pos){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(stu);
        normalDialog.setTitle("��ʾ");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("ȷ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	stu.delStuByUid(pos);
            }
        });
        normalDialog.setNegativeButton("ȡ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // ��ʾ
        normalDialog.show();
	}
	// �����Ƿ�ɾ��¥��Ի���
	public static void genDelMapNormalDialog(MainActivity con, final MapLayerFragment map, String msg, final int fid){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("��ʾ");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("ȷ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	map.delOneMapFromSqliteAndList(fid);
            }
        });
        normalDialog.setNegativeButton("ȡ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        // ��ʾ
        normalDialog.show();
	}
	// ������ͨ��Ϣ��
	public static void genNormalDialog(AppCompatActivity con, String msg){
		AlertDialog.Builder normalDialog = new AlertDialog.Builder(con);
        normalDialog.setTitle("��ʾ");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("ȷ��", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // ��ʾ
        normalDialog.show();
	}
}
