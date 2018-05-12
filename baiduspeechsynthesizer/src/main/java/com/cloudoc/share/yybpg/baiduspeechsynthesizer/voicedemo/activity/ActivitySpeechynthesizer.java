package com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.R;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.util.BaiDuSpeechUtil;

/**
 * 这里由于集成的主要是用于语音识别的功能，语音合成是后续自己添加进来的，可能导致不能正常播放，
 * 整体流程都是一样的
 * 具体可以下载百度的demo一探究竟
 */
public class ActivitySpeechynthesizer extends AppCompatActivity implements SpeechSynthesizerListener {



    private BaiDuSpeechUtil mBaiDuSpeechUtil = null;
    private static final String WARM_PROMPT = "百度语音测试,看是否可以播报";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechynthesizer);
    }

    private void startSpeakText() {
        mBaiDuSpeechUtil = BaiDuSpeechUtil.getInstance();
        mBaiDuSpeechUtil.setInitialEnv(this);
        mBaiDuSpeechUtil.setInitialTts(this, this);

        speakText(WARM_PROMPT);
    }

    private void speakText(String content) {
        if (mBaiDuSpeechUtil != null) {
            mBaiDuSpeechUtil.speakText(content);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSpeakText();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseSpeechSynthesizer();
    }

    private void releaseSpeechSynthesizer() {

        if (mBaiDuSpeechUtil != null) {
            mBaiDuSpeechUtil.endSpeechSynthesizer();
        }
    }

    @Override
    public void onSynthesizeStart(String s) {

    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

    }

    @Override
    public void onSynthesizeFinish(String s) {

    }

    @Override
    public void onSpeechStart(String s) {

    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {

    }

    @Override
    public void onSpeechFinish(String s) {

    }

    @Override
    public void onError(String s, SpeechError speechError) {
    }

}
