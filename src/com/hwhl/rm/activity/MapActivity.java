package com.hwhl.rm.activity;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hwhl.rm.R;
import com.hwhl.rm.model.Map;
import com.hwhl.rm.model.RiskRelation;
import com.hwhl.rm.util.AppManager;
import com.hwhl.rm.util.Manages;
import com.hwhl.rm.util.StrUtil;
import com.hwhl.rm.util.WSApplication;

/**
 * 地图类
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class MapActivity extends Activity implements OnTouchListener {
	private List<Map> mapList;
	private List<RiskRelation> riskRelationList;
	// 右侧按钮
	private Button right_btn;
	// 左侧按钮
	private Button left_btn;
    // 中间按钮
    private Button center_btn;
	private int WIDTH, HEIGHT;
    private int MAP_WIDTH, MAP_HEIGHT;
	// 地图文件目录
	private String projectDir;
	private String projectId;

	private AbsoluteLayout absoluteLayout;
	// 显示比例
	private double rate = 25;

	private Manages manages;

	float x_down = 0;
	float y_down = 0;
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	int mode = NONE;

	boolean matrixCheck = false;

	private List<View> viewList;
	// private

	private int centerX = 0;
	private int centerY = 0;
	// 线视图
	private List<View> lineViewList;
	// 影响
	private List<View> effectViewList;
	//
	private List<View> starViewList;
	// 风险路径存储
	private List<View> riskPathList;
	// 地图标签是否可点击

    private HashMap<String, Boolean> layerMap;

    private String[] layerTitles ;

    private boolean[] layerValues ;

    // 地图标签是否可点击

	private boolean enableBtn = true;
	private float mapWidth = 0f;
	private float mapHeight = 0f;

	private float minPositionX = 0f;
	private float minPositionY = 0f;
	private float maxPositionX = 0f;
	private float maxPositionY = 0f;

	private int originX = 0;
	private int originY = 0;

	private int maxRate = 160;
	private int minRate = 5;
	
	private boolean  showRiskRelation = false;
	DrawRiskRelation riskRelationView;
	private String []imageUrl;
	
	private boolean isRiskRelation = false;
	private int osVersion = 11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		osVersion = AppManager.getAndroidSDKVersion();
		manages = new Manages(MapActivity.this);
		left_btn = (Button) findViewById(R.id.left_title_button_fanhui);
		absoluteLayout = (AbsoluteLayout) findViewById(R.id.mapLayout);
		WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		HEIGHT = getWindowManager().getDefaultDisplay().getHeight()
				- convertDIP2PX(45) - 50;
        MAP_WIDTH = WIDTH ;
        MAP_HEIGHT = HEIGHT ;
		projectId = getIntent().getStringExtra("projectId");
		projectDir = WSApplication.getDataTemppath() + "/";
		mapList = new ArrayList<Map>();
		riskRelationList = new ArrayList<RiskRelation>();
		// 解析数据
        loadLayers();
		loadData();
		initData();

		viewList = new ArrayList<View>();
		lineViewList = new ArrayList<View>();
		effectViewList = new ArrayList<View>();
		starViewList = new ArrayList<View>();
		riskPathList = new ArrayList<View>();

		// 依次划线
		drawView();
		// 给布局添加触摸事件
		absoluteLayout.setOnTouchListener(this);
		super.onCreate(savedInstanceState);
	}

    protected void repaintView() {
        enableBtn = true;
        left_btn.setText(R.string.map_tv_dtlb);
        if(showRiskRelation)
        {
            absoluteLayout.removeView(riskRelationView);
            showRiskRelation = false;
        }
        for (View view : viewList) {
            Map map = (Map) view.getTag();
            for(int i = 0; i < layerTitles.length; i++){
                if(layerValues[i] && map.getBelongLayers().indexOf(layerTitles[i]) >=0){
                    if(osVersion<11)
                    {
                        AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
                        alpha.setDuration(0); // Make animation instant
                        alpha.setFillAfter(true); // Tell it to persist after the animation ends
                        view.startAnimation(alpha);
                    }else{
                        view.setAlpha(1f);
                    }
                    view.setEnabled(true);
                    break ;
                }else{
                    view.setAlpha(0f);
                    view.setEnabled(false);
                }
            }
        }
    }

	/**
	 * 视图平移
	 * 
	 * @param x
	 * @param y
	 */
	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	private void moveView(float x, float y) {
        Log.d("####", "MAP_WIDTH:" + MAP_WIDTH + " MAP_HEIGHT:" + MAP_HEIGHT + " centerX:" + centerX + " centerY:" + centerY) ;
		if ((centerX + x) < - MAP_WIDTH
				|| (centerX + x) > MAP_WIDTH
				|| (centerY + y) < -MAP_HEIGHT
				|| (centerY + y) > MAP_HEIGHT)
			return;
		centerX += x;
		centerY += y;
		for (View view : viewList) {

			if(osVersion<11)
			{
				@SuppressWarnings("deprecation")
				AbsoluteLayout.LayoutParams  params = new AbsoluteLayout.LayoutParams(view.getWidth(),view.getHeight(),(int)(view.getLeft() + x),(int)(view.getTop() + y));
				view.setLayoutParams(params);
			}else{
				view.setX(view.getX() + x);
				view.setY(view.getY() + y);
			}
		}
		
		if(showRiskRelation)
		{
			showRiskRelation();
		}
		

	}

	/**
	 * 缩放视图
	 * 
	 * @param x
	 * @param y
	 */
	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	private void zoomView(float scale) {
        MAP_WIDTH = 0 ;
        MAP_HEIGHT = 0 ;
		if (rate * scale > minRate && rate * scale < maxRate) {
			rate = rate * scale;
			for (View view : viewList) {
				Map map = (Map) view.getTag();
				int positionX = handlerNumber((float) (StrUtil.nullToDouble(map
						.getPositionX()) * rate));
				int positionY = handlerNumber((float) (StrUtil.nullToDouble(map
						.getPositionY()) * rate));
				int width = handlerNumber((float) (StrUtil.nullToDouble(map
						.getWidth()) * rate));
				int height = handlerNumber((float) (StrUtil.nullToDouble(map
						.getHeight()) * rate));
                if((positionX + width) > MAP_WIDTH){
                    MAP_WIDTH = positionX + width ;
                }
                if((positionY + height) > MAP_HEIGHT){
                    MAP_HEIGHT = positionY + height ;
                }
				@SuppressWarnings("deprecation")
				AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
						Math.abs(width), Math.abs(height), originX + positionX
								- Math.abs(width / 2) - 60, -originY + HEIGHT
								- Math.abs(height / 2) - positionY);
				view.setLayoutParams(ivLayoutParams);
			}
			if(showRiskRelation)
			{
				showRiskRelation();
			}
		}
	}

	/**
	 * 左侧按钮点击事件
	 */
	public void OnLeftBtnListener(View v) {
		if (enableBtn) {
			finish();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		} else {
			enableBtn = true;
			left_btn.setText(R.string.map_tv_dtlb);
			if(showRiskRelation)
			{
				absoluteLayout.removeView(riskRelationView);
				showRiskRelation = false;
			}
			repaintView();
		}
	}

    /**
     * 中间按钮点击事件
     */
    public void OnCenterBtnListener(View v) {
        Intent intent = new Intent(MapActivity.this, SelectTypeActivity.class);
        intent.putExtra("type", 9);
        intent.putExtra("isRiskRelation", isRiskRelation);
        startActivityForResult(intent, 4);
        // finish();
    }

	/**
	 * 右侧按钮点击事件
	 */
	public void OnRightBtnListener(View v) {
		Intent intent = new Intent(MapActivity.this, SelectTypeActivity.class);
		intent.putExtra("type", 0);
		intent.putExtra("isRiskRelation", isRiskRelation);
		startActivityForResult(intent, 0);
		// finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			switch (resultCode) {
			case 1:
				//if (!enableBtn) {
					left_btn.setText(R.string.map_tv_dtlb);
					if(showRiskRelation)
					{
						absoluteLayout.removeView(riskRelationView);
						showRiskRelation = false;
					}
					for (View view : viewList) {
                        if(osVersion < 11){
                            AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
                            alpha.setDuration(0); // Make animation instant
                            alpha.setFillAfter(true); // Tell it to persist after the animation ends
                            view.startAnimation(alpha);
                        }else{
                            view.setAlpha(1f);
                        }
					}
				//}
				break;
			case 2:
				Intent hotListIntent = new Intent(MapActivity.this,
						RiskHotActivity.class);
				hotListIntent.putExtra("projectId", projectId);
				startActivity(hotListIntent);
				break;
			case 3:
				Intent riskListIntent = new Intent(MapActivity.this,
						RiskListActivity.class);
				riskListIntent.putExtra("projectId", projectId);
				startActivityForResult(riskListIntent, 1);
				break;
			case 4:
				// 跳到分类统计
				Intent riskStatisticsIntent = new Intent(MapActivity.this,
						RiskStatisticsActivity.class);
				riskStatisticsIntent.putExtra("projectId", projectId);
				startActivity(riskStatisticsIntent);
				break;
			case 5:
				// 跳到风险成本
				Intent riskCostIntent = new Intent(MapActivity.this,
						RiskCostActivity.class);
				riskCostIntent.putExtra("projectId", projectId);
				startActivityForResult(riskCostIntent,1);
				break;
			case 6:
				// 跳到过滤器
				Intent riskFilterIntent = new Intent(MapActivity.this,
						RiskFilterActivity.class);
				riskFilterIntent.putExtra("projectId", projectId);
				startActivityForResult(riskFilterIntent,1);
				break;
			case 7:
				showRiskRelation = true;
				enableBtn = false;
				left_btn.setText(R.string.map_tv_fhdt);

				for (View view : viewList) {
					//if (!isMapExistForRiskPath(view)) {
                    if(osVersion < 11){
                        AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.1f);
                        alpha.setDuration(0); // Make animation instant
                        alpha.setFillAfter(true); // Tell it to persist after the animation ends
                        view.startAnimation(alpha);
                    }else{
                        view.setAlpha(0.1f);
                    }
					//}
				}

				// 显示相关性
				showRiskRelation();
				break;
            case 8:
                new AlertDialog.Builder(this).setTitle("选择图层")
                .setIcon(R.drawable.ic_launcher)
                .setMultiChoiceItems(layerTitles, layerValues, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        layerValues[which] = isChecked ;
                        repaintView();
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        repaintView();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).create().show();
                break;
			default:
				break;
			}
		} else if (requestCode == 1) {
			switch (resultCode) {
			case 1:
				String riskCode = data.getStringExtra("riskCode");
				for (View view : viewList) {
                    if(osVersion < 11){
                        AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.1f);
                        alpha.setDuration(0); // Make animation instant
                        alpha.setFillAfter(true); // Tell it to persist after the animation ends
                        view.startAnimation(alpha);
                    }else{
                        view.setAlpha(0.1f);
                    }
				}
				for (View view : starViewList) {
					Map map = (Map) view.getTag();
					if (map.getObj_remark().equals(riskCode)) {
                        if(osVersion < 11){
                            AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
                            alpha.setDuration(0); // Make animation instant
                            alpha.setFillAfter(true); // Tell it to persist after the animation ends
                            view.startAnimation(alpha);
                        }else{
                            view.setAlpha(1f);
                        }
						break;
					}
				}
				break;

			case 2:
			case 3:
				String id = data.getStringExtra("id");
				for (View view : viewList) {
                    if(osVersion < 11){
                        AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.1f);
                        alpha.setDuration(0); // Make animation instant
                        alpha.setFillAfter(true); // Tell it to persist after the animation ends
                        view.startAnimation(alpha);
                    }else{
                        view.setAlpha(0.1f);
                    }
				}
				for (View view : effectViewList) {
					Map map = (Map) view.getTag();
					if (map.getId().equals(id)) {
                        if(osVersion < 11){
                            AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
                            alpha.setDuration(0); // Make animation instant
                            alpha.setFillAfter(true); // Tell it to persist after the animation ends
                            view.startAnimation(alpha);
                        }else{
                            view.setAlpha(1f);
                        }
						break;
					}

				}
				break;
			default:
				break;
			}
			enableBtn = false;
			left_btn.setText(R.string.map_tv_fhdt);
		} else if (requestCode == 3) {
			if(resultCode<1)
			{
				return;
			}
			Intent intent = new Intent(MapActivity.this,
					ImageActivity.class);
			intent.putExtra("url", imageUrl[resultCode-1]);
			startActivity(intent);
			
		} else if (requestCode == 4) {
            //选择图层
            if (resultCode == 1) {
            }
        }
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 得到xml的数据
	 * 
	 * @return
	 */

    private void loadLayers(){
        //风险图层
        layerMap = new HashMap<String, Boolean>();
        SQLiteDatabase db = manages.db();
        Cursor cursor = null;
        cursor = db.rawQuery(
                "select id, layerName from projectMapPageLayer where visible=1",
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                layerMap.put(StrUtil.nullToStr(cursor
                        .getString(cursor.getColumnIndex("layerName"))), true);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();

        layerTitles = new String[layerMap.size()];
        layerValues = new boolean[layerMap.size()];
        Iterator iter = layerMap.entrySet().iterator();
        int i = 0 ;
        while (iter.hasNext()) {
            java.util.Map.Entry<String, Boolean> entry = (java.util.Map.Entry) iter.next();
            String title = entry.getKey();
            Boolean value = entry.getValue();
            layerTitles[i] = title ;
            layerValues[i] = true;
            i++ ;
        }
    }

	private void loadData() {
        mapList.clear();
		SQLiteDatabase db = manages.db();
		Cursor cursor = db.rawQuery(
				"select * from projectMap where projectId =? and Obj_maptype <> '相关性' ",
				new String[] { projectId });
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Map map = new Map();
				map.setId(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("id"))));
				map.setProjectId(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("projectId"))));
				map.setObjectId(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("objectId"))));
				map.setTitle(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("title"))));
				map.setPositionX(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("positionX"))));
				map.setPositionY(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("positionY"))));
				map.setIsline(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("isline"))));
				map.setFromWho(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("fromWho"))));
				map.setToWho(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("toWho"))));
				map.setPicPng(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("picPng"))));
				String picEmz = StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("picEmz")));
				map.setPicEmz(picEmz);
				picEmz = picEmz.split(".png")[0] + "_1.png";
				Bitmap bm = BitmapFactory.decodeFile(projectDir + picEmz);
				map.setWidth(""+(double)bm.getWidth()/38.286);
				map.setHeight(""+(double)bm.getHeight()/38.286);
				map.setBelongPage(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("belongPage"))));
				map.setObj_maptype(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_maptype"))));
				map.setObj_belongwho(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_belongwho"))));
				map.setObj_remark(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_remark"))));
				map.setObj_db_id(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_db_id"))));
				map.setObj_riskTypeStr(StrUtil.nullToStr(cursor
						.getString(cursor.getColumnIndex("Obj_riskTypeStr"))));
				map.setObj_other1(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_other1"))));
				map.setObj_other2(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_other2"))));
				map.setObj_data1(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_data1"))));
				map.setObj_data2(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_data2"))));
				map.setObj_data3(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("Obj_data3"))));
				map.setLineType(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("lineType"))));
				map.setLineType2(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("lineType2"))));
				map.setIsDel(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("isDel"))));
				map.setLinkPics(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("linkPics"))));
				map.setCardPic(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("cardPic"))));
                map.setBelongLayers(StrUtil.nullToStr(cursor.getString(cursor
                        .getColumnIndex("belongLayers"))));
                mapList.add(map);
				cursor.moveToNext();
			}
			cursor.close();
		}
		
		cursor = db.rawQuery(
				"select * from riskRelation where projectId =?",
				new String[] { projectId });
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				 RiskRelation relation = new RiskRelation();
				 relation.setId(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("id"))));
				 relation.setRelationRemark( cursor.getString(cursor
						.getColumnIndex("relationRemark")));
				 relation.setRiskFrom( StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskFrom"))));
				 relation.setRiskTo(StrUtil.nullToStr(cursor.getString(cursor
						.getColumnIndex("riskTo"))));
				 riskRelationList.add(relation);
				cursor.moveToNext();
			}
			cursor.close();
		}
		db.close();
		//如果相关性为0  则没有相关性
		if(riskRelationList.size() > 0)
		{
			isRiskRelation = true;
		}

	}

	/**
	 * 初始化数据 得到地图的宽度和高度
	 * 
	 * @return
	 */
	private void initData() {
		for (Map map : mapList) {
			float width = Math.abs(StrUtil.nullToDouble(map.getWidth())
					.floatValue());
			float height = Math.abs(StrUtil.nullToDouble(map.getHeight())
					.floatValue());
			float positionX = Math.abs(StrUtil.nullToDouble(map.getPositionX())
					.floatValue());
			float positionY = Math.abs(StrUtil.nullToDouble(map.getPositionY())
					.floatValue());
			maxPositionX = (positionX + width) > maxPositionX ? (positionX + width)
					: maxPositionX;
			maxPositionY = (positionY + height) > maxPositionY ? (positionY + height)
					: maxPositionY;
			minPositionX = positionX < minPositionX ? positionX : minPositionX;
			minPositionY = positionY < minPositionY ? positionY : minPositionY;
		}
		mapWidth = maxPositionX - minPositionX;
		mapHeight = maxPositionY - minPositionY;
		rate = HEIGHT * 4 / (5 * mapHeight);

		originX = (int) (WIDTH - mapWidth * rate) / 2;
		originY = (int) (HEIGHT - mapHeight * rate) / 2;
	}

	/**
	 * 地图描绘
	 */
	private void drawView() {
		// 画影响
		for (Map map : mapList) {
			if (StrUtil.nullToStr(map.getCardPic()).equals("")
					&& !StrUtil.nullToStr(map.getIsline()).equals("1")) {
                Log.d("#### LAYER 画影响", map.getTitle());
				int positionX = handlerNumber((float) ((StrUtil
						.nullToDouble(map.getPositionX()) - minPositionX) * rate));
				int positionY = handlerNumber((float) ((StrUtil
						.nullToDouble(map.getPositionY()) - minPositionY) * rate));
				int width = handlerNumber((float) (StrUtil.nullToDouble(map
						.getWidth()) * rate));
				int height = handlerNumber((float) (StrUtil.nullToDouble(map
						.getHeight()) * rate));
				String picEmz = map.getPicEmz();
				ImageView imageView = new ImageView(MapActivity.this);
				@SuppressWarnings("deprecation")
				AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
						Math.abs(width), Math.abs(height), originX
								+ Math.abs(positionX) - Math.abs(width / 2)
								- 60, -originY + HEIGHT - Math.abs(height / 2)
								- Math.abs(positionY));
				imageView.setLayoutParams(ivLayoutParams);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setTag(map);
				picEmz = picEmz.split(".png")[0] + "_1.png";
				Bitmap bm = BitmapFactory.decodeFile(projectDir + picEmz);
				imageView.setImageBitmap(bm);
				effectViewList.add(imageView);
				// 点击事件
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (enableBtn) {
							enableBtn = false;
							left_btn.setText(R.string.map_tv_fhdt);
							showRiskPath(v);
						}
					}
				});

				// 添加长按事件
				imageView.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						if (isAuthority("show_card")) {
							Map map = (Map) v.getTag();
							String cardPic = map.getCardPic();
							String linkPics = map.getLinkPics();
							if (StrUtil.nullToStr(cardPic).equals("")
									&& StrUtil.nullToStr(linkPics).equals("")) {
								AppManager.showToastMessage("没有关联文件");
								return true;
							}
							String cardPicStr =  cardPic +"|"+ linkPics;
							imageUrl = cardPicStr.split("\\|");
							Intent intent = new Intent(MapActivity.this,
									SelectTypeActivity.class);
							intent.putExtra("type", 8);
							intent.putExtra("cardPic", cardPicStr);
							// addImageView(cardPic.split("\\|"));
							/*Intent intent = new Intent(MapActivity.this,
									ImageActivity.class);
							intent.putExtra("cardPic", cardPic + linkPics);
							*/
							startActivityForResult(intent,3);
						} else {
							AppManager.showToastMessage("你没有权限");
						}
						return true;
					}
				});
				viewList.add(imageView);
				absoluteLayout.addView(imageView);
			}
		}

		// 画箭头
		for (Map map : mapList) {
			if (StrUtil.nullToStr(map.getIsline()).equals("1")) {
				int positionX = handlerNumber((float) ((StrUtil
						.nullToDouble(map.getPositionX()) - minPositionX) * rate));
				int positionY = handlerNumber((float) ((StrUtil
						.nullToDouble(map.getPositionY()) - minPositionY) * rate));
				int width = handlerNumber((float) (StrUtil.nullToDouble(map
						.getWidth()) * rate));
				int height = handlerNumber((float) (StrUtil.nullToDouble(map
						.getHeight()) * rate));
				String picEmz = map.getPicEmz();
				ImageView imageView = new ImageView(MapActivity.this);
				@SuppressWarnings("deprecation")
				AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
						Math.abs(width), Math.abs(height), originX
								+ Math.abs(positionX) - Math.abs(width / 2)
								- 60, -originY + HEIGHT - Math.abs(height / 2)
								- Math.abs(positionY));
				imageView.setLayoutParams(ivLayoutParams);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setTag(map);
				lineViewList.add(imageView);
				picEmz = picEmz.split(".png")[0] + "_1.png";
				Bitmap bm = BitmapFactory.decodeFile(projectDir + picEmz);
				imageView.setImageBitmap(bm);
				viewList.add(imageView);
				// if(width>0 && height>0)
				absoluteLayout.addView(imageView);
			}
		}
		// 画五角星
		for (Map map : mapList) {
			if (!StrUtil.nullToStr(map.getCardPic()).equals("")) {
				int positionX = handlerNumber((float) ((StrUtil
						.nullToDouble(map.getPositionX()) - minPositionX) * rate));
				int positionY = handlerNumber((float) ((StrUtil
						.nullToDouble(map.getPositionY()) - minPositionY) * rate));
				int width = handlerNumber((float) (StrUtil.nullToDouble(map
						.getWidth()) * rate));
				int height = handlerNumber((float) (StrUtil.nullToDouble(map
						.getHeight()) * rate));

				String picEmz = map.getPicEmz();
				ImageView imageView = new ImageView(MapActivity.this);

				@SuppressWarnings("deprecation")
				AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
						Math.abs(width), Math.abs(height), originX
								+ Math.abs(positionX) - Math.abs(width / 2)
								- 60, -originY + HEIGHT - Math.abs(height/2)
								- Math.abs(positionY));
				imageView.setLayoutParams(ivLayoutParams);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setTag(map);
				starViewList.add(imageView);
				picEmz = picEmz.split(".png")[0] + "_1.png";
				Bitmap bm = BitmapFactory.decodeFile(projectDir + picEmz);
				imageView.setImageBitmap(bm);
				// 点击事件
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (enableBtn) {
							enableBtn = false;
							left_btn.setText(R.string.map_tv_fhdt);
							Map map = (Map) v.getTag();
							riskPathList.add(v);
							showRiskPath(map.getObj_other1());
						}
					}
				});

				// 添加长按事件
				imageView.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						if (isAuthority("show_card")) {
							Map map = (Map) v.getTag();
							String cardPic = map.getCardPic();
						//	String linkPics = map.getLinkPics();
							if (StrUtil.nullToStr(cardPic).equals("")) {
								AppManager.showToastMessage("没有关联文件");
								return true;
							}
							/*
							String cardPicStr =  cardPic +"|"+ linkPics;
							imageUrl = cardPicStr.split("\\|");
							Intent intent = new Intent(MapActivity.this,
									SelectTypeActivity.class);
							intent.putExtra("type", 8);
							intent.putExtra("cardPic", cardPicStr);
							*/
							// addImageView(cardPic.split("\\|"));
							Intent intent = new Intent(MapActivity.this,
									ImageActivity.class);
							intent.putExtra("url", cardPic);
							
							startActivityForResult(intent,3);
						} else {
							AppManager.showToastMessage("你没有权限");
						}
						return true;
					}

				});
				viewList.add(imageView);
				absoluteLayout.addView(imageView);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
            Log.d("####", "MotionEvent.ACTION_DOWN") ;
            mode = DRAG;
			x_down = event.getX();
			y_down = event.getY();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
            Log.d("####", "MotionEvent.ACTION_POINTER_DOWN") ;
			mode = ZOOM;
			oldDist = spacing(event);
			midPoint(mid, event);
			break;
		case MotionEvent.ACTION_MOVE:
            Log.d("####", "MotionEvent.ACTION_MOVE") ;
			if (mode == ZOOM) {
				float newDist = spacing(event);
				float scale = newDist / oldDist;
				zoomView(scale);
				oldDist = newDist;
			} else if (mode == DRAG) {
				moveView(event.getX() - x_down, event.getY() - y_down);
				x_down = event.getX();
				y_down = event.getY();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		}
		return true;
	}

	// 触碰两点间距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 取手势中心点
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private int handlerNumber(float number) {
		int numInt = (int) number;
		return (number - numInt) > 0.5 ? numInt + 1 : numInt;
	}
	
	private int handlerNumber(double number) {
		int numInt = (int) number;
		return (number - numInt) > 0.5 ? numInt + 1 : numInt;
	}

	private void showRiskPath(String str) {
		if (StrUtil.nullToStr(str).equals(""))
			return;
		View view = null;
		for (View v : effectViewList) {
			Map map = (Map) v.getTag();
			if (map.getObj_db_id().equals(str)) {
				view = v;
				break;
			}
		}
		if (view == null)
			return;
		showRiskPath(view);
	}

	private void showRiskPath(View v) {
		riskPathList.clear();
		riskPathList.add(v);
		Map map = (Map) v.getTag();
		for (View view : starViewList) {
			Map starMap = (Map) view.getTag();
			if (map.getObj_db_id().equals(starMap.getObj_other1())) {
				riskPathList.add(view);
			}
		}
		Map m = (Map) v.getTag();
		showRiskPath(m);
	}

	private void showRiskPath(Map m) {

        if(m.getBelongLayers().indexOf("风险地图") < 0){
            enableBtn = true;
            left_btn.setText(R.string.map_tv_dtlb);
            if(showRiskRelation)
            {
                absoluteLayout.removeView(riskRelationView);
                showRiskRelation = false;
            }
            return ;
        }

        for (View view : viewList) {
            Map map = (Map) view.getTag();
            if(map.getBelongLayers().indexOf("风险地图") >= 0 || "1".equals(map.getIsline())){
                /*
                if(osVersion < 11){
                    AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
                    alpha.setDuration(0); // Make animation instant
                    alpha.setFillAfter(true); // Tell it to persist after the animation ends
                    view.startAnimation(alpha);
                }else{
                    view.setAlpha(1.0f);
                }
                */
            }else{
                view.setAlpha(0.0f);
                view.setEnabled(false);
            }
        }

		for (View view : lineViewList) {
			Map map = (Map) view.getTag();
			if (StrUtil.nullToStr(map.getFromWho()).equals(m.getObjectId())) {
                Boolean isContinue = true ;
                for (View v : viewList) {
                    Map mm = (Map) v.getTag();
                    if(StrUtil.nullToStr(map.getToWho()).equals(mm.getObjectId())){
                        if(mm.getBelongLayers().indexOf("风险地图") < 0){
                            isContinue = false ;
                        }
                    }
                }
                if(isContinue){
                    riskPathList.add(view);
                    searchFrom(map.getToWho());
                }
			}
			if (StrUtil.nullToStr(map.getToWho()).equals(m.getObjectId())) {
                Boolean isContinue = true ;
                for (View v : viewList) {
                    Map mm = (Map) v.getTag();
                    if(StrUtil.nullToStr(map.getFromWho()).equals(mm.getObjectId())){
                        if(mm.getBelongLayers().indexOf("风险地图") < 0){
                            isContinue = false ;
                        }
                    }
                }
                if(isContinue){
				    riskPathList.add(view);
				    searchTo(map.getFromWho());
                }
			}
		}

		for (View view : viewList) {
			if (!isMapExistForRiskPath(view)) {
                Map map = (Map) view.getTag();
                if(map.getBelongLayers().indexOf("风险地图") < 0){
                    if(osVersion < 11){
                        AlphaAnimation alpha = new AlphaAnimation(0.0F, 0.0F);
                        alpha.setDuration(0); // Make animation instant
                        alpha.setFillAfter(true); // Tell it to persist after the animation ends
                        view.startAnimation(alpha);
                    }else{
                        view.setAlpha(0.0f);
                    }
                }else{
                    if(osVersion < 11){
                        AlphaAnimation alpha = new AlphaAnimation(0.1f, 0.1f);
                        alpha.setDuration(0); // Make animation instant
                        alpha.setFillAfter(true); // Tell it to persist after the animation ends
                        view.startAnimation(alpha);
                    }else{
                        view.setAlpha(0.1f);
                    }
                }
			}
		}
	}

	private void showRiskRelation() {
		List<HashMap<String,String>> mapList = new ArrayList<HashMap<String,String>>(); 
		for(RiskRelation relation:riskRelationList)
		{
				HashMap<String, String> m = new HashMap<String, String>();
				// Map map = new Map();
				String id = relation.getId();
				String relationRemark = relation.getRelationRemark();
				String riskFrom =relation.getRiskFrom();
				String riskTo = relation.getRiskTo();

				float startPointX = 0.0f;
				float startPointY = 0.0f;
				float endPointX = 0.0f;
				float endPointY = 0.0f;

				for (View view : starViewList) {
					Map map = (Map) view.getTag();
					if (map.getObj_data3().equals(riskFrom)) {
                        if(osVersion < 11){
                            AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
                            alpha.setDuration(0); // Make animation instant
                            alpha.setFillAfter(true); // Tell it to persist after the animation ends
                            view.startAnimation(alpha);
                        }else{
                            view.setAlpha(1f);
                        }
                        if(osVersion < 11){
                            startPointX = view.getLeft()+view.getWidth()/2;
                            startPointY = view.getBottom()+view.getHeight()/2;
                        }else{
                            startPointX = view.getX()+view.getWidth()/2;
                            startPointY = view.getY()+view.getHeight()/2;
                        }
					}
					if (map.getObj_data3().equals(riskTo)) {
                        if(osVersion < 11){
                            AlphaAnimation alpha = new AlphaAnimation(1.0F, 1.0F);
                            alpha.setDuration(0); // Make animation instant
                            alpha.setFillAfter(true); // Tell it to persist after the animation ends
                            view.startAnimation(alpha);
                        }else{
                            view.setAlpha(1f);
                        }
                        if(osVersion < 11){
                            endPointX = view.getLeft()+view.getWidth()/2;
                            endPointY = view.getBottom()+view.getHeight()/2;
                        }else{
                            endPointX = view.getX()+view.getWidth()/2;
                            endPointY = view.getY()+view.getHeight()/2;
                        }
					}
				}
				m.put("id", id);
				m.put("startPointX", "" + startPointX);
				m.put("startPointY", "" + startPointY);
				m.put("endPointX", "" + endPointX);
				m.put("endPointY", "" + endPointY);
				m.put("relationRemark",  relationRemark);
				mapList.add(m);	
	}
		if(riskRelationView != null){

			absoluteLayout.removeView(riskRelationView);
		
		}
		riskRelationView = new DrawRiskRelation(MapActivity.this,mapList);
		absoluteLayout.addView(riskRelationView);
		
	}

	private void searchTo(String objectId) {
		for (View view : effectViewList) {
			Map map = (Map) view.getTag();
            if(map.getBelongLayers().indexOf("风险地图") < 0){
                continue;
            }
			if (StrUtil.nullToStr(map.getObjectId()).equals(objectId)) {

                /*
                Boolean isContinue = true ;
                for (View v : viewList) {
                    Map mm = (Map) v.getTag();
                    if(StrUtil.nullToStr(map.getToWho()).equals(mm.getObjectId())){
                        if(mm.getBelongLayers().indexOf("风险地图") < 0){
                            isContinue = false ;
                        }
                    }
                }

                if(!isContinue)
                    continue;
                    */

				if (!isMapExistForRiskPath(view)) {
					riskPathList.add(view);
					// String lineObjectId = map.getToWho();
					// 添加影响所属星星
					for (View viewStar : starViewList) {
						Map starMap = (Map) viewStar.getTag();
						if (map.getObj_db_id().equals(starMap.getObj_other1())) {
							if (!isMapExistForRiskPath(viewStar)) {
								riskPathList.add(viewStar);
							}
						}
					}
					for (View viewLine : lineViewList) {
						Map mapLine = (Map) viewLine.getTag();
						if (mapLine.getToWho().equals(objectId)) {
							if (!isMapExistForRiskPath(viewLine)) {
                                Boolean isContinue = true ;
                                for (View v : viewList) {
                                    Map mm = (Map) v.getTag();
                                    if(StrUtil.nullToStr(mapLine.getFromWho()).equals(mm.getObjectId())){
                                        if(mm.getBelongLayers().indexOf("风险地图") < 0){
                                            isContinue = false ;
                                        }
                                    }
                                }
                                if(isContinue){
                                    riskPathList.add(viewLine);
                                    searchTo(mapLine.getFromWho());
                                }
							}
						}
					}
				}
			}
		}
	}

	private void searchFrom(String objectId) {
		for (View view : effectViewList) {
			Map map = (Map) view.getTag();
            if(map.getBelongLayers().indexOf("风险地图") < 0){
                continue;
            }
			if (StrUtil.nullToStr(map.getObjectId()).equals(objectId)) {

                /*
                Boolean isContinue = true ;
                for (View v : viewList) {
                    Map mm = (Map) v.getTag();
                    if(StrUtil.nullToStr(map.getToWho()).equals(mm.getObjectId())){
                        if(mm.getBelongLayers().indexOf("风险地图") < 0){
                            isContinue = false ;
                        }
                    }
                }

                if(!isContinue)
                    continue;
                    */

				if (!isMapExistForRiskPath(view)) {
					riskPathList.add(view);
					// 添加影响所属星星
					for (View viewStar : starViewList) {
						Map starMap = (Map) viewStar.getTag();
						if (map.getObj_db_id().equals(starMap.getObj_other1())) {
							riskPathList.add(viewStar);
						}
					}
					// String lineObjectId = map.getFromWho();
					for (View viewLine : lineViewList) {
						Map mapLine = (Map) viewLine.getTag();
						if (mapLine.getFromWho().equals(objectId)) {
                            Boolean isContinue = true ;
                            for (View v : viewList) {
                                Map mm = (Map) v.getTag();
                                if(StrUtil.nullToStr(mapLine.getToWho()).equals(mm.getObjectId())){
                                    if(mm.getBelongLayers().indexOf("风险地图") < 0){
                                        isContinue = false ;
                                    }
                                }
                            }
                            if(isContinue){
                                riskPathList.add(viewLine);
                                searchFrom(mapLine.getToWho());
                            }
						}
					}
				}
			}
		}
	}

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

	/**
	 * 是否存在记录
	 * 
	 * @param v
	 * @return
	 */
	private boolean isMapExistForRiskPath(View v) {
		for (View view : riskPathList) {
			if (view == v) {
				return true;
			}
		}
		return false;
	}

	public int convertDIP2PX(int dip) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

    public void switchMapLayer(){

    }

	public class DrawRiskRelation extends View {
		private List<HashMap<String,String>> list;

		DrawRiskRelation(Context context,List<HashMap<String,String>> list) {
			super(context);
			this.list = list;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);

			// 首先定义一个paint
			Paint paint = new Paint();

			// 绘制矩形区域-实心矩形
			// 设置颜色
			paint.setColor(Color.TRANSPARENT);
			// 设置样式-填充
			paint.setStyle(Style.FILL);
			// 绘制一个矩形
			//canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
			// canvas.drawLine(startX, startY, stopX, stopY, paint);

			// 绘空心矩形
			// 设置颜色
			paint.setColor(Color.RED);
			// 设置样式-空心矩形
		//	paint.setStyle(Style.STROKE);
			// 绘制一个矩形
			//canvas.drawRect(new Rect(10, 10, 100, 30), paint);

			// 绘文字
			// 设置颜色
		//	paint.setColor(Color.GREEN);
			// 绘文字
		//	canvas.drawText("Hello", 10, 50, paint);
			paint.setStrokeWidth((float) 3.0); 
			paint.setTextSize((int)rate);
			for(HashMap<String,String> map :list)
			{
				float startPointX = StrUtil.StringToFloat(map.get("startPointX"));
				float startPointY = StrUtil.StringToFloat(map.get("startPointY"));
				float endPointX = StrUtil.StringToFloat(map.get("endPointX"));
				float endPointY = StrUtil.StringToFloat(map.get("endPointY"));
				canvas.drawLine(startPointX, startPointY, endPointX, endPointY, paint);
				paint.setColor(Color.BLACK);
				//http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2013/0409/1143.html
				canvas.drawText(map.get("relationRemark"),(startPointX+endPointX)/2, (startPointY+endPointY)/2, paint);
				paint.setColor(Color.RED);
			}

			// 绘图
			// 从资源文件中生成位图
			// Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
			// R.drawable.icon);
			// 绘图
			// canvas.drawBitmap(bitmap, 10, 60, paint);
		}

	}

}
