package com.example.libraryselection.mappack;


import com.example.libraryselection.ChooseSeatMapActivity;
import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.SeatInfoBean;
import com.example.libraryselection.resource.Constant;
import com.example.libraryselection.resource.MapConstant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

// ѡ����λ�ĵ�ͼ��view,���ǹ���ԭʼ��ͼ�뵱ǰ��ͼ�������ܱ�ö������ˣ�ֻ����λ
public class ChooseSeatMapLayerView extends View{
	// �ϼ�
	Context supercon;
	// ����Ҫ��
	ChooseSeatMapActivity chooseactivity;
	
	private Paint yuxuanpaint;// Ԥѡλ�õ�paint,����䲢����ɫ
	private Paint myseatpaint;// ��ѡ�����λ
	private Paint beichooseseatpaint;// ��ѡ�����λ
	// ��ͼ��Χ
//	private int mapdata[][];
	public ChooseSeatMapLayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		supercon = context;
		// ��Ҫǿ��ת��
		chooseactivity = (ChooseSeatMapActivity)context;
		// ֱ�Ӵ�maplist��ȡ��ͼ����
		initData();
		
		initPaint();
		Log.i("library", "ChooseSeatMapLayerView���캯��");
	}
	public void initData(){
		EditMapDataUtil.getMapDataFromMapList();
		invalidate();
	}
	
	protected void initPaint(){
		
		yuxuanpaint = new Paint();
		yuxuanpaint.setColor(Color.GREEN);
		yuxuanpaint.setStyle(Paint.Style.STROKE);
		yuxuanpaint.setStrokeWidth(2);
		
		myseatpaint = new Paint();
		myseatpaint.setColor(Color.YELLOW);
		myseatpaint.setStyle(Paint.Style.STROKE);
		myseatpaint.setStrokeWidth(2);
		
		beichooseseatpaint = new Paint();
		beichooseseatpaint.setColor(Color.RED);
		beichooseseatpaint.setStyle(Paint.Style.STROKE);
		beichooseseatpaint.setStrokeWidth(2);
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		// ���¸��»���
		super.invalidate();
	}
	// ��������
	/*ֱ�ӱ�ΪԤѡ*/
	int lasti,lastj;
	int mychoosei, mychoosej;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// �����λ�� /40
		mychoosej = (int) (event.getX() / MapConstant.MapPiexl);
		mychoosei = (int) (event.getY() / MapConstant.MapPiexl);
		switch(event.getAction()){
			// ��ָ����
			case  MotionEvent.ACTION_DOWN:
				
				break;
			// ��ָ�ɿ�, ѡ���λ��Ԥѡ
			case MotionEvent.ACTION_UP:
				// 1.�Ȼָ�֮ǰѡ���Ԥѡλ�á������ֻ��ָ������һ��ij����
				if(!EditMapDataUtil.isOverLine(lasti, lastj)){
					EditMapDataUtil.recoverDataFromYuanByIJ(lasti, lastj);
				}
				// 2.��ǰλ�ñ�ΪԤѡ��ǰ�᲻�Ǳ߽�
				if(!EditMapDataUtil.isOverLine(mychoosei, mychoosej)){
					EditMapDataUtil.setIjBeYu(mychoosei, mychoosej);
					// �÷�����
					lasti = mychoosei;
					lastj = mychoosej;
				}
				// ���»�ͼ
				invalidate();
				break;
		}
//		return super.onTouchEvent(event);
		return true;
	}
	// ��ΪԤѡλ��Ϊѡ������Ҫ�ж��Ƿ���λ
	// 1.�ж��Ƿ���λ
	// 2.�ж��Ƿ��ǲ�������λ
	// 2.�ж������λ�Ƿ�������ռ�ˣ��ǲ���ѡ
	// 3.�ж����ʱ��Σ����죬���Ƿ���������λ��ѯ���Ƿ�ȡ��֮ǰ��λ���ǣ��޸�ԭ����λ�������ͬһ�����view
	// ����bean Ϊ��ͨ��
	public void MapDatayuToMySeat(){
		if(!EditMapDataUtil.isOverLine(mychoosei, mychoosej)){
			// 1
			int zhi = EditMapDataUtil.yuanMapData[mychoosei][mychoosej];
			//2
			if(zhi == MapConstant.SEATNO){
				Constant.commonToast(supercon, "����λ������");
				return;
			}
			if(zhi == MapConstant.SEAT){
				// 2
				boolean isoccur = thiseatIsOccur(mychoosei, mychoosej);
				if(!isoccur){
					// 3 
					if(!isThisTimeIhadSeat(mychoosei, mychoosej)){
//						SeatInfoBean seat = new SeatInfoBean();
//						// ��ӵ����ݿ��У���ǰi,j��fid��sday��stime����
//						seat.setSnumber(mychoosei+""+mychoosej);
//						seat.setSx(mychoosej);
//						seat.setSy(mychoosei);
						// ��Ԥѡ��λ������ָ�
						if(!EditMapDataUtil.isOverLine(mychoosei, mychoosej)){
							EditMapDataUtil.recoverDataFromYuanByIJ(mychoosei, mychoosej);
						}
						chooseactivity.insertOneSeatToSqiteAndList(mychoosei, mychoosej);
					}
				}else{
					Constant.commonToast(supercon, "����λ�ѱ�ռ�ã���ѡ������");
				}
			}else{
				Constant.commonToast(supercon, "��ѡ����λ");
			}
		}else{
			Constant.commonToast(supercon, "����ѡ����λ");
		}
//		invalidate();
	}
	// �ж������λ�Ƿ�ռ
	public boolean thiseatIsOccur(int i, int j){
		for(int z = 0; z < MapConstant.curallseatlist.size(); z++){
			SeatInfoBean seat = MapConstant.curallseatlist.get(z);
			// x, j
			int m = seat.getSx();
			int n = seat.getSy();
			if(m == j && n == i){
				return true;
			}
		}
		return false;
	}
	// �ж������ʱ����Ƿ�������λ
	public boolean isThisTimeIhadSeat(int i, int j){
		// һ��ֻ��һ��
		for(int z = 0; z < MapConstant.myseatlist.size(); z++){
			SeatInfoBean oldseat = MapConstant.myseatlist.get(z);
			// ����
			SeatInfoBean newseat = new SeatInfoBean();
			newseat.setSy(i);
			newseat.setSx(j);
			newseat.setSnumber(i+""+j);
			// ������¥���fid
			newseat.setFid(MapConstant.getCurLayerZhujian());
			
			chooseactivity.generateHadSeatDialog(oldseat, newseat);
//			chooseactivity.generateHadSeatDialog(seat.getSid(), seat.getSy(), seat.getSx(), MapConstant.getLayerNumberByFid(seat.getFid()), i, j, MapConstant.getLayerNumber());
			return true;
		}
		return false;
	}
	// ����
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		Log.e("library", "onMeasure--widthMode-->" + widthMode);
		Log.e("library", "onMeasure--widthSize-->" + widthSize);
		Log.e("library", "onMeasure--heightMode-->" + heightMode);
		Log.e("library", "onMeasure--heightSize-->" + heightSize);
		// ͨ��height����width��
		// ��Ϊheight��match ��height��ȥ100�� Ҫ����
		heightSize = heightSize - 100;
		// ����ȡ���������ش�С
		int pix = (int) Math.floor(heightSize / (double)MapConstant.i);
		// ��Ϊȫ�ֵ�����
		MapConstant.MapPiexl = pix;

		Log.e("library", "onMeasure--pix-->" + pix);
//        Toast.makeText(supercon, "pix:"+pix, Toast.LENGTH_SHORT).show();

		// ��Ϊ������������ȡ����ƫС�ˣ���С��ȥ
		heightSize = pix * MapConstant.i;

		// ���ݻ������صõ�width
		widthSize = pix * MapConstant.j;

		//ȡ��С��Ϊ�ؼ��Ŀ�ߵ���Сֵ
		setMeasuredDimension(widthSize,heightSize);

	}
	// �滭����
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// i���У�j����
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				int leftj = j * MapConstant.MapPiexl;
				int lefti = i * MapConstant.MapPiexl;
				int righti = j * MapConstant.MapPiexl + MapConstant.MapPiexl;
				int rightj = i * MapConstant.MapPiexl + MapConstant.MapPiexl;
				// ʵʱĿ��λ��Ҫ����ġ�
				Rect rc = new Rect(leftj, lefti, righti, rightj);
				// �����ǽ
				if(EditMapDataUtil.currMapData[i][j] == MapConstant.BAR){
//					canvas.drawBitmap(MapConstant.bigwallbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigwallbitmap, MapConstant.BigimgRect, rc, null);
				}
				// �����Ԥѡ��ͼ
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.YU){
					canvas.drawRect(rc, yuxuanpaint);
				}
				// �������λ
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.SEAT){
//					canvas.drawBitmap(MapConstant.bigseatbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigseatbitmap, MapConstant.BigimgRect, rc, null);
				}
				// ����ǵذ�
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.FIELD){
//					canvas.drawBitmap(MapConstant.bigfloorbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigfloorbitmap, MapConstant.BigimgRect, rc, null);
				}
				// ��������
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.SHUJIA){
//					canvas.drawBitmap(MapConstant.bigshujiabitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigshujiabitmap, MapConstant.BigimgRect, rc, null);
				}
				// ����ǲ�������λ
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.SEATNO){
//					canvas.drawBitmap(MapConstant.bigseatnobitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigseatnobitmap, MapConstant.BigimgRect, rc, null);
				}
			}
		}
		// ���Ʊ�ѧ��ѡ���ĵ�ͼ,ѭ��
		// ֱ�Ӹ���ij��ָ��λ�û���, ��Ҫ�ж�¥���Ƿ��Ӧ
		Paint curp = new Paint();
		for(int z = 0; z < MapConstant.curallseatlist.size(); z++){
			SeatInfoBean seat = MapConstant.curallseatlist.get(z);
			if(seat.getFid() == MapConstant.getCurLayerZhujian()){
				// ��������λ���Ҷ��ģ���Paint
				if(seat.getUid() == MyApplication.user.getUid()){
					curp = myseatpaint;
				}else{
					curp = beichooseseatpaint;
				}
				// x, j
				int j = seat.getSx();
				int i = seat.getSy();
				int leftj = j * MapConstant.MapPiexl;
				int lefti = i * MapConstant.MapPiexl;
				int righti = j * MapConstant.MapPiexl + MapConstant.MapPiexl;
				int rightj = i * MapConstant.MapPiexl + MapConstant.MapPiexl;
				Rect rc = new Rect(leftj, lefti, righti, rightj);
				canvas.drawRect(rc, curp);
//			int r = MapConstant.MapPiexl / 2;
//			canvas.drawCircle(leftj + r, lefti + r, r, curp);
			}

		}
		
//		canvas.drawText("��Ļ��С������"+Constant.ScreenWidth+"�ߣ�"+Constant.ScreenHeight, 50, 50, seatpaint);
//		this.invalidate();
	}
}
