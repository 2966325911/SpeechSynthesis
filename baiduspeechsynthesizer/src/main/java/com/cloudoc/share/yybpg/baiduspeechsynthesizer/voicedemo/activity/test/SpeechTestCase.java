package com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.activity.test;

import java.util.Map;

/**
 * Created by fujiayi on 2017/11/3.
 */

public class SpeechTestCase {

    private String name;

    private String fileName;

    private Map<String, Object> params;

    public SpeechTestCase(String name, String fileName, Map<String, Object> params) {
        this.name = name;
        this.fileName = fileName;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
