package com.pai8.ke.activity.takeaway.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.AddressChooseAdapter;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.entity.Address;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.MyAMapUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.widget.MarkerView;
import com.pai8.ke.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.global.EventCode.EVENT_CHOOSE_ADDRESS;

public class MapAddressChooseActivity extends BaseActivity implements AMap.OnCameraChangeListener,
        PoiSearch.OnPoiSearchListener {

    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.iv_btn_back)
    ImageView ivBtnBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_address)
    RecyclerView rvAddress;
    @BindView(R.id.marker_view)
    MarkerView markerView;

    private final int RC_SEARCH = 1;
    private final int INTERVAL = 500;
    private static final String mType =
            "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

    private AddressChooseAdapter mAdapter;

    private AMap mAMap;
    private UiSettings mUiSettings;
    private GeocodeSearch mGeocodeSearch;
    private AMapLocation mAMapLocation;
    private PoiSearch.Query mPoiquery;

    private String mSearch = "";
    private boolean isCanSearch = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RC_SEARCH) {
                LatLonPoint latLonPoint = new LatLonPoint(mAMapLocation.getLatitude(),
                        mAMapLocation.getLongitude());
                doSearchQuery(latLonPoint, mAMapLocation.getCity());
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_map_address_choose;
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
        mAdapter = new AddressChooseAdapter(this);
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 2));
        rvAddress.setAdapter(mAdapter);

        initMapView();

        mAMapLocation = MyApp.mAMapLocation;

        moveToMy();

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
        mGeocodeSearch = new GeocodeSearch(this);
        mAMap.setOnCameraChangeListener(this);
    }

    @Override
    public void initListener() {
        mAdapter.setClick(address -> {
            isCanSearch = false;
            moveMapCamera(address.getLat(), address.getLon());
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearch = StringUtils.strSafe(editable.toString());
                if (mHandler.hasMessages(RC_SEARCH)) {
                    mHandler.removeMessages(RC_SEARCH);
                }
                mHandler.sendEmptyMessageDelayed(RC_SEARCH, INTERVAL);
            }
        });
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

    @OnClick({R.id.iv_btn_back, R.id.iv_btn_my_loc, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_btn_back:
                finish();
                break;
            case R.id.iv_btn_my_loc:
                moveToMy();
                break;
            case R.id.tv_submit:
                Address select = mAdapter.getSelect();
                if (select == null) {
                    toast("请选择地址");
                    return;
                }
//                EventBusUtils.sendEvent(new BaseEvent(EVENT_CHOOSE_ADDRESS, select));
//
//                Intent intent = new Intent();
//                intent.putExtra("address",select);
//                setResult(RESULT_OK,intent);
//                finish();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ADDRESS", select);
                launch(ChangeDetailAddressActivity.class, bundle);
                break;
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        if (event.getCode() == EVENT_CHOOSE_ADDRESS) {
            finish();
        }
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
                        MyAMapUtils.getFormatDistance(AMapUtils.calculateLineDistance(startLatLng,
                                endLatLng));
                address.setDistance(formatDistance);
                lists.add(address);
            }
            mAdapter.setDataList(lists);
            mAdapter.setSelectDef();
            if (CollectionUtils.isNotEmpty(poiItems)) {
                LatLonPoint latLonPoint = poiItems.get(0).getLatLonPoint();
                moveMapCamera(latLonPoint.getLatitude(), latLonPoint.getLongitude());
            }
            markerView.transactionAnimWithMarker();
        }
        dismissLoadingDialog();
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
            LatLonPoint latLonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target
                    .longitude);
            doSearchQuery(latLonPoint, mAMapLocation.getCity());
        }
        isCanSearch = true;
    }

}
