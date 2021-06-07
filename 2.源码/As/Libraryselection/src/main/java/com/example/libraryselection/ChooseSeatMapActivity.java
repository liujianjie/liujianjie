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


// �޸ĵ�ͼ����activity
public class ChooseSeatMapActivity extends AppCompatActivity implements OnClickListener,OnItemSelectedListener{
	// ���ݿ����
	private SeatInfoDao seatdao;
	private SQLiteDatabase db;
	// ��ͼview
	ChooseSeatMapLayerView chooseseatmapview;
	
	// ���
	private TextView showfloorcentv;// չʾtextview
	private Button quitbt;// �˳�
	private Button lastlayerbt;//��һ��
	private Button nextlayerbt;// ��һ��
	private Button chooseseatbt;// ѡ�����λ
	private Spinner daysping;// ����
	private Spinner timesping;// ʱ���
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_chooseseatmap);
		// ��ʼ�����ݿ�
		initDaoAndDb();
		
		findView();// ��view
		setClickLis();// ����
		
		// �����������ʾ
		setTextView();
//		Log.i("library", "ChooseSeatMapActivity���캯��");
		
		setSpinnerListVal();
		setSpinnerApdater();
		
		// ���ҳ����ݿ�Ĭ�ϵ�ǰ���ڵ�ǰʱ��ε����ݣ���Ҫȫ�� ���ã��������Զ�
//		selSeatByDayAndTime();
	}
	
	// Ѱ�ҿؼ�
	protected void findView(){
		chooseseatmapview = (ChooseSeatMapLayerView)findViewById(R.id.chooseseatmap_view);
		
		// ���
		showfloorcentv = (TextView)findViewById(R.id.chooseseatmap_tx_showfloorc);
		quitbt = (Button) findViewById(R.id.chooseseatmap_bt_quit);
		lastlayerbt = (Button) findViewById(R.id.chooseseatmap_bt_lastlayer);
		nextlayerbt = (Button) findViewById(R.id.chooseseatmap_bt_nextlayer);
		chooseseatbt = (Button) findViewById(R.id.chooseseatmap_bt_chooseseat);
		
		daysping = (Spinner) findViewById(R.id.chooseseatmap_sp_day);
		timesping = (Spinner) findViewById(R.id.chooseseatmap_sp_time);
	}
	// ���ü���
	protected void setClickLis(){
		chooseseatbt.setOnClickListener(this);
		quitbt.setOnClickListener(this);
		lastlayerbt.setOnClickListener(this);
		nextlayerbt.setOnClickListener(this);
	}
	// ������ʾ�ڼ���
	protected void setTextView() {
		showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
	}
	
	// 
	List<String> daylist;
	List<String> timelist;
	ArrayAdapter<String> dayad;
    ArrayAdapter<String> timead;
    
    // ��ǰʱ�䣬����ʱ�����ڵ��ַ���
    String curdaystr;
    String tomdaystr;
    
    // ��ѯ���ݿ���Ҫ�Ĳ���
    String selday;// ����
    int seltime;// ʱ���
    
	// ����spinnerֵ���������죬���磬���磬����
	protected void setSpinnerListVal() {
		daylist = new ArrayList<String>();
		timelist = new ArrayList<String>();
		
		// ������������
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		curdaystr = formatter.format(calendar.getTime());// ����
		// ����
		calendar.setTime(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		tomdaystr = formatter.format(calendar.getTime());// ����
		// ����ǰ׺
		daylist.add(Constant.Today+curdaystr);
		daylist.add(Constant.Tomorrow+tomdaystr);
		
		selday = curdaystr;// Ĭ�ϵ�ǰ
		
		// ���ɽ����time
		geneListForTodayTime();
	}
	// ���ɽ�������磬���磬����
	public void geneListForTodayTime(){
		timelist.clear();
		// timelist
		// ��Ҫ���ݵ�ǰʱ�䣬���ж�.��������ڣ�����ѡ�κ�һ��λ�ã���Ҫע��
		// 1.��ǰʱ����9��ǰ�ſ���ѡ sw
		// 2.��ǰʱ����13��30ǰ����ѡ xw
		// 3.��ǰʱ����18��00ǰ����ѡws
		SimpleDateFormat hourformatter = new SimpleDateFormat("HHMM");
		Calendar curcal = Calendar.getInstance();
		int curhour = Integer.valueOf(hourformatter.format(curcal.getTime()));
//		Toast.makeText(this, hourformatter.format(curcal.getTime()), Toast.LENGTH_SHORT).show();
		// ��һ����ӵģ�Ĭ��3 ��
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
			// û��ѡ��
			timelist.add(Constant.NOSEL);
			if(fistaddtime == 3){
				fistaddtime = 3;
			}
		}
		seltime = fistaddtime;
		// ����
		if(timead != null){
			timead.notifyDataSetChanged();
		}
	}
	// ������������磬���磬����
	public void geneListForTomTime(){
		timelist.clear();
		timelist.add(Constant.SW);
		timelist.add(Constant.XW);
		timelist.add(Constant.WS);
		// ��ѯ��ʱ����0
		seltime = 0;
		
		// ����
		if(timead != null){
			timead.notifyDataSetChanged();
		}
	}
	// ����spinner
	protected void setSpinnerApdater() {
		dayad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,daylist);
		timead = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,timelist);
		
		daysping.setAdapter(dayad);
		timesping.setAdapter(timead);
		
		// ���ü���������ı��ˣ���Ҫ���²��ҹ�ѡ��������list
		daysping.setOnItemSelectedListener(this);
		timesping.setOnItemSelectedListener(this);
	}
	@Override
	public void onClick(View ve) {
		int id = ve.getId();
		switch (id) {
			case R.id.chooseseatmap_bt_chooseseat:
				// ��Ϊ��λ
				// Ҫ��ǰʱ���
				if(seltime != Constant.TIMEDUAN.length - 1){
					chooseseatmapview.MapDatayuToMySeat();
				}else{
					Toast.makeText(this, "��ǰʱ��β���ѡ��λ", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.chooseseatmap_bt_quit:
				// �˳�
				GenerateDialog.genEditMapQuitNormalDialog(this, "ȷ����ѡ��λ����");
				break;
			case R.id.chooseseatmap_bt_lastlayer:
				// ��һ��
				if(MapConstant.curmappos > 0){
					MapConstant.curmappos--;
					
					// ���»�ȡ��λlist
					selSeatByDayAndTime();
					
					// ���¸���ͼֵ������ˢ��
					EditMapDataUtil.getMapDataFromMapList();
					chooseseatmapview.invalidate();
					// ����仯
					showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
					
				}else{
					Toast.makeText(this, "û����һ��", Toast.LENGTH_LONG).show();
				}
				break;	
			case R.id.chooseseatmap_bt_nextlayer:
				// ��һ��
				// ��һ��, ��Ҫ��1 �м�
				if(MapConstant.curmappos < MapConstant.showandeditmaplist.size() - 1){
					MapConstant.curmappos++;
					// ���»�ȡ��λlist
					selSeatByDayAndTime();
					
					// ���¸���ͼֵ������ˢ��
					EditMapDataUtil.getMapDataFromMapList();
					// ˢ��
					chooseseatmapview.invalidate();
					// ����仯
					showfloorcentv.setText(Constant.maplayerString[MapConstant.getLayerNumber()]);
				}else{
					Toast.makeText(this, "�������һ����", Toast.LENGTH_LONG).show();
				}
				break;
		}
	}
	// ���ݿ���س�ʼ��
	protected void initDaoAndDb() {
		db = MyApplication.lihelper.getReadableDatabase();
		seatdao = new SeatInfoDao();
	}
	
	// ��ѯ��ǰ���ڵ�ǰʱ��ε���λ
	protected void selSeatByDayAndTime() {
		// �������seltimeʱ��β����޲�ȥ����
		if(seltime != Constant.TIMEDUAN.length - 1){
			// ��ȡ��ǰ��¥��id����
			int fid = MapConstant.getCurLayerZhujian();
			int uid = MyApplication.user.getUid();
			// ��ǰʱ������¥��ѡ�����λ
			MapConstant.curallseatlist = seatdao.readSeatInfoByTime(db, selday, seltime, fid);
			// ��ǰʱ�����ѡ�����λ
			MapConstant.myseatlist = seatdao.selMySeatBySpeTime(db, selday, seltime, uid);
			
			// �����µ�ͼ
			chooseseatmapview.invalidate();
		}
	}
	// ��ӵ����ݿ���
	public void insertOneSeatToSqiteAndList(int mychoosei, int mychoosej){// ѡ�����λ
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
		// �������һ�������ݿ⣬ҲҪ��ӵ��б����𣿣�Ҫ�ɣ���ӵ���ǰѡ��Ľڵ�
		if(sid > 0){
			seat.setSid(sid);
			// ��ӵ���ǰ¥�㣬��ǰ�����λ
			MapConstant.curallseatlist.add(seat);
			
			// ��ӵ���ǰ�ε���λ
			MapConstant.myseatlist.add(seat);
			Toast.makeText(this, "ѡ���ɹ�", Toast.LENGTH_SHORT).show();
			
			// ˢ�µ�ͼ
			chooseseatmapview.invalidate();
		}
	}
	// ������λ���޸ı�
	// �ȵ��� hi,hj,����λ��ni,nj����λ
	public void generateHadSeatDialog(SeatInfoBean oldseat, SeatInfoBean newseat){
		String msg = "����"+curdaystr+" - "+Constant.TIMEDUAN[seltime]+"ʱ����ڣ�\n"
					+"������λ:"+Constant.maplayerString[MapConstant.getLayerNumberByFid(oldseat.getFid())]+","+oldseat.getSy()+""+oldseat.getSx()+"��\n"
					+"�Ƿ����Ϊ����λ��"+Constant.maplayerString[MapConstant.getLayerNumber()]
					+","+newseat.getSy()+""+newseat.getSx();
		GenerateDialog.genHadSeatDialog(this, msg, oldseat, newseat);
	}
	// �޸ı�,���ݿ��к��ҵ���λlist�С���Ҫ����id��sid������ni
	public void updateOneSeatToSqliteAndlist(SeatInfoBean oldseat, SeatInfoBean newseat){
		//  ����old��sid�޸���snumber��si��sj��fid��
		int ct = seatdao.updateSeatById(db, newseat, oldseat.getSid());
		if(ct > 0){
			// �޸��ҵ���λlist����ǰ¥�����λlist��
			// ��ǰ��λ��list��Ҫƥ��sid�޸�
			// �ҵ���λlist��ֱ������������� �����������鷳
			// ֱ�����²�ѯ�ѣ�����
			// ��Ԥѡ��λ������ָ�
			if(!EditMapDataUtil.isOverLine(newseat.getSy(), newseat.getSx())){
				EditMapDataUtil.recoverDataFromYuanByIJ(newseat.getSy(), newseat.getSx());
			}
			selSeatByDayAndTime();
			Toast.makeText(this, "�޸���λ�ɹ�", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "�޸���λʧ��", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
		// TODO Auto-generated method stub
		// ��ѡ�����
		int id = parent.getId();
		switch (id) {
		case R.id.chooseseatmap_sp_day:
//			selday = dayad.getItem(i).toString();	
//			Toast.makeText(this, "ѡ���ˣ�"+i, Toast.LENGTH_SHORT).show();
			// �����ʱ���
			if(i == 0){
				selday = curdaystr;
				geneListForTodayTime();
			}
			// ����
			if(i == 1){
				selday = tomdaystr;
				geneListForTomTime();
			}
			Log.i("library", "ѡ��"+selday);
			break;
		case R.id.chooseseatmap_sp_time:
			String stime = timead.getItem(i).toString();
			for(int m = 0; m < Constant.TIMEDUAN.length; m++){
				if(stime == Constant.TIMEDUAN[m]){
					seltime = m;
					break;
				}
			}
			Log.i("library", "ѡ��"+stime+" ,"+seltime);
			break;
		default:
			break;
		}
		// ���²���
		selSeatByDayAndTime();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	long exitTime = 0;
	// ��Ӧ�����ؼ� ���˳���Ӧ���˳��󻬲˵�
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// û����ʽ ���η��ؼ��˳�
		if(System.currentTimeMillis() - exitTime > 2000){// ����������� ��ʾ ��������
			Toast.makeText(this,"�ٰ�һ���˳�ѡ��", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		}else{
			// TODO Auto-generated method stub
			super.onBackPressed();
		}
	}
}
