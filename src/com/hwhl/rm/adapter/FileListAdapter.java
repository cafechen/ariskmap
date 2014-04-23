package com.hwhl.rm.adapter;

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
public class FileListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<String> list;

	public FileListAdapter(Context context,
			List<String> list) {
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
			convertView = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			//holder.iconImage = (ImageView) convertView.findViewById(ItemIDs[1]);
			holder.nameText = (TextView) convertView.findViewById(R.id.name_txt);
//			/holder.idText = (TextView) convertView.findViewById(R.id.id_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nameText.setText(StrUtil.nullToStr(list.get(position)));
	//	holder.idText.setText(StrUtil.nullToStr(list.get(position).getId()));
		/*
		String url = (String) list.get(position).get(flag[1]);
		BitmapDrawable bd = (BitmapDrawable) XmppService.getUserImage(
				XmppConnection.getConnection(), url);
		if (bd != null) {
			Bitmap bitmap = bd.getBitmap();
			Drawable drawable = new BitmapDrawable(bitmap);
			holder.iconImage.setImageDrawable(drawable);
		}

		holder.nameText.setText((String) list.get(position).get(flag[0]));
		String check = (String) list.get(position).get(flag[2]);
		if (check.equals("true")) {
			holder.selectImage.setImageResource(R.drawable.btn_check_on_normal);
		} else {
			holder.selectImage.setImageResource(R.drawable.btn_check_off_normal);
		}
		*/
		return convertView;
	}
	
	static class ViewHolder {
		//ImageView iconImage;
		TextView nameText;
		//TextView idText;
//	/	ImageView selectImage;

	}

}
