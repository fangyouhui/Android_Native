package com.pai8.ke.manager;

import com.pai8.ke.app.MyApp;
import com.qiniu.droid.rtc.QNLogLevel;
import com.qiniu.droid.rtc.QNRTCEngine;
import com.qiniu.droid.rtc.QNRTCEngineEventListener;
import com.qiniu.droid.rtc.QNRTCEnv;
import com.qiniu.droid.rtc.QNRTCSetting;
import com.qiniu.droid.rtc.QNVideoFormat;

public class QNRTCManager {

    private QNRTCEngineEventListener mQNRTCEngineEventListener;

    public void setQNRTCEngineEventListener(QNRTCEngineEventListener QNRTCEngineEventListener) {
        mQNRTCEngineEventListener = QNRTCEngineEventListener;
    }

    private QNRTCManager() {

    }

    public static QNRTCManager getInstance() {
        return QNRTCManager.QNRTCManagerHolder.mInstance;
    }

    public static class QNRTCManagerHolder {
        public static QNRTCManager mInstance = new QNRTCManager();
    }

    public void init() {
        QNRTCEnv.init(MyApp.getMyApp());
        QNRTCEnv.setLogFileEnabled(true);
        QNRTCEnv.setLogLevel(QNLogLevel.INFO);
    }

    public QNRTCEngine getQNRTCEngine() {
        QNVideoFormat format = new QNVideoFormat(1280, 720, 30);
        QNRTCSetting setting = new QNRTCSetting();
        setting.setCameraID(QNRTCSetting.CAMERA_FACING_ID.FRONT)
                .setHWCodecEnabled(true)
                /**
                 * 默认情况下，网络波动时 SDK 内部会降低帧率或者分辨率来保证带宽变化下的视频质量；
                 * 如果打开分辨率保持开关，则只会调整帧率来适应网络波动。
                 */
                .setMaintainResolution(true)
                .setVideoBitrate(2000 * 1000)
                .setLowAudioSampleRateEnabled(false)
                .setAEC3Enabled(false)
                .setVideoEncodeFormat(format)
                .setVideoPreviewFormat(format);
        return QNRTCEngine.createEngine(MyApp.getMyApp(), setting, mQNRTCEngineEventListener);
    }
}
