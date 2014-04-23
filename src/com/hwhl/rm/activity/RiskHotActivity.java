package com.hwhl.rm.activity;

import java.io.Serializable;
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
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
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
public class RiskHotActivity extends Activity {
	private Manages manages;
	private LayoutInflater inflater;
	private String projectId;
	private LinearLayout contentLinearLayout;
	private AbsoluteLayout matrixLayout;
	private List<HashMap<String, String>> matrixList;

	private List<HashMap<String, String>> xLevelList;
	private List<HashMap<String, String>> yLevelList;
	private List<HashMap<String, String>> riskList;

	private int WIDTH = 0;
	private int HEIGHT = 0;
	private int matrixWidth;
	private int matrixHeight;

	private int topDistance = 50;
	private int bottomDistance = 50;
	private int leftDistance = 50;

	private int statusBarHeight = 45;

	private int maxX = 0;
	private int maxY = 0;
	private String type;

	private String scoreType = "管理前";

	private LinearLayout riskListLLY;

	private String JH = "机会概率影响矩阵";
	private String WX = "威胁概率影响矩阵";

	private int lastIndex = 0;
	private View lastLayout;
	private View lastScorePoint;

	private String lastScoreTag = "";
	// x轴编号
	private String xCode;
	// y轴编号
	private String yCode;
	private int unitWidth;
	private int unitHeight;
	// 分数显示点的半径
	private int scorePointWidth = 15;

	private HashMap<String, View> imageViewMap;
	// 风险排序
	private Button fxpxButton;
	// 管理前按钮
	private Button glqButton;
	// 坐标字体
	private int coordinateFontSize;

	private int coordinateTVWidth;
	private boolean isPad = false;

	private List<String> matrixTypeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.risk_hot_activity);
		manages = new Manages(RiskHotActivity.this);
		inflater = RiskHotActivity.this.getLayoutInflater();
		WIDTH = getWindowManager().getDefaultDisplay().getWidth();
        HEIGHT = getWindowManager().getDefaultDisplay().getHeight() - convertDIP2PX(statusBarHeight) - 50;
        android.util.Log.v("####", "WIDTH:" + WIDTH + " HEIGHT:" + HEIGHT);
        if(HEIGHT > 800){
            HEIGHT = HEIGHT - 50 ;
        }
		// 判断是否为pad
		isPad = isPad();
		if (isPad) {
			topDistance = 50;
			bottomDistance = 50;
			leftDistance = 80;
		} else {
			topDistance = 30;
			bottomDistance = 30;
			leftDistance = 30;
		}

		// 设置热图边距
		topDistance = convertDIP2PX(topDistance);
		bottomDistance = convertDIP2PX(bottomDistance);
		leftDistance = convertDIP2PX(leftDistance);
		projectId = getIntent().getStringExtra("projectId");
		contentLinearLayout = (LinearLayout) findViewById(R.id.contentLinearLayout);
		matrixLayout = (AbsoluteLayout) findViewById(R.id.matrixLayout);
		fxpxButton = (Button) findViewById(R.id.fxpx_btn);
		glqButton = (Button) findViewById(R.id.glq_btn);
		riskListLLY = (LinearLayout) findViewById(R.id.riskList);
		// type = WX;

		// loadData("威胁概率影响矩阵");
		matrixList = new ArrayList<HashMap<String, String>>();
		matrixTypeList = new ArrayList<String>();
		xLevelList = new ArrayList<HashMap<String, String>>();
		yLevelList = new ArrayList<HashMap<String, String>>();
		riskList = new ArrayList<HashMap<String, String>>();

		// 管理后的权限权限
		if (!isAuthority("show_after")) {
			glqButton.setVisibility(View.GONE);
		}
		if (!isAuthority("show_sort")) {
			fxpxButton.setVisibility(View.GONE);
		}
		SQLiteDatabase db = manages.db();
		// 获取矩阵类型
		Cursor cursor = db.rawQuery(
				"select distinct(matrix_title) as title from projectMatrix", null);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				matrixTypeList.add(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("title"))));
				cursor.moveToNext();
			}
			cursor.close();
		}
		db.close();
		if (matrixTypeList != null && matrixTypeList.size() > 0) {
			// 设置默认矩阵类型
			type = matrixTypeList.get(0);
			// 刷新数据
			refreshData();
		}
		super.onCreate(savedInstanceState);
	}

	/**
	 * 刷新数据
	 */
	private void refreshData() {
		if (matrixTypeList == null || matrixTypeList.size() <= 0) {
			return;
		}

		loadData(type);
		// 加载视图
		loadViews();
	}

	/**
	 * 加载数据
	 */
	private void loadData(String type) {
		matrixList.clear();
		xLevelList.clear();
		yLevelList.clear();
		riskList.clear();
		maxX = 0;
		maxY = 0;

		SQLiteDatabase db = manages.db();
		Cursor cursor = db
				.rawQuery(
						"select id, projectId, matrix_title, matrix_x , matrix_y , xIndex , yIndex , Color , levelType FROM projectMatrix where projectId=? and matrix_title=?",
						new String[] { projectId, type });
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("matrix_title", StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("matrix_title"))));
				map.put("matrix_x", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("matrix_x"))));
				map.put("matrix_y", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("matrix_y"))));
				xCode = StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("matrix_x")));
				yCode = StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("matrix_y")));
				map.put("xIndex", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("xIndex"))));
				map.put("yIndex", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("yIndex"))));
				int xIndex = StrUtil.nullToInt(map.get("xIndex"));
				int yIndex = StrUtil.nullToInt(map.get("yIndex"));
				maxX = xIndex > maxX ? xIndex : maxX;
				maxY = yIndex > maxY ? yIndex : maxY;
				map.put("Color", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Color"))));
				map.put("levelType", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("levelType"))));
				matrixList.add(map);
				cursor.moveToNext();
			}
			cursor.close();
		}
		cursor = db
				.rawQuery(
						"select levelTitle, score, sort from projectVectorDetail where projectId=? and fatherid = (select distinct(matrix_x) FROM projectMatrix where projectId = ? and matrix_title=?) order by sort ASC",
						new String[] { projectId, projectId, type });
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("levelTitle", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("levelTitle"))));
				map.put("score", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("score"))));
				map.put("sort", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("sort"))));
				xLevelList.add(map);
				cursor.moveToNext();
			}
			cursor.close();
		}

		cursor = db
				.rawQuery(
						"select levelTitle, score, sort from projectVectorDetail where projectId=? and fatherid = (select distinct(matrix_y) FROM projectMatrix where projectId = ? and matrix_title=?) order by  sort ASC",
						new String[] { projectId, projectId, type });
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("levelTitle", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("levelTitle"))));
				map.put("score", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("score"))));
				map.put("sort", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("sort"))));
				yLevelList.add(map);
				cursor.moveToNext();
			}
			cursor.close();
		}

		// 加载风险分数数据
		loadRiskScoreData(db);
		db.close();
		maxX++;
		maxY++;
	}

	private void loadRiskScoreData(SQLiteDatabase db) {
		Cursor cursor = null;
		if (scoreType.equals("管理前")) {
			cursor = db
					.rawQuery(
							"select riskCode,riskTitle,(select scoreBefore from riskScore where riskid = r.id and scoreVectorId=?) as xScore,(select scoreBefore from riskScore where riskid = r.id and scoreVectorId=?) as yScore  from risk r,riskscore rs where r.id = rs.riskid and  projectId=? and scoreVectorId in (?,?) order by riskCode",
							new String[] { xCode, yCode, projectId,
									xCode});
		} else
			cursor = db
					.rawQuery(
							"select riskCode,riskTitle,(select scoreEnd from riskScore where riskid = r.id and scoreVectorId=?) as xScore,(select scoreEnd from riskScore where riskid = r.id and scoreVectorId=?) as yScore  from risk r,riskscore rs where r.id = rs.riskid and  projectId=? and scoreVectorId in (?,?)  order by riskCode",
							new String[] { xCode, yCode, projectId,
									xCode});
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("riskCode", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskCode"))));
				map.put("riskTitle", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskTitle"))));
				map.put("xScore", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("xScore"))));
				map.put("yScore", StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("yScore"))));
				riskList.add(map);
				cursor.moveToNext();
			}
			cursor.close();
		}
	}

	/**
	 * 加载视图
	 */
	private void loadViews() {
		matrixWidth = HEIGHT - (topDistance + bottomDistance);
        matrixHeight = matrixWidth;
        matrixLayout.removeAllViews();
		riskListLLY.removeAllViews();
		unitWidth = matrixHeight / (maxX);
		unitHeight = matrixHeight / (maxY);
        if (isPad) {
			coordinateFontSize = (int) (unitWidth > unitHeight ? unitHeight / 8
					: unitWidth / 8);
			coordinateTVWidth = (int) (unitWidth > unitHeight ? unitHeight / 4
					: unitWidth / 4);
		} else {
			coordinateFontSize = (int) (unitWidth > unitHeight ? unitHeight / 10
					: unitWidth / 10);
			coordinateTVWidth = (int) (unitWidth > unitHeight ? unitHeight / 2
					: unitWidth / 2);
		}
        android.util.Log.v("####", "coordinateFontSize:" + coordinateFontSize + " coordinateTVWidth:" + coordinateTVWidth);
        if(coordinateTVWidth <= 10){
            coordinateTVWidth = coordinateTVWidth + 10 ;
        }
        if(coordinateFontSize >= 10){
            coordinateFontSize = 10 ;
        }
        android.util.Log.v("####", "coordinateFontSize:" + coordinateFontSize + " coordinateTVWidth:" + coordinateTVWidth);
        for (HashMap<String, String> map : matrixList) {
			map.get("matrix_title");
			map.get("matrix_x");
			map.get("matrix_y");
			int xIndex = StrUtil.nullToInt(map.get("xIndex"));
			int yIndex = StrUtil.nullToInt(map.get("yIndex"));
			map.get("yIndex");
			int color = StrUtil.nullToInt(map.get("Color"));

			if (color == -65536) {
				color = Color.RED;
			} else if (color == -256) {
				color = Color.YELLOW;
			} else if (color == -16744448) {
				color = Color.GREEN;
			}
			map.get("levelType");
			ImageView imageView = new ImageView(RiskHotActivity.this);
			@SuppressWarnings("deprecation")
			AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
					unitWidth, unitHeight, xIndex * unitWidth + leftDistance,
					yIndex * unitHeight + topDistance);
			imageView.setBackgroundColor(color);
			imageView.setLayoutParams(ivLayoutParams);
			matrixLayout.addView(imageView);
		}
		// 绘制颜色块竖分割线
		for (int i = 0; i <= maxX; i++) {
			ImageView imageView = new ImageView(RiskHotActivity.this);
			@SuppressWarnings("deprecation")
			AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
					1, matrixHeight, i * unitWidth + leftDistance, topDistance);

			imageView.setBackgroundColor(Color.WHITE);

			imageView.setLayoutParams(ivLayoutParams);
			matrixLayout.addView(imageView);
		}
		// 绘制颜色块横分割线
		for (int i = 0; i <= maxY; i++) {
			ImageView imageView = new ImageView(RiskHotActivity.this);
			@SuppressWarnings("deprecation")
			AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
					matrixHeight, 1, leftDistance, i * unitHeight + topDistance);

			imageView.setBackgroundColor(Color.WHITE);
			imageView.setLayoutParams(ivLayoutParams);
			matrixLayout.addView(imageView);
		}

		// // 横坐绘制
		for (int i = 0; i < xLevelList.size(); i++) {
			HashMap<String, String> map = xLevelList.get(i);

			TextView titleTV = new TextView(RiskHotActivity.this);
			TextView scoreTV = new TextView(RiskHotActivity.this);
			@SuppressWarnings("deprecation")
			AbsoluteLayout.LayoutParams scoreTVLayoutParams = new AbsoluteLayout.LayoutParams(
					coordinateTVWidth, coordinateTVWidth, (i + 1) * unitWidth
							+ leftDistance - coordinateTVWidth / 2, topDistance
							+ matrixHeight + 5);
			AbsoluteLayout.LayoutParams titleTVLayoutParams = new AbsoluteLayout.LayoutParams(
					coordinateTVWidth, coordinateTVWidth, i * unitWidth
							+ leftDistance - coordinateTVWidth / 2 + unitWidth
							/ 2, topDistance + matrixHeight + 5);
			titleTV.setLayoutParams(titleTVLayoutParams);
			titleTV.setTextColor(getResources().getColor(R.color.blue));
			titleTV.setTextSize(coordinateFontSize);
			titleTV.setGravity(Gravity.CENTER);

			scoreTV.setLayoutParams(scoreTVLayoutParams);
			scoreTV.setTextColor(Color.BLACK);
			scoreTV.setTextSize(coordinateFontSize);
			scoreTV.setGravity(Gravity.CENTER);
			// String title = map.get("levelTitle");
			// Sring score = map.get("s");
			titleTV.setText(StrUtil.nullToStr(map.get("levelTitle")));
			scoreTV.setText(StrUtil.nullToStr(map.get("score").split("-")[1]));
			matrixLayout.addView(titleTV);
			matrixLayout.addView(scoreTV);
		}
		// 纵坐标绘制
		for (int i = 0; i < yLevelList.size(); i++) {
			HashMap<String, String> map = yLevelList.get(i);

			TextView titleTV = new TextView(RiskHotActivity.this);
			TextView scoreTV = new TextView(RiskHotActivity.this);
			@SuppressWarnings("deprecation")
			AbsoluteLayout.LayoutParams scoreTVLayoutParams = new AbsoluteLayout.LayoutParams(
					leftDistance / 2, coordinateTVWidth, leftDistance / 2,
					(yLevelList.size() - i - 1) * unitHeight + topDistance
							- coordinateTVWidth / 2);
			AbsoluteLayout.LayoutParams titleTVLayoutParams = new AbsoluteLayout.LayoutParams(
					leftDistance / 2, coordinateTVWidth*4/3, leftDistance / 2,
					(yLevelList.size() - i) * unitHeight + topDistance
							- unitHeight/2  - coordinateTVWidth*2 /3);
			titleTV.setLayoutParams(titleTVLayoutParams);
			titleTV.setTextColor(getResources().getColor(R.color.blue));
			titleTV.setGravity(Gravity.CENTER);
			titleTV.setTextSize(coordinateFontSize);
			scoreTV.setLayoutParams(scoreTVLayoutParams);
			scoreTV.setTextColor(Color.BLACK);
			scoreTV.setGravity(Gravity.CENTER);
			scoreTV.setTextSize(coordinateFontSize);
			titleTV.setText(handleString(StrUtil.nullToStr(map.get("levelTitle"))));
			scoreTV.setText(StrUtil.nullToStr(map.get("score").split("-")[1]));
			matrixLayout.addView(titleTV);
			matrixLayout.addView(scoreTV);
		}

		// 原点绘制
		TextView zeroTV = new TextView(RiskHotActivity.this);
		@SuppressWarnings("deprecation")
		AbsoluteLayout.LayoutParams zeroTVLayoutParams = new AbsoluteLayout.LayoutParams(
				coordinateTVWidth, coordinateTVWidth, leftDistance
						- coordinateTVWidth, topDistance + matrixHeight);
		zeroTV.setLayoutParams(zeroTVLayoutParams);
		zeroTV.setTextColor(getResources().getColor(R.color.black));
		zeroTV.setTextSize(coordinateFontSize + 3);
		zeroTV.setText("0");
		zeroTV.setGravity(Gravity.CENTER);
		// X轴名称
		TextView xTitleTV = new TextView(RiskHotActivity.this);
		AbsoluteLayout.LayoutParams xTitleTVLayoutParams = null ;
        if(HEIGHT < 800){
            xTitleTVLayoutParams = new AbsoluteLayout.LayoutParams(
                    coordinateTVWidth, coordinateTVWidth, leftDistance
                    + matrixWidth / 2 - coordinateTVWidth / 2, topDistance
                    + matrixHeight + 25);
        }else{
            xTitleTVLayoutParams = new AbsoluteLayout.LayoutParams(
                    coordinateTVWidth, coordinateTVWidth, leftDistance
                    + matrixWidth / 2 - coordinateTVWidth / 2, topDistance
                    + matrixHeight + 50);
        }
		xTitleTV.setLayoutParams(xTitleTVLayoutParams);
		xTitleTV.setTextColor(Color.BLACK);
		xTitleTV.setText("影响");
		xTitleTV.setTextSize(coordinateFontSize);
		xTitleTV.setGravity(Gravity.CENTER);
		// Y轴名称
		TextView yTitleTV = new TextView(RiskHotActivity.this);
		AbsoluteLayout.LayoutParams yTitleTVLayoutParams = null ;
        if(HEIGHT < 800){
            yTitleTVLayoutParams = new AbsoluteLayout.LayoutParams(
                    leftDistance / 2, coordinateTVWidth, 0, topDistance
                    + matrixHeight / 2 - coordinateTVWidth / 2);
        }else{
            yTitleTVLayoutParams = new AbsoluteLayout.LayoutParams(
                    leftDistance / 2, coordinateTVWidth + 20, 0, topDistance
                    + matrixHeight / 2 - coordinateTVWidth / 2);
        }
		yTitleTV.setLayoutParams(yTitleTVLayoutParams);
		yTitleTV.setTextColor(Color.BLACK);
		yTitleTV.setTextSize(coordinateFontSize);
		yTitleTV.setText("概\n率");
		yTitleTV.setGravity(Gravity.CENTER);

		matrixLayout.addView(zeroTV);
		matrixLayout.addView(xTitleTV);
		matrixLayout.addView(yTitleTV);

		TextView titleTV = new TextView(RiskHotActivity.this);
		AbsoluteLayout.LayoutParams titleTVLayoutParams = new AbsoluteLayout.LayoutParams(
				matrixWidth * 2 / 3, topDistance, leftDistance + matrixWidth
						/ (3 * 2), 0);
		titleTV.setLayoutParams(titleTVLayoutParams);
		titleTV.setTextColor(Color.BLACK);
		titleTV.setTextSize(coordinateFontSize + 5);
		TextPaint localPaint = titleTV.getPaint();
		localPaint.setFakeBoldText(true);
		titleTV.setText(type);
		titleTV.setGravity(Gravity.CENTER);
		matrixLayout.addView(titleTV);

		loadScore();

		// 加载右边风险列表
		for (int i = 0; i < riskList.size(); i++) {
			LinearLayout lly = (LinearLayout) inflater.inflate(
					R.layout.list_item2, null);

			HashMap<String, String> map = riskList.get(i);
			((TextView) lly.findViewById(R.id.name_txt)).setText(map
					.get("riskTitle"));
			((TextView) lly.findViewById(R.id.code_txt)).setText(map
					.get("riskCode"));
			lly.setTag(map.get("riskCode") + "|" + map.get("riskTitle") + "|"
					+ map.get("xScore") + "|" + map.get("yScore"));
			lly.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (lastLayout != null) {
						((TextView) lastLayout.findViewById(R.id.name_txt))
								.setTextColor(Color.BLACK);
						((TextView) lastLayout.findViewById(R.id.code_txt))
								.setTextColor(Color.BLACK);
					}
					((TextView) v.findViewById(R.id.name_txt))
							.setTextColor(getResources().getColor(R.color.blue));
					((TextView) v.findViewById(R.id.code_txt))
							.setTextColor(getResources().getColor(R.color.blue));
					// 保存当前点击的 项
					lastLayout = v;
					// 设置当前选中的项对应的图片的颜色
					String[] tagArray = v.getTag().toString().split("\\|");
					String imageViewTag = tagArray[2] + "|" + tagArray[3];
					View currentIV = matrixLayout.findViewWithTag(imageViewTag);
					currentIV.setBackgroundResource(R.drawable.select);
					if (lastScorePoint != null
							&& !lastScorePoint.getTag().toString()
									.equals(imageViewTag)) {
						currentIV.setBackgroundResource(R.drawable.select);
						lastScorePoint
								.setBackgroundResource(R.drawable.unselect);
					}
					// 保存选中的图片
					lastScorePoint = currentIV;
				}
			});

			lly.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					Intent intent = new Intent(RiskHotActivity.this,
							SelectTypeActivity.class);
					intent.putExtra("data", v.getTag().toString());
					intent.putExtra("type", 3);
					startActivityForResult(intent, 1);
					return true;
				}
			});
			riskListLLY.addView(lly);

		}
	}

	// 加载分数视图
	private void loadScore() {
		for (int i = 0; i < riskList.size(); i++) {
			HashMap<String, String> map = riskList.get(i);
			ImageView scoreImageView = new ImageView(RiskHotActivity.this);
			String riskTitle = StrUtil.nullToStr(map.get("riskTitle"));
			String riskCode = StrUtil.nullToStr(map.get("riskCode"));
			Double xScore = StrUtil.nullToDouble(map.get("xScore"));
			Double yScore = StrUtil.nullToDouble(map.get("yScore"));
			int xPosition = 0;
			int yPosition = 0;
			for (int x = 0; x < xLevelList.size(); x++) {
				HashMap<String, String> xLevelmap = xLevelList.get(x);
				Double start = StrUtil.nullToDouble(xLevelmap.get("score")
						.split("-")[0]);
				Double end = StrUtil.nullToDouble(xLevelmap.get("score").split(
						"-")[1]);
				if (start <= xScore && xScore <= end) {
					xPosition = leftDistance
							+ x
							* unitWidth
							+ (int) ((xScore - start) * unitWidth / (end - start));
					break;
				}
			}

			for (int y = 0; y < yLevelList.size(); y++) {
				HashMap<String, String> yLevelmap = yLevelList.get(y);
				Double start = StrUtil.nullToDouble(yLevelmap.get("score")
						.split("-")[0]);
				Double end = StrUtil.nullToDouble(yLevelmap.get("score").split(
						"-")[1]);
				if (start <= yScore && yScore <= end) {
					yPosition = matrixHeight
							+ topDistance
							- (y * unitHeight + (int) ((yScore - start)
									* unitHeight / (end - start)));
					break;
				}
			}
			String imageViewTag = map.get("xScore") + "|" + map.get("yScore");
			if (matrixLayout.findViewWithTag(imageViewTag) == null) {
				AbsoluteLayout.LayoutParams scoreIVLayoutParams = new AbsoluteLayout.LayoutParams(
						scorePointWidth, scorePointWidth, xPosition
								- scorePointWidth / 2, yPosition
								- scorePointWidth / 2);
				scoreImageView.setTag(imageViewTag);
				scoreImageView.setLayoutParams(scoreIVLayoutParams);
				scoreImageView.setBackgroundResource(R.drawable.unselect);
				matrixLayout.addView(scoreImageView);
			}
		}
	}

	public void OnLeftBtnListener(View v) {
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	// 右边选择矩阵事件
	public void OnRightBtnListener(View v) {
		if (matrixTypeList != null && matrixTypeList.size() > 0) {
			Intent intent = new Intent(RiskHotActivity.this,
					SelectTypeActivity.class);
			intent.putExtra("type", 2);
			intent.putExtra("matrixTypeList", (Serializable) matrixTypeList);
			startActivityForResult(intent, 0);
		}
	}

	// 右边选择矩阵事件
	public void OnRiskOrderBtnListener(View v) {
		Intent intent = new Intent(RiskHotActivity.this,
				RiskOrderActivity.class);
		intent.putExtra("riskList", (Serializable) riskList);
		startActivity(intent);
	}

	// 右边选择矩阵事件
	public void OnRiskScoreTypeBtnListener(View v) {
		// SQLiteDatabase db = manages.db();
		if (scoreType.equals("管理后")) {
			scoreType = "管理前";
			((Button) v).setText(scoreType);
		} else {
			scoreType = "管理后";
			((Button) v).setText(scoreType);
		}
		// loadRiskScoreData(db);
		refreshData();
		// db.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode > 0) {
				type = matrixTypeList.get(resultCode - 1);
				refreshData();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public int convertDIP2PX(int dip) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	// 像素转dip
	public float convertPx2Dip(int px) {
		float scale = getResources().getDisplayMetrics().density;
		float dips = px / scale;
		return dips;
	}

	/**
	 * 权限查询
	 * 
	 * @param type
	 * @return
	 */
	private boolean isAuthority(String type) {
		boolean result = false;
		String sql = "select " + type + " from project where id =?";
		SQLiteDatabase db = manages.db();
		Cursor cursor = db.rawQuery(sql, new String[] { projectId });
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				if (StrUtil.nullToStr(
						cursor.getString(cursor.getColumnIndex(type))).equals(
						"1")) {
					result = true;
				}
			}
			cursor.close();
		}
		db.close();
		return result;
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
	
	private String handleString(String str){
		String result = "";
		for(int i=0;i<str.length();i++)
		{
			result+= str.charAt(i)+"\n";
		}
		
		result = result.substring(0,result.length()-1);
		return result;
			
	}
}
