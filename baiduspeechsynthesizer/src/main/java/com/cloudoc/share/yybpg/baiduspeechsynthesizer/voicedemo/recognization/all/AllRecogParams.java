package com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.all;

import android.app.Activity;
import android.content.SharedPreferences;
import com.baidu.speech.asr.SpeechConstant;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.CommonRecogParams;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.PidBuilder;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by fujiayi on 2017/6/24.
 */

public class AllRecogParams extends CommonRecogParams {


    private static final String TAG = "NluRecogParams";

    public AllRecogParams(Activity context) {
        super(context);
        stringParams.addAll(Arrays.asList(
                SpeechConstant.NLU,
                "_language",
                "_model"));

        intParams.addAll(Arrays.asList(
                SpeechConstant.DECODER,
                SpeechConstant.PROP));

        boolParams.addAll(Arrays.asList(SpeechConstant.DISABLE_PUNCTUATION, "_nlu_online"));

        // copyOfflineResource(context);
    }

    public Map<String, Object> fetch(SharedPreferences sp) {

        Map<String, Object> map = super.fetch(sp);

        PidBuilder builder = new PidBuilder();
        map = builder.addPidInfo(map);
        return map;

    }


}
