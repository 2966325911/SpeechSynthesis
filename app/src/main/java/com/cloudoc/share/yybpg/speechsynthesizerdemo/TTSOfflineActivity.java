package com.cloudoc.share.yybpg.speechsynthesizerdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class TTSOfflineActivity extends Activity {

	private static boolean TTS_PLAY_FLAGE = false;

	private EditText mTTSText;
	private TextView mTextViewTip;
	private TextView mTextViewStatus;
	private Button mTTSPlayBtn;
	private SpeechSynthesizer mTTSPlayer;
	private final String mFrontendModel= "frontend_model";
//	private final String mBackendModel = "backend_lzl";
	private final String mBackendModel = "backend_female";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_tts);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.status_bar_main);

		mTTSText = (EditText) findViewById(R.id.textViewResult);
		mTextViewStatus = (TextView) findViewById(R.id.textViewStatus);
		mTextViewTip = (TextView) findViewById(R.id.textViewTip);
		mTTSPlayBtn = (Button) findViewById(R.id.recognizer_btn);
		mTTSPlayBtn.setEnabled(false);
		mTTSPlayBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TTSPlay();
			}
		});


		//如果报错，引擎错误之类的，查看assets的文件是否写入到了sd卡中，查看存储权限是否开启
		// 这里6.0以上动态权限适配，自己适配
		initialEnv();
		// 初始化本地TTS播报
		initTts();
	}

	/**
	 * 初始化本地离线TTS
	 */
	private void initTts() {

		// 初始化语音合成对象
		mTTSPlayer = new SpeechSynthesizer(this, Config.appKey, Config.secret);
		// 设置本地合成
		mTTSPlayer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_LOCAL);
//		File _FrontendModelFile = new File(mFrontendModel);
//		if (!_FrontendModelFile.exists()) {
//			toastMessage("文件：" + mFrontendModel + "不存在，请将assets下相关文件拷贝到SD卡指定目录！");
//		}
//		File _BackendModelFile = new File(mBackendModel);
//		if (!_BackendModelFile.exists()) {
//			toastMessage("文件：" + mBackendModel + "不存在，请将assets下相关文件拷贝到SD卡指定目录！");
//		}
		// 设置前端模型
		mTTSPlayer.setOption(SpeechConstants.TTS_KEY_FRONTEND_MODEL_PATH, mDirPath+ "/" + mFrontendModel);
		// 设置后端模型
		mTTSPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, mDirPath + "/" + mBackendModel);
		// 设置回调监听
		mTTSPlayer.setTTSListener(new SpeechSynthesizerListener() {

			@Override
			public void onEvent(int type) {
				switch (type) {
					case SpeechConstants.TTS_EVENT_INIT:
						// 初始化成功回调
						log_i("onInitFinish");
						mTTSPlayBtn.setEnabled(true);
						break;
					case SpeechConstants.TTS_EVENT_SYNTHESIZER_START:
						// 开始合成回调
						log_i("beginSynthesizer");
						break;
					case SpeechConstants.TTS_EVENT_SYNTHESIZER_END:
						// 合成结束回调
						log_i("endSynthesizer");
						break;
					case SpeechConstants.TTS_EVENT_BUFFER_BEGIN:
						// 开始缓存回调
						log_i("beginBuffer");
						break;
					case SpeechConstants.TTS_EVENT_BUFFER_READY:
						// 缓存完毕回调
						log_i("bufferReady");
						break;
					case SpeechConstants.TTS_EVENT_PLAYING_START:
						// 开始播放回调
						log_i("onPlayBegin");
						break;
					case SpeechConstants.TTS_EVENT_PLAYING_END:
						// 播放完成回调
						log_i("onPlayEnd");
						setTTSButtonReady();
						break;
					case SpeechConstants.TTS_EVENT_PAUSE:
						// 暂停回调
						log_i("pause");
						break;
					case SpeechConstants.TTS_EVENT_RESUME:
						// 恢复回调
						log_i("resume");
						break;
					case SpeechConstants.TTS_EVENT_STOP:
						// 停止回调
						log_i("stop");
						break;
					case SpeechConstants.TTS_EVENT_RELEASE:
						// 释放资源回调
						log_i("release");
						break;
					default:
						break;
				}

			}

			@Override
			public void onError(int type, String errorMSG) {
				// 语音合成错误回调
				log_i("onError");
				toastMessage(errorMSG);
				setTTSButtonReady();
			}
		});
		// 初始化合成引擎
		mTTSPlayer.init("");
	}

	private void TTSPlay() {
		if (!TTS_PLAY_FLAGE) {
			mTTSPlayer.playText(mTTSText.getText().toString());
//			String s = "本次测量血压120mmHg,本次测量血压110毫米汞柱,本次测量血糖2.3摩尔每升";
//			mTTSPlayer.playText(s);
			setTTSButtonStop();
		} else {
			mTTSPlayer.stop();
			setTTSButtonReady();
		}

	}

	private void setTTSButtonStop() {
		TTS_PLAY_FLAGE = true;
		mTTSPlayBtn.setText(R.string.stop_tts);
	}

	private void setTTSButtonReady() {
		mTTSPlayBtn.setText(R.string.start_tts);
		TTS_PLAY_FLAGE = false;
	}

	protected void setTipText(String tip) {

		mTextViewTip.setText(tip);
	}

	protected void setStatusText(String status) {

		mTextViewStatus.setText(getString(R.string.lable_status) + "(" + status + ")");
	}

	@Override
	public void onPause() {
		super.onPause();
		// 主动停止识别
		if (mTTSPlayer != null) {
			mTTSPlayer.stop();
		}
	}

	private void log_i(String log) {
		Log.i("demo", log);
	}

	@Override
	protected void onDestroy() {
		// 主动释放离线引擎
		if (mTTSPlayer != null) {
			mTTSPlayer.release(SpeechConstants.TTS_RELEASE_ENGINE, null);
		}
		super.onDestroy();
	}

	private void toastMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}



	/**
	 * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
	 *
	 * @param isCover 是否覆盖已存在的目标文件
	 * @param source
	 * @param dest
	 */
	private void copyFromAssetsToSdcard(Context context, boolean isCover, String source, String dest) {
		File file = new File(dest);
		if (isCover || (!isCover && !file.exists())) {
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = context.getAssets().open(source);
				String path = dest;
				fos = new FileOutputStream(path);
				byte[] buffer = new byte[1024];
				int size = 0;
				while ((size = is.read(buffer, 0, 1024)) != -1) {
					fos.write(buffer, 0, size);
				}
				fos.flush();


			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				if (fos != null) {
					try {
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}


			}
		}


	}

	private void makeDir(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private String mDirPath = null;
	private void initialEnv(){
		if(mDirPath == null) {
			String sdcardPath = Environment.getExternalStorageDirectory().toString();
			mDirPath = sdcardPath + "/unsound/tts/";
		}

		makeDir(mDirPath);
		copyFromAssetsToSdcard(this,false,mFrontendModel,mDirPath +"/" + mFrontendModel);
		copyFromAssetsToSdcard(this,false,mBackendModel,mDirPath + "/" + mBackendModel);
	}

}
