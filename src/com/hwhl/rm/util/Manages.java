package com.hwhl.rm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

import com.hwhl.rm.R;

public class Manages {
	Activity m_activity;
	Context m_context;
	String databaseSys = "rm.db";
	String databaseUser = "user.db";
	public ArrayList<LinkedHashMap<String, Object>> data;
	public String[] Tag;

	/**
	 * 获取数据
	 * 
	 * @param activity
	 * @param sql
	 */
	public Manages(Activity activity) {
		this.m_activity = activity;
		this.m_context = activity;
	}

	/**
	 * 信息提示
	 * 
	 * @param str
	 *            内容
	 */
	public void ToastShow(String str) {
		Toast.makeText(m_activity, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 弹出提示框
	 * 
	 * @param context
	 * @param Title
	 * @param Message
	 * @param y
	 * @param n
	 */
	public void ExitConfirm(String Title, String Message, String yes,
			String no, DialogInterface.OnClickListener OnClickOk,
			DialogInterface.OnClickListener OnClickCancel) {// 退出确认
		AlertDialog.Builder ad = new AlertDialog.Builder(m_context);
		ad.setTitle(Title);
		ad.setMessage(Message);
		ad.setPositiveButton(yes, OnClickOk);
		ad.setNegativeButton(no, OnClickCancel);
		ad.show();// 显示对话框
	}

	/**
	 * @param title
	 *            标题
	 * @param msg
	 *            提示信息
	 * @param buttontxt
	 *            按钮提示内容
	 */
	public void ShowDialog(String title, String msg, String buttontxt) {
		new AlertDialog.Builder(m_activity)
				.setTitle(title)
				.setMessage(msg)
				.setPositiveButton(buttontxt,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();
	}

	/**
	 * 设置语言
	 * 
	 * @param str
	 */
	public void setLan(String str) {
		WSApplication ws = (WSApplication) m_activity.getApplication();
		ws.setlan(str);
	}

	/**
	 * 获取语言
	 */
	public String getLan() {
		WSApplication ws = (WSApplication) m_activity.getApplication();
		return ws.getlan();
	}

	/**
	 * 获取R.string资源文件中的内容
	 * 
	 * @param id
	 * @return
	 */
	public String getString(int id) {
		return m_activity.getResources().getString(id);
	}

	/**
	 * 自动格式化中文字符，把文字排整齐，把中间补空格
	 * 
	 * @param id
	 *            string资源文件id
	 * @return
	 */
	public String getFormatString(int id) {
		return getFormatString(id, false);
	}

	/**
	 * 自动格式化中文字符，把文字排整齐，把中间补空格
	 * 
	 * @param id
	 *            string资源文件id
	 * @return
	 */
	public String[] getFormatStringArray(int id) {
		String[] array = m_activity.getResources().getStringArray(id);
		String[] temp = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			temp[i] = formatString(array[i]);
		}

		return temp;
	}

	/**
	 * 自动格式化中文字符，把文字排整齐，把中间补空格
	 * 
	 * @param id
	 *            string资源文件id
	 * @param colon
	 *            (true=自动去除冒号)
	 * @return
	 */
	public String getFormatString(int id, boolean colon) {
		String str = getString(id).trim();
		if (colon == true) {
			str = str.replace(":", "").replace("：", "");
		}
		if (str.length() == 2) {
			return str.charAt(0) + "        " + str.charAt(1);
		} else if (str.length() == 3) {
			return str.charAt(0) + "  " + str.charAt(1) + "  " + str.charAt(2);
		} else {
			return str;
		}
	}

	public String formatString(String str) {
		if (str.length() == 2) {
			return str.charAt(0) + "        " + str.charAt(1);
		} else if (str.length() == 3) {
			return str.charAt(0) + "  " + str.charAt(1) + "  " + str.charAt(2);
		} else {
			return str;
		}

	}



	/**
	 * 打开本地系统数据库
	 * 
	 * @return
	 */
	public SQLiteDatabase db() {
		SQLiteDatabase sqldb = null;
		try {
			File file = new File(WSApplication.getDbpath()+ databaseSys);
			// 因为本地数据库都是COPY的，所以不能自己创建数据库
			if (!file.exists()) {
				try {
				createFileByStream(
						m_activity.getResources().openRawResource(R.raw.rm),
						WSApplication.getDbpath()+ databaseSys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sqldb = SQLiteDatabase.openOrCreateDatabase(file, null);
		} catch (Exception e) {
			ShowDialog(getString(R.string.msgTitle), e.getMessage(), "OK");
		}
		return sqldb;
	}
	
	public void createDb()
	{
		try {
			File file = new File(WSApplication.getDbpath()+ databaseSys);
			// 因为本地数据库都是COPY的，所以不能自己创建数据库
			if (!file.exists()) {
				createFileByStream(
						m_activity.getResources().openRawResource(R.raw.rm),
						WSApplication.getDbpath()+ databaseSys);	
			}
		} catch (Exception e) {
			ShowDialog(getString(R.string.msgTitle), e.getMessage(), "OK");
		}	
	}

	public static String getSysPath() {
		return WSApplication.getSyspath();

	}
	
	public static String getUserPath() {	
		
		return WSApplication.getSyspath()+"/"+WSApplication.getUserName()+"/";

	}
	
	/*
	public String getUserPath() {
		String userName = "gdswww_no";
		SharedPreferences preferences = getSharedPreferences("gdswww_user",
				Context.MODE_WORLD_READABLE);
		userName = preferences.getString("uid", "");
		if(StrUtil.nullToStr(userName).equals(""))
		{
			userName = "1000";
		}
		return WSApplication.syspath+;

	}
*/
	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 * 
	 */
	public boolean checkSd() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	



	/**
	 * 检查是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public boolean hasInternet() {
		ConnectivityManager manager = (ConnectivityManager) m_activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}


	/**
	 * 从inStream拷贝到SD卡生成文件
	 * 
	 * @return
	 */
	public boolean createFileByStream(InputStream inStream, String dstFileName) {
		try {
			createPathByFile(dstFileName);// 创建多级目录结构

			int len = 0;
			byte[] bytes = new byte[1024];
			FileOutputStream fos = new FileOutputStream(new File(dstFileName));
			while ((len = inStream.read(bytes, 0, 1024)) > 0) {
				fos.write(bytes, 0, len);
			}
			inStream.close();
			fos.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 根据文件建立目录
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean createPathByFile(String fileName) {
		int start = fileName.lastIndexOf("/");
		if (start > 0) {
			String path = fileName.substring(0, start);
			File dstfile = new File(path);
			if (!dstfile.exists())
				dstfile.mkdirs();// 创建多级目录结构
		}
		return true;
	}

	/**
	 * 根据文件建立目录
	 * 
	 * @param fileName
	 * @return
	 */
	public static String encode(String str) {
		String value = "";
		try {
			value = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

}
