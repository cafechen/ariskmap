package com.hwhl.rm.util;

import java.io.File;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.hwhl.rm.R;

/**
 * APK上下文
 * 
 * @author
 * 
 */
public class WSApplication extends Application {
	public static String userName;
	public static String uid;
	/** 当前语言 */
	String lan = null;
	public static String syspath = null;
	/** 上下文 */
	public static Context context;
	public Thread thread;
	public static String datapath = null;
	public static String dataTempPath = null;
	public static String dbpath = null;

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("WSApplication init");
		// 初始化参数
		context = this.getApplicationContext();
		//数据路径
		datapath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/RiskMap/data/";
		//数据路径
		dataTempPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/RiskMap/temp/";
		//数据路径
		dbpath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/RiskMap/db/";
		//获取应用程序路径    当SD卡不存在时 获取应用程序下路径   否则取得SD卡的路径
		if (FileUtil.getSDPath() != null) {
			File dataFile = new File(datapath);
			if(!dataFile.exists())
			{
				dataFile.mkdirs();
			}
			
			File dbFile = new File(dbpath);
			if(!dbFile.exists())
			{
				dbFile.mkdirs();
			}
			
			File dataTempTempFile = new File(dataTempPath);
			if(!dataTempTempFile.exists())
			{
				dataTempTempFile.mkdirs();
			}
		}
		else
		{
			AppManager.showToastMessage(getString(R.string.sd_error));
		}

		/*
		 * // log TrSetData.checkLogPath(); CrashHandler crashHandler =
		 * CrashHandler.getInstance();
		 * crashHandler.init(getApplicationContext()); //
		 * TrSetData.checkDbSys(); TrSetData.getAllConfig();// 初始化所有设置数据 //
		 * Log.i("System", TrSetData.LAND_ID); setlan(TrSetData.LAND_ID);
		 * LocalDBPatch.checkDBVersion(TrSetData.getDbSysPathName(),
		 * LocalDBPatch.SYSTEM_DB);
		 */
	}

	/**
	 * 获取语言全局变量
	 * 
	 * @return
	 */
	public String getlan() {
		return lan;
	}

	/**
	 * 设置语言全局变量
	 * 
	 * @param s
	 */
	public void setlan(String s) {
		this.lan = s;
		Resources resources = getResources();// 获得res资源对象
		Configuration config = resources.getConfiguration();// 获得设置对象
		DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
		if ("1".equals(s)) {
			config.locale = Locale.SIMPLIFIED_CHINESE; // 简体中文
			resources.updateConfiguration(config, dm);
		} else if ("2".equals(s)) {
			config.locale = Locale.ENGLISH; // 英文
			resources.updateConfiguration(config, dm);
		}
	}

	
	public static String getUserName() {
		if(StrUtil.nullToStr(userName).equals(""))
		{
			SharedPreferences preferences = context.getSharedPreferences("gdswww_user",
					Context.MODE_WORLD_READABLE);
			userName = preferences.getString("name", "");
			
		}
		return userName;
	}
	



	public static String getSyspath() {
		if(StrUtil.nullToStr(syspath).equals(""))
		{
			if (FileUtil.getSDPath() == null) {
				syspath = context.getFilesDir().getAbsolutePath()+ "/RiskMap/";	
			} else {
				syspath = android.os.Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/Navigate/";
			}
			
		}
		return syspath;
	}
	
	//得到数据路径
	public static String getDatapath() {
		if(datapath == null)
		{
		return Environment.getExternalStorageDirectory()
				.getAbsolutePath()+ "/RiskMap/data/";
		}
		return datapath;
	}
	
	//得到数据路径
	public static String getDataTemppath() {
		if(dataTempPath == null)
		{
		return Environment.getExternalStorageDirectory()
				.getAbsolutePath()+ "/RiskMap/temp/";
		}
		return dataTempPath;
	}
	
	//得到数据路径
	public static String getDbpath() {
		if(dbpath == null)
		{
		return Environment.getExternalStorageDirectory()
				.getAbsolutePath()+ "/RiskMap/db/";
		}
		return dbpath;
	}


	public static void setSyspath(String syspath) {
		WSApplication.syspath = syspath;
	}

	public static String getUid() {
		if(StrUtil.nullToStr(uid).equals(""))
		{
			SharedPreferences preferences = context.getSharedPreferences("gdswww_user",
					Context.MODE_WORLD_READABLE);
			uid = preferences.getString("uid", "");
			
		}
		return uid;
	}

	public static void setUid(String uid) {
		WSApplication.uid = uid;
	}

	public static void setUserName(String userName) {
		WSApplication.userName = userName;
	}

	public void initThread() {
		if (thread != null) {
			try {
				thread.sleep(50);
			} catch (Exception e) {
			}
			try {
				thread.interrupt();
				thread = null;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
