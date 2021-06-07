package com.example.libraryselection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.SeatInfoBean;
import com.example.libraryselection.dao.SeatInfoDao;
import com.example.libraryselection.dialog.GenerateDialog;
import com.example.libraryselection.mappack.ChooseSeatMapLayerView;
import com.example.libraryselection.mappack.EditMapDataUtil;
import com.example.libraryselection.resource.Constant;
import com.example.libraryselection.resource.MapConstant;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


// 修改地图数据activity
public class ChooseSeatMapActivity extends AppCompatActivity implements OnClickListener,OnItemSelectedListener{
	// 数据库相关
	private SeatInfoDao seatdao;
	private SQLiteDatabase db;
	// 地图view
	ChooseSeatMapLayerView chooseseatmapview;
	
	// 组件
	private TextView showfloorcentv;// 展示textview
	private Button quitbt;// 退出
	private Button lastlayerbt;//上一层
	private Button nextlayerbt;// 下一层
	private Button chooseseatbt;// 选择此座位
	private Spinner daysping;// 日期
	private Spinner timesping;// 时间段
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_chooseseatmap);
		// 初始化数据库
		initDaoAndDb();
		
		findView();// 找view
		setClickLis();// 监听
		
		// 给组件设置显示
		setTextView();
//		Log.i("library", "ChooseSeatMapActivity构造函数");
		
		setSpinnerListVal();
		setSpinnerApdater();
		
		// 查找出数据库默认当前日期当前时间段的数据，需要全局 不用，监听会自动
//		selSeatByDayAndTime();
	}
	
	// 寻找控件
	protected void findView(){
		chooseseatmapview = (ChooseSeatMapLayerView)findViewById(R.id.chooseseatmap_view);
		
		// 组件
		showfloorcentv = (TextView)findViewById(R.id.chooseseatmap_tx_showfloorc);
		quitbt = (Button) findViewById(R.id.chooseseatmap_bt_quit);
		lastlayerbt = (Button) findViewById(R.id.chooseseatmap_bt_lastlayer);
		nextlayerbt = (Button) findViewById(R.id.chooseseatmap_bt_nextlayer);
		chooseseatbt = (Button) findViewById(R.id.chooseseatmap_bt_chooseseat);
		
		daysping = (Spinner) findViewById(R.id.chooseseatmap_sp_day);
		timesping = (Spinner) findViewById(R.id.chooseseatmap_sp_time);
	}
	// 设置监听
	protected void setClickLis(){
		chooseseatbt.setOnClickListener(this);
		quitbt.setOnClickListener(this);
		lastlayerbt.setOnClickListener(this);
		nextlayerbt.setOnClickListener(this);
	}
	// 设置显示第几层
	protected void setTextView() {
		showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
	}
	
	// 
	List<String> daylist;
	List<String> timelist;
	ArrayAdapter<String> dayad;
    ArrayAdapter<String> timead;
    
    // 当前时间，明天时间日期的字符串
    String curdaystr;
    String tomdaystr;
    
    // 查询数据库需要的参数
    String selday;// 日期
    int seltime;// 时间段
    
	// 设置spinner值，今天明天，上午，下午，晚上
	protected void setSpinnerListVal() {
		daylist = new ArrayList<String>();
		timelist = new ArrayList<String>();
		
		// 今天明天日期
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		curdaystr = formatter.format(calendar.getTime());// 今天
		// 明天
		calendar.setTime(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		tomdaystr = formatter.format(calendar.getTime());// 明天
		// 加上前缀
		daylist.add(Constant.Today+curdaystr);
		daylist.add(Constant.Tomorrow+tomdaystr);
		
		selday = curdaystr;// 默认当前
		
		// 生成今天的time
		geneListForTodayTime();
	}
	// 生成今天的上午，下午，晚上
	public void geneListForTodayTime(){
		timelist.clear();
		// timelist
		// 需要根据当前时间，来判断.明天的日期，可以选任何一个位置，需要注意
		// 1.当前时间在9点前才可以选 sw
		// 2.当前时间在13：30前可以选 xw
		// 3.当前时间在18：00前可以选ws
		SimpleDateFormat hourformatter = new SimpleDateFormat("HHMM");
		Calendar curcal = Calendar.getInstance();
		int curhour = Integer.valueOf(hourformatter.format(curcal.getTime()));
//		Toast.makeText(this, hourformatter.format(curcal.getTime()), Toast.LENGTH_SHORT).show();
		// 第一次添加的，默认3 无
		int fistaddtime = 3;
		if(curhour <= Constant.ISWSTART){
			timelist.add(Constant.SW);
			fistaddtime = 0;
		}
		if(curhour <= Constant.IXWSTART){
			timelist.add(Constant.XW);
			if(fistaddtime == 3){
				fistaddtime = 1;
			}
		} 
		if(curhour <= Constant.IWSSTART){
			timelist.add(Constant.WS);
			if(fistaddtime == 3){
				fistaddtime = 2;
			}
		} 
		if(curhour > Constant.IWSSTART){
			// 没有选择
			timelist.add(Constant.NOSEL);
			if(fistaddtime == 3){
				fistaddtime = 3;
			}
		}
		seltime = fistaddtime;
		// 更新
		if(timead != null){
			timead.notifyDataSetChanged();
		}
	}
	// 生成明天的上午，下午，网上
	public void geneListForTomTime(){
		timelist.clear();
		timelist.add(Constant.SW);
		timelist.add(Constant.XW);
		timelist.add(Constant.WS);
		// 查询的时间变成0
		seltime = 0;
		
		// 更新
		if(timead != null){
			timead.notifyDataSetChanged();
		}
	}
	// 适配spinner
	protected void setSpinnerApdater() {
		dayad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,daylist);
		timead = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,timelist);
		
		daysping.setAdapter(dayad);
		timesping.setAdapter(timead);
		
		// 设置监听，如果改变了，需要重新查找过选座的数据list
		daysping.setOnItemSelectedListener(this);
		timesping.setOnItemSelectedListener(this);
	}
	@Override
	public void onClick(View ve) {
		int id = ve.getId();
		switch (id) {
			case R.id.chooseseatmap_bt_chooseseat:
				// 设为座位
				// 要当前时间点
				if(seltime != Constant.TIMEDUAN.length - 1){
					chooseseatmapview.MapDatayuToMySeat();
				}else{
					Toast.makeText(this, "当前时间段不可选座位", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.chooseseatmap_bt_quit:
				// 退出
				GenerateDialog.genEditMapQuitNormalDialog(this, "确定不选座位了吗？");
				break;
			case R.id.chooseseatmap_bt_lastlayer:
				// 上一层
				if(MapConstant.curmappos > 0){
					MapConstant.curmappos--;
					
					// 重新获取座位list
					selSeatByDayAndTime();
					
					// 重新赋地图值，并且刷新
					EditMapDataUtil.getMapDataFromMapList();
					chooseseatmapview.invalidate();
					// 标题变化
					showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
					
				}else{
					Toast.makeText(this, "没有上一层", Toast.LENGTH_LONG).show();
				}
				break;	
			case R.id.chooseseatmap_bt_nextlayer:
				// 下一层
				// 下一层, 需要减1 切记
				if(MapConstant.curmappos < MapConstant.showandeditmaplist.size() - 1){
					MapConstant.curmappos++;
					// 重新获取座位list
					selSeatByDayAndTime();
					
					// 重新赋地图值，并且刷新
					EditMapDataUtil.getMapDataFromMapList();
					// 刷新
					chooseseatmapview.invalidate();
					// 标题变化
					showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
				}else{
					Toast.makeText(this, "这是最后一层了", Toast.LENGTH_LONG).show();
				}
				break;
		}
	}
	// 数据库相关初始化
	protected void initDaoAndDb() {
		db = MyApplication.lihelper.getReadableDatabase();
		seatdao = new SeatInfoDao();
	}
	
	// 查询当前日期当前时间段的座位
	protected void selSeatByDayAndTime() {
		// 如果不是seltime时间段不是无才去查找
		if(seltime != Constant.TIMEDUAN.length - 1){
			// 获取当前的楼层id主键
			int fid = MapConstant.getCurLayerZhujian();
			int uid = MyApplication.user.getUid();
			// 当前时间点这个楼层选择的座位
			MapConstant.curallseatlist = seatdao.readSeatInfoByTime(db, selday, seltime, fid);
			// 当前时间点我选择的座位
			MapConstant.myseatlist = seatdao.selMySeatBySpeTime(db, selday, seltime, uid);
			
			// 给更新地图
			chooseseatmapview.invalidate();
		}
	}
	// 添加到数据库中
	public void insertOneSeatToSqiteAndList(int mychoosei, int mychoosej){// 选择的座位
		SeatInfoBean seat = new SeatInfoBean();
//		Toast.makeText(this, "insertOneSeatToSqiteAndList", Toast.LENGTH_LONG).show();
		seat.setSnumber(mychoosei+""+mychoosej);
		seat.setSx(mychoosej);
		seat.setSy(mychoosei);
		seat.setUid(MyApplication.user.getUid());
		seat.setFid(MapConstant.getCurLayerZhujian());
		seat.setSday(selday);
		seat.setStime(seltime);
		int sid = seatdao.insertSeatReturnId(db, seat);
		// 当添加了一个进数据库，也要添加到列表中吗？，要吧，添加到当前选择的节点
		if(sid > 0){
			seat.setSid(sid);
			// 添加到当前楼层，当前点的座位
			MapConstant.curallseatlist.add(seat);
			
			// 添加到当前段的座位
			MapConstant.myseatlist.add(seat);
			Toast.makeText(this, "选座成功", Toast.LENGTH_SHORT).show();
			
			// 刷新地图
			chooseseatmapview.invalidate();
		}
	}
	// 更换座位，修改表
	// 先弹框 hi,hj,老座位，ni,nj新座位
	public void generateHadSeatDialog(SeatInfoBean oldseat, SeatInfoBean newseat){
		String msg = "你在"+curdaystr+" - "+Constant.TIMEDUAN[seltime]+"时间段在：\n"
					+"已有座位:"+Constant.maplayerString[MapConstant.getLayerNumberByFid(oldseat.getFid())]+","+oldseat.getSy()+""+oldseat.getSx()+"。\n"
					+"是否更换为新座位："+Constant.maplayerString[MapConstant.getLayerNumber()]
					+","+newseat.getSy()+""+newseat.getSx();
		GenerateDialog.genHadSeatDialog(this, msg, oldseat, newseat);
	}
	// 修改表,数据库中和我的座位list中。需要主键id。sid主键，ni
	public void updateOneSeatToSqliteAndlist(SeatInfoBean oldseat, SeatInfoBean newseat){
		//  根据old的sid修改新snumber，si，sj，fid，
		int ct = seatdao.updateSeatById(db, newseat, oldseat.getSid());
		if(ct > 0){
			// 修改我的座位list，当前楼层的座位list。
			// 当前座位的list需要匹配sid修改
			// 我的座位list，直接清除再添加这个 。这两个都麻烦
			// 直接重新查询把，更简单
			// 将预选的位置数组恢复
			if(!EditMapDataUtil.isOverLine(newseat.getSy(), newseat.getSx())){
				EditMapDataUtil.recoverDataFromYuanByIJ(newseat.getSy(), newseat.getSx());
			}
			selSeatByDayAndTime();
			Toast.makeText(this, "修改座位成功", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "修改座位失败", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
		// TODO Auto-generated method stub
		// 看选择的项
		int id = parent.getId();
		switch (id) {
		case R.id.chooseseatmap_sp_day:
//			selday = dayad.getItem(i).toString();	
//			Toast.makeText(this, "选择了："+i, Toast.LENGTH_SHORT).show();
			// 今天的时间段
			if(i == 0){
				selday = curdaystr;
				geneListForTodayTime();
			}
			// 明天
			if(i == 1){
				selday = tomdaystr;
				geneListForTomTime();
			}
			Log.i("library", "选择："+selday);
			break;
		case R.id.chooseseatmap_sp_time:
			String stime = timead.getItem(i).toString();
			for(int m = 0; m < Constant.TIMEDUAN.length; m++){
				if(stime == Constant.TIMEDUAN[m]){
					seltime = m;
					break;
				}
			}
			Log.i("library", "选择："+stime+" ,"+seltime);
			break;
		default:
			break;
		}
		// 重新查找
		selSeatByDayAndTime();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	long exitTime = 0;
	// 响应按返回键 不退出，应该退出左滑菜单
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// 没有显式 两次返回键退出
		if(System.currentTimeMillis() - exitTime > 2000){// 间隔大于两秒 提示 连按两次
			Toast.makeText(this,"再按一次退出选座", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		}else{
			// TODO Auto-generated method stub
			super.onBackPressed();
		}
	}
}
