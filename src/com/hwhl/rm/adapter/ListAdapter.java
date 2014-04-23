package com.hwhl.rm.adapter;

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
public class ListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<HashMap<String,String>> list;

	public ListAdapter(Context context,
			List<HashMap<String,String>> list) {
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
			convertView = inflater.inflate(R.layout.list_item4, null);
			holder = new ViewHolder();
			//holder.iconImage = (ImageView) convertView.findViewById(ItemIDs[1]);
			holder.nameText = (TextView) convertView.findViewById(R.id.name_txt);
//			/holder.idText = (TextView) convertView.findViewById(R.id.id_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nameText.setText(StrUtil.nullToStr(list.get(position).get("title")));
		return convertView;
	}
	
	static class ViewHolder {
		TextView nameText;
	}

}
