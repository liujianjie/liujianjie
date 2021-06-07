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

// չʾ��ͼ���ݵ�map
public class MapLayerFragment extends Fragment implements OnClickListener{
	// �ϼ�
	private MainActivity maincontext;
	
	// �ؼ�
//	private LinearLayout maplayerlinearoperation;// ���Ͻǲ���linear
	private ImageView addtv;
	private ImageView quittv;
	
	// �м�maplist
//	private List<MapBean> maplist;
	
	// ���ݿ����
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
	// ��ʼ��
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initMapList();
		// ��ʼ������
		initArgument();
		// ���ݿ��������
		selMapDataFromSqlite();
	}
	// ��ʼ������
	protected void initMapList() {
//		maplist = new ArrayList<MapBean>();
		// ��������
//		MapBean map = new MapBean();
//		maplist.add(map);
//		MapBean map1 = new MapBean();
//		maplist.add(map1);
	}
	// ��ʼ������
	protected void initArgument(){
		layoutManager = new LinearLayoutManager(maincontext);
		
		// ���ݿ����
		mapdao = new MapDao();
		seatdao = new SeatInfoDao();
		// �Զ���ʽ��
		db = MyApplication.lihelper.getReadableDatabase();
		
		historydao = new MyHistoryUserDao();
		
	}
	// ��veiw
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_maplayer, null);
		findView(view);
		// ���ݸ���
		setRecyleDataAndLayout();
		Log.i("library", "MapLayerFragment��onCreateView");
		
		// ����ȫ��user����ͨ�û����ǹ���Ա �޸���ʾ�ؼ�
		updateViewByUser();
		
		return view;
	}
	// ����
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ˢ��
		notifyReclyerView();
		Log.i("library", "MapLayerFragment������");
	}
	// Ѱ�ҿؼ�
	protected void findView(View view){
		recycle = (RecyclerView) view.findViewById(R.id.frg_maplayer_recycleview);
		addtv = (ImageView)view.findViewById(R.id.frag_maplayer_img_add);
		
		// ��ͨ�ؼ�
		quittv = (ImageView)view.findViewById(R.id.frag_maplayer_img_quit);
		
		addtv.setOnClickListener(this);
		quittv.setOnClickListener(this);
	}
	// ��ȡ��ֻ��һ�εĺá�
	protected void selMapDataFromSqlite() {
		// ��ȡmap��
		MapConstant.showandeditmaplist = mapdao.readMapLayerData(db);
		showmapadapter = new ShowMapLayerAdapter(MapConstant.showandeditmaplist, maincontext, this);
	}
	// ����recycleֵ�Ͳ���
	protected void setRecyleDataAndLayout(){
		recycle.setLayoutManager(layoutManager);
		recycle.setAdapter(showmapadapter);
	}
	// ˢ��reclyerview
	protected void notifyReclyerView(){
		// ȫ��ˢ���򵥿��
		showmapadapter.notifyDataSetChanged();
	}
	
	// ����
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.frag_maplayer_img_add:
			// ���һ�㣬�������ݿ������һ����������id������list�������������������ܳ���15��
			if(MapConstant.showandeditmaplist.size() < 15){
				addOneMapToSqliteData();
			}else{
				Constant.commonToast(maincontext, "���ܳ���15��");
			}
			// ˢ��
			notifyReclyerView();
			break;
		case R.id.frag_maplayer_img_quit:
			quitCurUserLogin();
			break;
		default:
			break;
		}
	}
	// ���һ��mapdata�����ݿ���,����ӵ�list��
	protected void addOneMapToSqliteData() {
		MapBean mapBean = new MapBean();
		// ���ò���,��Ҫ�����ݿ���ҿ�ȱ��¥��
//		int layer = mapdao.readLayerCol(db);
		int layer = MapConstant.getAddLayerPos();
		Log.i("library", "Ҫ��ӵ�layer���ǣ�"+layer);
		mapBean.setFlayer(layer);
		mapBean.mapToStr();// ת��
		int fid = mapdao.insertMapBeanReturnZhu(db, mapBean);
		if(fid > 0){
			mapBean.setFid(fid);
			// layerҲ��list���±꣬Ҫ-1
			addOneMapTolist(mapBean, layer - 1);
		}
		
	}
	// ���һ��listmap
	protected void addOneMapTolist(MapBean mapbean, int pos) {
		MapConstant.showandeditmaplist.add(pos, mapbean);
	}
	
	// ����ɾ����ʾ����
	public void createdelOneMapDialog(int fid){
		GenerateDialog.genDelMapNormalDialog(maincontext, this, "��ȷ��ɾ����¥����ѧ������λҲ����Ӧɾ��", fid);
	}
	// ɾ��һ��map 1.ɾ�����ݿ�����ݣ�����id��2.ɾ��list�д������id���� ����id.3ɾ�����id����λ
	public void delOneMapFromSqliteAndList(int fid) {
		// ɾ�����ݿ�
		int count = mapdao.deleteMapBeanByid(db, fid);
		if(count > 0){
			// ɾ��list
			for(MapBean map : MapConstant.showandeditmaplist){
				if(map.getFid() == fid){
					MapConstant.showandeditmaplist.remove(map);
					break;
				}
			}
			// ɾ����λ
			seatdao.deleteSeatByFid(db, fid);
			// ����
			notifyReclyerView();
		}
	}
	
	// Ϊ���׼���ģ�Ҫ��ӵ��ڼ��㣬��list���õ�, ��ȫ��ȥ��

	
	
	// ��Ծactivity
	// ȥ�޸ĵ�ͼ��
	public void goEditFloorMap(int pos){
		Intent it = new Intent(maincontext, EditFloorMapActivity.class);
		// ��Ҫ����flag
		it.putExtra("flag", 1);
		// ��Ҫ����postion����list�еڼ���map��ֱ������constant
		MapConstant.curmappos = pos;
		startActivity(it);
	}
	// ȥѡ����ͼ��
	public void goChoosFloorMap(int pos){
		Intent it = new Intent(maincontext, ChooseSeatMapActivity.class);
		// ��Ҫ����postion����list�еڼ���map��ֱ������constant
		MapConstant.curmappos = pos;
		startActivity(it);
	}
	
	
	// �����û������޸Ŀؼ�
	// Ĭ����ʽ�ؼ��ǹ���Ա����
	public void updateViewByUser(){
		// ��Ϊ��
		if(MyApplication.user != null){
			int role = MyApplication.user.getRole();
			// ��ͨ�û�
			if(role == 0){
				addtv.setVisibility(View.GONE);
			}else if(role == 1){
				// ����Ա������Ҫ�䡣
			}
		}
	}
	
	// �˳���¼
	public void quitCurUserLogin(){
		// ����
		GenerateDialog.geneIsQuitCurUserLogin(maincontext, "ȷ���Ƴ���ǰ�û���");
	}
}



