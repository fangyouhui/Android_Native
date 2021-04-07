package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.NoViewModel;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pai8.ke.databinding.ActivityQrcodeHandleBinding;
import com.pai8.ke.qrcode.QRCodeReaderView;
import com.pai8.ke.qrcode.QRCodeTools;
import com.pai8.ke.utils.ChoosePicUtils;

import org.jetbrains.annotations.Nullable;

import java.util.List;


public class QRCodeHandleActivity extends BaseActivity<NoViewModel, ActivityQrcodeHandleBinding> {

    private Bitmap scanImage;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        selectPhotos();
        mBinding.back.setOnClickListener(v -> finish());
        mBinding.scanView.closeScanAnimation();
        mBinding.scanView.setScanMaskColor(Color.parseColor("#88000000"));
    }

    private void selectPhotos() {
        ChoosePicUtils.picSingle(this, new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                scanImage = QRCodeTools.decodeFile(result.get(0).getRealPath());
                if (scanImage != null) {
                    mBinding.scanImage.setVisibility(View.VISIBLE);
                    mBinding.scanImage.setImageBitmap(scanImage);
                    scanBitmap();
                }
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }


    private void scanBitmap() {
        mBinding.handleTv.setVisibility(View.VISIBLE);
        QRCodeTools.qrReaderBitmap(scanImage, new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(String text, PointF[] points) {
                if (!TextUtils.isEmpty(text)) {
                    mBinding.handleTv.setVisibility(View.INVISIBLE);
                    setResult(RESULT_OK, new Intent().putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, text));
                }
            }

            @Override
            public void cameraNotFound() {

            }

            @Override
            public void QRCodeNotFoundOnCamImage() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scanImage != null) {
            scanImage.recycle();
            scanImage = null;
        }
    }

}