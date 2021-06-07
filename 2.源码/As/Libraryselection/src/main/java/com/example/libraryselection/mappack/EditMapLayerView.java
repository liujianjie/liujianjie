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

// 修改地图的view
public class EditMapLayerView extends View{
	// 上级
	Context supercon;
	private Paint yuxuanpaint;// 预选位置的paint,不填充并且绿色
	private Paint bitmappaint;// 位图的绘图paint
	// 地图范围
//	private int mapdata[][];
	public EditMapLayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		supercon = context;
//		initData();
		initPaint();
		Log.i("library", "EditMapLayerView构造函数");
	}
	
	// 初始化静态数据,mapdata这些
	public void initDataFromMapList(){
//		mapdata = new int[MapConstant.i][MapConstant.j];
		// 从公共list读取地图信息
		EditMapDataUtil.getMapDataFromMapList();
		invalidate();
	}
	public void initDataFromMapMoudleList(){
//		mapdata = new int[MapConstant.i][MapConstant.j];
		// 从公共mapmoudlelist读取地图信息
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
		// 重新更新画布
		super.invalidate();
	}
	// 触碰函数
	/**
	 1.让之前的绘图的预选地方绘制为原来的值
	 2.让数组去保存
	 **/
	// 是否恢复被预选的位置区域
	boolean isrecoveryuanxianyuxuan;
	int start[] = new int[2];// 开始点
	int end[] = new int[2];// 结束点
	int mid[] = new int[2];// 中间点
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 让这个位置 /40
		int j = (int) (event.getX() / MapConstant.MapPiexl);
		int i = (int) (event.getY() / MapConstant.MapPiexl);
		switch(event.getAction()){
			// 手指按下
			case  MotionEvent.ACTION_DOWN:
				// 让这个地方的ij，变为预选
//				Toast.makeText(supercon, "按下", Toast.LENGTH_SHORT).show();
				// 变为预选，前提不是边界
				if(!EditMapDataUtil.isOverLine(i, j)){
					start[0] = j;// 0是列
					start[1] = i;
//					EditMapDataUtil.setIjBeYu(EditMapDataUtil.currMapData, i, j);
				}
				break;
			// 手指移动， 持续绘画
			case MotionEvent.ACTION_MOVE:
//				Toast.makeText(supercon, "移动", Toast.LENGTH_SHORT).show();
				if(!EditMapDataUtil.isOverLine(i, j)){
					end[0] = j;
					end[1] = i;
					
					// 先要更新持续绘画中的残留预选位置，当行比上一个的位置要小的时候 就要重新恢复地图原值 再绘画当前
					if(isrecoveryuanxianyuxuan){
						EditMapDataUtil.setStillDraw(start, mid, 2);//
					}
					isrecoveryuanxianyuxuan = true;// 一直要更新，知道抬起是false
					
					// 再持续更新
					EditMapDataUtil.setStillDraw(start, end, 1);
					// 更新绘图
					invalidate();
					
					// 保存上一个右下角的点
					mid[0] = end[0];
					mid[1] = end[1];
				}
				break;
			// 手指松开, 选择的位置预选
			case MotionEvent.ACTION_UP:
//				Toast.makeText(supercon, "抬起", Toast.LENGTH_SHORT).show();
				// 1.先恢复之前选择的。
				EditMapDataUtil.recoverMapDataFromData();
				if(!EditMapDataUtil.isOverLine(i, j)){
					end[0] = j;
					end[1] = i;
					// 2.确保拖动时候预选多个还是保持绘画
					EditMapDataUtil.setStillDraw(start, end, 3);
					// 更新绘图
					invalidate();
					
					// 初始值
					isrecoveryuanxianyuxuan = false;
				}
				break;
		}
//		return super.onTouchEvent(event);
		return true;
	}
	// 设为预选位置为座位，障碍物，空地
	public void MapDatayuToThing(int flag){
		// 在变之前，stack保存原始地图
		// 应该判断是否有变化
		// 先放入，没有改变就弹出
		EditMapDataUtil.hismapsta.add(EditMapDataUtil.getYuanMapDataofDeep());
		boolean ischange = false;
		// 循环
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				if(EditMapDataUtil.currMapData[i][j] == MapConstant.YU){
					EditMapDataUtil.setIjBeThing(i, j, flag);
					ischange = true;
				}
			}
		}
		// 没有改变
		if(!ischange){
			// 出栈
			EditMapDataUtil.hismapsta.pop();
		}
		// 更新
		invalidate();
	}
	// 测量
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
		// 通过height设置width，
		// 因为height是match ，height减去100， 要居中
		heightSize = heightSize - 100;
		// 向下取整绘制像素大小
		int pix = (int) Math.floor(heightSize / (double)MapConstant.i);
		// 设为全局的像素
		MapConstant.MapPiexl = pix;

		Log.e("library", "onMeasure--pix-->" + pix);
//        Toast.makeText(supercon, "pix:"+pix, Toast.LENGTH_SHORT).show();

		// 因为绘制像素向下取整，偏小了，就小下去
		heightSize = pix * MapConstant.i;

		// 根据绘制像素得到width
		widthSize = pix * MapConstant.j;

		//取最小边为控件的宽高的最小值
		setMeasuredDimension(widthSize,heightSize);

	}
	// 绘画函数
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// i是行，j是列
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				int leftj = j * MapConstant.MapPiexl;
				int lefti = i * MapConstant.MapPiexl;
				int righti = j * MapConstant.MapPiexl + MapConstant.MapPiexl;
				int rightj = i * MapConstant.MapPiexl + MapConstant.MapPiexl;
				// 实时目标位置要计算的。
				Rect rc = new Rect(leftj, lefti, righti, rightj);
				// 如果是墙
				if(EditMapDataUtil.currMapData[i][j] == MapConstant.BAR){
//					canvas.drawBitmap(MapConstant.bigwallbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigwallbitmap, MapConstant.BigimgRect, rc, null);
				}
				// 如果是预选地图
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.YU){
					canvas.drawRect(rc, yuxuanpaint);
				}
				// 如果是座位
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.SEAT){
					// 确定圆心位置要加上半径
//					canvas.drawBitmap(MapConstant.bigseatbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigseatbitmap, MapConstant.BigimgRect, rc, null);
				}
				// 如果是地板
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.FIELD){
//					canvas.drawBitmap(MapConstant.bigfloorbitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigfloorbitmap, MapConstant.BigimgRect, rc, null);
				}
				// 如果是书架
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.SHUJIA){
//					canvas.drawBitmap(MapConstant.bigshujiabitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigshujiabitmap, MapConstant.BigimgRect, rc, null);
				}
				// 如果是不可用座位
				else if(EditMapDataUtil.currMapData[i][j] == MapConstant.SEATNO){
//					canvas.drawBitmap(MapConstant.bigseatnobitmap, leftj, lefti, null);
					canvas.drawBitmap(MapConstant.bigseatnobitmap, MapConstant.BigimgRect, rc, null);
				}
			}
		}
		
//		canvas.drawText("屏幕大小：宽：，"+Constant.ScreenWidth+"高："+Constant.ScreenHeight, 50, 50, seatpaint);
//		this.invalidate();
	}
}

/*
TestCode
		super.onDraw(canvas);
		// 需要偏移量
//		canvas.translate((Constant.ScreenWidth - MapConstant.j * MapConstant.MapPiexl) / 2, (Constant.ScreenHeight - MapConstant.i * MapConstant.MapPiexl) / 2);
//		canvas.translate((Constant.ScreenHeight - MapConstant.i * MapConstant.MapPiexl) / 2, (Constant.ScreenWidth - MapConstant.j * MapConstant.MapPiexl) / 2);
//		canvas.translate(80, 80);
		// i是行，j是列
		for(int i = 0; i < MapConstant.i; i++){
			for(int j = 0; j < MapConstant.j; j++){
				Rect rc = new Rect(j * MapConstant.MapPiexl, i * MapConstant.MapPiexl, j * MapConstant.MapPiexl + MapConstant.MapPiexl, i * MapConstant.MapPiexl + MapConstant.MapPiexl);
				if(i == 0 || i == mapdata.length - 1 || j == 0 || j == mapdata[i].length - 1){
					canvas.drawRect(rc, barpaint);
				}
			}
		}
		
		canvas.drawText("屏幕大小：宽：，"+Constant.ScreenWidth+"高："+Constant.ScreenHeight, 50, 50, seatpaint);
//		this.invalidate();
 */