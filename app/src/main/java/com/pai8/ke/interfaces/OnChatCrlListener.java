package com.pai8.ke.interfaces;

public interface OnChatCrlListener {

    // 手机麦克风 isSilent = true静音
    void onCrlMic(boolean isSilent);

    // 扬声器模式 isSpeaker = true打开
    void onCrlSpeaker(boolean isSpeaker);

    // 挂断 isActive主动
    void onCrlHangUp(boolean isActive);

    // 接听
    void onCrlListener();

    // 摄像头
    void onCrlCamera(boolean isBackCamera);
}
