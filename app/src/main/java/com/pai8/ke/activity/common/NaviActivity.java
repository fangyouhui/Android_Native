package com.pai8.ke.activity.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.utils.NavUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地图导航
 */
public class NaviActivity extends BaseActivity {

    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.iv_btn_back)
    ImageView ivBtnBack;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_distance)
    TextView tvDistance;

    private AMap mAMap;
    private UiSettings mUiSettings;
    private String mAddress;
    private LatLng mLatLng;

    public static void launch(Context context, String address, String distance, String longitude,
                              String latitude) {
        Intent intent = new Intent(context, NaviActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("address", address);
        intent.putExtra("distance", distance);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_navi;
    }

    @Override
    public void initCreate(Bundle savedInstanceState) {
        super.initCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .titleBarMarginTop(ivBtnBack)
                .init();
        initMapView();
        Bundle extras = getIntent().getExtras();
        mAddress = extras.getString("address");
        String distance = extras.getString("distance");
        String longitude = extras.getString("longitude");
        String latitude = extras.getString("latitude");

        tvAddress.setText("地址：" + mAddress);
        tvDistance.setText("距离您当前位置" + distance);

        try {
            // 添加当前坐标覆盖物
            mLatLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            MarkerOptions markerOption = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_loc)))
                    .position(mLatLng)
                    .draggable(true);
            mAMap.addMarker(markerOption);
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 14));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMapView() {
        mAMap = mMapView.getMap();
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
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @OnClick({R.id.iv_btn_back, R.id.btn_navi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_btn_back:
                finish();
                break;
            case R.id.btn_navi:
                NavUtils.selectNav(this, mLatLng.latitude, mLatLng.longitude, mAddress);
                break;
        }
    }

}
