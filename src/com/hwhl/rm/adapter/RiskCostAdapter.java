package com.hwhl.rm.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
 * @author Administrator
 *
 */
public class RiskCostAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<HashMap<String,String>> list;
	private Context context;

	public RiskCostAdapter(Context context,
			List<HashMap<String,String>> list) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
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
			convertView = inflater.inflate(R.layout.risk_cost_item, null);
			holder = new ViewHolder();	
			holder.riskNameTV = (TextView) convertView.findViewById(R.id.txt1);
			holder.riskCodeTV = (TextView) convertView.findViewById(R.id.txt2);
			holder.riskTypeTV = (TextView) convertView.findViewById(R.id.txt3);
			holder.beforeGailvTV = (TextView) convertView.findViewById(R.id.txt4);
			holder.beforeAffectTV = (TextView) convertView.findViewById(R.id.txt5);
			
			holder.beforeAffectQiTV = (TextView) convertView.findViewById(R.id.txt6);
			holder.manaChengbenTV = (TextView) convertView.findViewById(R.id.txt7);
			holder.afterGailvTV = (TextView) convertView.findViewById(R.id.txt8);
			holder.afterAffectTV = (TextView) convertView.findViewById(R.id.txt9);
			holder.afterQiTV = (TextView) convertView.findViewById(R.id.txt10);
			
			holder.affectQiTV = (TextView) convertView.findViewById(R.id.txt11);
			holder.shouyiTV = (TextView) convertView.findViewById(R.id.txt12);
			holder.jingshouyiTV = (TextView) convertView.findViewById(R.id.txt13);
			holder.bilvTV = (TextView) convertView.findViewById(R.id.txt14);
	
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		HashMap<String,String> map = list.get(position);
		if(map == null)return null;
		holder.riskNameTV.setText(map.get("riskName"));
		holder.riskCodeTV.setText(map.get("riskCode"));
		holder.riskTypeTV.setText(map.get("riskType"));
		holder.beforeGailvTV.setText(formatBigDecimal(map.get("beforeGailv")));
		holder.beforeAffectTV.setText(formatBigDecimal(map.get("beforeAffect")));
		
		holder.beforeAffectQiTV.setText(formatBigDecimal(map.get("beforeAffectQi")));
		holder.manaChengbenTV.setText(formatBigDecimal(map.get("manaChengben")));
		holder.afterGailvTV.setText(formatBigDecimal(map.get("afterGailv")));
		holder.afterAffectTV.setText(formatBigDecimal(map.get("afterAffect")));
		holder.afterQiTV.setText(formatBigDecimal(map.get("afterQi")));
		
		holder.affectQiTV.setText(formatBigDecimal(map.get("affectQi")));
		holder.shouyiTV.setText(formatBigDecimal(map.get("shouyi")));
		holder.jingshouyiTV.setText(formatBigDecimal(map.get("jingshouyi")));
		holder.bilvTV.setText(formatBigDecimal(map.get("bilv")));
		
		holder.riskNameTV.setWidth(convertDIP2PX(170));
		holder.riskCodeTV.setWidth(convertDIP2PX( 100));
		holder.riskTypeTV.setWidth(convertDIP2PX( 70));
		holder.beforeGailvTV.setWidth(convertDIP2PX( 120));
		holder.beforeAffectTV.setWidth(convertDIP2PX( 140));
		holder.beforeAffectQiTV.setWidth(convertDIP2PX( 170));
		holder.manaChengbenTV.setWidth(convertDIP2PX( 120));
		holder.afterGailvTV.setWidth(convertDIP2PX( 120));
		holder.afterAffectTV.setWidth(convertDIP2PX( 140));
		holder.afterQiTV.setWidth(convertDIP2PX(170));
		holder.affectQiTV.setWidth(convertDIP2PX( 120));
		holder.shouyiTV.setWidth(convertDIP2PX( 120));
		holder.jingshouyiTV.setWidth(convertDIP2PX( 120));
		holder.bilvTV.setWidth(convertDIP2PX( 170));
		
		return convertView;
	}
	
	static class ViewHolder {

		TextView riskNameTV;
		TextView riskCodeTV;
		TextView riskTypeTV;
		TextView beforeGailvTV;
		TextView beforeAffectTV;
		
		TextView beforeAffectQiTV;
		TextView manaChengbenTV;
		TextView afterGailvTV;
		TextView afterAffectTV;
		TextView afterQiTV;
		
		TextView affectQiTV;
		TextView shouyiTV;
		TextView jingshouyiTV;
		TextView bilvTV;
	}
	
	
	public int convertDIP2PX(int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}
	

	/**
	 * 把字符串转换成BigDecimal,并用format进行格式化操作
	 * 
	 * @param obj
	 * @param format
	 * @return
	 */
	public static String formatBigDecimal(String value) {
		if(value.equals("--")){
		return value;
		}
		// if (isNum(StrUtil.nullToDouble(value)+"")) {
		Number bd = StrUtil.nullToDouble(value);
		DecimalFormat df = new DecimalFormat("#.00");
		//setMaximumFractionDigits(int) 设置数值的小数部分允许的最大位数。 
		  //setMaximumIntegerDigits(int)  设置数值的整数部分允许的最大位数。 
		  //setMinimumFractionDigits(int) 设置数值的小数部分允许的最小位数。 
		  //setMinimumIntegerDigits(int)  设置数值的整数部分允许的最小位数.
		   NumberFormat format = NumberFormat.getNumberInstance();
		   format.setMinimumFractionDigits(2);
		   format.setMaximumFractionDigits(2);
		return  "" + format.format(new BigDecimal(df.format(bd)));
		// } else
		// return value;
	}


}
