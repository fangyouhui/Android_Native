package com.pai8.ke.activity.me;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.activity.me.adapter.AddressChooseAdapter;
import com.pai8.ke.activity.takeaway.ui.ChangeDetailAddressActivity;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.databinding.ActivityAddressChooseBinding;
import com.pai8.ke.entity.Address;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.groupBuy.adapter.TextWatcherAdapter;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.MyAMapUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.SpaceItemDecoration;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址选择
 * Created by gh on 2020/11/16.
 */
public class AddressChooseActivity extends BaseActivity<NoViewModel, ActivityAddressChooseBinding> implements AMap.OnCameraChangeListener, PoiSearch.OnPoiSearchListener {

    private final int RC_SEARCH = 1;
    private final int INTERVAL = 500;
    private static final String mType =
            "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

    private AddressChooseAdapter mAdapter;

    private AMap mAMap;
    private UiSettings mUiSettings;
    private AMapLocation mAMapLocation;
    private PoiSearch.Query mPoiquery;
    private String mSearch = "";
    private boolean isCanSearch = true;
    private ActivityResultLauncher activityResultLauncher;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RC_SEARCH) {
                LatLonPoint latLonPoint = new LatLonPoint(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
                doSearchQuery(latLonPoint, mAMapLocation.getCity());
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.mapView.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                setResult(RESULT_OK, result.getData());
                EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_CHOOSE_ADDRESS, result.getData()));
                finish();
            }
        });
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mAdapter = new AddressChooseAdapter(this);
        mBinding.recyclerView.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 2));
        mBinding.recyclerView.setAdapter(mAdapter);
        initMapView();
        mAMapLocation = MyApp.mAMapLocation;
        moveToMy();
        mAdapter.setClick(address -> {
            isCanSearch = false;
            moveMapCamera(address.getLat(), address.getLon());
        });

        mBinding.etSearch.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                mSearch = StringUtils.strSafe(s.toString());
                if (mHandler.hasMessages(RC_SEARCH)) {
                    mHandler.removeMessages(RC_SEARCH);
                }
                mHandler.sendEmptyMessageDelayed(RC_SEARCH, INTERVAL);
            }
        });

        mBinding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) {
                mHandler.sendEmptyMessageDelayed(RC_SEARCH, INTERVAL);
                return true;
            }
            return false;
        });

        mBinding.ivBtnBack.setOnClickListener(v -> finish());
        mBinding.ivBtnMyLoc.setOnClickListener(v -> moveToMy());
        mBinding.tvSubmit.setOnClickListener(v -> {
            Address select = mAdapter.getSelect();
            if (select == null) {
                ToastUtils.showShort("请选择地址");
                return;
            }
            Intent intent = new Intent(this, ChangeDetailAddressActivity.class);
            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, select);
            //     startActivity(intent);
            // EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_CHOOSE_ADDRESS, select));
            activityResultLauncher.launch(intent);
            //  finish();
        });
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
        mAMap.setOnCameraChangeListener(this);
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

    private void moveToMy() {
        moveMapCamera(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
        LatLonPoint latLonPoint = new LatLonPoint(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
        doSearchQuery(latLonPoint, mAMapLocation.getCity());
    }

    /**
     * 把地图画面移动到定位地点
     *
     * @param latitude
     * @param longitude
     */
    private void moveMapCamera(double latitude, double longitude) {
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
    }

    /**
     * 执行POI检索
     *
     * @param city
     */
    protected void doSearchQuery(LatLonPoint lp, String city) {
//        showLoadingDialog(null);
        mPoiquery = new PoiSearch.Query(mSearch, mType, city);
        mPoiquery.setPageSize(100);
        mPoiquery.setPageNum(1);
        PoiSearch poiSearch = new PoiSearch(this, mPoiquery);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    @Override
    public void onPoiSearched(PoiResult result, int code) {
        if (code == 1000 && result != null && result.getQuery() != null) {
            List<PoiItem> poiItems = result.getPois();
            final List<Address> lists = new ArrayList<>();
            for (PoiItem poiItem : poiItems) {
                LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                Address address = new Address();
                address.setTitle(poiItem.getTitle());
                address.setAddress(poiItem.getSnippet());
                address.setLat(latLonPoint.getLatitude());
                address.setLon(latLonPoint.getLongitude());
                LatLng startLatLng = new LatLng(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
                LatLng endLatLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                String formatDistance =
                        MyAMapUtils.getFormatDistance(AMapUtils.calculateLineDistance(startLatLng, endLatLng));
                address.setDistance(formatDistance);
                lists.add(address);
            }
            mAdapter.setDataList(lists);
            mAdapter.setSelectDef();
            if (CollectionUtils.isNotEmpty(poiItems)) {
                LatLonPoint latLonPoint = poiItems.get(0).getLatLonPoint();
                moveMapCamera(latLonPoint.getLatitude(), latLonPoint.getLongitude());
            }
            mBinding.markerView.transactionAnimWithMarker();
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem result, int code) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (isCanSearch) {
            LatLonPoint latLonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
            doSearchQuery(latLonPoint, mAMapLocation.getCity());
        }
        isCanSearch = true;
    }

}
