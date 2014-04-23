package com.hwhl.rm.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwhl.rm.R;
import com.hwhl.rm.util.AppManager;
import com.hwhl.rm.util.StrUtil;
import com.hwhl.rm.util.WSApplication;

/**
 * 项目列表
 * 
 * @author Administrator
 * 
 */
public class ImageActivity extends Activity {

	private ImageView zoomIV;
	private TextView titleTV;
	private int currentIndex = 0;
	private String cardPic;

	private String projectDir;

	private int WIDTH, HEIGHT;
	private AbsoluteLayout absoluteLayout;
	// 显示比例
	private double rate = 0.5;
	// 图片宽度
	private int imageWidth;
	// 图片高度
	private int imageHeight;

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

	private int centerX = 0;
	private int centerY = 0;

	private double maxRate = 2;
	private double minRate = 0.2;

	private Button rightBtn;

	private double originRate = 0.5;

	private int originWidth = 0;
	private int originHeight = 0;
	private float originX = 0;
	private float originY = 0;
	private String url;
    private int osVersion = 11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_activity);
        osVersion = AppManager.getAndroidSDKVersion();
		absoluteLayout = (AbsoluteLayout) findViewById(R.id.imageLayout);
		rightBtn = (Button) findViewById(R.id.right_btn);
		url = getIntent().getStringExtra("url");
		projectDir = WSApplication.getDataTemppath() + "/";
		WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		HEIGHT = getWindowManager().getDefaultDisplay().getHeight()
				- convertDIP2PX(45) - 50;
		zoomIV = (ImageView) findViewById(R.id.zoomIV);
		// 加载数据
		loadData();


		absoluteLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					mode = DRAG;
					x_down = event.getX();
					y_down = event.getY();
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					mode = ZOOM;
					oldDist = spacing(event);
					midPoint(mid, event);
					break;
				case MotionEvent.ACTION_MOVE:
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
		});

		super.onCreate(savedInstanceState);
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

	// 右侧按钮事件
	public void OnRightBtnListener(View view) {
		absoluteLayout.setVisibility(View.GONE);
		rightBtn.setVisibility(View.INVISIBLE);
		AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
				originWidth, originHeight, (int) originX, (int) originY);
		zoomIV.setLayoutParams(ivLayoutParams);
		// zoomIV.setX(originX);
		// zoomIV.setY(originY);
	}

	// 左侧按钮事件
	public void OnLeftBtnListener(View view) {
		this.finish();

	}

	// 加载数据
	private void loadData() {
		Bitmap bm = BitmapFactory.decodeFile(projectDir+url);
		if(bm == null)
			return;
		imageWidth = bm.getWidth();
		imageHeight = bm.getHeight();
		centerX = 0;
		centerY = 0;
		// 初始比率
		rate = HEIGHT * 1.0 / imageHeight;
		originRate = rate;
		// 算得最大放大率
		maxRate = rate * 5;
		// 算得最大缩小率
		minRate = rate / 2;
		@SuppressWarnings("deprecation")
		AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
				(int) (imageWidth * rate),
				(int) (imageHeight * rate),
				(int) ((WIDTH - imageWidth * rate) / 2),
				(int) ((HEIGHT - imageHeight * rate) / 2));
		zoomIV.setLayoutParams(ivLayoutParams);
		zoomIV.setImageBitmap(bm);

		originWidth = zoomIV.getWidth();
		originHeight = zoomIV.getHeight();
        if(osVersion < 11){
            originX = zoomIV.getLeft();
            originY = zoomIV.getBottom();
        }else{
            originX = zoomIV.getX();
            originY = zoomIV.getY();
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
		if ((centerX + x) < -WIDTH * rate / 0
				|| (centerX + x) > WIDTH * rate / originRate
				|| (centerY + y) < -HEIGHT * rate / originRate
				|| (centerY + y) > HEIGHT * rate / originRate)
			return;
		centerX += x;
		centerY += y;
		// for (View view : viewList) {
        if(osVersion < 11){
            @SuppressWarnings("deprecation")
            AbsoluteLayout.LayoutParams  params = new AbsoluteLayout.LayoutParams(zoomIV.getWidth(),zoomIV.getHeight(),(int)(zoomIV.getLeft() + x),(int)(zoomIV.getTop() + y));
            zoomIV.setLayoutParams(params);
        }else{
            zoomIV.setX(zoomIV.getX() + x);
            zoomIV.setY(zoomIV.getY() + y);
        }
		// }

	}

	/**
	 * 缩放视图
	 * 
	 * @param x
	 * @param y
	 */
	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	private void zoomView(float scale) {
		if (rate * scale > minRate && rate * scale < maxRate) {
			rate = rate * scale;
			int width = handlerNumber((float) (imageWidth * rate));
			int height = handlerNumber((float) (imageHeight * rate));
			@SuppressWarnings("deprecation")
			AbsoluteLayout.LayoutParams ivLayoutParams = new AbsoluteLayout.LayoutParams(
					width, height, (int) ((WIDTH - imageWidth * rate) / 2),
					(int) ((HEIGHT - imageHeight * rate) / 2));
			zoomIV.setLayoutParams(ivLayoutParams);
		}
	}

	private int handlerNumber(float number) {
		int numInt = (int) number;
		return (number - numInt) > 0.5 ? numInt + 1 : numInt;
	}

	public int convertDIP2PX(int dip) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

}
