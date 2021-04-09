package com.pai8.ke.activity.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.R;
import com.pai8.ke.databinding.ActivityNaviBinding;
import com.pai8.ke.utils.MyAMapUtils;
import com.pai8.ke.utils.NavUtils;
import com.pai8.ke.utils.PreferencesUtils;

import org.jetbrains.annotations.Nullable;

/**
 * 地图导航
 */
public class NaviActivity extends BaseActivity<NoViewModel, ActivityNaviBinding> {

    private AMap mAMap;
    private UiSettings mUiSettings;
    private String mAddress;
    private LatLng mLatLng;

    public static void launch(Context context, String address, String distance, String longitude, String latitude) {
        Intent intent = new Intent(context, NaviActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("distance", distance);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        context.startActivity(intent);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.mapView.onCreate(savedInstanceState);
        initMapView();
        mAddress = getIntent().getStringExtra("address");
          String distance = getIntent().getStringExtra("distance");
        String longitude = getIntent().getStringExtra("longitude");
        String latitude = getIntent().getStringExtra("latitude");

        mBinding.tvAddress.setText("地址：" + mAddress);
        mBinding.tvDistance.setText("距离您当前位置" +distance);

        try {
            // 添加当前坐标覆盖物
            mLatLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            MarkerOptions markerOption = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_loc)))
                    .position(mLatLng)
                    .draggable(true);
            mAMap.addMarker(markerOption);
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 14));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBinding.ivBtnBack.setOnClickListener(v -> finish());
        mBinding.btnNavi.setOnClickListener(v -> NavUtils.selectNav(this, mLatLng.latitude, mLatLng.longitude, mAddress));
    }


    private void initMapView() {
        mAMap = mBinding.mapView.getMap();
        mUiSettings = mAMap.getUiSettings();
        // 地图不能倾斜
        mUiSettings.setTiltGesturesEnabled(false);
        // 不能旋转
        mUiSettings.setRotateGesturesEnabled(false);
        // 不显示缩放按钮，加号减号
        mUiSettings.setZoomControlsEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.mapView.onDestroy();
    }

}
