package com.hwhl.rm.activity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwhl.rm.R;
import com.hwhl.rm.util.Manages;
import com.hwhl.rm.util.StrUtil;

/**
 * 分类统计
 * 
 * @author Administrator
 * 
 */
public class RiskStatisticsActivity extends Activity {
	private Manages manages;
	private String projectId;
	private AbsoluteLayout absoluteLayout;
	private List<HashMap<String, String>> riskTypeList;
	private int WIDTH = 0;
	private int HEIGHT = 0;
	private int leftDistance = 50;
	private int rightDistance = 50;
	private int topDistance = 50;
	private int bottomDistance = 60;
	private int statusBarHeight = 50;

	private int tableWidth = 0;
	private int tableHeight = 0;
	
	private int topBarHeight = 0;

	private int maxValue = 0;
	// 单位距离
	private int unitDistance = 0;

	// 坐标字体
	private int coordinateFontSize;
	private boolean isPad = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.risk_statistics_activity);
		manages = new Manages(RiskStatisticsActivity.this);
		projectId = getIntent().getStringExtra("projectId");
		absoluteLayout = (AbsoluteLayout) findViewById(R.id.absoluteLayout);
		WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		HEIGHT = getWindowManager().getDefaultDisplay().getHeight()-50-convertDIP2PX(statusBarHeight);
		isPad = isPad();
		if (isPad) {
			topDistance = convertDIP2PX(50);
			bottomDistance = convertDIP2PX(60);
			leftDistance = convertDIP2PX(50);
		} else {
			topDistance = convertDIP2PX(10);
			bottomDistance = convertDIP2PX(40);
			leftDistance = convertDIP2PX(30);
		}
		loadData();
		// 加载视图
		loadViews();
		super.onCreate(savedInstanceState);
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		SQLiteDatabase db = manages.db();
		riskTypeList = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db
				.rawQuery(
						"select dt.title as title, count(*) as riskCount from risk r,dictype dt  where r.riskTypeId= dt.id and r.projectId=? GROUP BY riskTypeId",
						new String[] { projectId });
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("riskTitle", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("title"))));
				map.put("riskCount", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskCount"))));
				maxValue = StrUtil.nullToInt(map.get("riskCount")) < maxValue ? maxValue
						: StrUtil.nullToInt(map.get("riskCount"));
				riskTypeList.add(map);
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
		tableWidth = WIDTH - leftDistance - rightDistance;
		tableHeight = HEIGHT - topDistance - bottomDistance;
		// x轴
		ImageView xView = new ImageView(RiskStatisticsActivity.this);
		AbsoluteLayout.LayoutParams xViewParams = new AbsoluteLayout.LayoutParams(
				tableWidth, 1, leftDistance, tableHeight + topDistance);
		xView.setLayoutParams(xViewParams);
		xView.setBackgroundColor(Color.BLACK);
		absoluteLayout.addView(xView);
		// y轴
		ImageView yView = new ImageView(RiskStatisticsActivity.this);
		AbsoluteLayout.LayoutParams viewParams = new AbsoluteLayout.LayoutParams(
				1, tableHeight, leftDistance, topDistance);
		yView.setLayoutParams(viewParams);
		yView.setBackgroundColor(Color.BLACK);
		absoluteLayout.addView(yView);
		unitDistance = (maxValue / 10 + 1) * 1;

		for (int i = 0; i <= 10; i++) {
            if(i%2 != 0){
                continue;
            }
			TextView tv = new TextView(RiskStatisticsActivity.this);
			AbsoluteLayout.LayoutParams tvParams = new AbsoluteLayout.LayoutParams(
					leftDistance - 10, 50, leftDistance - leftDistance,
					topDistance + tableHeight - tableHeight / 10 * i - 15);
			tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			tv.setLayoutParams(tvParams);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(14);
			tv.setText("" + unitDistance * i);
			absoluteLayout.addView(tv);
		}
		int itemWidth = tableWidth / riskTypeList.size();

		for (int i = 0; i < riskTypeList.size(); i++) {
			// 标题
			HashMap<String, String> map = riskTypeList.get(i);
			TextView tv = new TextView(RiskStatisticsActivity.this);
            AbsoluteLayout.LayoutParams tvParams = new AbsoluteLayout.LayoutParams(
					itemWidth, bottomDistance, leftDistance + i * itemWidth, topDistance
							+ tableHeight);
			tv.setGravity(Gravity.CENTER);
			tv.setLayoutParams(tvParams);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(14);
			tv.setText(map.get("riskTitle"));
			absoluteLayout.addView(tv);
			// 绘制柱形图
			ImageView iv = new ImageView(RiskStatisticsActivity.this);
			int ivHeight = (int) (StrUtil.nullToDouble(map.get("riskCount"))
					/ (unitDistance * 10) * tableHeight);
			AbsoluteLayout.LayoutParams ivParams = new AbsoluteLayout.LayoutParams(
					itemWidth / 2, ivHeight, leftDistance + i * itemWidth
							+ itemWidth / 4, topDistance + tableHeight
							- ivHeight);
			iv.setLayoutParams(ivParams);
			iv.setBackgroundColor(Color.BLUE);
			absoluteLayout.addView(iv);
			// 数量标注
			TextView countTV = new TextView(RiskStatisticsActivity.this);
			AbsoluteLayout.LayoutParams countTVParams = null;
            if(WIDTH > 1600){
                countTVParams = new AbsoluteLayout.LayoutParams(
                        itemWidth / 2, 60, leftDistance + i * itemWidth + itemWidth
                        / 4, topDistance + tableHeight - ivHeight - 60);
            }else{
                countTVParams = new AbsoluteLayout.LayoutParams(
                        itemWidth / 2, 30, leftDistance + i * itemWidth + itemWidth
                        / 4, topDistance + tableHeight - ivHeight - 40);
            }
			countTV.setGravity(Gravity.CENTER);
			countTV.setLayoutParams(countTVParams);
			countTV.setTextColor(Color.BLACK);
			countTV.setTextSize(14);
			countTV.setText(map.get("riskCount"));
			absoluteLayout.addView(countTV);
		}
	}

	public void OnLeftBtnListener(View v) {
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	public void OnChangeGl(View v) {
		Intent intent = new Intent(RiskStatisticsActivity.this,
				SelectTypeActivity.class);
		intent.putExtra("type", 1);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			switch (resultCode) {
			case 1:
				break;
			case 2:
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void query(View v) {
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
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

	public int convertDIP2PX(int dip) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	public double formatDouble(double d) {
		BigDecimal b = new BigDecimal(d);
		return b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private boolean isPad() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// 屏幕宽度
		float screenWidth = display.getWidth();
		// 屏幕高度
		float screenHeight = display.getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// 屏幕尺寸
		double screenInches = Math.sqrt(x + y);
		// 大于6尺寸则为Pad
		if (screenInches >= 6.0) {
			return true;
		}
		return false;
	}

}
