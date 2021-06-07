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

// 选择座位的地图的view,还是公用原始地图与当前地图，不过能变得东西少了，只有座位
public class ChooseSeatMapLayerView extends View{
	// 上级
	Context supercon;
	// 很重要的
	ChooseSeatMapActivity chooseactivity;
	
	private Paint yuxuanpaint;// 预选位置的paint,不填充并且绿色
	private Paint myseatpaint;// 我选择的座位
	private Paint beichooseseatpaint;// 被选择的座位
	// 地图范围
//	private int mapdata[][];
	public ChooseSeatMapLayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		supercon = context;
		// 需要强制转换
		chooseactivity = (ChooseSeatMapActivity)context;
		// 直接从maplist获取地图数据
		initData();
		
		initPaint();
		Log.i("library", "ChooseSeatMapLayerView构造函数");
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
		// 重新更新画布
		super.invalidate();
	}
	// 触碰函数
	/*直接变为预选*/
	int lasti,lastj;
	int mychoosei, mychoosej;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 让这个位置 /40
		mychoosej = (int) (event.getX() / MapConstant.MapPiexl);
		mychoosei = (int) (event.getY() / MapConstant.MapPiexl);
		switch(event.getAction()){
			// 手指按下
			case  MotionEvent.ACTION_DOWN:
				
				break;
			// 手指松开, 选择的位置预选
			case MotionEvent.ACTION_UP:
				// 1.先恢复之前选择的预选位置。，这个只需指定是哪一个ij就行
				if(!EditMapDataUtil.isOverLine(lasti, lastj)){
					EditMapDataUtil.recoverDataFromYuanByIJ(lasti, lastj);
				}
				// 2.当前位置变为预选，前提不是边界
				if(!EditMapDataUtil.isOverLine(mychoosei, mychoosej)){
					EditMapDataUtil.setIjBeYu(mychoosei, mychoosej);
					// 得放这里
					lasti = mychoosei;
					lastj = mychoosej;
				}
				// 更新绘图
				invalidate();
				break;
		}
//		return super.onTouchEvent(event);
		return true;
	}
	// 设为预选位置为选座，需要判断是否座位
	// 1.判断是否座位
	// 2.判断是否是不可用座位
	// 2.判断这个座位是否被其它人占了，是不能选
	// 3.判断这个时间段，这天，我是否有其它座位，询问是否取消之前座位。是：修改原来座位，如果是同一层更新view
	// 返回bean 为了通信
	public void MapDatayuToMySeat(){
		if(!EditMapDataUtil.isOverLine(mychoosei, mychoosej)){
			// 1
			int zhi = EditMapDataUtil.yuanMapData[mychoosei][mychoosej];
			//2
			if(zhi == MapConstant.SEATNO){
				Constant.commonToast(supercon, "此座位不可用");
				return;
			}
			if(zhi == MapConstant.SEAT){
				// 2
				boolean isoccur = thiseatIsOccur(mychoosei, mychoosej);
				if(!isoccur){
					// 3 
					if(!isThisTimeIhadSeat(mychoosei, mychoosej)){
//						SeatInfoBean seat = new SeatInfoBean();
//						// 添加到数据库中，当前i,j，fid，sday，stime，等
//						seat.setSnumber(mychoosei+""+mychoosej);
//						seat.setSx(mychoosej);
//						seat.setSy(mychoosei);
						// 将预选的位置数组恢复
						if(!EditMapDataUtil.isOverLine(mychoosei, mychoosej)){
							EditMapDataUtil.recoverDataFromYuanByIJ(mychoosei, mychoosej);
						}
						chooseactivity.insertOneSeatToSqiteAndList(mychoosei, mychoosej);
					}
				}else{
					Constant.commonToast(supercon, "此座位已被占用，请选择其它");
				}
			}else{
				Constant.commonToast(supercon, "请选择座位");
			}
		}else{
			Constant.commonToast(supercon, "请先选择座位");
		}
//		invalidate();
	}
	// 判断这个座位是否被占
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
	// 判断我这个时间段是否有了座位
	public boolean isThisTimeIhadSeat(int i, int j){
		// 一般只有一条
		for(int z = 0; z < MapConstant.myseatlist.size(); z++){
			SeatInfoBean oldseat = MapConstant.myseatlist.get(z);
			// 弹框
			SeatInfoBean newseat = new SeatInfoBean();
			newseat.setSy(i);
			newseat.setSx(j);
			newseat.setSnumber(i+""+j);
			// 放入新楼层的fid
			newseat.setFid(MapConstant.getCurLayerZhujian());
			
			chooseactivity.generateHadSeatDialog(oldseat, newseat);
//			chooseactivity.generateHadSeatDialog(seat.getSid(), seat.getSy(), seat.getSx(), MapConstant.getLayerNumberByFid(seat.getFid()), i, j, MapConstant.getLayerNumber());
			return true;
		}
		return false;
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
		// 绘制被学生选座的地图,循环
		// 直接根据ij将指定位置绘制, 需要判断楼层是否对应
		Paint curp = new Paint();
		for(int z = 0; z < MapConstant.curallseatlist.size(); z++){
			SeatInfoBean seat = MapConstant.curallseatlist.get(z);
			if(seat.getFid() == MapConstant.getCurLayerZhujian()){
				// 如果这个座位是我订的，换Paint
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
		
//		canvas.drawText("屏幕大小：宽：，"+Constant.ScreenWidth+"高："+Constant.ScreenHeight, 50, 50, seatpaint);
//		this.invalidate();
	}
}
