package com.hwhl.rm.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hwhl.rm.R;
import com.hwhl.rm.adapter.ProjectListAdapter;
import com.hwhl.rm.model.DicType;
import com.hwhl.rm.model.Directory;
import com.hwhl.rm.model.Map;
import com.hwhl.rm.model.Project;
import com.hwhl.rm.model.ProjectMatrix;
import com.hwhl.rm.model.ProjectVector;
import com.hwhl.rm.model.ProjectVectorDetail;
import com.hwhl.rm.model.Risk;
import com.hwhl.rm.model.RiskCost;
import com.hwhl.rm.model.RiskRelation;
import com.hwhl.rm.model.RiskScore;
import com.hwhl.rm.model.RiskScoreFather;
import com.hwhl.rm.util.FileUtil;
import com.hwhl.rm.util.Manages;
import com.hwhl.rm.util.StrUtil;
import com.hwhl.rm.util.WSApplication;
import com.hwhl.rm.xml.DicTypeDataParser;
import com.hwhl.rm.xml.DirectoryDataParser;
import com.hwhl.rm.xml.MapDataParser;
import com.hwhl.rm.xml.ProjectDataParser;
import com.hwhl.rm.xml.ProjectMatrixDataParser;
import com.hwhl.rm.xml.ProjectVectorDataParser;
import com.hwhl.rm.xml.ProjectVectorDetailDataParser;
import com.hwhl.rm.xml.RiskCostDataParser;
import com.hwhl.rm.xml.RiskDataParser;
import com.hwhl.rm.xml.RiskRelationDataParser;
import com.hwhl.rm.xml.RiskScoreDataParser;
import com.hwhl.rm.xml.RiskScoreFatherDataParser;

/**
 * 地图列表
 * 
 * @author Administrator
 * 
 */
public class MapListActivity extends Activity {
	private ListView listView;
	private ProjectListAdapter mapListAdapter;
	private List<Project> projectList;
	private String projectDir;
	private String fileName;
	private Manages manages;
	// 定位进度条
	private ProgressDialog dialog;
	private String zipFileName;
	private String zipTempFileName;

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
			case 1:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_list_activity);
		manages = new Manages(MapListActivity.this);
		fileName = getIntent().getStringExtra("name");
		//获取待解压的risk文件
		zipFileName = WSApplication.getDatapath() + fileName + ".risk";
		//新建临时文件 准备解压
		zipTempFileName = WSApplication.getDataTemppath() + "temp.zip";
		//获取地图数据目录
		projectDir = WSApplication.getDataTemppath();
		listView = (ListView) findViewById(R.id.lv);
		projectList = new ArrayList<Project>();
		if (projectList == null)
			return;
		// 定义适配器
		mapListAdapter = new ProjectListAdapter(this, projectList);
		listView.setAdapter(mapListAdapter);

		// 添加事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// 跳转到地图显示界面
				Intent intent = new Intent(MapListActivity.this,
						MapActivity.class);
				// 地图ID
				intent.putExtra("projectId", projectList.get(position).getId());
				// 地图ID
				intent.putExtra("fileName", fileName);
				startActivity(intent);
			}
		});
		
		new AsyncHandleData().execute();
		super.onCreate(savedInstanceState);
	}

	public void OnLeftBtnListener(View v) {
		finish();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	private class AsyncHandleData extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(MapListActivity.this);
			dialog.setMessage("正在处理数据,请稍等...");
			dialog.show();
			dialog.setCancelable(false);
		}

		@SuppressLint({ "SdCardPath", "SimpleDateFormat" })
		@Override
		protected String doInBackground(Void... params) {
			try {
				//拷贝文件
				FileUtil.createFileByStream(new FileInputStream(new File(
						zipFileName)), zipTempFileName);
				File file = new File(zipTempFileName);
				//删除原来的目录
				File oldFile = new File(WSApplication.getDataTemppath()+"project");
				oldFile.delete();
				if (file.exists()) {
					upzip(zipTempFileName, WSApplication.getDataTemppath());
					file.delete();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			loadData();
			dialog.dismiss();
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			mapListAdapter.notifyDataSetInvalidated();
		}
	}


	/**
	 * 得到xml的数据
	 *
	 * @return
	 */
	private void loadData() {
		parseFile(projectDir);
        android.util.Log.v("########", "loadData open");
		SQLiteDatabase db = manages.db();
		Cursor cursor = db.rawQuery("select id,title from project", null);
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Project project = new Project();
				project.setId(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("id"))));
				project.setTitle(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("title"))));
				projectList.add(project);
				cursor.moveToNext();
			}
			cursor.close();
		}
        android.util.Log.v("########", "loadData close");
		db.close();
	}

	/**
	 * 解析xml文件
	 * 
	 * @param dir
	 */
	private void parseFile(String dir) {
		SQLiteDatabase db = manages.db();
		db.beginTransaction();
		db.execSQL("delete from dictype");
		db.execSQL("delete from directory");
		db.execSQL("delete from project");
		db.execSQL("delete from projectMap");
		db.execSQL("delete from projectMatrix");
		db.execSQL("delete from projectVector");
		db.execSQL("delete from projectVectorDetail");
		db.execSQL("delete from risk");
		db.execSQL("delete from riskCost");
		db.execSQL("delete from riskScore");
		db.setTransactionSuccessful();
		db.endTransaction();
		InputStream is = null;
		try {
			is = new FileInputStream(dir + "project/t_project_folder.xml");
			List<Directory> itemList = new DirectoryDataParser().getItems(is);
			db.beginTransaction();
			for (Directory item : itemList) {
				db.execSQL(
						"insert into directory(id, fatherid , title )values(?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getFatherid()),
								StrUtil.nullToStr(item.getTitle()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			is = new FileInputStream(dir + "project/project.xml");
			List<Project> projectList = new ProjectDataParser().getProjects(is);
			db.beginTransaction();
			for (Project item : projectList) {
				db.execSQL(
						"insert into project( id, fatherid, title, belong_department,remark,AddDate,isUpload , isComplete , show_card , show_hot , show_sort , show_chengben , show_static , show_after,huobi )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getFatherId()),
								StrUtil.nullToStr(item.getTitle()),
								StrUtil.nullToStr(item.getBelong_department()),
								StrUtil.nullToStr(item.getRemark()),
								StrUtil.nullToStr(item.getAddDate()),
								StrUtil.nullToStr(item.getIsUpload()),
								StrUtil.nullToStr(item.getIsComplete()),
								StrUtil.nullToStr(item.getShow_card()),
								StrUtil.nullToStr(item.getShow_hot()),
								StrUtil.nullToStr(item.getShow_sort()),
								StrUtil.nullToStr(item.getShow_chengben()),
								StrUtil.nullToStr(item.getShow_static()),
								StrUtil.nullToStr(item.getShow_after())
						 ,StrUtil.nullToStr(item.getHuobi())
						});
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			is = new FileInputStream(dir + "project/projectMap.xml");
			List<Map> mapList = new MapDataParser().getMaps(is);
			db.beginTransaction();
			for (Map item : mapList) {
				db.execSQL(
						"insert into projectMap(id , projectId , objectId , title ,positionX , positionY ,width , height ,isline , picPng ,picEmz ,belongPage , Obj_maptype , Obj_belongwho , Obj_remark , Obj_db_id , Obj_riskTypeStr , Obj_other1 ,"
								+ " Obj_other2 ,Obj_data1 ,Obj_data2 ,Obj_data3 ,lineType ,lineType2 ,isDel ,linkPics ,cardPic ,fromWho ,toWho )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getProjectId()),
								StrUtil.nullToStr(item.getObjectId()),
								StrUtil.nullToStr(item.getTitle()),
								StrUtil.nullToStr(item.getPositionX()),
								StrUtil.nullToStr(item.getPositionY()),
								StrUtil.nullToStr(item.getWidth()),
								StrUtil.nullToStr(item.getHeight()),
								StrUtil.nullToStr(item.getIsline()),
								StrUtil.nullToStr(item.getPicPng()),
								StrUtil.nullToStr(item.getPicEmz()),
								StrUtil.nullToStr(item.getBelongPage()),
								StrUtil.nullToStr(item.getObj_maptype()),
								StrUtil.nullToStr(item.getObj_belongwho()),
								StrUtil.nullToStr(item.getObj_remark()),
								StrUtil.nullToStr(item.getObj_db_id()),
								StrUtil.nullToStr(item.getObj_riskTypeStr()),
								StrUtil.nullToStr(item.getObj_other1()),
								StrUtil.nullToStr(item.getObj_other2()),
								StrUtil.nullToStr(item.getObj_data1()),
								StrUtil.nullToStr(item.getObj_data2()),
								StrUtil.nullToStr(item.getObj_data3()),
								StrUtil.nullToStr(item.getLineType()),
								StrUtil.nullToStr(item.getLineType2()),
								StrUtil.nullToStr(item.getIsDel()),
								StrUtil.nullToStr(item.getLinkPics()),
								StrUtil.nullToStr(item.getCardPic()),
								StrUtil.nullToStr(item.getFromWho()),
								StrUtil.nullToStr(item.getToWho()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			is = new FileInputStream(dir + "project/projectMatrix.xml");
			List<ProjectMatrix> projectMatrixList = new ProjectMatrixDataParser()
					.getItems(is);
			db.beginTransaction();
			for (ProjectMatrix item : projectMatrixList) {
				db.execSQL(
						"insert into projectMatrix(id, projectId, matrix_title , matrix_x , matrix_y ,fatherid_matrix , xIndex , yIndex , Color , levelType )values(?,?,?,?,?,?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getProjectId()),
								StrUtil.nullToStr(item.getMatrix_title()),
								StrUtil.nullToStr(item.getMatrix_x()),
								StrUtil.nullToStr(item.getMatrix_y()),
								StrUtil.nullToStr(item.getFatherid_matrix()),
								StrUtil.nullToStr(item.getxIndex()),
								StrUtil.nullToStr(item.getyIndex()),
								StrUtil.nullToStr(item.getColor()),
								StrUtil.nullToStr(item.getLevelType()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			is = new FileInputStream(dir + "project/project_vector.xml");
			List<ProjectVector> projectVectorList = new ProjectVectorDataParser()
					.getItems(is);
			db.beginTransaction();
			for (ProjectVector item : projectVectorList) {
				db.execSQL(
						"insert into projectVector(id, title , remark , theType , projectId)values(?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getTitle()),
								StrUtil.nullToStr(item.getRemark()),
								StrUtil.nullToStr(item.getTheType()),
								StrUtil.nullToStr(item.getProjectId()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			is = new FileInputStream(dir + "project/project_vectordetail.xml");
			List<ProjectVectorDetail> projectVectorDetailList = new ProjectVectorDetailDataParser()
					.getItems(is);
			db.beginTransaction();
			for (ProjectVectorDetail item : projectVectorDetailList) {
				db.execSQL(
						"insert into projectVectorDetail(id , fatherid , levelTitle , score ,remarkTitle , remarkContent , theType , projectId , sort )values(?,?,?,?,?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getFatherid()),
								StrUtil.nullToStr(item.getLevelTitle()),
								StrUtil.nullToStr(item.getScore()),
								StrUtil.nullToStr(item.getRemarkTitle()),
								StrUtil.nullToStr(item.getRemarkContent()),
								StrUtil.nullToStr(item.getTheType()),
								StrUtil.nullToStr(item.getProjectId()),
								StrUtil.nullToStr(item.getSort()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			is = new FileInputStream(dir + "project/risk.xml");
			List<Risk> riskList = new RiskDataParser().getItems(is);
			db.beginTransaction();
			for (Risk item : riskList) {
				db.execSQL(
						"insert into risk(id, projectId , pageDetailId , riskTitle ,riskCode , riskTypeId ,riskTypeStr , pageId  )values(?,?,?,?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getProjectId()),
								StrUtil.nullToStr(item.getPageDetailId()),
								StrUtil.nullToStr(item.getRiskTitle()),
								StrUtil.nullToStr(item.getRiskCode()),
								StrUtil.nullToStr(item.getRiskTypeId()),
								StrUtil.nullToStr(item.getRiskTypeStr()),
								StrUtil.nullToStr(item.getPageId()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			is = new FileInputStream(dir + "project/T_resultTemp.xml");
			List<RiskCost> riskCostList = new RiskCostDataParser().getItems(is);
			db.beginTransaction();
			for (RiskCost item : riskCostList) {
				db.execSQL(
						"insert into riskCost(id, riskName, riskCode, riskType,beforeGailv, beforeAffect, beforeAffectQi, manaChengben, afterGailv, afterAffect, afterQi, affectQi, shouyi, jingshouyi, bilv, projectId, pageid, riskvecorid, chanceVecorid)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getRiskName()),
								StrUtil.nullToStr(item.getRiskCode()),
								StrUtil.nullToStr(item.getRiskType()),
								""+StrUtil.nullToDouble(item.getBeforeGailv()),
								""+StrUtil.nullToDouble(item.getBeforeAffect()),
								""+StrUtil.nullToDouble(item.getBeforeAffectQi()),
								""+StrUtil.nullToDouble(item.getManaChengben()),
								""+StrUtil.nullToDouble(item.getAfterGailv()),
								""+StrUtil.nullToDouble(item.getAfterAffect()),
								""+StrUtil.nullToDouble(item.getAfterQi()),
								""+StrUtil.nullToDouble(item.getAffectQi()),
								""+StrUtil.nullToDouble(item.getShouyi()),
								""+StrUtil.nullToDouble(item.getJingshouyi()),
								""+StrUtil.nullToDouble(item.getBilv()),
								StrUtil.nullToStr(item.getProjectId()),
								StrUtil.nullToStr(item.getPageid()),
								StrUtil.nullToStr(item.getRiskvecorid()),
								StrUtil.nullToStr(item.getChanceVecorid()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			is = new FileInputStream(dir + "project/risk_score.xml");
			List<RiskScore> riskScoreList = new RiskScoreDataParser()
					.getItems(is);
			db.beginTransaction();
			for (RiskScore item : riskScoreList) {
				db.execSQL(
						"insert into riskScore(id , riskid , scoreVectorId , scoreBefore ,scoreEnd)values(?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getRiskid()),
								StrUtil.nullToStr(item.getScoreVectorId()),
								StrUtil.nullToStr(item.getScoreBefore()),
								StrUtil.nullToStr(item.getScoreEnd()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();

			is = new FileInputStream(dir + "project/dictype.xml");
			List<DicType> dicTypeList = new DicTypeDataParser().getItems(is);
			db.beginTransaction();
			for (DicType item : dicTypeList) {
				db.execSQL(
						"insert into dictype(id ,fatherId ,title ,typeStr ,isDel)values(?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getFatherId()),
								StrUtil.nullToStr(item.getTitle()),
								StrUtil.nullToStr(item.getTypeStr()),
								StrUtil.nullToStr(item.getIsDel()), });
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			
			is = new FileInputStream(dir + "project/T_project_risk_Relation.xml");
			List<RiskRelation> riskRelationList = new RiskRelationDataParser().getItems(is);
			db.beginTransaction();
			for (RiskRelation item : riskRelationList) {
				db.execSQL(
						"insert into riskRelation(id ,projectid ,riskFrom ,riskTo ,relationRemark)values(?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getProjectid()),
								StrUtil.nullToStr(item.getRiskFrom()),
								StrUtil.nullToStr(item.getRiskTo()),
								StrUtil.nullToStr(item.getRelationRemark()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			
			is = new FileInputStream(dir + "project/risk_score_father.xml");
			List<RiskScoreFather> riskScoreFatherList = new RiskScoreFatherDataParser().getItems(is);
			db.beginTransaction();
			for (RiskScoreFather item : riskScoreFatherList) {
				db.execSQL(
						"insert into riskScoreFather(id ,riskId ,before ,Send ,projectId)values(?,?,?,?,?)",
						new String[] { StrUtil.nullToStr(item.getId()),
								StrUtil.nullToStr(item.getRiskId()),
								StrUtil.nullToStr(item.getBefore()),
								StrUtil.nullToStr(item.getSend()),
								StrUtil.nullToStr(item.getProjectId()) });
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db != null && db.isOpen()) {
                if(db.inTransaction()){
                    db.endTransaction();
                }
                db.close();
			}
		}
	}

	/**
	 * 解压一个压缩文档 到指定位置
	 * 
	 * @param zipFileString
	 *            压缩包的名字
	 * @param outPathString
	 *            指定的路径
	 * @throws Exception
	 */
	public static void upzip(String zipFilePath, String targetPath)
			throws Exception {
		OutputStream os = null;
		InputStream is = null;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zipFilePath);
			String directoryPath = "";
			if (null == targetPath || "".equals(targetPath)) {
				directoryPath = zipFilePath.substring(0,
						zipFilePath.lastIndexOf("."));
			} else {
				directoryPath = targetPath;
			}
			Enumeration entryEnum = zipFile.entries();
			if (null != entryEnum) {
				ZipEntry zipEntry = null;
				while (entryEnum.hasMoreElements()) {
					zipEntry = (ZipEntry) entryEnum.nextElement();
					if (zipEntry.isDirectory()) {
						directoryPath = directoryPath + File.separator
								+ zipEntry.getName();
						System.out.println(directoryPath);
						continue;
					}
					if (zipEntry.getSize() > 0) {
						// 文件
						File targetFile = FileUtil.buildFile(directoryPath
								+ File.separator + zipEntry.getName(), false);
						os = new BufferedOutputStream(new FileOutputStream(
								targetFile));
						is = zipFile.getInputStream(zipEntry);
						byte[] buffer = new byte[4096];
						int readLen = 0;
						while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
							os.write(buffer, 0, readLen);
						}

						os.flush();
						os.close();
					} else {
						// 空目录
						FileUtil.buildFile(directoryPath + File.separator
								+ zipEntry.getName(), true);
					}
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (null != zipFile) {
				zipFile = null;
			}
			if (null != is) {
				is.close();
			}
			if (null != os) {
				os.close();
			}
		}

	}// end of func
	
	
}
