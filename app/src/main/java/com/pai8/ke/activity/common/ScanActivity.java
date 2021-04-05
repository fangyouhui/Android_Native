package com.pai8.ke.activity.common;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener { // 实现相关接口
    @BindView(R.id.dbv_custom)
    DecoratedBarcodeView mDBV;
    @BindView(R.id.btn_switch)
    Button swichLight;
    private CaptureManager captureManager;
    private boolean isLightOn = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sacn;
    }

    @Override
    public void initView() {
        setBarTrans(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void initCreate(Bundle savedInstanceState) {
        super.initCreate(savedInstanceState);
        mDBV.setTorchListener(this);
        // 如果没有闪光灯功能
        if (!hasFlash()) {
            swichLight.setVisibility(View.GONE);
        }
        // 初始化捕获
        captureManager = new CaptureManager(this, mDBV);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    // torch 手电筒
    @Override
    public void onTorchOn() {
        isLightOn = true;
        swichLight.setText("关闭闪关灯");
    }

    @Override
    public void onTorchOff() {
        isLightOn = false;
        swichLight.setText("打开闪关灯");
    }

    // 判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @OnClick({R.id.btn_switch, R.id.iv_btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_switch:
                if (isLightOn) {
                    mDBV.setTorchOff();
                } else {
                    mDBV.setTorchOn();
                }
                break;
            case R.id.iv_btn_back:
                finish();
                break;
        }
    }

}