package com.hwhl.rm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hwhl.rm.R;

/**
 * 项目列表
 * 
 * @author Administrator
 * 
 */
public class Welcome extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
	}

	public void OnBtnListener(View view) {
		Intent intent = new Intent(Welcome.this, FileListActivity.class);
		startActivity(intent);
		finish();
	}

}
