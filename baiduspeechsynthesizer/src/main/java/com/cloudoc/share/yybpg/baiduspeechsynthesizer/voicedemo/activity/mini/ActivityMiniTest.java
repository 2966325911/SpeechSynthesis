package com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.activity.mini;

import android.os.Message;
import android.util.Log;
import android.view.View;

import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.activity.ActivityCommon;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.activity.test.AlarmListener;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.activity.test.SpeechTestCase;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.RecogEventAdapter;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.online.InFileStream;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.util.Logger;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fujiayi on 2017/8/15.
 */

public class ActivityMiniTest extends ActivityCommon {
    private boolean enableOffline = true; // 测试离线命令词，需要改成true
    private EventManager asr;

    private String runningTestName = "";

    private static String TAG = "ActivityMiniTest";

    private int index = 0;

    private ArrayList<SpeechTestCase> cases;

    {
        descText = "高级，批量测试音频文件， 具体定义在getTest()中\n"
                + "请先使用如‘在线识别’界面确认识别参数，先测试一个音频文件，成功后再批量进行\n"
                + "如果测试出问题，请将原始音频一起反馈\n"
                + "测试音频请放在 app/src/main/resources/com/baidu/android/voicedemo/test/ \n\n";
    }

    @Override
    protected void initRecog() {
        Logger.setHandler(handler);
        asr = EventManagerFactory.create(this, "asr");
        AlarmListener listener = new AlarmListener(handler);
        asr.registerListener(new RecogEventAdapter(listener));
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start();
            }
        });
        if (enableOffline) {
            loadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
    }

    private void start() {
        cases = getTests();
        begin();

    }

    private void begin() {
        if (index >= cases.size()) {
            return;
        }
        SpeechTestCase testCase = cases.get(index);
        runningTestName = testCase.getName();
        Map<String, Object> params = testCase.getParams();
        String filename = "com/baidu/android/voicedemo/test/" + testCase.getFileName();

        InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            txtLog.append("filename:" + filename + " does  not exist");
            index++;
            begin();
        } else {
            InFileStream.reset();
            InFileStream.setInputStream(is);
            params.put(SpeechConstant.IN_FILE,
                    "#com.baidu.android.voicedemo.recognization.online.InFileStream.create16kStream()");
            Log.i(TAG, "file:" + filename);
            String json = new JSONObject(params).toString();
            Log.i(TAG, runningTestName + " ," + json);
            asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
        }

    }

    @Override
    protected void handleMsg(Message msg) {
        int what = msg.what;
        switch (what) {
            case 901:
                msg.obj = runningTestName + ": success : " + msg.obj.toString();
                break;
            case 900:
                msg.obj = runningTestName + " : finish and exit\n";
                index++;
                begin();
                break;
            case -801:
                msg.obj = runningTestName + " error:" + msg.obj.toString();
                // index= 9999999; 立即停止
                break;
        }

        super.handleMsg(msg);
    }

    protected void initView() {
        super.initView();
        setting.setVisibility(View.INVISIBLE);
    }

    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    private void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
    }

    // ========================TEST CASE ==============================


    private ArrayList<SpeechTestCase> getTests() {
        ArrayList<SpeechTestCase> cases = new ArrayList<SpeechTestCase>();

        Map<String, Object> defaultParams = new HashMap<String, Object>();
        defaultParams.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // defaultParams.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音。默认普通识别，识别出音频文件的第一句话。
        // defaultParams.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // =========================
        Map<String, Object> params = new HashMap<String, Object>(defaultParams);

        cases.add(new SpeechTestCase("firstTest", "16k_test.pcm", params));
        // =========================
        Map<String, Object> params2 = new HashMap<String, Object>(defaultParams);
        cases.add(new SpeechTestCase("secondTest", "luyin1.pcm", params2));

        return cases;
    }
}
