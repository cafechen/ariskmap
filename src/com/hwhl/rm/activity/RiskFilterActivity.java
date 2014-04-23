package com.hwhl.rm.activity;

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
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwhl.rm.R;
import com.hwhl.rm.util.Manages;
import com.hwhl.rm.util.StrUtil;

/**
 * 风险列表
 * 
 * @author Administrator
 * 
 */
public class RiskFilterActivity extends Activity {
	private Manages manages;
	private LayoutInflater inflater;
	private String projectId;
	private LinearLayout contentLinearLayout;
	private LinearLayout contentTitleLinearLayout;
	private List<HashMap<String, String>> fxList;

	private String riskCato = "";
	private String riskVector = "";
	private String manageType = "管理前评分";
	private String maxScore = "";
	private String minScore = "";

	private TextView riskCatoTV;
	private TextView riskVectorTV;
	private TextView symbolTV1;
	private TextView symbolTV2;
	private TextView manageTV;

	private EditText maxScoreET;
	private EditText minScoreET;
	// 总计
	private TextView countTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.risk_filter_activity);
		countTV = (TextView) findViewById(R.id.risk_count);
		riskCatoTV = (TextView) findViewById(R.id.risk_cato);
		riskVectorTV = (TextView) findViewById(R.id.risk_vector);
		symbolTV1 = (TextView) findViewById(R.id.risk_symbol1);
		symbolTV1.setText(">=");
		symbolTV2 = (TextView) findViewById(R.id.risk_symbol2);
		symbolTV2.setText("<=");
		manageTV = (TextView) findViewById(R.id.risk_manage);
		maxScoreET = (EditText) findViewById(R.id.max_score_et);
		minScoreET = (EditText) findViewById(R.id.min_score_et);
		manages = new Manages(RiskFilterActivity.this);
		inflater = RiskFilterActivity.this.getLayoutInflater();
		projectId = getIntent().getStringExtra("projectId");
		contentLinearLayout = (LinearLayout) findViewById(R.id.contentLinearLayout);
		contentTitleLinearLayout = (LinearLayout) findViewById(R.id.contentTitleLinearLayout);
		fxList = new ArrayList<HashMap<String, String>>();
		reloadData();
		super.onCreate(savedInstanceState);
	}

	/**
	 * 重新加载数据
	 */
	private void reloadData() {
		loadData();
		// 加载视图
		loadViews();
		countTV.setText("" + fxList.size());
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		fxList.clear();// select riskTitle,riskCode,d.title as
						// riskCato,riskTypeStr,(select scoreBefore from
						// riskScore where riskid = r.id and scoreVectorId=
						// rs.scoreVectorId )*(select scoreBefore from riskScore
						// where riskid = r.id and scoreVectorId =
						// rs.scoreVectorId) as score from risk r,riskScore rs,
						// dictype d where r.id=rs.riskid and r.riskTypeId =
						// d.id and riskTypeId like '%%' and scoreVectorId like
						// '%%' and score >0.4

		String sql = "select riskTitle,riskCode,d.title as riskCato,riskTypeStr from risk r, dictype d where r.riskTypeId = d.id and r.projectId = '"
				+ projectId
				+ "' and  position0 and r.id in (select riskid from riskScore where  position1 and position2 and position3)";
		if (!StrUtil.nullToStr(riskCato).equals("")) {
			// sql.append(" and riskTypeId='" + riskCato + "'");
			sql = sql.replaceAll("position0", " riskTypeId='" + riskCato + "'");
		} else
			sql = sql.replaceAll("position0", " 1=1 ");
		if ((manageType.equals("管理前评分") || manageType.equals("管理后评分"))&&!StrUtil.nullToStr(riskVector).equals("")) {
			// sql.append(" and scoreVectorId='" + riskVector + "'");
			sql = sql.replaceAll("position1", " scoreVectorId='" + riskVector
					+ "'");
		} else
			sql = sql.replaceAll("position1", " 1=1 ");

		if (!StrUtil.nullToStr(maxScore).equals("")) {
			sql = sql.replaceAll("position2", " ( score < " + maxScore
					+ " or score = " + maxScore + ") ");
		} else
			sql = sql.replaceAll("position2", " 1=1 ");

		if (!StrUtil.nullToStr(minScore).equals("")) {
			sql = sql.replaceAll("position3", " ( score > " + minScore
					+ " or score = " + minScore + ")");
		} else
			sql = sql.replaceAll("position3", " 1=1 ");
		if (manageType.equals("管理前评分"))
			sql = sql.replaceAll(" score ", " scoreBefore ");
		else if (manageType.equals("管理后评分"))
			sql = sql.replaceAll(" score ", " scoreEnd ");
		else if (manageType.equals("管理前影响"))
		{
			sql = sql.replaceAll(" score ", " before ");
			sql = sql.replaceAll(" riskScore ", " riskScoreFather ");
		}
		else if (manageType.equals("管理后影响")){
			sql = sql.replaceAll(" score ", " send ");
			sql = sql.replaceAll(" riskScore ", " riskScoreFather ");
		}

		Log.i("System", sql.toString());
		SQLiteDatabase db = manages.db();
		/*
		 * Cursor cursor = db .rawQuery(
		 * "select riskTitle,riskCode,d.title as riskCato,riskTypeStr,(select scoreBefore from riskScore where riskid = r.id and scoreVectorId= rs.scoreVectorId )*(select scoreBefore from riskScore where riskid = r.id and scoreVectorId = rs.scoreVectorId) as score from risk r,riskScore rs, dictype d where r.id=rs.riskid and r.riskTypeId = d.id and r.projectId = ? and riskTypeId like ? and scoreVectorId like ?"
		 * , new String[] { projectId,"'%"+riskCato+"%'","'%"+riskVector+"%'"
		 * });
		 */
		Cursor cursor = db.rawQuery(sql.toString(), null);
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
		db.close();
	}

	/**
	 * 加载视图
	 */
	private void loadViews() {
		contentTitleLinearLayout.removeAllViews();
		contentLinearLayout.removeAllViews();
		// 添加标题
		contentTitleLinearLayout.addView(getRow(true,
				getString(R.string.risk_filter_item_tv_mc),
				getString(R.string.risk_filter_item_tv_bh),
				getString(R.string.risk_filter_item_tv_lb),
				getString(R.string.risk_filter_item_tv_lx)));
		for (HashMap<String, String> map : fxList) {
			// 添加标题
			View view = getRow(false, map.get("riskTitle"),
					map.get("riskCode"), map.get("riskCato"),
					map.get("riskTypeStr"));
			view.setTag(map.get("riskCode"));
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				String tag = (String) v.getTag();
				Intent intent = new Intent();
				intent.putExtra("riskCode", tag);
				setResult(1 ,intent);
				finish();
					
				}
			});
			contentLinearLayout.addView(view);
		}
	}

	public void OnLeftBtnListener(View v) {
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	/**
	 * 选择条件
	 * 
	 * @param v
	 */
	public void OnSelectCondition(View v) {
		int tag = StrUtil.nullToInt(v.getTag());
		Intent intent = new Intent(RiskFilterActivity.this,
				SelectTypeActivity.class);
		switch (tag) {
		case 0:
			intent.putExtra("type", 5);
			break;
		case 1:
			intent.putExtra("type", 6);
			intent.putExtra("projectId", projectId);
			break;
		case 2:
			intent.putExtra("type", 1);
			break;
		case 3:
			intent.putExtra("type", 7);
			break;
		default:
			break;
		}
		startActivityForResult(intent, tag);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			switch (resultCode) {
			case 1:
				manageType = "管理前评分";
				manageTV.setText(manageType);
				break;
			case 2:
				manageType = "管理后评分";
				manageTV.setText(manageType);
				break;
			case 3:
				manageType = "管理前影响";
				manageTV.setText(manageType);
				break;
			case 4:
				manageType = "管理后影响";
				manageTV.setText(manageType);
				break;
			default:
				break;
			}
		} else if (requestCode == 3) {
			/*
			 * switch (resultCode) { case 1: symbol = ">=";
			 * symbolTV.setText(symbol); break; case 2: symbol = "<=";
			 * symbolTV.setText(symbol); break; default: break; }
			 */
		} else if (requestCode == 0) {
			if (resultCode == 1) {
				riskCato = data.getStringExtra("id");
				riskCatoTV.setText(data.getStringExtra("title"));
			}

		} else if (requestCode == 1) {
			if (resultCode == 1) {
				riskVector = data.getStringExtra("id");
				riskVectorTV.setText(data.getStringExtra("title"));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 查询
	 * 
	 * @param v
	 */
	public void query(View v) {
		String maxScoreStr = maxScoreET.getText().toString();
		String minScoreStr = minScoreET.getText().toString();
		if (!maxScoreStr.equals(""))
			maxScore = StrUtil.nullToStr(maxScoreStr) + "";
		else
			maxScore = "";
		if (!minScoreStr.equals(""))
			minScore = StrUtil.nullToStr(minScoreStr) + "";
		else
			minScore = "";
		reloadData();
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
			row.findViewById(R.id.top_line).setVisibility(View.VISIBLE);
		} else {
			row = inflater.inflate(R.layout.risk_list_item, null);
		}

		((TextView) row.findViewById(R.id.txt1)).setText(mc);
		((TextView) row.findViewById(R.id.txt2)).setText(bh);
		((TextView) row.findViewById(R.id.txt3)).setText(lb);
		((TextView) row.findViewById(R.id.txt4)).setText(lx);
		return row;
	}
}
