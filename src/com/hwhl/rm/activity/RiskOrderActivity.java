package com.hwhl.rm.activity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
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
public class RiskOrderActivity extends Activity {
	private Manages manages;
	private LayoutInflater inflater;
	private String projectId;
	private LinearLayout contentLinearLayout;
	private AbsoluteLayout absoluteLayout;
	private List<HashMap<String, String>> riskList;
	private int WIDTH = 0;
	private int topHeight = 25;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.risk_order_activity);
		manages = new Manages(RiskOrderActivity.this);
		inflater = RiskOrderActivity.this.getLayoutInflater();
		riskList = (List<HashMap<String, String>>) getIntent()
				.getSerializableExtra("riskList");
		Collections.sort(riskList, comparator);
		contentLinearLayout = (LinearLayout) findViewById(R.id.contentLinearLayout);
		absoluteLayout = (AbsoluteLayout) findViewById(R.id.absoluteLayout);
		WIDTH = getWindowManager().getDefaultDisplay().getWidth() - 30;
		topHeight = convertDIP2PX(topHeight);
		loadData();
		// 加载视图
		loadViews();
		super.onCreate(savedInstanceState);
	}

	Comparator<HashMap<String, String>> comparator = new Comparator<HashMap<String, String>>() {
		public int compare(HashMap<String, String> map1,
				HashMap<String, String> map2) {

			double map1Score = formatDouble(StrUtil.nullToDouble(map1
					.get("xScore")) * StrUtil.nullToDouble(map1.get("yScore")));

			double map2Score = formatDouble(StrUtil.nullToDouble(map2
					.get("xScore")) * StrUtil.nullToDouble(map2.get("yScore")));
			// 先排年龄
			if (map1Score == map2Score) {
				return 0;

			} else if (map1Score < map2Score) {
				return 1;
			}

			else {
				return -1;
			}

		}
	};

	/**
	 * 加载数据
	 */
	private void loadData() {

	}

	/**
	 * 加载视图
	 */
	private void loadViews() {
		// 添加标题
		/*
		 * contentTitleLinearLayout.addView(getRow(true,
		 * getString(R.string.risk_filter_item_tv_mc),
		 * getString(R.string.risk_filter_item_tv_bh),
		 * getString(R.string.risk_filter_item_tv_lb),
		 * getString(R.string.risk_filter_item_tv_lx)));
		 */
		ImageView view = new ImageView(RiskOrderActivity.this);
		AbsoluteLayout.LayoutParams viewParams = new AbsoluteLayout.LayoutParams(
				WIDTH / 2 - 20, 1, WIDTH / 2 + 20, topHeight);
		view.setLayoutParams(viewParams);
		view.setBackgroundColor(Color.BLACK);
		absoluteLayout.addView(view);

		ImageView graduationView0 = new ImageView(RiskOrderActivity.this);
		AbsoluteLayout.LayoutParams graduationViewParams0 = new AbsoluteLayout.LayoutParams(
				1, 5, WIDTH / 2 + 20, topHeight-5);
		graduationView0.setLayoutParams(graduationViewParams0);
		graduationView0.setBackgroundColor(Color.BLACK);
		absoluteLayout.addView(graduationView0);

		ImageView graduationView1 = new ImageView(RiskOrderActivity.this);
		AbsoluteLayout.LayoutParams graduationViewParams1 = new AbsoluteLayout.LayoutParams(
				1, 5, WIDTH / 4 * 3 + 10, topHeight-5);
		graduationView1.setLayoutParams(graduationViewParams1);
		graduationView1.setBackgroundColor(Color.BLACK);
		absoluteLayout.addView(graduationView1);

		ImageView graduationView2 = new ImageView(RiskOrderActivity.this);
		AbsoluteLayout.LayoutParams graduationViewParams2 = new AbsoluteLayout.LayoutParams(
				1, 5, WIDTH-2, topHeight-5);
		graduationView2.setLayoutParams(graduationViewParams2);
		graduationView2.setBackgroundColor(Color.BLACK);
		absoluteLayout.addView(graduationView2);

		TextView graduationTV0 = new TextView(RiskOrderActivity.this);
		AbsoluteLayout.LayoutParams graduationTVParams0 = null ;
        if(WIDTH > 1600){
            graduationTVParams0 = new AbsoluteLayout.LayoutParams(
                    30, 30, WIDTH / 2 + 5, 30);
        }else{
            graduationTVParams0 = new AbsoluteLayout.LayoutParams(
                    30, 30, WIDTH / 2 + 5, 0);
        }
		graduationTV0.setLayoutParams(graduationTVParams0);
		graduationTV0.setGravity(Gravity.CENTER);
		graduationTV0.setTextColor(Color.BLACK);
		graduationTV0.setText("0");
		graduationTV0.setTextSize(10);
        if(WIDTH > 1600){
            graduationTV0.setTextSize(6);
        }
		absoluteLayout.addView(graduationTV0);

		TextView graduationTV1 = new TextView(RiskOrderActivity.this);
		AbsoluteLayout.LayoutParams graduationTVParams1 = null ;
        if(WIDTH > 1600){
            graduationTVParams1 = new AbsoluteLayout.LayoutParams(
                    30, 30, WIDTH / 4 * 3 - 5, 30);
        }else{
            graduationTVParams1 = new AbsoluteLayout.LayoutParams(
                    30, 30, WIDTH / 4 * 3 - 5, 0);
        }
		graduationTV1.setLayoutParams(graduationTVParams1);
		graduationTV1.setTextSize(10);
		graduationTV1.setGravity(Gravity.CENTER);
		graduationTV1.setTextColor(Color.BLACK);
		graduationTV1.setText("0.5");
        if(WIDTH > 1600){
            graduationTV1.setTextSize(6);
        }
		absoluteLayout.addView(graduationTV1);

		TextView graduationTV2 = new TextView(RiskOrderActivity.this);
		AbsoluteLayout.LayoutParams graduationTVParams2 = null ;
        if(WIDTH > 1600){
            graduationTVParams2 = new AbsoluteLayout.LayoutParams(
                    30, 30, WIDTH - 15, 30);
        }else{
            graduationTVParams2 = new AbsoluteLayout.LayoutParams(
                    30, 30, WIDTH - 15, 0);
        }
		graduationTV2.setLayoutParams(graduationTVParams2);
		graduationTV2.setTextSize(10);
		graduationTV2.setGravity(Gravity.CENTER);
		graduationTV2.setTextColor(Color.BLACK);
		graduationTV2.setText("1");
        if(WIDTH > 1600){
            graduationTV2.setTextSize(6);
        }
		absoluteLayout.addView(graduationTV2);
		for (HashMap<String, String> map : riskList) {
			// 添加标题
			contentLinearLayout.addView(getRow(map.get("riskCode") + "  "
					+ map.get("riskTitle"),
					formatDouble(StrUtil.nullToDouble(map.get("xScore"))
							* StrUtil.nullToDouble(map.get("yScore")))));
		}
	}

	public void OnLeftBtnListener(View v) {
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	public void OnChangeGl(View v) {
		Intent intent = new Intent(RiskOrderActivity.this,
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
		// double f = 9.223372036854784E14;
		BigDecimal b = new BigDecimal(d);
		return b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
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
	/*
	 * private View getRow(boolean isTitle, String mc, String bh, String lb,
	 * String lx) { View row = null; if (isTitle) { row =
	 * inflater.inflate(R.layout.risk_list_title, null);
	 * row.findViewById(R.id.top_line).setVisibility(View.VISIBLE); } else { row
	 * = inflater.inflate(R.layout.risk_list_item, null); }
	 * 
	 * ((TextView) row.findViewById(R.id.txt1)).setText(mc); ((TextView)
	 * row.findViewById(R.id.txt2)).setText(bh); ((TextView)
	 * row.findViewById(R.id.txt3)).setText(lb); ((TextView)
	 * row.findViewById(R.id.txt4)).setText(lx); return row; }
	 */

	private View getRow(String title, double score) {
		View row = inflater.inflate(R.layout.list_item3, null);
		LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,30);
		row.setLayoutParams(rowLayoutParams);
		TextView titleTxt = (TextView) row.findViewById(R.id.title_txt);
		LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
				WIDTH / 2, LayoutParams.WRAP_CONTENT);
		titleLayoutParams.rightMargin=20;
		titleTxt.setLayoutParams(titleLayoutParams);
		titleTxt.setText(title);
        if(WIDTH > 1600){
            titleTxt.setTextSize(6) ;
        }
		ImageView imageIV = (ImageView) row.findViewById(R.id.score_iv);
		LinearLayout.LayoutParams ivLayoutParams = new LinearLayout.LayoutParams(
				(int) (WIDTH / 2 * (score)), 20);
		imageIV.setLayoutParams(ivLayoutParams);
		((TextView) row.findViewById(R.id.score_txt)).setText(score + "");
        if(WIDTH > 1600){
            ((TextView) row.findViewById(R.id.score_txt)).setTextSize(6);
        }
		return row;
	}
}
