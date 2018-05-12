package com.cloudoc.share.yybpg.speechsynthesizerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * 云知音 语音
 */
public class MainActivity extends Activity implements OnClickListener {
	private static final String ASR_ONLINE = "在线识别";
	private static final String OFFLINE_TTS = "本地合成";
	private ArrayList<String> mFunctionsArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.status_bar_main);
		initFunctionArray();
		((ListView) findViewById(R.id.lv_functions)).setAdapter(new FunctionsAdapter());
	}


	@Override
	public void onClick(View view) {
		Intent intent = null;
		Object tag = view.getTag();
		if(tag.equals(ASR_ONLINE)){
			intent = new Intent(this, ASROnlineActivity.class);
		}
		if(tag.equals(OFFLINE_TTS)){
			intent = new Intent(this, TTSOfflineActivity.class);
		}
		if (intent != null) {
			startActivity(intent);
		}
		
	}


	private class FunctionsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mFunctionsArray.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.button_list_item, null);
				holder = new ViewHolder();
				holder.btn = (Button) convertView.findViewById(R.id.btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.btn.setText(mFunctionsArray.get(position));
			holder.btn.setTag(mFunctionsArray.get(position));
			holder.btn.setOnClickListener(MainActivity.this);
			return convertView;
		}
	}

	public final class ViewHolder {
		public Button btn;
	}
	private void initFunctionArray() {
		mFunctionsArray = new ArrayList<String>();
		mFunctionsArray.add(ASR_ONLINE);
		mFunctionsArray.add(OFFLINE_TTS);
	}

}
