package com.hwhl.rm.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hwhl.rm.R;
import com.hwhl.rm.R.color;
import com.hwhl.rm.adapter.ProjectListAdapter;
import com.hwhl.rm.adapter.RiskListAdapter;
import com.hwhl.rm.model.Project;
import com.hwhl.rm.util.Manages;
import com.hwhl.rm.util.StrUtil;

/**
 * 风险列表
 * 
 * @author Administrator
 * 
 */
public class RiskListActivity extends Activity {
	private Manages manages;
	private ViewPager mTabPager;
	// 风险tab
	private LinearLayout fxLLY;
	// 因素tab
	private LinearLayout ysLLY;
	// 目标tab
	private LinearLayout mbLLY;
	private ArrayList<View> views;
	private LayoutInflater inflater;
	private String projectId;
	private List<HashMap<String, String>> fxList;
	private List<HashMap<String, String>> ysList;
	private List<HashMap<String, String>> mbList;

	private int currIndex;
	private TextView countTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.risk_list_activity);
		countTV = (TextView) findViewById(R.id.risk_count);
		manages = new Manages(RiskListActivity.this);
		inflater = RiskListActivity.this.getLayoutInflater();
		projectId = getIntent().getStringExtra("projectId");
		views = new ArrayList<View>();
		loadData();
		// 加载视图
		loadViews();
		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new RiskOnPageChangeListener());
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);

			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		fxLLY = (LinearLayout) findViewById(R.id.tab_fx);
		ysLLY = (LinearLayout) findViewById(R.id.tab_ys);
		mbLLY = (LinearLayout) findViewById(R.id.tab_mb);
		fxLLY.setBackgroundColor(Color.GRAY);
		ysLLY.setBackgroundColor(Color.BLACK);
		mbLLY.setBackgroundColor(Color.BLACK);
		countTV.setText(""+fxList.size());
		super.onCreate(savedInstanceState);
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		SQLiteDatabase db = manages.db();
		fxList = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db
				.rawQuery(
						"select riskTitle,riskCode,d.title as riskCato,riskTypeStr from risk r, dictype d where r.riskTypeId = d.id and r.projectId =?",
						new String[] { projectId });
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("riskTitle", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskTitle"))));
				map.put("riskCode", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskCode"))));
				map.put("riskCato", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskCato"))));
				map.put("riskTypeStr", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("riskTypeStr"))));
				fxList.add(map);
				cursor.moveToNext();
			}
			cursor.close();
		}

		ysList = new ArrayList<HashMap<String, String>>();
		mbList = new ArrayList<HashMap<String, String>>();
		cursor = db
				.rawQuery(
						"select id, title,Obj_remark,Obj_maptype from projectMap where projectId =? and Obj_maptype in('因素','目标') ",
						new String[] { projectId });
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String type = StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_maptype")));
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("id"))));
				map.put("title", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("title"))));
				map.put("Obj_remark", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_remark"))));
				map.put("Obj_maptype", type);
				if (type.equals("因素")) {
					ysList.add(map);
				} else if (type.equals("目标")) {
					mbList.add(map);
				}
				cursor.moveToNext();
			}
			cursor.close();
		}
		db.close();
		
	}

	/**
	 * 加载视图
	 */
	private void loadViews() {
		//风险
		LinearLayout fxContentLinearLayout = new LinearLayout(
				RiskListActivity.this);
		fxContentLinearLayout.setOrientation(LinearLayout.VERTICAL);
		ListView fxListView = new ListView(RiskListActivity.this);
		// 添加标题
		fxContentLinearLayout.addView(getRow(true,
				getString(R.string.risk_list_item_tv_mc),
				getString(R.string.risk_list_item_tv_bh),
				getString(R.string.risk_list_item_tv_lb),
				getString(R.string.risk_list_item_tv_lx)));

		RiskListAdapter fxAdapter = new RiskListAdapter(RiskListActivity.this,
				fxList, 0);
		fxListView.setAdapter(fxAdapter);
		fxListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HashMap<String,String> map = fxList.get(position);
				Intent intent = new Intent();
				intent.putExtra("riskCode", map.get("riskCode"));
				setResult(1 ,intent);
				finish();
			}
		});
		fxContentLinearLayout.addView(fxListView);
		//因素
		LinearLayout ysContentLinearLayout = new LinearLayout(
				RiskListActivity.this);
		ysContentLinearLayout.setOrientation(LinearLayout.VERTICAL);
		ListView ysListView = new ListView(RiskListActivity.this);
		// 添加标题
		ysContentLinearLayout.addView(getRow(true,
				getString(R.string.risk_list_item_tv_mc),
				getString(R.string.risk_list_item_tv_bz),
				getString(R.string.risk_list_item_tv_lb)));
		RiskListAdapter ysAdapter = new RiskListAdapter(RiskListActivity.this,
				ysList, 1);
		ysListView.setAdapter(ysAdapter);
		ysListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				HashMap<String,String> map = ysList.get(position);
				Intent intent = new Intent();
				intent.putExtra("id", map.get("id"));
				setResult(2 ,intent);
				finish();
			
			}
		});
		ysContentLinearLayout.addView(ysListView);
		//目标
		LinearLayout mbContentLinearLayout = new LinearLayout(
				RiskListActivity.this);
		mbContentLinearLayout.setOrientation(LinearLayout.VERTICAL);
		ListView mbListView = new ListView(RiskListActivity.this);
		// 添加标题
		mbContentLinearLayout.addView(getRow(true,
				getString(R.string.risk_list_item_tv_mc),
				getString(R.string.risk_list_item_tv_bz),
				getString(R.string.risk_list_item_tv_lb)));

		RiskListAdapter mbAdapter = new RiskListAdapter(RiskListActivity.this,
				mbList, 1);
		mbListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				HashMap<String,String> map = mbList.get(position);
				Intent intent = new Intent();
				intent.putExtra("id", map.get("id"));
				setResult(3 ,intent);
				finish();
			
			}
		});
		mbListView.setAdapter(mbAdapter);
		mbContentLinearLayout.addView(mbListView);

		views.add(fxContentLinearLayout);
		views.add(ysContentLinearLayout);
		views.add(mbContentLinearLayout);
	}

	public void OnLeftBtnListener(View v) {
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	/**
	 * 创建列表项
	 * 
	 * @param isTitle
	 * @param mc
	 * @param bh
	 * @param lb
	 * @param lx
	 * @return
	 */
	private View getRow(boolean isTitle, String mc, String bh, String lb,
			String lx) {
		View row = null;
		if (isTitle) {
			row = inflater.inflate(R.layout.risk_list_title, null);
		} else
			row = inflater.inflate(R.layout.risk_list_item, null);

		((TextView) row.findViewById(R.id.txt1)).setText(mc);
		((TextView) row.findViewById(R.id.txt2)).setText(bh);
		((TextView) row.findViewById(R.id.txt3)).setText(lb);
		((TextView) row.findViewById(R.id.txt4)).setText(lx);
		return row;
	}

	/**
	 * 创建列表项
	 * 
	 * @param isTitle
	 * @param mc
	 * @param bh
	 * @param lb
	 * @param lx
	 * @return
	 */
	private View getRow(boolean isTitle, String bt, String bz, String lb) {
		View row = null;
		if (isTitle) {
			row = inflater.inflate(R.layout.risk_list_title1, null);
		} else
			row = inflater.inflate(R.layout.risk_list_item1, null);
		((TextView) row.findViewById(R.id.txt1)).setText(bt);
		((TextView) row.findViewById(R.id.txt2)).setText(bz);
		//((TextView) row.findViewById(R.id.txt3)).setText(lb);
		return row;

	}

	/*
	 * 页卡切换监听(原作者:D.Winter)
	 */
	class RiskOnPageChangeListener implements OnPageChangeListener {
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				fxLLY.setBackgroundColor(Color.GRAY);
				ysLLY.setBackgroundColor(Color.BLACK);
				mbLLY.setBackgroundColor(Color.BLACK);
				countTV.setText(""+fxList.size());

				break;
			case 1:
				fxLLY.setBackgroundColor(Color.BLACK);
				ysLLY.setBackgroundColor(Color.GRAY);
				mbLLY.setBackgroundColor(Color.BLACK);
				countTV.setText(""+ysList.size());
				break;
			case 2:
				fxLLY.setBackgroundColor(Color.BLACK);
				ysLLY.setBackgroundColor(Color.BLACK);
				mbLLY.setBackgroundColor(Color.GRAY);
				countTV.setText(""+mbList.size());
				break;
			case 3:
				break;
			}
			currIndex = arg0;

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}
}
