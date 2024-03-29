package com.intelligent_robot;

import java.text.SimpleDateFormat;
import java.util.List;

import com.intelligent_robot.R;
import com.intelligent_robot.bean.ChatMessage;
import com.intelligent_robot.bean.ChatMessage.Type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ChatMessage> mDatas;

	public ChatMessageAdapter(Context context, List<ChatMessage> mDatas) {
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {

		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		ChatMessage msg = mDatas.get(position);
		if (msg.getType() == Type.InComing)
			return 0;
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ChatMessage msg = mDatas.get(position);
		ViewHolder holder = null;
		// 通过ItemType设置布局
		if (convertView == null) {
			if (getItemViewType(position) == 0) {
				convertView = mInflater.inflate(R.layout.item_from_msg, parent,
						false);
				holder = new ViewHolder();
				holder.mDate = (TextView) convertView
						.findViewById(R.id.id_from_msg_date);
				holder.mMsg = (TextView) convertView
						.findViewById(R.id.id_from_msg_info);
			} else {
				convertView = mInflater.inflate(R.layout.item_to_msg, parent,
						false);
				holder = new ViewHolder();
				holder.mDate = (TextView) convertView
						.findViewById(R.id.id_to_msg_date);
				holder.mMsg = (TextView) convertView
						.findViewById(R.id.id_to_msg_info);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置数据
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		holder.mDate.setText(df.format(msg.getDate()));
		holder.mMsg.setText(msg.getMsg());
		return convertView;
	}

	private final class ViewHolder {
		TextView mDate;
		TextView mMsg;
	}
}
