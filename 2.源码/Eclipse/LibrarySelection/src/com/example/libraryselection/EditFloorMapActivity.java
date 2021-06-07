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


// �޸ĵ�ͼ����activity
public class EditFloorMapActivity extends AppCompatActivity implements OnClickListener{
	// ���ݿ����
	private MapDao mapdao;
	private SQLiteDatabase db;
	private MapMoudleDao mapmoudledao;
	// ��ͼview
	EditMapLayerView editfloormapview;
	// �ұ߰�ť
	LinearLayout editfloormaplin;
	
	// ���
	private TextView showfloorcentv;// չʾtextview
//	private Button savebt;// ��������޸�
	private Button savemoudlebt;// ���浽ģ��
	private Button quitbt;// �˳�
	private Button lastlayerbt;//��һ��
	private Button nextlayerbt;// ��һ��
	private Button setseatbt, setbarbt ,setfieldbt;// ��Ϊ��λ���ϰ����յ�
	private Button setshujiabt;
	private Button setseatnot;//������
	private Button recoverbt;// �ָ���һ���ͼ����ջʵ��
	// ģ������
	private Button geneLayerbt;
	private Button lastmouldebt, nextmouldebt;// ����ģ��
	private Button geneyqseatbt;// ����������λ
	private Button cancleseatbt;// ȡ��������λ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_editfoormap);
		initDaoAndDb();
		
		findView();// ��view
		setClickLis();// ����
		
		getIntentForSetting();
		
		// ��Ԥ����ͼ������ʱ��Ҫ���õ�
		if(flag == 1){
			setViewVisibleForMap();
			// ������ͼview��data
			editfloormapview.initDataFromMapList();
		}else if(flag == 2){
			setViewVisibleForMapMould();
			// ������ͼview��data
			editfloormapview.initDataFromMapMoudleList();
		}
		
		Log.i("library", "EditFloorMapActivity���캯��");
	}
	int flag;// ��ʶ����������ģ�1��Ԥ����ͼ��2��ģ�������
	// ��ȡintentΪ������
	public void getIntentForSetting(){
		Intent in = getIntent();
		flag = in.getIntExtra("flag", 1);
	}
	
	// Ѱ�ҿؼ�
	protected void findView(){
		editfloormapview = (EditMapLayerView)findViewById(R.id.edfloormap_view);
		editfloormaplin = (LinearLayout)findViewById(R.id.edfloormap_linear);
		
		// ���
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
	// ���ü���
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
		// ģ���
		geneLayerbt.setOnClickListener(this);
		lastmouldebt.setOnClickListener(this);
		nextmouldebt.setOnClickListener(this);
	}
	// ����Ԥ����ͼ����ʱ��Ĵ���
	// ���õ�ͼ�������ʾ�ڼ���
	protected void setEditMapViewDataAndTextView() {
		showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
		// ��������
		// �ӹ���list��ȡ��ͼ��Ϣ,�������췽�������
	}
	// ��Ԥ����ͼ������ʱ������ؼ���ʾ, Ĭ�Ͼ���������ͼ�Ĳ���
	public void setViewVisibleForMap(){
		// �ǳ�
		setEditMapViewDataAndTextView();
	}
	@Override
	public void onClick(View ve) {
		int id = ve.getId();
		switch (id) {
		case R.id.edfloormap_bt1_setseat:
			// ��Ϊ��λ
			editfloormapview.MapDatayuToThing(1);
			break;
		case R.id.edfloormap_bt_setshujia:
			// ���
			editfloormapview.MapDatayuToThing(4);
			break;
			// ��������λ
		case R.id.edfloormap_bt_setseatno:
			// ���
			editfloormapview.MapDatayuToThing(3);
			break;
		case R.id.edfloormap_bt2_setbar:
			// ��Ϊ�ϰ�
			editfloormapview.MapDatayuToThing(2);
			break;
		case R.id.edfloormap_bt3_setfield:
			// ��Ϊ�յ�
			editfloormapview.MapDatayuToThing(0);
			break;
		case R.id.edfloormap_bt_editmap_geneyqseat:
			// ����������λ
			geneYqSeat();
			break;
		case R.id.edfloormap_bt_setcancleseatno:
			// ȡ����������λ
			cancleYqSeat();
			break;
		case R.id.edfloormap_bt_editmap_recover:
			// �ָ�
			recoverLastMaplayer();
			break;
//		case R.id.edfloormap_bt4_save:
			// ���桢�޸� ��ͼֵ
			// ��Ҫ�ж��Ǳ����ͼֵ����ģ��ֵ
//			saveMapDataForMapAndMapMoudle();
//			break;
		case R.id.edfloormap_bt6_quit:
			// �˳�, �����Ƿ����
//			GenerateDialog.genEditMapQuitNormalDialog(this, "�˳����Զ������ͼ���ݣ��Ƿ����?");
			// �ȱ���
			saveMapDataForMapAndMapMoudle();
			
			// �˳�
			finish();
			break;
		case R.id.edfloormap_bt5_savemold:
			// ����ģ��
			GenerateDialog.genEditMapInputDialog(this);
			break;	
		case R.id.edfloormap_bt7_lastlayer:
			// ��һ��
			if(MapConstant.curmappos > 0){
				// �ȱ���
				saveMapDataForMapAndMapMoudle();
				
				MapConstant.curmappos--;
				// ���¸���ͼֵ������ˢ��
				EditMapDataUtil.getMapDataFromMapList();
//				EditMapDataUtil.setMapDataFromData(MapConstant.showandeditmaplist.get(MapConstant.curmappos).getMapdata());
				editfloormapview.invalidate();
				// ����仯
				showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
			}else{
				Toast.makeText(this, "û����һ��", Toast.LENGTH_LONG).show();
			}
			break;	
		case R.id.edfloormap_bt8_nextlayer:
			// ��һ��, ��Ҫ��1 �м�
			if(MapConstant.curmappos < MapConstant.showandeditmaplist.size() - 1){
				// �ȱ���
				saveMapDataForMapAndMapMoudle();
				
				MapConstant.curmappos++;
				// ���¸���ͼֵ������ˢ��
				EditMapDataUtil.getMapDataFromMapList();
//				EditMapDataUtil.setMapDataFromData(MapConstant.showandeditmaplist.get(MapConstant.curmappos).getMapdata());
				// ˢ��
				editfloormapview.invalidate();
				// ����仯
				showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
			}else{
				Toast.makeText(this, "�������һ����", Toast.LENGTH_LONG).show();
			}
			break;		
			
		//ģ����ؿؼ�
		case R.id.edfloormap_bt9_geneMapLayer:
			// ����¥�㣬�൱�����һ��¥��, ��Ҫ��list�����ݿ������һ��������Ӹ����ݿ⣬�ٸ�list
			// ����ʾ
			isgeneLayerDialog();
			break;
		case R.id.edfloormap_bt10_lastmoudle:
			// ��һ��ģ��
			if(MapConstant.curmapmoudlepos > 0){
				saveMapDataForMapAndMapMoudle();
				
				MapConstant.curmapmoudlepos--;
				// ���¸���ͼֵ������ˢ��
				EditMapDataUtil.getMapDataFromMapMoudleList();
				editfloormapview.invalidate();
				// ����仯
				showfloorcentv.setText(MapConstant.getMoudleName());
			}else{
				Toast.makeText(this, "û����һ��ģ��", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.edfloormap_bt11_lastmoudle:
			// ��һ��ģ��  ��Ҫ��1 �м�
			if(MapConstant.curmapmoudlepos < MapConstant.showeditmapmoudlelist.size() - 1){
				saveMapDataForMapAndMapMoudle();
				
				MapConstant.curmapmoudlepos++;
				// ���¸���ͼֵ������ˢ��
				EditMapDataUtil.getMapDataFromMapMoudleList();
				editfloormapview.invalidate();
				// ����仯
				showfloorcentv.setText(MapConstant.getMoudleName());
			}else{
				Toast.makeText(this, "�������һ��ģ����", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}
	// �����ͼ����Ϊ��ͼ�͵�ͼģ��
	protected void saveMapDataForMapAndMapMoudle(){
		// ��Ҫ��ջ��գ���Ȼ����
		EditMapDataUtil.hismapsta.clear();
		if(flag == 1){
			// ��ͼֵ���ӵ�ǰ��ȡ������Ҫȥ��Ԥѡλ�á�
			// 1.�޸�map�� 2.�޸Ĺ���maplist��mapdata 3.����showmapfrag��recycleview,��frag�Զ�����
			updateMapBeanToSqliteAndList();
		}else if(flag == 2){
			// ��ͼֵ���ӵ�ǰ��ȡ������Ҫȥ��Ԥѡλ�á����ã�ֱ�ӻ�ȡԭʼ��ͼ
			// 1.�޸�mamoudlep�� 2.�޸Ĺ���mapmoudlelist��mapdata 3.����mapmoudlefrag��recycleview,��frag�Զ�����
			updateMapMoudleBeanToSqliteAndList();
		}
	}
	// ���ݿ���س�ʼ��
	protected void initDaoAndDb() {
		mapdao = new MapDao();
		db = MyApplication.lihelper.getReadableDatabase();
		
		mapmoudledao = new MapMoudleDao();
	}
	// �޸�map��
	protected void updateMapBeanToSqliteAndList() {
		// ��ȡ�ڼ����ͼ
		MapBean map = MapConstant.showandeditmaplist.get(MapConstant.curmappos);
		// map���õ�ͼ����Ҫ��ԭʼ��ͼ
		map.setMapdata(EditMapDataUtil.getYuanMapDataofDeep());
		map.mapToStr();// ת��
		int c = mapdao.updateMapBeanByid(db, map);
		
		// �޸�list
		if(c > 0){
			MapConstant.showandeditmaplist.set(MapConstant.curmappos, map);

//			Toast.makeText(this, "����/�޸ĳɹ�", Toast.LENGTH_LONG).show();
		}else{

//			Toast.makeText(this, "����/�޸�ʧ��", Toast.LENGTH_LONG).show();
		}
	}
	
	// �����ͼ��ģ��
	// ��ȡ�����õ�ģ������
	String mouldename;
	public void setMoudleMname(String name){
		mouldename = name;
	}
	// ����ģ�������ݿ���
	public void setMoudleToSqlite(){
		// ����������
//		MapMoudleBean mapmouldebean = new MapMoudleBean(mouldename, EditMapDataUtil.yuanMapData);
		
		MapMoudleBean mapmouldebean = new MapMoudleBean(mouldename);
		mapmouldebean.setMapdata(EditMapDataUtil.getYuanMapDataofDeep());
		mapmouldebean.mapToStr();// ת��string
		int mid = mapmoudledao.insertMapMoudleBeanReturnZhu(db, mapmouldebean);
		if(mid > 0){
			// ��Ҫ����1.mapmoudlelist������ģ�� 2.����mouldfrag���ã��Զ�ˢ��
			// ��������
			mapmouldebean.setMid(mid);
			MapConstant.showeditmapmoudlelist.add(mapmouldebean);
			Toast.makeText(this, "����ģ��ɹ�", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "����ģ��ʧ��", Toast.LENGTH_SHORT).show();
		}
	}
	
	// ����ģ�������ʱ�򣬸����ؼ���ʾ
	public void setViewVisibleForMapMould(){
		// ��������
		showfloorcentv.setText(MapConstant.getMoudleName());
		
		// ����ģ�壬��һ�㣬��һ������
		savemoudlebt.setVisibility(View.GONE);
		lastlayerbt.setVisibility(View.GONE);
		nextlayerbt.setVisibility(View.GONE);
		
		// ����¥�㣬��һ��ģ�壬��һ��ģ�����
		geneLayerbt.setVisibility(View.VISIBLE);
		lastmouldebt.setVisibility(View.VISIBLE);
		nextmouldebt.setVisibility(View.VISIBLE);
	}
	
	// �޸�moudlemap��
	protected void updateMapMoudleBeanToSqliteAndList() {
		// ��ȡ�ڼ����ͼ
		MapMoudleBean mapmoudle = MapConstant.showeditmapmoudlelist.get(MapConstant.curmapmoudlepos);
		// map���õ�ͼ����Ҫ��ԭʼ��ͼ
		mapmoudle.setMapdata(EditMapDataUtil.getYuanMapDataofDeep());
		mapmoudle.mapToStr();// ת��
		int c = mapmoudledao.updateMapMoudleBeanByid(db, mapmoudle);
		
		// �޸�list
		if(c > 0){
			MapConstant.showeditmapmoudlelist.set(MapConstant.curmapmoudlepos, mapmoudle);

//			Toast.makeText(this, "����/�޸ĳɹ�", Toast.LENGTH_LONG).show();
		}else{

//			Toast.makeText(this, "����/�޸�ʧ��", Toast.LENGTH_LONG).show();
		}
	}
	public void isgeneLayerDialog(){
		int layer = MapConstant.getAddLayerPos();
		GenerateDialog.genLayerNormalDialog(this, "�Ƿ�����Ϊ��¥�㣺"+Constant.maplayerString[layer]);
	}
	// ����һ��¥��
	public void geneOneLayer() {
		// ����ʾ�����ɵڼ��㣬�Ƿ�ȷ��������
		int layer = MapConstant.getAddLayerPos();
		Log.i("library", "geneOneLayer��Ҫ��ӵ�layer���ǣ�"+layer);
		// 1.���bean
		MapBean mapbean = new MapBean();
		// ��
		mapbean.setFlayer(layer);
		// ���ݴӵ�ǰ��ԭʼ��ͼ��ȡ����Ϊԭʼ��ͼ���� ��ģ���������ĵ�ͼֵ
		mapbean.setMapdata(EditMapDataUtil.getYuanMapDataofDeep());
		// 
		mapbean.mapToStr();// ת��
		int fid = mapdao.insertMapBeanReturnZhu(db, mapbean);
		if(fid > 0){
			// ��ӵ�list
			mapbean.setFid(fid);
			// layerҲ��list���±꣬Ҫ-1
			addOneMapMoudleTolist(mapbean, layer - 1);
			Toast.makeText(this, "��Ϊ��¥��ɹ�", Toast.LENGTH_SHORT).show();
		}
	}
	// ���һ��listmap
	protected void addOneMapMoudleTolist(MapBean mapbean, int pos) {
		MapConstant.showandeditmaplist.add(pos, mapbean);
	}
	
	long exitTime = 0;
	// ��Ӧ�����ؼ� ���˳���Ӧ���˳��󻬲˵�
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// û����ʽ ���η��ؼ��˳�
		if(System.currentTimeMillis() - exitTime > 2000){// ����������� ��ʾ ��������
			Toast.makeText(this,"�ٰ�һ���˳��༭��ͼ", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		}else{
			// TODO Auto-generated method stub
			super.onBackPressed();
		}
	}
	
	// �¹��ܣ��ָ���һ��
	public void recoverLastMaplayer(){
		// 1.�ж�ջ�Ƿ���
		// 2.�н�currmap��yuanshimap�������ϵģ�ˢ�µ�ͼ������ջ
		if(EditMapDataUtil.hismapsta.size() != 0){
			// ��ջ�����ض�ά����
			EditMapDataUtil.recoverMapLayerFromStack(EditMapDataUtil.hismapsta.pop());
			// ˢ��
			editfloormapview.invalidate();
		}else{
			Constant.commonToast(this, "û����һ����ͼ���Իָ��ˡ�");
		}
	}
	// һ������������λ
	// 0���У�1��j
	int r[] = new int[2];
	int x[] = new int[2];
//	int rx[] = new int[2];
	public void geneYqSeat(){
		// ������ʷ
		EditMapDataUtil.hismapsta.add(EditMapDataUtil.getYuanMapDataofDeep());
		boolean ischange = false;
		// �޸�curr��edit����
		// ѭ�� ǽ����
		for(int i = 1; i < MapConstant.i - 1; i++){
			r[0] = i;
			x[0] = i + 1;
//			rx[0] = i + 1;
			for(int j = 1; j < MapConstant.j - 1; j++){
				r[1] = j + 1;
				x[1] = j;
//				rx[1] = j + 1;
				// ���жϣ����λ���ǲ��ǿ�����λ
				if(EditMapDataUtil.yuanMapData[i][j] == MapConstant.SEAT){
					// �ǵĻ�����Ҫ������Χ�ı�ɲ�������λ
					//  �ң��£�����λ�þ���
					// û��Խ��
					if(!EditMapDataUtil.isOverLine(r[0], r[1])){
						// ���ж����λ������λ
						if(EditMapDataUtil.yuanMapData[r[0]][r[1]] == MapConstant.SEAT){
							// ��ǰ��ԭʼ��Ϊ������
							EditMapDataUtil.setIjBeSeatNo(r[0], r[1]);
							ischange = true;
						}
					}
					if(!EditMapDataUtil.isOverLine(x[0], x[1])){
						// ��ǰ��ԭʼ��Ϊ������
						// ���ж����λ������λ
						if(EditMapDataUtil.yuanMapData[x[0]][x[1]] == MapConstant.SEAT){
							EditMapDataUtil.setIjBeSeatNo(x[0], x[1]);
							ischange = true;
						}
					}
//					if(!EditMapDataUtil.isOverLine(rx[0], rx[1])){
//						// ��ǰ��ԭʼ��Ϊ������
//						// ���ж����λ������λ
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
			// ������������飬��Ҫˢ��view
			editfloormapview.invalidate();
		}
	}
	
	// ȡ��������������λ
	public void cancleYqSeat(){
		// ������ʷ
		EditMapDataUtil.hismapsta.add(EditMapDataUtil.getYuanMapDataofDeep());
		boolean ischange = false;
		// ѭ��
		ischange = EditMapDataUtil.cancleYQseat();
		if(!ischange){
			EditMapDataUtil.hismapsta.pop();
		}else{
			// ������������飬��Ҫˢ��view
			editfloormapview.invalidate();
		}
	}
}
