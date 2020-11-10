package com.pai8.ke.interfaces;

public interface OnChatCrlListener {

    void onCrlMic(int status);

    void onCrlSpeaker(int status);

    // 挂断 isActive主动
    void onCrlHangUp(boolean isActive);

    // 接听
    void onCrlListener();

    // 摄像头
    void onCrlCamera(boolean isBackCamera);
}
