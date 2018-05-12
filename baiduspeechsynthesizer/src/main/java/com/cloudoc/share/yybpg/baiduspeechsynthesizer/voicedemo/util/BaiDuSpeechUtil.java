package com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.util;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.cloudoc.share.yybpg.baiduspeechsynthesizer.voicedemo.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


/**
 * @author : Vic
 *         time   : 2018/01/11
 *         desc   : 百度语音相关
 */

public class BaiDuSpeechUtil {

    private final String TAG = this.getClass().getSimpleName();
    private SpeechSynthesizer mSpeechSynthesizer;
    private String mSampleDirPath;
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";

    private static BaiDuSpeechUtil baiDuSpeechUtil = null;
    public static BaiDuSpeechUtil getInstance(){
        if(baiDuSpeechUtil == null) {
            synchronized (BaiDuSpeechUtil.class) {
                if(baiDuSpeechUtil == null) {
                    baiDuSpeechUtil = new BaiDuSpeechUtil();
                }
            }
        }
        return baiDuSpeechUtil;
    }

    /**
     * 初始化百度语音资源
     * @param context
     */
    public void setInitialEnv(Context context) {
        initialEnv(context);
    }
    /**
     * 初始化百度语音播报相关
     * @param context
     */
    public void setInitialTts(Context context, SpeechSynthesizerListener speechSynthesizerListener){
        initialTts(context,speechSynthesizerListener);
    }


    private void initialEnv(Context context) {
//        long start_time= System.currentTimeMillis();
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(context,false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(context,false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(context,false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        copyFromAssetsToSdcard(context,false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(context,false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(context,false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_TEXT_MODEL_NAME);

//        Log.d(TAG,"initialEnv cost:"+ (System.currentTimeMillis()-start_time));
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
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

    private void initialTts(Context context, SpeechSynthesizerListener speechSynthesizerListener) {
//        long start_time= System.currentTimeMillis();
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(context);
        mSpeechSynthesizer.setSpeechSynthesizerListener(speechSynthesizerListener);
        mSpeechSynthesizer.setApiKey(Constant.APP_KEY_BAIDU, Constant.APP_SECRET_BAIDU);
        mSpeechSynthesizer.setAppId(Constant.APP_ID_BAIDU);

        // 文本模型文件路径 (离线引擎使用)
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
        // 如果合成结果出现临时授权文件将要到期的提示，说明使用了临时授权文件，请删除临时授权即可。

        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置Mix模式的合成策略,  //mix模式下，wifi使用在线合成，非wifi使用离线合成)
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);
//        if(SystemUtil.isNetWorkConnected(getCurrentActivity())) {
//            // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时
//            AuthInfo authInfo=this.mSpeechSynthesizer.auth(TtsMode.MIX);
//
//            if (authInfo.isSuccess()){
//                toPrint("auth success");
//            }else{
//                String errorMsg=authInfo.getTtsError().getDetailMessage();
//                toPrint("auth failed errorMsg=" + errorMsg);
//            }
//        }

        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        // 加载离线英文资源（提供离线英文合成功能）
        mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + mSampleDirPath + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
//        Log.d(TAG,"initialTts cost:"+ (System.currentTimeMillis()-start_time));
    }


    /**
     * 播报的文字
     * @param content
     */
    public  void speakText(String content) {
        try{
            if(mSpeechSynthesizer != null) {
                int result = mSpeechSynthesizer.speak(content);
                if (result < 0) {
                    Log.d(TAG,"error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void pauseSpeechSynthesizer(){
        if(mSpeechSynthesizer != null) {
            mSpeechSynthesizer.pause();
        }
    }

    public void stopSpeechSynthesizer(){
        if(mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
        }
    }

    public void resumeSpeechSynthesizer(){
        if(mSpeechSynthesizer != null) {
            mSpeechSynthesizer.resume();
        }
    }

    public void releaseSpeechSynthesizer(){
        if(mSpeechSynthesizer != null) {
            mSpeechSynthesizer.release();
        }
    }

    public void setSpeechSynthesizerNull(){
        if(mSpeechSynthesizer != null) {
            mSpeechSynthesizer = null;
        }
    }


    public void endSpeechSynthesizer(){
        pauseSpeechSynthesizer();
        stopSpeechSynthesizer();
        releaseSpeechSynthesizer();
        setSpeechSynthesizerNull();
    }
}
