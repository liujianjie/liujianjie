package com.example.libraryselection.mappack;


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

// �޸ĵ�ͼ��view
public class EditMapLayerView extends View{
	// �ϼ�
	Context supercon;
	private Paint yuxuanpaint;// Ԥѡλ�õ�paint,����䲢����ɫ
	private Paint bitmappaint;// λͼ�Ļ�ͼpaint
	// ��ͼ��Χ
//	private int mapdata[][];
	public EditMapLayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		supercon = context;
//		initData();
		initPaint();
		Log.i("library", "EditMapLayerView���캯��");
	}
	
	// ��ʼ����̬����,mapdata��Щ
	public void initDataFromMapList(){
//		mapdata = new int[MapConstant.i][MapConstant.j];
		// �ӹ���list��ȡ��ͼ��Ϣ
		EditMapDataUtil.getMapDataFromMapList();
		invalidate();
	}
	public void initDataFromMapMoudleList(){
//		mapdata = new int[MapConstant.i][MapConstant.j];
		// �ӹ���mapmoudlelist��ȡ��ͼ��Ϣ
		EditMapDataUtil.getMapDataFromMapMoudleList();
		invalidate();
	}
	protected void initPaint(){
		yuxuanpaint = new Paint();
		yuxuanpaint.setColor(Color.GREEN);
		yuxuanpaint.setStyle(Paint.Style.STROKE);
		yuxuanpaint.setStrokeWidth(2);
		
		bitmappaint = new Paint();
		bitmappaint.setColor(Color.BLACK);
		bitmappaint.setStyle(Paint.Style.STROKE);
		bitmappaint.setStrokeWidth(1);
	}
	
	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		// ���¸��»���
		super.invalidate();
	}
	// ��������
	/**
	 1.��֮ǰ�Ļ�ͼ��Ԥѡ�ط�����Ϊԭ����ֵ
	 2.������ȥ����
	 **/
	// �Ƿ�ָ���Ԥѡ��λ������
	boolean isrecoveryuanxianyuxuan;
	int start[] = new int[2];// ��ʼ��
	int end[] = new int[2];// ������
	int mid[] = new int[2];// �м��
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// �����λ�� /40
		int j = (int) (event.getX() / MapConstant.MapPiexl);
		int i = (int) (event.getY() / MapConstant.MapPiexl);
		switch(event.getAction()){
			// ��ָ����
			case  MotionEvent.ACTION_DOWN:
				// ������ط���ij����ΪԤѡ
//				Toast.makeText(supercon, "����", Toast.LENGTH_SHORT).show();
				// ��ΪԤѡ��ǰ�᲻�Ǳ߽�
				if(!EditMapDataUtil.isOverLine(i, j)){
					start[0] = j;// 0����
					start[1] = i;
//					EditMapDataUtil.setIjBeYu(EditMapDataUtil.currMapData, i, j);
				}
				break;
			// ��ָ�ƶ��� �����滭
			case MotionEvent.ACTION_MOVE:
//				Toast.makeText(supercon, "�ƶ�", Toast.LENGTH_SHORT).show();
				if(!EditMapDataUtil.isOverLine(i, j)){
					end[0] = j;
					end[1] = i;
					
					// ��Ҫ���³����滭�еĲ���Ԥѡλ�ã����б���һ����λ��ҪС��ʱ�� ��Ҫ���»ָ���ͼԭֵ �ٻ滭��ǰ
					if(isrecoveryuanxianyuxuan){
						EditMapDataUtil.setStillDraw(start, mid, 2);//
					}
					isrecoveryuanxianyuxuan = true;// һֱҪ���£�֪��̧����false
					
					// �ٳ�������
					EditMapDataUtil.setStillDraw(start, end, 1);
					// ���»�ͼ
					invalidate();
					
					// ������һ�����½ǵĵ�
					mid[0] = end[0];
					mid[1] = end[1];
				}
				break;
			// ��ָ�ɿ�, ѡ���λ��Ԥѡ
			case MotionEvent.ACTION_UP:
//				Toast.makeText(supercon, "̧��", Toast.LENGTH_SHORT).show();
				// 1.�Ȼָ�֮ǰѡ��ġ�
				EditMapDataUtil.recoverMapDataFromData();
				if(!EditMapDataUtil.isOverLine(i, j)){
					end[0] = j;
					end[1] = i;
					// 2.ȷ���϶�ʱ��Ԥѡ������Ǳ��ֻ滭
					EditMapDataUtil.setStillDraw(start, end, 3);
					// ���»�ͼ
					invalidate();
					
					// ��ʼֵ
					isrecoveryuanxianyuxuan = false;
				}
				break;
		}
//		return super.onTouchEvent(event);
		return true;
	}
	// ��ΪԤѡλ��Ϊ��λ���ϰ���յ�
	public void MapDatayuToThing(int flag){
		// �ڱ�֮ǰ��stack����ԭʼ��ͼ
		// Ӧ���ж��Ƿ��б仯
		// �ȷ��룬û�иı�͵���
		EditMapDataUtil.hismapsta.add(EditMapDataUtil.getYuanMapDataofDeep());
		boolean ischange = false;
		// ѭ��
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				if(EditMapDataUtil.currMapData[i][j] == MapConstant.YU){
					EditMapDataUtil.setIjBeThing(i, j, flag);
					ischange = true;
				}
			}
		}
		// û�иı�
		if(!ischange){
			// ��ջ
			EditMapDataUtil.hismapsta.pop();
		}
		// ����
		invalidate();
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
					// ȷ��Բ��λ��Ҫ���ϰ뾶
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
		
//		canvas.drawText("��Ļ��С������"+Constant.ScreenWidth+"�ߣ�"+Constant.ScreenHeight, 50, 50, seatpaint);
//		this.invalidate();
	}
}

/*
TestCode
		super.onDraw(canvas);
		// ��Ҫƫ����
//		canvas.translate((Constant.ScreenWidth - MapConstant.j * MapConstant.MapPiexl) / 2, (Constant.ScreenHeight - MapConstant.i * MapConstant.MapPiexl) / 2);
//		canvas.translate((Constant.ScreenHeight - MapConstant.i * MapConstant.MapPiexl) / 2, (Constant.ScreenWidth - MapConstant.j * MapConstant.MapPiexl) / 2);
//		canvas.translate(80, 80);
		// i���У�j����
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				Rect rc = new Rect(j * MapConstant.MapPiexl, i * MapConstant.MapPiexl, j * MapConstant.MapPiexl + MapConstant.MapPiexl, i * MapConstant.MapPiexl + MapConstant.MapPiexl);
				if(i == 0 || i == mapdata.length - 1 || j == 0 || j == mapdata[i].length - 1){
					canvas.drawRect(rc, barpaint);
				}
			}
		}
		
		canvas.drawText("��Ļ��С������"+Constant.ScreenWidth+"�ߣ�"+Constant.ScreenHeight, 50, 50, seatpaint);
//		this.invalidate();
 */