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

// 展示地图的view
public class ShowMapLayerView extends View{
	// 上级
	Context supercon;
	private Paint yuxuanpaint;// 预选位置的paint,不填充并且绿色
	private Paint bitmappaint;// 位图的绘图paint
	// 此view的地图
	private int showmapview[][];

	public ShowMapLayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		supercon = context;
		initData();
		initPaint();
	}

	// 初始化静态数据,mapdata这些
	protected void initData(){
		showmapview = new int[MapConstant.i][MapConstant.j];
//		EditMapDataUtil.setMapDataSpace();
	}
	// 设置地图数组
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
		// 重新更新画布
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
		// 通过width设置height，
		// 因为width是match
		// 向下取整
		int pix = (int) Math.floor(widthSize / (double)MapConstant.j);
		// 设为全局的像素
		MapConstant.ShowMapPiexl = pix;

		// 因为绘制像素向下取整，偏小了，就小下去
		widthSize = pix * MapConstant.j;

		// 根据绘制像素得到heightSize
		heightSize = pix * MapConstant.i;

		//取最小边为控件的宽高的最小值
		setMeasuredDimension(widthSize,heightSize);

		Log.e("library", "onMeasure--pix-->" + pix);
	}

	// 绘画函数
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// i是行，j是列
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				int leftj = j * MapConstant.ShowMapPiexl;
				int lefti = i * MapConstant.ShowMapPiexl;
				int righti = j * MapConstant.ShowMapPiexl + MapConstant.ShowMapPiexl;
				int rightj = i * MapConstant.ShowMapPiexl + MapConstant.ShowMapPiexl;
				Rect rc = new Rect(leftj, lefti, righti, rightj);
				// 如果是墙
				if(showmapview[i][j] == MapConstant.BAR){
//					canvas.drawBitmap(MapConstant.smallwallbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallwallbitmap, MapConstant.SmallimgRect, rc, null);
				}
				// 如果是预选地图
				else if(showmapview[i][j] == MapConstant.YU){
					canvas.drawRect(rc, yuxuanpaint);
				}
				// 如果是座位
				else if(showmapview[i][j] == MapConstant.SEAT){
//					canvas.drawBitmap(MapConstant.smallseatbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallseatbitmap, MapConstant.SmallimgRect, rc, null);
				}
				// 如果是地板
				else if(showmapview[i][j] == MapConstant.FIELD){
//					canvas.drawBitmap(MapConstant.smallfloorbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallfloorbitmap, MapConstant.SmallimgRect, rc, null);
				}
				// 如果是书架
				else if(showmapview[i][j] == MapConstant.SHUJIA){
//					canvas.drawBitmap(MapConstant.smallshujiabitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallshujiabitmap, MapConstant.SmallimgRect, rc, null);
				}
				// 如果是不可用座位
				else if(showmapview[i][j] == MapConstant.SEATNO){
//					canvas.drawBitmap(MapConstant.smallseatnobitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.smallseatnobitmap, MapConstant.SmallimgRect, rc, null);
				}

			}
		}

//		canvas.drawText("屏幕大小：宽：，"+Constant.ScreenWidth+"高："+Constant.ScreenHeight, 50, 50, seatpaint);
//		this.invalidate();
	}
}
