package com.pai8.ke.activity.takeaway.ui;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.AddressChooseAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.resq.AddressInfo;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.Address;
import com.pai8.ke.utils.AMapLocationUtils;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.MyAMapUtils;
import com.pai8.ke.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.pai8.ke.global.EventCode.EVENT_CHOOSE_ADDRESS;

public class AddressActivity extends BaseActivity implements View.OnClickListener,
        PoiSearch.OnPoiSearchListener {

    public static AMapLocation mAMapLocation;
    private TextView mTvLocation;
    private RecyclerView mRvAddress;
    private EditText mEtSearch;
    private TextView tvAddressTitle;
    private final int RC_SEARCH = 1;
    private final int INTERVAL = 500;
    private static final String mType =
            "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

    private AddressChooseAdapter mAdapter;
    private String key;
    private TextView mTvAddress;
    private int page = 1;

    private PoiSearch.Query mPoiquery;

    private String mSearch = "";
    private boolean isCanSearch = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RC_SEARCH) {
                LatLonPoint latLonPoint = new LatLonPoint(mAMapLocation.getLatitude(),
                        mAMapLocation.getLongitude());
                doSearchQuery(latLonPoint, "");
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        findViewById(R.id.tv_relocation).setOnClickListener(this);
        mTvLocation = findViewById(R.id.tv_location);
        mEtSearch = findViewById(R.id.et_search);
        mRvAddress = findViewById(R.id.rv_address);
        tvAddressTitle = findViewById(R.id.tv_address_title);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvAddress.setLayoutManager(layoutManager);
        mAdapter = new AddressChooseAdapter(this);
        mRvAddress.setAdapter(mAdapter);

        mAdapter.setClick(address -> {
            if (address == null) {
                toast("请选择地址");
                return;
            }
            EventBusUtils.sendEvent(new BaseEvent(EVENT_CHOOSE_ADDRESS, address));
            finish();
        });
    }



    @Override
    public void initData() {
        super.initData();
        AMapLocationUtils.init(this);
        loadAddress();
        mEtSearch.addTextChangedListener(new TextWatcher() {
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

        getAddress();
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
//        poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    @Override
    public void onPoiSearched(PoiResult result, int code) {
        if (code == 1000 && result != null && result.getQuery() != null) {
            List<PoiItem> poiItems = result.getPois();
            if (CollectionUtils.isEmpty(poiItems)) {
                getAddress();
            } else {
                tvAddressTitle.setVisibility(View.GONE);
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
            }

            if (CollectionUtils.isNotEmpty(poiItems)) {
                LatLonPoint latLonPoint = poiItems.get(0).getLatLonPoint();
            }
        }
        dismissLoadingDialog();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void loadAddress(){
        AMapLocationUtils.getLocation(location -> {
            LogUtils.d("AMap Location:" + location.getAddress());
            mAMapLocation = location;
            mTvLocation.setText(mAMapLocation.getAddress());
        }, false);
    }

    private void getAddress(){
        tvAddressTitle.setVisibility(View.VISIBLE);
        TakeawayApi.getInstance().addressList()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<AddressInfo>>() {
                    @Override
                    protected void onSuccess(List<AddressInfo> data){
                        List<Address> list = new ArrayList<>();
                        for(AddressInfo poiItem : data){
                            Address address = new Address();
                            address.setLocalAddress(true);
                            address.setTitle(TextUtils.isEmpty(poiItem.phone) ? "":poiItem.phone);
                            address.setAddress(TextUtils.isEmpty(poiItem.address) ? "":poiItem.address);
                            address.setDistance(TextUtils.isEmpty(poiItem.linkman) ? "":poiItem.linkman);
                            address.setLat(TextUtils.isEmpty(poiItem.latitude)?0:Double.parseDouble(poiItem.latitude));
                            address.setLon(TextUtils.isEmpty(poiItem.longitude)?0:Double.parseDouble(poiItem.longitude));

                            list.add(address);
                        }
                        mAdapter.setDataList(list);
                        mAdapter.setSelectDef();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }else if(v.getId() == R.id.tv_relocation){
            loadAddress();
        }
    }
}
