package com.pai8.ke.activity.takeaway.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.databinding.ActivityScanItBinding;
import com.pai8.ke.qrcode.QRCodeReaderView;
import com.pai8.ke.qrcode.QRCodeTools;

import org.jetbrains.annotations.Nullable;

/**
 * @Description: 扫一扫（二维码识别）-支持Carame取景和手机内置图片
 */
public class ScanItActivity extends BaseActivity<NoViewModel, ActivityScanItBinding> {
    /**
     * 扫一扫结果 key值  通过Intent向外部发送识别结果
     */
    public static final String QR_CODE_RESULT = "scan_result";
    public static final int REQUEST_CODE_HANDLE_CONTENT = 115;

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
        mBinding.qrcoderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(String text, PointF[] points) {
                LogUtils.eTag(TAG, "扫描结果:" + text);
                setResult(Activity.RESULT_OK, new Intent().putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, text));
                finish();
            }

            @Override
            public void cameraNotFound() {
                mBinding.scanView.cameraOpenFailed();
            }

            @Override
            public void QRCodeNotFoundOnCamImage() {

            }
        });

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