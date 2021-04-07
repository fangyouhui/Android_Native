package com.pai8.ke.qrcode;

public interface QRCodeObserver {
    void onAnalysisFinished();

    void onResetScan();
}
