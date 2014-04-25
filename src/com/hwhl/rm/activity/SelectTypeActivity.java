package com.hwhl.rm.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hwhl.rm.R;
import com.hwhl.rm.adapter.ListAdapter;
import com.hwhl.rm.util.Manages;
import com.hwhl.rm.util.StrUtil;
import com.hwhl.rm.util.WSApplication;

/**
 * 选择操作类型
 * 
 * @author Administrator
 * 
 */
public class SelectTypeActivity extends Activity {
	private int type = 0;
	private Manages manages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manages = new Manages(SelectTypeActivity.this);
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		if (type == 0) { // 选择选项类型
			setContentView(R.layout.select_type_activity);
			LinearLayout contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
			if (!isAuthority("show_hot")) {
				contentLayout.findViewWithTag("2").setVisibility(View.GONE);
			} 
			// 跳到分类统计
			if (!isAuthority("show_static")) {
				contentLayout.findViewWithTag("4").setVisibility(View.GONE);
			} 
			// 跳到风险成本
			if (!isAuthority("show_chengben")) {
				contentLayout.findViewWithTag("5").setVisibility(View.GONE);
			}
			boolean isRiskRelation = intent.getBooleanExtra("isRiskRelation", false);
			if (!isRiskRelation) {
				contentLayout.findViewWithTag("7").setVisibility(View.GONE);
			}
			
		} else if (type == 1) { // 选择管理前 还是管理后
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			setContentView(R.layout.select_manage_activity);
		} else if (type == 2) { // 选择矩阵列表
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			setContentView(R.layout.select_matrix_activity);
			LinearLayout contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
		    List<String> matrixTypeList = intent.getStringArrayListExtra("matrixTypeList");
			for(int i=0;i<matrixTypeList.size();i++)
			{
				Button button = (Button) contentLayout.findViewWithTag(""+(i+1));
				button.setVisibility(View.VISIBLE);
				button.setText(matrixTypeList.get(i));
			}
		} else if (type == 3) { // 选择矩阵列表
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			setContentView(R.layout.select_score_activity);
			String data = StrUtil.nullToStr(intent.getStringExtra("data"));
			String[] datas = data.split("\\|");
			((TextView) findViewById(R.id.dialog_title)).setText(datas[0]
					+ "\n" + datas[1]);
			((TextView) findViewById(R.id.yxTxt)).setText("影响：" + datas[2]);
			((TextView) findViewById(R.id.glTxt)).setText("概率：" + datas[3]);
		} else if (type == 4) { // 选择矩阵列表
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			setContentView(R.layout.select_risktype_activity);
			LinearLayout contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
			String title = StrUtil.nullToStr(intent.getStringExtra("title"));
			List<HashMap<String,String>> list = (List<HashMap<String, String>>) intent.getSerializableExtra("list");
			((TextView) findViewById(R.id.dialog_title)).setText(title);
			for(int i=0;i<list.size();i++)
			{
				HashMap<String,String> map = list.get(i);
				Button button = (Button) contentLayout.findViewWithTag(""+(i+1));
				button.setVisibility(View.VISIBLE);
				button.setText(map.get("title"));
			}
		} else if (type == 5 || type == 6) { // 选择顺序量表
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			setContentView(R.layout.select_riskcato_activity);
			ListView listView = (ListView) findViewById(R.id.listView);
			final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "");
			Manages manages = new Manages(SelectTypeActivity.this);
			SQLiteDatabase db = manages.db();
			Cursor cursor = null;
			if (type == 5) {
				((TextView) findViewById(R.id.dialog_title))
						.setText(R.string.select_type_tv_fxfl);
				map.put("title", "全部分类");
				cursor = db.rawQuery(
						"select id, title from dictype where typeStr='风险' and fatherid=0",
						null);
			} else {
				((TextView) findViewById(R.id.dialog_title))
						.setText(R.string.select_type_tv_sxlb);
				map.put("title", "全部量表");
				String projectId = intent.getStringExtra("projectId");
				cursor = db
						.rawQuery(
								"select id, title,theType from projectVector where projectId=?",
								new String[] { projectId });
			}
			list.add(map);
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					map = new HashMap<String, String>();
					map.put("id", StrUtil.nullToStr(cursor.getString(cursor
							.getColumnIndex("id"))));
					if (type == 5) {
						map.put("title", StrUtil.nullToStr(cursor
								.getString(cursor.getColumnIndex("title"))));
					} else {
						map.put("title",
								StrUtil.nullToStr(cursor.getString(cursor
										.getColumnIndex("title")))
										+ "-"
										+ StrUtil.nullToStr(cursor.getString(cursor
												.getColumnIndex("theType"))));
					}

					list.add(map);
					cursor.moveToNext();
				}
				cursor.close();
			}
			db.close();
			ListAdapter listAdapter = new ListAdapter(SelectTypeActivity.this,
					list);
			listView.setAdapter(listAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long arg3) {
					Intent intent = new Intent();
					intent.putExtra("id",
							StrUtil.nullToStr(list.get(position).get("id")));
					intent.putExtra("title",
							StrUtil.nullToStr(list.get(position).get("title")));
					setResult(1, intent);
					SelectTypeActivity.this.finish();
				}
			});
		}else if( type == 7){
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			setContentView(R.layout.select_manage_activity);
			((TextView) findViewById(R.id.dialog_title)).setText("范围");
			((TextView) findViewById(R.id.btn1)).setText(">=");
			((TextView) findViewById(R.id.btn2)).setText("<=");
		}else if( type == 8){
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			setContentView(R.layout.select_image_activity);
			LinearLayout contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
			/*
			((TextView) findViewById(R.id.dialog_title)).setText("范围");
			((TextView) findViewById(R.id.btn1)).setText(">=");
			((TextView) findViewById(R.id.btn2)).setText("<=");
			*/
			String cardPic = intent.getStringExtra("cardPic");
			String[] url = cardPic.split("\\|");
			String projectDir = WSApplication.getDataTemppath() + "/";
			for (int i=0;i<url.length;i++) {
				String u = url[i];
				if (StrUtil.nullToStr(u).equals("")) 
					continue;
				File file = new File(projectDir+u);
				if(!file.exists())
					continue;
				Button button = (Button) contentLayout.findViewWithTag(""+(i+1));
				button.setVisibility(View.VISIBLE);
				String fileName = u.substring(0, u.indexOf("."));
				button.setText(StrUtil.decode(fileName));	
			}
		}/*else if (type == 9) { // 选择图层
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            setContentView(R.layout.select_layer_activity);
            ListView listView = (ListView) findViewById(R.id.listView);
            final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            Manages manages = new Manages(SelectTypeActivity.this);
            SQLiteDatabase db = manages.db();
            Cursor cursor = null;
            ((TextView) findViewById(R.id.dialog_title))
                    .setText(R.string.select_type_tclb);
            cursor = db.rawQuery(
                    "select id, layerName from projectMapPageLayer where visible=1",
                    null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", StrUtil.nullToStr(cursor.getString(cursor
                            .getColumnIndex("id"))));
                    map.put("title", StrUtil.nullToStr(cursor
                            .getString(cursor.getColumnIndex("layerName"))));
                    list.add(map);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            db.close();
            ListAdapter listAdapter = new ListAdapter(SelectTypeActivity.this,
                    list);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View v,
                                        int position, long arg3) {
                    Intent intent = new Intent();
                    intent.putExtra("id",
                            StrUtil.nullToStr(list.get(position).get("id")));
                    intent.putExtra("title",
                            StrUtil.nullToStr(list.get(position).get("title")));
                    setResult(1, intent);
                    SelectTypeActivity.this.finish();
                }
            });
        }*/
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void OnSelectClickListener(View v) {
		int index = StrUtil.nullToInt(v.getTag());
		setResult(index);
		this.finish();
	}
	
	private boolean isAuthority(String type) {
		boolean result = false;
		String sql = "select " + type + " from project";
		SQLiteDatabase db = manages.db();
		Cursor cursor = db.rawQuery(sql,null);
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

}
