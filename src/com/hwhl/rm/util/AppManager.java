package com.hwhl.rm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * 
 * @author fuchun
 * 
 *         1.获取安装的应用列表 2.获取运行的应用列表 3.安装应用 4.卸载应用 5.运行应用 6.停止应用
 */
public class AppManager {
	private static final String TAG = "AppManager";

	/**
	 * 获取程序包信息(versionCode)
	 * 
	 * @param activity
	 * @return
	 */
	public static int getVerCode() {
		int verCode = -1;
		verCode = getPackageInfo().versionCode;

		return verCode;
	}

	/**
	 * 获取程序包信息(versionName)
	 * 
	 * @param activity
	 * @return
	 */
	public static String getVerName() {
		String verName = "";
		verName = getPackageInfo().versionName;
		return verName;
	}

	/**
	 * 获取程序包信息
	 * 
	 * @param activity
	 * @return
	 */
	public static PackageInfo getPackageInfo() {
		PackageManager pm = WSApplication.context.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(WSApplication.context.getPackageName(), 0);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		return pi;
	}

	/**
	 * 程序包名中最后一个单词 如:com.unisoft.ptl则返回ptl
	 * 
	 * @param activity
	 * @return
	 */
	public static String getPackageLastName() {
		String pkgName = getPackageName();
		return pkgName.substring(pkgName.lastIndexOf(".") + 1);
	}

	/**
	 * 程序包名
	 * 
	 * @param activity
	 * @return
	 */
	public static String getPackageName() {
		String pkgName = getPackageInfo().packageName;
		return pkgName;
	}

	/**
	 * 获取安装的包,MAP
	 * 
	 * @return
	 */
	public static List<PackageInfo> getInstallPkg() {
		// ArrayList<Object> packmInfo = new ArrayList<Object>();
		PackageManager pm = WSApplication.context.getPackageManager();
		// 查询所有已经安装的应用程序
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		return packs;
	}

	/**
	 * 获取正在运行的应用
	 * 
	 * @return
	 */
	public static List<ActivityManager.RunningAppProcessInfo> getRunningProcs() {
		ActivityManager mActivityManager = (ActivityManager) WSApplication.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				.getRunningAppProcesses();
		return appProcessList;
	}

	/**
	 * 获取正在运行的应用
	 * 
	 * @return
	 */
	public static Set<String> getRunningPkgs() {
		Set<String> apppSet = new HashSet<String>();
		ActivityManager mActivityManager = (ActivityManager) WSApplication.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 应用程序位于堆栈的顶层
		List<RunningTaskInfo> tasksInfos = mActivityManager.getRunningTasks(20);
		for (RunningTaskInfo tasksInfo : tasksInfos) {
			apppSet.add(tasksInfo.baseActivity.getPackageName());// 获得运行在该进程里的所有应用程序包
		}
		/*
		 * ActivityManager mActivityManager = (ActivityManager) context
		 * .getSystemService(Context.ACTIVITY_SERVICE); //
		 * 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		 * List<ActivityManager.RunningAppProcessInfo> appProcessList =
		 * mActivityManager .getRunningAppProcesses(); for
		 * (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
		 * String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包 for
		 * (int i = 0; i < pkgNameList.length; i++) {
		 * apppSet.add(pkgNameList[i]); } }
		 */
		return apppSet;
	}

	/**
	 * 获取当前的应用，RunningTaskInfo
	 * 
	 * @return
	 */
	public static RunningTaskInfo getFirstRunningTask() {
		ActivityManager mActivityManager = (ActivityManager) WSApplication.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 应用程序位于堆栈的顶层
		List<RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			return tasksInfo.get(0);
		}
		return null;
	}

	/**
	 * 获取安装的应用列表
	 * 
	 * @param type
	 *            判断是否显示系统应用程序
	 * @return
	 */
	public static ArrayList<Map<String, Object>> getInstallPkg(boolean noSysPkg) {
		ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		PackageManager pm = WSApplication.context.getPackageManager();
		// 得到系统安装的所有程序包的PackageInfo对象
		List<PackageInfo> packs = pm.getInstalledPackages(0);
		for (PackageInfo pi : packs) {
			// 显示用户安装的应用程序，而不显示系统程序
			if (noSysPkg
					&& ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
					|| (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
				continue;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("icon", pi.applicationInfo.loadIcon(pm));// 图标
			map.put("appName", pi.applicationInfo.loadLabel(pm));// 应用程序名称
			map.put("packageName", pi.applicationInfo.packageName);// 应用程序包名
			// 循环读取并存到HashMap中，再增加到ArrayList上，一个HashMap就是一项
			items.add(map);
		}
		return items;
	}

	/**
	 * 获取运行的应用列表,MAP
	 * 
	 * @return
	 */
	public static ArrayList<Map<String, Object>> getRunApps() {
		PackageManager pm = WSApplication.context.getPackageManager();
		// 保存所有正在运行的包名 以及它所在的进程信息
		ActivityManager mActivityManager = (ActivityManager) WSApplication.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				.getRunningAppProcesses();
		Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
			String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包
			// 输出所有应用程序的包名
			for (int i = 0; i < pkgNameList.length; i++) {
				String pkgName = pkgNameList[i];
				// 加入至map对象里
				pgkProcessAppMap.put(pkgName, appProcess);
			}
		}
		// 保存所有正在运行的应用程序信息
		// 查询所有已经安装的应用程序
		List<ApplicationInfo> listAppcations = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		Collections.sort(listAppcations,
				new ApplicationInfo.DisplayNameComparator(pm));// 排序
		ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (ApplicationInfo app : listAppcations) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// 如果该包名存在 则构造一个RunningAppInfo对象
			if (pgkProcessAppMap.containsKey(app.packageName)) {
				// 获得该packageName的 pid 和 processName
				// int pid = pgkProcessAppMap.get(app.packageName).pid;
				String processName = pgkProcessAppMap.get(app.packageName).processName;
				map.put("appName", processName);// 应用程序名称
				map.put("packageName", app.packageName);// 应用程序包名
				items.add(map);
			}
		}
		return items;
	}

	/**
	 * 系统卸载应用
	 */
	public static boolean uninstallPkg(String pkName) {
		Uri uri = Uri.parse("package:" + pkName);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		WSApplication.context.startActivity(intent);
		return true;
	}

	/**
	 * 系统安装应用 pkName .apk路径
	 */
	public static boolean installPkg(String pkName) {
		// 通过启动一个Intent让系统来帮你安装新的APK
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(pkName)),
				"application/vnd.android.package-archive");
		WSApplication.context.startActivity(intent);
		return true;
	}

	/**
	 * 运行应用
	 */
	public static boolean startApp(String pkName) {
		if (!isunning(pkName)) {// 是否在运行
			PackageManager packageManager = WSApplication.context
					.getPackageManager();
			Intent intent = new Intent();
			intent = packageManager.getLaunchIntentForPackage(pkName);
			WSApplication.context.startActivity(intent);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 停止运行应用 pkgName 包名
	 */
	public static boolean stopApp(String pkgName) {
		ActivityManager activityManager = (ActivityManager) WSApplication.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// activityManager.killBackgroundProcesses(pkgName);
		activityManager.restartPackage(pkgName);
		return true;
	}

	/**
	 * 退出应用
	 * 
	 * @param activity
	 */
	public static void ExitApp(Activity activity) {
		if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 8) {
			// Intent startMain = new Intent(Intent.ACTION_MAIN);
			// startMain.addCategory(Intent.CATEGORY_HOME);
			// startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// activity.startActivity(startMain);
			System.exit(0);
		} else {
			ActivityManager am = (ActivityManager) activity
					.getSystemService(Context.ACTIVITY_SERVICE);
			am.restartPackage(activity.getPackageName());
		}
	}

	/**
	 * 检查是否安装
	 * 
	 * @param pkName
	 * @return
	 */
	public static boolean isInstalled(String pkName) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = WSApplication.context.getPackageManager()
					.getPackageInfo(pkName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			// e.printStackTrace();
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检查是否运行
	 * 
	 * @param pkName
	 * @return
	 */
	public static boolean isunning(String pkName) {
		boolean bRunning = false;
		// 保存所有正在运行的包名 以及它所在的进程信息
		ActivityManager mActivityManager = (ActivityManager) WSApplication.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
			String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包
			// 输出所有应用程序的包名
			for (int i = 0; i < pkgNameList.length; i++) {
				if (pkName.equalsIgnoreCase(pkgNameList[i])) {
					bRunning = true;
					break;
				}
			}
		}
		return bRunning;
	}

	/**
	 * 从apk文件里面获取应用相关信息：包名等
	 * 
	 * @param apkName
	 * @return
	 */
	public static String getPkNameByApk(String apkName) {
		PackageManager pm = WSApplication.context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkName,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			// String appName = pm.getApplicationLabel(appInfo).toString();
			String packageName = appInfo.packageName; // 得到安装包名称
			// String version=info.versionName; //得到版本信息

			return packageName;
		}
		return null;
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/**
	 * 获取桌面进程
	 * 
	 * @return
	 */
	public static String getSysLauncherName() {
		// 1切换到HOME
		Intent backhome = new Intent("android.intent.action.MAIN");
		backhome.addCategory("android.intent.category.HOME");
		backhome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		WSApplication.context.startActivity(backhome);
		// 获取桌面任务
		RunningTaskInfo tasksInfo = AppManager.getFirstRunningTask();
		if (tasksInfo == null)
			return "null";
		String pkgName = tasksInfo.baseActivity.getPackageName();
		return pkgName;
	}

	/**
	 * 获取正在运行的应用的进程名称
	 * 
	 * @return
	 */
	public static String getProcessName(String pkgName) {
		String procName = pkgName;
		ActivityManager mActivityManager = (ActivityManager) WSApplication.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
			String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包
			for (int i = 0; i < pkgNameList.length; i++) {
				if (pkgName.equalsIgnoreCase(pkgNameList[i])) {
					procName = appProcess.processName;
					break;
				}
			}
		}
		if (procName == null)
			procName = pkgName;
		return procName;
	}

	/**
	 * 切换到HOME界面
	 * 
	 * @param ctx
	 */
	public static void backToHome(Context ctx) {
		Intent backhome = new Intent("android.intent.action.MAIN");
		backhome.addCategory("android.intent.category.HOME");
		backhome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(backhome);
	}

	/**
	 * 在线程显示Toast
	 */
	private static Handler gMsgHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Toast.makeText(WSApplication.context, (String) msg.obj,
						Toast.LENGTH_LONG).show();
			} else if (msg.what == 2) {
				Toast.makeText(WSApplication.context, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 0 || msg.what == -1) {
				// 提示信息
				new AlertDialog.Builder(WSApplication.context)
						.setTitle(msg.what == -1 ? "错误信息" : "提示信息")
						.setMessage((String) msg.obj)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();
			}
		}
	};

	public static void showToastMessageShort(String msgTxt) {
		showMessage(2, msgTxt);
	}

	private static void showMessage(int msgType, String msgTxt) {
		if (WSApplication.context == null) {
			Log.e("AppManager.showToastMessage", "error: context is null");
			return;
		}

		Message msg = new Message();
		msg.what = msgType;
		msg.obj = msgTxt;
		gMsgHandler.sendMessage(msg);
	}

	/**
	 * 显示播放信息
	 */
	public static void showToastMessage(String msgTxt) {
		showMessage(1, msgTxt);
	}

	/**
	 * 显示提示信息窗口，可在线程处理
	 * 
	 * @param msg
	 *            提示信息
	 */
	public static void ShowDialog(String msgTxt, boolean isError) {
		showMessage(isError ? (-1) : 0, msgTxt);
	}

    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;
    }
}
