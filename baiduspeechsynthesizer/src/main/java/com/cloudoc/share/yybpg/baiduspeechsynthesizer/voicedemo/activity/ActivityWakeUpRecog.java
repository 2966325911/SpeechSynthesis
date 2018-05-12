package com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.activity;

import android.os.Message;

import com.baidu.speech.asr.SpeechConstant;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.control.MyRecognizer;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.control.MyWakeup;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.IStatus;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.MessageStatusRecogListener;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.PidBuilder;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.StatusRecogListener;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.wakeup.IWakeupListener;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.wakeup.RecogWakeupListener;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 唤醒后识别 本例可与ActivityWakeUp 及对比作为集成识别代码的参考
 */
public class ActivityWakeUpRecog extends ActivityWakeUp implements IStatus {
    {
        descText = "请先单独测试唤醒词功能和在线识别功能。\n"
                + "唤醒后识别，即唤醒词识别成功后，立即在线识别。\n"
                + "根据用户说唤醒词之后有无停顿。共2种实现方式。\n"
                + "1. 您的场景需要唤醒词之后无停顿，用户一下子说出。那么可以使用录音回溯功能：连同唤醒词一起整句识别。\n"
                + "2. 您的场景引导唤醒词之后有短暂停顿。那么不做回溯，识别出唤醒词停顿后的句子\n"
                + "两个方案的优劣 ：方案1 中，整句回溯时间是经验值1.5s。 此外由于整句识别，可能唤醒词会识别成别的结果。\n"
                + " 方案2中， 如果用户不停顿，将可能造成首字或者首词的识别错误。\n"
                + "代码中通过backTrackInMs可以选择测试方案1还是方案2。\n\n"
                + "测试请说：百度一下【此处可以停顿】，今天天气真好";
    }


    private static final String TAG = "ActivityWakeUpRecog";

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;

    /**
     * 0: 方案1， 唤醒词说完后，直接接句子，中间没有停顿。
     * >0 : 方案2： 唤醒词说完后，中间有停顿，然后接句子。推荐4个字 1500ms
     * <p>
     * backTrackInMs 最大 15000，即15s
     */
    private int backTrackInMs = 1500;


    @Override
    protected void initRecog() {
        // 初始化识别引擎

        StatusRecogListener recogListener = new MessageStatusRecogListener(handler);
        myRecognizer = new MyRecognizer(this, recogListener);

        IWakeupListener listener = new RecogWakeupListener(handler);
        myWakeup = new MyWakeup(this, listener);

    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        if (msg.what == STATUS_WAKEUP_SUCCESS) {
            // 此处 开始正常识别流程
            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
            params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
            int pid = PidBuilder.create().model(PidBuilder.INPUT).toPId();
            // 如识别短句，不需要需要逗号，将PidBuilder.INPUT改为搜索模型PidBuilder.SEARCH
            params.put(SpeechConstant.PID, pid);
            if (backTrackInMs > 0) { // 方案1， 唤醒词说完后，直接接句子，中间没有停顿。
                params.put(SpeechConstant.AUDIO_MILLS, System.currentTimeMillis() - backTrackInMs);

            }
            myRecognizer.cancel();
            myRecognizer.start(params);
        }
    }

    @Override
    protected void stop() {
        super.stop();
        myRecognizer.stop();
    }

    @Override
    protected void onDestroy() {
        myRecognizer.release();
        super.onDestroy();
    }
}
