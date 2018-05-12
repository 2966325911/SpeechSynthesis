package com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.activity;


import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.activity.setting.AllSetting;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.CommonRecogParams;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.recognization.all.AllRecogParams;

/**
 * Created by fujiayi on 2017/6/24.
 */

public class ActivityAllRecog extends ActivityRecog {
    {
        descText = "所有识别参数一起的示例。请先参照之前的3个识别示例。\n";

        enableOffline = true; // 请确认不使用离线命令词功能后，改为false
    }

    public ActivityAllRecog() {
        super();
        settingActivityClass = AllSetting.class;
    }

    @Override
    protected CommonRecogParams getApiParams() {
        return new AllRecogParams(this);
    }

}
