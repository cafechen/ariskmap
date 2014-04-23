package com.hwhl.rm.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hwhl.rm.R;
import com.hwhl.rm.model.Project;
import com.hwhl.rm.util.StrUtil;

/**
 * 地图适配器
 * 
 * @author Administrator
 * 
 */
public class RiskListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<HashMap<String, String>> list;
	private int type;

	public RiskListAdapter(Context context, List<HashMap<String, String>> list,
			int type) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.type = type;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			if (type == 0)
				convertView = inflater.inflate(R.layout.risk_list_item, null);
			else
				convertView = inflater.inflate(R.layout.risk_list_item1, null);
			holder = new ViewHolder();
			holder.txt1TV = (TextView) convertView.findViewById(R.id.txt1);
			holder.txt2TV = (TextView) convertView.findViewById(R.id.txt2);
			holder.txt3TV = (TextView) convertView.findViewById(R.id.txt3);
			if (type == 0)
				holder.txt4TV = (TextView) convertView.findViewById(R.id.txt4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, String> map = list.get(position);
		if (type == 0){
		holder.txt1TV.setText(map.get("riskTitle"));
		holder.txt2TV.setText(map.get("riskCode"));
		holder.txt3TV.setText(map.get("riskCato"));
		holder.txt4TV.setText(map.get("riskTypeStr"));		
		}
		else
		{
			holder.txt1TV.setText(map.get("title"));
			holder.txt2TV.setText(map.get("Obj_remark"));
			//holder.txt3TV.setText(map.get("Obj_maptype"));
		}
		/*
		 * holder.affectQiTV.setText(formatBigDecimal(map.get("affectQi")));
		 * holder.shouyiTV.setText(formatBigDecimal(map.get("shouyi")));
		 * holder.jingshouyiTV.setText(formatBigDecimal(map.get("jingshouyi")));
		 * holder.bilvTV.setText(formatBigDecimal(map.get("bilv")));
		 * holder.riskNameTV.setWidth(250); holder.riskCodeTV.setWidth(150);
		 * holder.riskTypeTV.setWidth(150); holder.beforeGailvTV.setWidth(150);
		 * holder.beforeAffectTV.setWidth(150);
		 * holder.beforeAffectQiTV.setWidth(200);
		 * holder.manaChengbenTV.setWidth(150);
		 * holder.afterGailvTV.setWidth(150);
		 * holder.afterAffectTV.setWidth(150); holder.afterQiTV.setWidth(200);
		 * holder.affectQiTV.setWidth(150); holder.shouyiTV.setWidth(150);
		 * holder.jingshouyiTV.setWidth(150); holder.bilvTV.setWidth(200);
		 */
		return convertView;
	}

	// 添加标题
	/*
	 * fxContentLinearLayout.addView(getRow(true,
	 * getString(R.string.risk_list_item_tv_mc),
	 * getString(R.string.risk_list_item_tv_bh),
	 * getString(R.string.risk_list_item_tv_lb),
	 * getString(R.string.risk_list_item_tv_lx))); for (HashMap<String, String>
	 * map : fxList) { // 添加标题 fxLinearLayout.addView(getRow(false,
	 * map.get("riskTitle"), map.get("riskCode"), map.get("riskCato"),
	 * map.get("riskTypeStr"))); }
	 */
	static class ViewHolder {

		TextView txt1TV;
		TextView txt2TV;
		TextView txt3TV;
		TextView txt4TV;

	}
}
