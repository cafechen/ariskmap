package com.hwhl.rm.activity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hwhl.rm.R;
import com.hwhl.rm.adapter.RiskCostAdapter;
import com.hwhl.rm.util.AppManager;
import com.hwhl.rm.util.Manages;
import com.hwhl.rm.util.StrUtil;

/**
 * 风险列表
 * 
 * @author Administrator
 * 
 */
public class RiskCostActivity extends Activity {
	private Manages manages;
	private LayoutInflater inflater;
	private String projectId;
	private LinearLayout contentLinearLayout;
	private LinearLayout contentTitleLinearLayout;
	// private List<HashMap<String, String>> riskCostList;
	private List<HashMap<String, String>> riskCostList;

	private String searchCondition = "";
	// 列宽度
	private int[] width = null;

	private int[] ID = { R.id.txt1, R.id.txt2, R.id.txt3, R.id.txt4, R.id.txt5,
			R.id.txt6, R.id.txt7, R.id.txt8, R.id.txt9, R.id.txt10, R.id.txt11,
			R.id.txt12, R.id.txt13, R.id.txt14 };
	// 标题资源ID
	private int[] title = { R.string.risk_cost_item_tv_fxmc,
			R.string.risk_cost_item_tv_fxdh, R.string.risk_cost_item_tv_fxlx,
			R.string.risk_cost_item_tv_glqfxgl,
			R.string.risk_cost_item_tv_glqfxyxz,
			R.string.risk_cost_item_tv_glqfxyxqwz,
			R.string.risk_cost_item_tv_fxglcb,
			R.string.risk_cost_item_tv_glhfxgl,
			R.string.risk_cost_item_tv_glhfxyxz,
			R.string.risk_cost_item_tv_glhfxyxqwz,
			R.string.risk_cost_item_tv_fxyxqwz,
			R.string.risk_cost_item_tv_fxglsy,
			R.string.risk_cost_item_tv_fxgljsy,
			R.string.risk_cost_item_tv_fxglsyycbbl };

	private String[] titleStr = null;

	private EditText searchET;
	private String jhTypeId = "";
	private String wxTypeId = "";

	private TextView wxTV;
	private TextView jhTV;

	private ListView listView;

	private RiskCostAdapter adapter;

	private TextView countTV;

	private List<HashMap<String, String>> wxList;
	private List<HashMap<String, String>> jhList;
	
private String huobi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.risk_cost_activity);
		countTV = (TextView) findViewById(R.id.risk_count);
		listView = (ListView) findViewById(R.id.listView);
		manages = new Manages(RiskCostActivity.this);
		inflater = RiskCostActivity.this.getLayoutInflater();
		projectId = getIntent().getStringExtra("projectId");
		contentTitleLinearLayout = (LinearLayout) findViewById(R.id.contentTitleLinearLayout);
		huobi = getHuobi();
		titleStr = new String[] { "风险名称", "风险代号", "风险类型", "管理前风险概率",
				"管理前风险影响值"+huobi, "管理前风险影响期望值"+huobi, "风险管理成本"+huobi, "管理后风险概率", "管理后风险影响值"+huobi,
				"管理后风险影响期望值"+huobi, "风险影响期望值"+huobi, "风险管理收益"+huobi, "风险管理净收益"+huobi, "风险管理收益与成本比例" };

		width = new int[] { convertDIP2PX(170), convertDIP2PX(100),
				convertDIP2PX(70), convertDIP2PX(120), convertDIP2PX(140),
				convertDIP2PX(170), convertDIP2PX(120), convertDIP2PX(120),
				convertDIP2PX(140), convertDIP2PX(170), convertDIP2PX(120),
				convertDIP2PX(120), convertDIP2PX(120), convertDIP2PX(170) };

		jhTV = (TextView) findViewById(R.id.risk_cost_tv_jh);
		wxTV = (TextView) findViewById(R.id.risk_cost_tv_wx);
		searchET = (EditText) findViewById(R.id.search_tv);
		riskCostList = new ArrayList<HashMap<String, String>>();
		wxList = new ArrayList<HashMap<String, String>>();
		jhList = new ArrayList<HashMap<String, String>>();
		contentTitleLinearLayout.addView(getRowTitle());
		adapter = new RiskCostAdapter(RiskCostActivity.this, riskCostList);
		listView.setAdapter(adapter);

		SQLiteDatabase db = manages.db();
		Cursor cursor = db.rawQuery(
				"select id,title,theType from projectVector", null);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String type = StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("theType")));
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("id"))));
				map.put("title", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("title"))));
				if (type.equals("威胁")) {
					wxList.add(map);
				} else {
					jhList.add(map);
				}
				cursor.moveToNext();
			}
			cursor.close();
		}
		db.close();

		if (wxList.size() > 0) {
			wxTypeId = wxList.get(0).get("id");
			wxTV.setText(wxList.get(0).get("title"));
		} else {
			wxTypeId = "";
			wxTV.setText("无选项");
		}

		if (jhList.size() > 0) {
			jhTypeId = jhList.get(0).get("id");
			jhTV.setText(jhList.get(0).get("title"));
		} else {
			jhTypeId = "";
			jhTV.setText("无选项");
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HashMap<String,String> map = riskCostList.get(position);
				Intent intent = new Intent();
				intent.putExtra("riskCode", map.get("riskCode"));
				setResult(1 ,intent);
				finish();
			}
		});
		// 重新加载数据
		reloadData();
		super.onCreate(savedInstanceState);
	}

	private void reloadData() {
		loadData();
		// 加载视图
		loadViews();
		countTV.setText("" + (riskCostList.size() - 1));
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		riskCostList.clear();
		SQLiteDatabase db = manages.db();
		Cursor cursor = db
				.rawQuery(
						"select riskName,riskCode ,riskType,beforeGailv ,beforeAffect ,beforeAffectQi ,manaChengben ,afterGailv ,"
								+ "afterAffect ,afterQi ,affectQi ,shouyi ,jingshouyi ,bilv FROM riskCost  where projectId=? and riskvecorid like ? and chanceVecorid like ? and (riskName like ?"
								+ " or riskCode like ? or riskType like?  or beforeGailv like ? or beforeAffect like ? or beforeAffectQi like ? "
								+ "or manaChengben like ? or afterGailv like ?  or afterAffect like ? or afterQi like ? or affectQi like ?  "
								+ "or shouyi like ? or jingshouyi like ? or bilv like ?)",
						new String[] { projectId, "%" + wxTypeId + "%",
								"%" + jhTypeId + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%" });
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("riskName", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskName"))));
				map.put("riskCode", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskCode"))));
				map.put("riskType", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskType"))));
				map.put("beforeGailv", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("beforeGailv"))));
				map.put("beforeAffect", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("beforeAffect"))));
				map.put("beforeAffectQi", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("beforeAffectQi"))));
				map.put("manaChengben", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("manaChengben"))));
				map.put("afterGailv", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("afterGailv"))));
				map.put("afterAffect", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("afterAffect"))));
				map.put("afterQi", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("afterQi"))));
				map.put("affectQi", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("affectQi"))));
				map.put("shouyi", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("shouyi"))));
				map.put("jingshouyi", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("jingshouyi"))));
				map.put("bilv", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("bilv"))));
				riskCostList.add(map);
				cursor.moveToNext();
			}
			cursor.close();
		}
		// 统计数据
		cursor = db
				.rawQuery(
						"select sum(beforeGailv) as  beforeGailv,sum(beforeAffect) as beforeAffect,sum(beforeAffectQi) as  beforeAffectQi,sum(manaChengben) as  manaChengben,sum(afterGailv) as afterGailv ,"
								+ "sum(afterAffect) as afterAffect,sum(afterQi) as afterQi,sum(affectQi) as affectQi,sum(shouyi) as shouyi,sum(jingshouyi) as jingshouyi,sum(bilv) as bilv FROM riskCost  where projectId=? and riskvecorid like ? and chanceVecorid like ? and (riskName like ?"
								+ " or riskCode like ? or riskType like?  or beforeGailv like ? or beforeAffect like ? or beforeAffectQi like ? "
								+ "or manaChengben like ? or afterGailv like ?  or afterAffect like ? or afterQi like ? or affectQi like ?  "
								+ "or shouyi like ? or jingshouyi like ? or bilv like ?)",
						new String[] { projectId, "%" + wxTypeId + "%",
								"%" + jhTypeId + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%",
								"%" + searchCondition + "%" });
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("riskName", getString(R.string.risk_zj));
				map.put("riskCode", "--");
				map.put("riskType", "--");
				map.put("beforeGailv","--");
				map.put("beforeAffect", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("beforeAffect"))));
				map.put("beforeAffectQi", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("beforeAffectQi"))));
				map.put("manaChengben", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("manaChengben"))));
				map.put("afterGailv", "--");
				map.put("afterAffect", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("afterAffect"))));
				map.put("afterQi", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("afterQi"))));
				map.put("affectQi", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("affectQi"))));
				map.put("shouyi", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("shouyi"))));
				map.put("jingshouyi", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("jingshouyi"))));
				map.put("bilv", "--");
				riskCostList.add(map);
				// cursor.moveToNext();
			}
			cursor.close();
		}
		db.close();
	}

	/**
	 * 加载视图
	 */
	private void loadViews() {
		adapter.notifyDataSetChanged();
	}

	public void OnLeftBtnListener(View v) {
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	private View getRowTitle() {
		View row = inflater.inflate(R.layout.risk_cost_title, null);
		for (int i = 0; i < ID.length; i++) {
			((TextView) row.findViewById(ID[i])).setText(titleStr[i]);
			((TextView) row.findViewById(ID[i])).setWidth(width[i]);
		}
		return row;

	}

	/**
	 * 选择威胁类型
	 * 
	 * @param v
	 */
	public void SelectWX(View v) {
		if (wxList.size() > 0) {
			Intent intent = new Intent(RiskCostActivity.this,
					SelectTypeActivity.class);
			intent.putExtra("type", 4);
			intent.putExtra("list", (Serializable) wxList);
			intent.putExtra("title", "选择威胁概率的顺序量表");
			startActivityForResult(intent, 0);
		} else {
			AppManager.showToastMessage("无可选择的类型");
		}
	}

	/**
	 * 选择机会类型
	 * 
	 * @param v
	 */
	public void SelectJH(View v) {
		if (jhList.size() > 0) {
			Intent intent = new Intent(RiskCostActivity.this,
					SelectTypeActivity.class);
			intent.putExtra("type", 4);
			intent.putExtra("list", (Serializable) jhList);
			intent.putExtra("title", "选择机会概率的顺序量表");
			startActivityForResult(intent, 1);
		} else {
			AppManager.showToastMessage("无可选择的类型");
		}
	}

	/**
	 * 查询
	 * 
	 * @param v
	 */
	public void query(View v) {
		searchCondition = StrUtil.nullToStr(searchET.getText().toString());
		reloadData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			/*
			 * if (resultCode == 1) { wxTypeId = getVectorId("影响", "威胁");
			 * wxTV.setText("影响"); } else if (resultCode == 2) { wxTypeId =
			 * getVectorId("概率", "威胁"); wxTV.setText("概率"); }
			 */
			if (resultCode > 0) {
				wxTypeId = wxList.get(resultCode - 1).get("id");
				wxTV.setText(wxList.get(resultCode - 1).get("title"));
			}

		} else if (requestCode == 1) {
			if (resultCode > 0) {
				jhTypeId = jhList.get(resultCode - 1).get("id");
				jhTV.setText(jhList.get(resultCode - 1).get("title"));
			}
			/*
			 * if (resultCode == 1) { jhTypeId = getVectorId("影响", "机会");
			 * jhTV.setText("影响"); } else if (resultCode == 2) { jhTypeId =
			 * getVectorId("概率", "机会"); jhTV.setText("概率"); }
			 */

		}
		reloadData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	public int convertDIP2PX(int dip) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/**
	 * 把字符串转换成BigDecimal,并用format进行格式化操作
	 * 
	 * @param obj
	 * @param format
	 * @return
	 */
	public static String formatBigDecimal(String value) {
		// if (isNum(StrUtil.nullToDouble(value)+"")) {
		Number bd = StrUtil.nullToDouble(value);
		DecimalFormat df = new DecimalFormat("#.00");
		return "" + new BigDecimal(df.format(bd));
		// } else
		// return value;
	}

	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	private String getHuobi() {
		String result = "$";
		String sql = "select huobi from project";
		SQLiteDatabase db = manages.db();
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				result = StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("huobi")));
			}
			cursor.close();
		}
		db.close();
		return result;
	}

}
