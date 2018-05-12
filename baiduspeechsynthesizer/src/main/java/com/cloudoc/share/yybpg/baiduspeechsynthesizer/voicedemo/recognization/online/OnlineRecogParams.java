package com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.online;

import android.app.Activity;
import android.content.SharedPreferences;

import com.baidu.speech.asr.SpeechConstant;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.CommonRecogParams;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.PidBuilder;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by fujiayi on 2017/6/13.
 */

public class OnlineRecogParams extends CommonRecogParams {


    private static final String TAG = "OnlineRecogParams";

    public OnlineRecogParams(Activity context) {
        super(context);

        stringParams.addAll(Arrays.asList(
                "_language", // 用于生成PID参数
                "_model" // 用于生成PID参数
        ));

        intParams.addAll(Arrays.asList(SpeechConstant.PROP));

        boolParams.addAll(Arrays.asList(SpeechConstant.DISABLE_PUNCTUATION));

    }


    public Map<String, Object> fetch(SharedPreferences sp) {
        Map<String, Object> map = super.fetch(sp);
        PidBuilder builder = new PidBuilder();
        map = builder.addPidInfo(map); // 生成PID， PID 网络在线有效
        return map;

    }

}
