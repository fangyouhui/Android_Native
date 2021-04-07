package com.pai8.ke.activity.takeaway.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.NoViewModel;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pai8.ke.databinding.ActivityScanItBinding;
import com.pai8.ke.qrcode.QRCodeReaderView;
import com.pai8.ke.qrcode.QRCodeTools;
import com.pai8.ke.utils.ChoosePicUtils;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Description: 扫一扫（二维码识别）-支持Carame取景和手机内置图片
 */
public class ScanItActivity extends BaseActivity<NoViewModel, ActivityScanItBinding> {

    private boolean light = false;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        if (!QRCodeTools.checkCameraHardware(this)) {
            ToastUtils.showShort("打开相机失败");
            finish();
        }
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            mBinding.carameLight.setVisibility(View.VISIBLE);//支持闪光灯
        } else {
            mBinding.carameLight.setVisibility(View.GONE);//不支持闪光灯
        }
        mBinding.qrcoderView.setDecoderView(this, mBinding.scanView);
        mBinding.carameLight.setOnClickListener(v -> {
            light = !light;
            mBinding.qrcoderView.setLightEnable(light);
        });
        mBinding.photo.setOnClickListener(v -> {
            mBinding.qrcoderView.stopPreview();
            selectPhotos();
        });
        mBinding.qrcoderView.setOnQRCodeReadListener(onQRCodeReadListener);
    }

    private void callBack(String result) {
        LogUtils.eTag(TAG, "扫描结果:" + result);
        setResult(Activity.RESULT_OK, new Intent().putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, result));
        finish();
    }

    private void selectPhotos() {
        ChoosePicUtils.picSingle(this, new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                ThreadUtils.runOnUiThreadDelayed(() -> mBinding.qrcoderView.stopPreview(), 500);
                Bitmap scanImage = QRCodeTools.decodeFile(result.get(0).getRealPath());
                if (scanImage != null) {
                    scanBitmap(scanImage);
                }
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private QRCodeReaderView.OnQRCodeReadListener onQRCodeReadListener = new QRCodeReaderView.OnQRCodeReadListener() {
        @Override
        public void onQRCodeRead(String text, PointF[] points) {
            LogUtils.eTag(TAG, "二维码结果:" + text);
            callBack(text);
        }

        @Override
        public void cameraNotFound() {
            mBinding.scanView.cameraOpenFailed();
            finish();
        }

        @Override
        public void QRCodeNotFoundOnCamImage() {

        }
    };


    private void scanBitmap(Bitmap bitmap) {
        mBinding.loading.setVisibility(View.VISIBLE);
        String content = QRCodeTools.qrReaderBitmap(bitmap, onQRCodeReadListener);
        if (TextUtils.isEmpty(content)) { //未识别出二维码
            setResult(11000);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            /**关闭相机*/
            mBinding.qrcoderView.closeCameraDriver();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mBinding.qrcoderView.startPreview();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mBinding.qrcoderView.stopPreview();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}