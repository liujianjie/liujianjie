package com.example.libraryselection;



import java.util.ArrayList;
import java.util.List;

import com.example.libraryselection.adpter.ShowMapLayerAdapter;
import com.example.libraryselection.application.MyApplication;
import com.example.libraryselection.bean.MapBean;
import com.example.libraryselection.dao.MapDao;
import com.example.libraryselection.dao.MyHistoryUserDao;
import com.example.libraryselection.dao.SeatInfoDao;
import com.example.libraryselection.dialog.GenerateDialog;
import com.example.libraryselection.mappack.ShowMapLayerView;
import com.example.libraryselection.resource.Constant;
import com.example.libraryselection.resource.MapConstant;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// 展示地图数据的map
public class MapLayerFragment extends Fragment implements OnClickListener{
	// 上级
	private MainActivity maincontext;
	
	// 控件
//	private LinearLayout maplayerlinearoperation;// 右上角操作linear
	private ImageView addtv;
	private ImageView quittv;
	
	// 中间maplist
//	private List<MapBean> maplist;
	
	// 数据库相关
	private MapDao mapdao;
	private SeatInfoDao seatdao;
	private SQLiteDatabase db;
	private MyHistoryUserDao historydao;
	
	// recycle
	private RecyclerView recycle;
	private ShowMapLayerAdapter showmapadapter;
	
	private LinearLayoutManager layoutManager;
	
	public MapLayerFragment(MainActivity con){
		this.maincontext=con;
	}
	// 初始化
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initMapList();
		// 初始化变量
		initArgument();
		// 数据库查找数据
		selMapDataFromSqlite();
	}
	// 初始化数组
	protected void initMapList() {
//		maplist = new ArrayList<MapBean>();
		// 测试数据
//		MapBean map = new MapBean();
//		maplist.add(map);
//		MapBean map1 = new MapBean();
//		maplist.add(map1);
	}
	// 初始化变量
	protected void initArgument(){
		layoutManager = new LinearLayoutManager(maincontext);
		
		// 数据库相关
		mapdao = new MapDao();
		seatdao = new SeatInfoDao();
		// 以读方式打开
		db = MyApplication.lihelper.getReadableDatabase();
		
		historydao = new MyHistoryUserDao();
		
	}
	// 绑定veiw
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maplayer, null);
		findView(view);
		// 数据赋予
		setRecyleDataAndLayout();
		Log.i("library", "MapLayerFragment：onCreateView");
		
		// 根据全局user是普通用户还是管理员 修改显示控件
		updateViewByUser();
		
		return view;
	}
	// 重启
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 刷新
		notifyReclyerView();
		Log.i("library", "MapLayerFragment被重启");
	}
	// 寻找控件
	protected void findView(View view){
		recycle = (RecyclerView) view.findViewById(R.id.frg_maplayer_recycleview);
		addtv = (ImageView)view.findViewById(R.id.frag_maplayer_img_add);
		
		// 普通控件
		quittv = (ImageView)view.findViewById(R.id.frag_maplayer_img_quit);
		
		addtv.setOnClickListener(this);
		quittv.setOnClickListener(this);
	}
	// 读取表，只读一次的好。
	protected void selMapDataFromSqlite() {
		// 读取map表
		MapConstant.showandeditmaplist = mapdao.readMapLayerData(db);
		showmapadapter = new ShowMapLayerAdapter(MapConstant.showandeditmaplist, maincontext, this);
	}
	// 设置recycle值和布局
	protected void setRecyleDataAndLayout(){
		recycle.setLayoutManager(layoutManager);
		recycle.setAdapter(showmapadapter);
	}
	// 刷新reclyerview
	protected void notifyReclyerView(){
		// 全部刷，简单快捷
		showmapadapter.notifyDataSetChanged();
	}
	
	// 监听
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.frag_maplayer_img_add:
			// 添加一层，先往数据库里添加一个返回主键id，再往list数据里添加这个，。不能超过15个
			if(MapConstant.showandeditmaplist.size() < 15){
				addOneMapToSqliteData();
			}else{
				Constant.commonToast(maincontext, "不能超过15层");
			}
			// 刷新
			notifyReclyerView();
			break;
		case R.id.frag_maplayer_img_quit:
			quitCurUserLogin();
			break;
		default:
			break;
		}
	}
	// 添加一个mapdata往数据库里,再添加到list中
	protected void addOneMapToSqliteData() {
		MapBean mapBean = new MapBean();
		// 设置层数,需要从数据库查找空缺的楼层
//		int layer = mapdao.readLayerCol(db);
		int layer = MapConstant.getAddLayerPos();
		Log.i("library", "要添加的layer层是："+layer);
		mapBean.setFlayer(layer);
		mapBean.mapToStr();// 转换
		int fid = mapdao.insertMapBeanReturnZhu(db, mapBean);
		if(fid > 0){
			mapBean.setFid(fid);
			// layer也是list的下标，要-1
			addOneMapTolist(mapBean, layer - 1);
		}
		
	}
	// 添加一个listmap
	protected void addOneMapTolist(MapBean mapbean, int pos) {
		MapConstant.showandeditmaplist.add(pos, mapbean);
	}
	
	// 产生删除提示弹框
	public void createdelOneMapDialog(int fid){
		GenerateDialog.genDelMapNormalDialog(maincontext, this, "你确定删除此楼层吗，学生的座位也会相应删除", fid);
	}
	// 删除一个map 1.删除数据库的数据，根据id。2.删除list中存在这个id的项 根据id.3删除这个id的座位
	public void delOneMapFromSqliteAndList(int fid) {
		// 删除数据库
		int count = mapdao.deleteMapBeanByid(db, fid);
		if(count > 0){
			// 删除list
			for(MapBean map : MapConstant.showandeditmaplist){
				if(map.getFid() == fid){
					MapConstant.showandeditmaplist.remove(map);
					break;
				}
			}
			// 删除座位
			seatdao.deleteSeatByFid(db, fid);
			// 更新
			notifyReclyerView();
		}
	}
	
	// 为添加准备的，要添加到第几层，用list来得到, 到全局去了

	
	
	// 跳跃activity
	// 去修改地图的
	public void goEditFloorMap(int pos){
		Intent it = new Intent(maincontext, EditFloorMapActivity.class);
		// 需要传入flag
		it.putExtra("flag", 1);
		// 需要传入postion，在list中第几个map，直接设置constant
		MapConstant.curmappos = pos;
		startActivity(it);
	}
	// 去选座地图的
	public void goChoosFloorMap(int pos){
		Intent it = new Intent(maincontext, ChooseSeatMapActivity.class);
		// 需要传入postion，在list中第几个map，直接设置constant
		MapConstant.curmappos = pos;
		startActivity(it);
	}
	
	
	// 根据用户类型修改控件
	// 默认显式控件是管理员界面
	public void updateViewByUser(){
		// 不为空
		if(MyApplication.user != null){
			int role = MyApplication.user.getRole();
			// 普通用户
			if(role == 0){
				addtv.setVisibility(View.GONE);
			}else if(role == 1){
				// 管理员，不需要变。
			}
		}
	}
	
	// 退出登录
	public void quitCurUserLogin(){
		// 弹框
		GenerateDialog.geneIsQuitCurUserLogin(maincontext, "确定推出当前用户吗？");
	}
}



