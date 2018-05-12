package com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.upload;

import com.baidu.speech.EventListener;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.util.Logger;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.wakeup.IWakeupListener;


/**
 * Created by fujiayi on 2017/6/20.
 */

public class UploadEventAdapter implements EventListener {

    private static final int ERROR_NONE = 0;

    private IWakeupListener listener;

    public UploadEventAdapter() {

    }

    private static final String TAG = "WakeupEventAdapter";

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        Logger.error(TAG, "name:" + name + "; params:" + params);

    }

}
