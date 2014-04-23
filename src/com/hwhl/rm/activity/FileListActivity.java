package com.hwhl.rm.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hwhl.rm.R;
import com.hwhl.rm.adapter.FileListAdapter;
import com.hwhl.rm.util.Manages;
import com.hwhl.rm.util.WSApplication;

/**
 * 项目列表
 * 
 * @author Administrator
 * 
 */
public class FileListActivity extends Activity {
	private ListView listView;
	private FileListAdapter fileListAdapter;
	private List<String> fileList;
	private Manages manages;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list_activity);
		listView = (ListView) findViewById(R.id.lv);
		WSApplication ws = (WSApplication) getApplicationContext();
		manages = new Manages(FileListActivity.this);
		// writeFileData();
		// upZip();
		fileList = new ArrayList<String>();
		// 加载数据
		loadData();
		// 定义适配器
		fileListAdapter = new FileListAdapter(this, fileList);
		listView.setAdapter(fileListAdapter);
		// 添加事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// 页面跳转 到群聊页面
				Intent intent = new Intent(FileListActivity.this,
						MapListActivity.class);
				intent.putExtra("name", fileList.get(position));
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}
		});

		super.onCreate(savedInstanceState);
	}

	// 加载数据
	private void loadData() {
		File file = new File(WSApplication.getDatapath());
		// AssetManager asset = FileListActivity.this.getAssets();
		String[] files = file.list();
		if (files == null)
			return;
		// 解析数据
	//	parseFiles(files);
		// 查询目录

		for (String fileStr : files) {
			if(fileStr.indexOf(".risk") != -1)
			{
				fileList.add(fileStr.substring(0,fileStr.indexOf(".risk")));
			}
		}
		/*
		 * SQLiteDatabase db = manages.db(); Cursor cursor =
		 * db.rawQuery("select title from directory",null); if (cursor != null)
		 * { cursor.moveToFirst(); if (!cursor.isAfterLast()) { String title =
		 * StrUtil.nullToStr(cursor.getString(cursor .getColumnIndex("title")));
		 * if(!title.equals("")) { fileList.add(title); } } cursor.close(); }
		 * db.close();
		 */
	}



	/**
	 * 判断文件是否被解析过 即数据库是否有记录
	 * 
	 * @param file
	 */
	private boolean isExistInDB(String file) {
		SQLiteDatabase db = manages.db();
		Cursor cursor = db.rawQuery("select * from directory where title=?",
				new String[] { file });
		if (cursor != null && cursor.getCount() > 0) {
			cursor.close();
			db.close();
			return true;
		}
		if (cursor != null)
			cursor.close();
		db.close();
		return false;
	}
	
	public void OnLeftBtnListener(View view) {
		Uri uri=Uri.parse(getString(R.string.main_link));
		Intent intent=new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intent);
	}

}
