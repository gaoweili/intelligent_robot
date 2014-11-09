package com.intelligent_robot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.intelligent_robot.R;
import com.intelligent_robot.bean.*;
import com.intelligent_robot.bean.ChatMessage.Type;
import com.intelligent_robot.utils.HttpUtils;

public class MainActivity extends ActionBarActivity {

	private ListView mMsgs;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mDatas;
	private Button mSendMsg;
	private TextView mInputMsg;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ChatMessage fromMsg = (ChatMessage) msg.obj;
			mDatas.add(fromMsg);
			mAdapter.notifyDataSetChanged();
			mMsgs.setSelection(mDatas.size()-1);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		initDatas();
		initListener();
	}

	private void initListener() {
		mSendMsg.setOnClickListener(new OnClickListener() {
			String msg = "";

			@Override
			public void onClick(View v) {
				msg = mInputMsg.getText().toString();
				if (TextUtils.isEmpty(msg)) {
					// 
					Toast.makeText(MainActivity.this, "输入内容不能为空哦",
							Toast.LENGTH_SHORT).show();
					return;
				}

				ChatMessage toMsg = new ChatMessage(msg, new Date(),
						Type.OutComing);
				;
				mDatas.add(toMsg);
				mAdapter.notifyDataSetChanged();
				mMsgs.setSelection(mDatas.size()-1);
				//文本清空
				mInputMsg.setText("");
				
				new Thread() {
					public void run() {
						ChatMessage fromMessage = HttpUtils.sendMessage(msg);
						Message m = Message.obtain();
						m.obj = fromMessage;
						mHandler.sendMessage(m);
					};
				}.start();
			}
		});
	}

	private void initDatas() {
		mDatas = new ArrayList<ChatMessage>();
		mDatas.add(new ChatMessage("您好，小立为您服务！", new Date(), Type.InComing));
		mAdapter = new ChatMessageAdapter(this, mDatas);
		mMsgs.setAdapter(mAdapter);
	}

	private void initView() {
		mMsgs = (ListView) findViewById(R.id.id_lv_center);
		mSendMsg = (Button) findViewById(R.id.id_send_msg);
		mInputMsg = (TextView) findViewById(R.id.id_editText);
	}
}
