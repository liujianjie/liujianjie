package com.example.libraryselection;

import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.bean.MapMoudleBean;
import com.example.libraryselection.dao.MapDao;
import com.example.libraryselection.dao.MapMoudleDao;
import com.example.libraryselection.dialog.GenerateDialog;
import com.example.libraryselection.mappack.EditMapDataUtil;
import com.example.libraryselection.mappack.EditMapLayerView;
import com.example.libraryselection.resource.Constant;
import com.example.libraryselection.resource.MapConstant;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


// 修改地图数据activity
public class EditFloorMapActivity extends AppCompatActivity implements OnClickListener{
	// 数据库相关
	private MapDao mapdao;
	private SQLiteDatabase db;
	private MapMoudleDao mapmoudledao;
	// 地图view
	EditMapLayerView editfloormapview;
	// 右边按钮
	LinearLayout editfloormaplin;
	
	// 组件
	private TextView showfloorcentv;// 展示textview
//	private Button savebt;// 保存或者修改
	private Button savemoudlebt;// 保存到模板
	private Button quitbt;// 退出
	private Button lastlayerbt;//上一层
	private Button nextlayerbt;// 下一层
	private Button setseatbt, setbarbt ,setfieldbt;// 设为座位，障碍，空地
	private Button setshujiabt;
	private Button setseatnot;//不可用
	private Button recoverbt;// 恢复上一层地图，用栈实现
	// 模板的组件
	private Button geneLayerbt;
	private Button lastmouldebt, nextmouldebt;// 上下模板
	private Button geneyqseatbt;// 生成疫情座位
	private Button cancleseatbt;// 取消疫情座位
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_editfoormap);
		initDaoAndDb();
		
		findView();// 找view
		setClickLis();// 监听
		
		getIntentForSetting();
		
		// 当预览地图过来的时候要设置的
		if(flag == 1){
			setViewVisibleForMap();
			// 更换地图view的data
			editfloormapview.initDataFromMapList();
		}else if(flag == 2){
			setViewVisibleForMapMould();
			// 更换地图view的data
			editfloormapview.initDataFromMapMoudleList();
		}
		
		Log.i("library", "EditFloorMapActivity构造函数");
	}
	int flag;// 标识是哪里过来的，1是预览地图，2是模板过来的
	// 获取intent为了设置
	public void getIntentForSetting(){
		Intent in = getIntent();
		flag = in.getIntExtra("flag", 1);
	}
	
	// 寻找控件
	protected void findView(){
		editfloormapview = (EditMapLayerView)findViewById(R.id.edfloormap_view);
		editfloormaplin = (LinearLayout)findViewById(R.id.edfloormap_linear);
		
		// 组件
		showfloorcentv = (TextView)findViewById(R.id.edfloormap_tx_showfloorc);
		setseatbt = (Button) findViewById(R.id.edfloormap_bt1_setseat);
		setbarbt = (Button) findViewById(R.id.edfloormap_bt2_setbar);
		setfieldbt = (Button) findViewById(R.id.edfloormap_bt3_setfield);
//		savebt = (Button) findViewById(R.id.edfloormap_bt4_save);
		quitbt = (Button) findViewById(R.id.edfloormap_bt6_quit);
		savemoudlebt = (Button) findViewById(R.id.edfloormap_bt5_savemold);
		lastlayerbt = (Button) findViewById(R.id.edfloormap_bt7_lastlayer);
		nextlayerbt = (Button) findViewById(R.id.edfloormap_bt8_nextlayer);
		
		setshujiabt= (Button) findViewById(R.id.edfloormap_bt_setshujia);
		
		setseatnot = (Button) findViewById(R.id.edfloormap_bt_setseatno);
		
		recoverbt = (Button) findViewById(R.id.edfloormap_bt_editmap_recover);
		geneyqseatbt = (Button) findViewById(R.id.edfloormap_bt_editmap_geneyqseat);
		
		cancleseatbt = (Button) findViewById(R.id.edfloormap_bt_setcancleseatno);
		
		geneLayerbt = (Button) findViewById(R.id.edfloormap_bt9_geneMapLayer);
		lastmouldebt = (Button) findViewById(R.id.edfloormap_bt10_lastmoudle);
		nextmouldebt = (Button) findViewById(R.id.edfloormap_bt11_lastmoudle);
	}
	// 设置监听
	protected void setClickLis(){
		setseatbt.setOnClickListener(this);
		setbarbt.setOnClickListener(this);
		setfieldbt.setOnClickListener(this);
//		savebt.setOnClickListener(this);
		quitbt.setOnClickListener(this);
		savemoudlebt.setOnClickListener(this);
		lastlayerbt.setOnClickListener(this);
		nextlayerbt.setOnClickListener(this);
		setshujiabt.setOnClickListener(this);
		setseatnot.setOnClickListener(this);
		recoverbt.setOnClickListener(this);
		geneyqseatbt.setOnClickListener(this);;
		cancleseatbt.setOnClickListener(this);
		// 模板的
		geneLayerbt.setOnClickListener(this);
		lastmouldebt.setOnClickListener(this);
		nextmouldebt.setOnClickListener(this);
	}
	// 当从预览地图过来时候的代码
	// 设置地图数组和显示第几层
	protected void setEditMapViewDataAndTextView() {
		showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
		// 设置数组
		// 从公共list读取地图信息,在它构造方法里就行
	}
	// 从预览地图过来的时候更换控件显示, 默认就是阅览地图的不用
	public void setViewVisibleForMap(){
		// 昵称
		setEditMapViewDataAndTextView();
	}
	@Override
	public void onClick(View ve) {
		int id = ve.getId();
		switch (id) {
		case R.id.edfloormap_bt1_setseat:
			// 设为座位
			editfloormapview.MapDatayuToThing(1);
			break;
		case R.id.edfloormap_bt_setshujia:
			// 书架
			editfloormapview.MapDatayuToThing(4);
			break;
			// 不可用座位
		case R.id.edfloormap_bt_setseatno:
			// 书架
			editfloormapview.MapDatayuToThing(3);
			break;
		case R.id.edfloormap_bt2_setbar:
			// 设为障碍
			editfloormapview.MapDatayuToThing(2);
			break;
		case R.id.edfloormap_bt3_setfield:
			// 设为空地
			editfloormapview.MapDatayuToThing(0);
			break;
		case R.id.edfloormap_bt_editmap_geneyqseat:
			// 生成疫情座位
			geneYqSeat();
			break;
		case R.id.edfloormap_bt_setcancleseatno:
			// 取消不可用座位
			cancleYqSeat();
			break;
		case R.id.edfloormap_bt_editmap_recover:
			// 恢复
			recoverLastMaplayer();
			break;
//		case R.id.edfloormap_bt4_save:
			// 保存、修改 地图值
			// 需要判断是保存地图值还是模板值
//			saveMapDataForMapAndMapMoudle();
//			break;
		case R.id.edfloormap_bt6_quit:
			// 退出, 弹框是否继续
//			GenerateDialog.genEditMapQuitNormalDialog(this, "退出后不自动保存地图数据，是否继续?");
			// 先保存
			saveMapDataForMapAndMapMoudle();
			
			// 退出
			finish();
			break;
		case R.id.edfloormap_bt5_savemold:
			// 保存模板
			GenerateDialog.genEditMapInputDialog(this);
			break;	
		case R.id.edfloormap_bt7_lastlayer:
			// 上一层
			if(MapConstant.curmappos > 0){
				// 先保存
				saveMapDataForMapAndMapMoudle();
				
				MapConstant.curmappos--;
				// 重新赋地图值，并且刷新
				EditMapDataUtil.getMapDataFromMapList();
//				EditMapDataUtil.setMapDataFromData(MapConstant.showandeditmaplist.get(MapConstant.curmappos).getMapdata());
				editfloormapview.invalidate();
				// 标题变化
				showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
			}else{
				Toast.makeText(this, "没有上一层", Toast.LENGTH_LONG).show();
			}
			break;	
		case R.id.edfloormap_bt8_nextlayer:
			// 下一层, 需要减1 切记
			if(MapConstant.curmappos < MapConstant.showandeditmaplist.size() - 1){
				// 先保存
				saveMapDataForMapAndMapMoudle();
				
				MapConstant.curmappos++;
				// 重新赋地图值，并且刷新
				EditMapDataUtil.getMapDataFromMapList();
//				EditMapDataUtil.setMapDataFromData(MapConstant.showandeditmaplist.get(MapConstant.curmappos).getMapdata());
				// 刷新
				editfloormapview.invalidate();
				// 标题变化
				showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
			}else{
				Toast.makeText(this, "这是最后一层了", Toast.LENGTH_LONG).show();
			}
			break;		
			
		//模板相关控件
		case R.id.edfloormap_bt9_geneMapLayer:
			// 生成楼层，相当于添加一个楼层, 需要往list和数据库中添加一个，先添加给数据库，再给list
			// 给提示
			isgeneLayerDialog();
			break;
		case R.id.edfloormap_bt10_lastmoudle:
			// 上一个模板
			if(MapConstant.curmapmoudlepos > 0){
				saveMapDataForMapAndMapMoudle();
				
				MapConstant.curmapmoudlepos--;
				// 重新赋地图值，并且刷新
				EditMapDataUtil.getMapDataFromMapMoudleList();
				editfloormapview.invalidate();
				// 标题变化
				showfloorcentv.setText(MapConstant.getMoudleName());
			}else{
				Toast.makeText(this, "没有上一个模板", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.edfloormap_bt11_lastmoudle:
			// 下一个模板  需要减1 切记
			if(MapConstant.curmapmoudlepos < MapConstant.showeditmapmoudlelist.size() - 1){
				saveMapDataForMapAndMapMoudle();
				
				MapConstant.curmapmoudlepos++;
				// 重新赋地图值，并且刷新
				EditMapDataUtil.getMapDataFromMapMoudleList();
				editfloormapview.invalidate();
				// 标题变化
				showfloorcentv.setText(MapConstant.getMoudleName());
			}else{
				Toast.makeText(this, "这是最后一个模板了", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}
	// 保存地图数据为地图和地图模板
	protected void saveMapDataForMapAndMapMoudle(){
		// 需要把栈情空，不然问题
		EditMapDataUtil.hismapsta.clear();
		if(flag == 1){
			// 地图值，从当前获取，但是要去除预选位置。
			// 1.修改map表 2.修改公共maplist的mapdata 3.更新showmapfrag的recycleview,打开frag自动更新
			updateMapBeanToSqliteAndList();
		}else if(flag == 2){
			// 地图值，从当前获取，但是要去除预选位置。不用，直接获取原始地图
			// 1.修改mamoudlep表 2.修改公共mapmoudlelist的mapdata 3.更新mapmoudlefrag的recycleview,打开frag自动更新
			updateMapMoudleBeanToSqliteAndList();
		}
	}
	// 数据库相关初始化
	protected void initDaoAndDb() {
		mapdao = new MapDao();
		db = MyApplication.lihelper.getReadableDatabase();
		
		mapmoudledao = new MapMoudleDao();
	}
	// 修改map表
	protected void updateMapBeanToSqliteAndList() {
		// 获取第几层地图
		MapBean map = MapConstant.showandeditmaplist.get(MapConstant.curmappos);
		// map设置地图，需要是原始地图
		map.setMapdata(EditMapDataUtil.getYuanMapDataofDeep());
		map.mapToStr();// 转换
		int c = mapdao.updateMapBeanByid(db, map);
		
		// 修改list
		if(c > 0){
			MapConstant.showandeditmaplist.set(MapConstant.curmappos, map);

//			Toast.makeText(this, "保存/修改成功", Toast.LENGTH_LONG).show();
		}else{

//			Toast.makeText(this, "保存/修改失败", Toast.LENGTH_LONG).show();
		}
	}
	
	// 保存地图至模板
	// 获取到设置的模板名称
	String mouldename;
	public void setMoudleMname(String name){
		mouldename = name;
	}
	// 保存模板至数据库中
	public void setMoudleToSqlite(){
		// 这个不用深拷贝
//		MapMoudleBean mapmouldebean = new MapMoudleBean(mouldename, EditMapDataUtil.yuanMapData);
		
		MapMoudleBean mapmouldebean = new MapMoudleBean(mouldename);
		mapmouldebean.setMapdata(EditMapDataUtil.getYuanMapDataofDeep());
		mapmouldebean.mapToStr();// 转换string
		int mid = mapmoudledao.insertMapMoudleBeanReturnZhu(db, mapmouldebean);
		if(mid > 0){
			// 需要进行1.mapmoudlelist添加这个模板 2.更新mouldfrag不用，自动刷新
			// 设置主键
			mapmouldebean.setMid(mid);
			MapConstant.showeditmapmoudlelist.add(mapmouldebean);
			Toast.makeText(this, "保存模板成功", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "保存模板失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	// 当从模板过来的时候，更换控件显示
	public void setViewVisibleForMapMould(){
		// 设置名称
		showfloorcentv.setText(MapConstant.getMoudleName());
		
		// 保存模板，上一层，下一层隐藏
		savemoudlebt.setVisibility(View.GONE);
		lastlayerbt.setVisibility(View.GONE);
		nextlayerbt.setVisibility(View.GONE);
		
		// 生成楼层，上一个模板，下一个模板出现
		geneLayerbt.setVisibility(View.VISIBLE);
		lastmouldebt.setVisibility(View.VISIBLE);
		nextmouldebt.setVisibility(View.VISIBLE);
	}
	
	// 修改moudlemap表
	protected void updateMapMoudleBeanToSqliteAndList() {
		// 获取第几层地图
		MapMoudleBean mapmoudle = MapConstant.showeditmapmoudlelist.get(MapConstant.curmapmoudlepos);
		// map设置地图，需要是原始地图
		mapmoudle.setMapdata(EditMapDataUtil.getYuanMapDataofDeep());
		mapmoudle.mapToStr();// 转换
		int c = mapmoudledao.updateMapMoudleBeanByid(db, mapmoudle);
		
		// 修改list
		if(c > 0){
			MapConstant.showeditmapmoudlelist.set(MapConstant.curmapmoudlepos, mapmoudle);

//			Toast.makeText(this, "保存/修改成功", Toast.LENGTH_LONG).show();
		}else{

//			Toast.makeText(this, "保存/修改失败", Toast.LENGTH_LONG).show();
		}
	}
	public void isgeneLayerDialog(){
		int layer = MapConstant.getAddLayerPos();
		GenerateDialog.genLayerNormalDialog(this, "是否生成为新楼层："+Constant.maplayerString[layer]);
	}
	// 生成一个楼层
	public void geneOneLayer() {
		// 先提示将生成第几层，是否确定，调用
		int layer = MapConstant.getAddLayerPos();
		Log.i("library", "geneOneLayer：要添加的layer层是："+layer);
		// 1.组成bean
		MapBean mapbean = new MapBean();
		// 层
		mapbean.setFlayer(layer);
		// 数据从当前的原始地图获取，因为原始地图就是 从模板过来赋予的地图值
		mapbean.setMapdata(EditMapDataUtil.getYuanMapDataofDeep());
		// 
		mapbean.mapToStr();// 转换
		int fid = mapdao.insertMapBeanReturnZhu(db, mapbean);
		if(fid > 0){
			// 添加到list
			mapbean.setFid(fid);
			// layer也是list的下标，要-1
			addOneMapMoudleTolist(mapbean, layer - 1);
			Toast.makeText(this, "设为新楼层成功", Toast.LENGTH_SHORT).show();
		}
	}
	// 添加一个listmap
	protected void addOneMapMoudleTolist(MapBean mapbean, int pos) {
		MapConstant.showandeditmaplist.add(pos, mapbean);
	}
	
	long exitTime = 0;
	// 响应按返回键 不退出，应该退出左滑菜单
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// 没有显式 两次返回键退出
		if(System.currentTimeMillis() - exitTime > 2000){// 间隔大于两秒 提示 连按两次
			Toast.makeText(this,"再按一次退出编辑地图", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		}else{
			// TODO Auto-generated method stub
			super.onBackPressed();
		}
	}
	
	// 新功能，恢复上一层
	public void recoverLastMaplayer(){
		// 1.判断栈是否还有
		// 2.有将currmap和yuanshimap拷贝顶上的，刷新地图，并出栈
		if(EditMapDataUtil.hismapsta.size() != 0){
			// 出栈并返回二维数组
			EditMapDataUtil.recoverMapLayerFromStack(EditMapDataUtil.hismapsta.pop());
			// 刷新
			editfloormapview.invalidate();
		}else{
			Constant.commonToast(this, "没有上一个地图可以恢复了。");
		}
	}
	// 一键生成疫情座位
	// 0是行，1是j
	int r[] = new int[2];
	int x[] = new int[2];
//	int rx[] = new int[2];
	public void geneYqSeat(){
		// 保存历史
		EditMapDataUtil.hismapsta.add(EditMapDataUtil.getYuanMapDataofDeep());
		boolean ischange = false;
		// 修改curr与edit完事
		// 循环 墙不用
		for(int i = 1; i < MapConstant.i - 1; i++){
			r[0] = i;
			x[0] = i + 1;
//			rx[0] = i + 1;
			for(int j = 1; j < MapConstant.j - 1; j++){
				r[1] = j + 1;
				x[1] = j;
//				rx[1] = j + 1;
				// 先判断，这个位置是不是可用座位
				if(EditMapDataUtil.yuanMapData[i][j] == MapConstant.SEAT){
					// 是的话就需要把它周围的变成不可用座位
					//  右，下，右下位置就行
					// 没有越界
					if(!EditMapDataUtil.isOverLine(r[0], r[1])){
						// 需判断这个位置是座位
						if(EditMapDataUtil.yuanMapData[r[0]][r[1]] == MapConstant.SEAT){
							// 当前和原始设为不可用
							EditMapDataUtil.setIjBeSeatNo(r[0], r[1]);
							ischange = true;
						}
					}
					if(!EditMapDataUtil.isOverLine(x[0], x[1])){
						// 当前和原始设为不可用
						// 需判断这个位置是座位
						if(EditMapDataUtil.yuanMapData[x[0]][x[1]] == MapConstant.SEAT){
							EditMapDataUtil.setIjBeSeatNo(x[0], x[1]);
							ischange = true;
						}
					}
//					if(!EditMapDataUtil.isOverLine(rx[0], rx[1])){
//						// 当前和原始设为不可用
//						// 需判断这个位置是座位
//						if(EditMapDataUtil.yuanMapData[rx[0]][rx[1]] == MapConstant.SEAT){
//							EditMapDataUtil.setIjBeSeatNo(rx[0], rx[1]);
//							ischange = true;
//						}
//					}
				}
			}
		}
		if(!ischange){
			EditMapDataUtil.hismapsta.pop();
		}else{
			// 代表更新了数组，需要刷新view
			editfloormapview.invalidate();
		}
	}
	
	// 取消不可用疫情座位
	public void cancleYqSeat(){
		// 保存历史
		EditMapDataUtil.hismapsta.add(EditMapDataUtil.getYuanMapDataofDeep());
		boolean ischange = false;
		// 循环
		ischange = EditMapDataUtil.cancleYQseat();
		if(!ischange){
			EditMapDataUtil.hismapsta.pop();
		}else{
			// 代表更新了数组，需要刷新view
			editfloormapview.invalidate();
		}
	}
}
