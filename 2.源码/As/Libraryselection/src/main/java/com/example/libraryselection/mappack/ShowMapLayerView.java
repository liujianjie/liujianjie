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

// չʾ��ͼ��view
public class ShowMapLayerView extends View{
	// �ϼ�
	Context supercon;
	private Paint yuxuanpaint;// Ԥѡλ�õ�paint,����䲢����ɫ
	private Paint bitmappaint;// λͼ�Ļ�ͼpaint
	// ��view�ĵ�ͼ
	private int showmapview[][];

	public ShowMapLayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		supercon = context;
		initData();
		initPaint();
	}

	// ��ʼ����̬����,mapdata��Щ
	protected void initData(){
		showmapview = new int[MapConstant.i][MapConstant.j];
//		EditMapDataUtil.setMapDataSpace();
	}
	// ���õ�ͼ����
	public void setMapData(int mapdata[][]){
		showmapview = mapdata;
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
		// ͨ��width����height��
		// ��Ϊwidth��match
		// ����ȡ��
		int pix = (int) Math.floor(widthSize / (double)MapConstant.j);
		// ��Ϊȫ�ֵ�����
		MapConstant.ShowMapPiexl = pix;

		// ��Ϊ������������ȡ����ƫС�ˣ���С��ȥ
		widthSize = pix * MapConstant.j;

		// ���ݻ������صõ�heightSize
		heightSize = pix * MapConstant.i;

		//ȡ��С��Ϊ�ؼ��Ŀ�ߵ���Сֵ
		setMeasuredDimension(widthSize,heightSize);

		Log.e("library", "onMeasure--pix-->" + pix);
	}

	// �滭����
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// i���У�j����
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				int leftj = j * MapConstant.ShowMapPiexl;
				int lefti = i * MapConstant.ShowMapPiexl;
				int righti = j * MapConstant.ShowMapPiexl + MapConstant.ShowMapPiexl;
				int rightj = i * MapConstant.ShowMapPiexl + MapConstant.ShowMapPiexl;
				Rect rc = new Rect(leftj, lefti, righti, rightj);
				// �����ǽ
				if(showmapview[i][j] == MapConstant.BAR){
//					canvas.drawBitmap(MapConstant.smallwallbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallwallbitmap, MapConstant.SmallimgRect, rc, null);
				}
				// �����Ԥѡ��ͼ
				else if(showmapview[i][j] == MapConstant.YU){
					canvas.drawRect(rc, yuxuanpaint);
				}
				// �������λ
				else if(showmapview[i][j] == MapConstant.SEAT){
//					canvas.drawBitmap(MapConstant.smallseatbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallseatbitmap, MapConstant.SmallimgRect, rc, null);
				}
				// ����ǵذ�
				else if(showmapview[i][j] == MapConstant.FIELD){
//					canvas.drawBitmap(MapConstant.smallfloorbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallfloorbitmap, MapConstant.SmallimgRect, rc, null);
				}
				// ��������
				else if(showmapview[i][j] == MapConstant.SHUJIA){
//					canvas.drawBitmap(MapConstant.smallshujiabitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallshujiabitmap, MapConstant.SmallimgRect, rc, null);
				}
				// ����ǲ�������λ
				else if(showmapview[i][j] == MapConstant.SEATNO){
//					canvas.drawBitmap(MapConstant.smallseatnobitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallseatnobitmap, MapConstant.SmallimgRect, rc, null);
				}

			}
		}

//		canvas.drawText("��Ļ��С������"+Constant.ScreenWidth+"�ߣ�"+Constant.ScreenHeight, 50, 50, seatpaint);
//		this.invalidate();
	}
}
